package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.serivice.EsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EsServiceImpl implements EsService {
    private final RestHighLevelClient restHighLevelClient;

    public Boolean createIndex(String index, String type) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(Settings.builder().put("number_of_shards", 5).put("number_of_replicas", 1).build());
        XContentBuilder builder = JsonXContent.contentBuilder();
        builder.startObject()//
                .startObject("properties")//
                .startObject("name").field("type", "text").field("index", "true").field("store", "true").endObject()//
                .startObject("author").field("type", "keyword").endObject()//
                .startObject("count").field("type", "long").endObject()//
                .startObject("on-sale").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").endObject()//
                .startObject("descr").field("type", "text").endObject()
                .endObject()
                .endObject();
        CreateIndexRequest mapping = request.mapping(type, builder);
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return null;
    }

    public void getFriendRecordByEsScroll(String INDEX) {
        // 1、创建search请求
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // searchRequest.types(EsConstant.TYPE);
        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 集合查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        RangeQueryBuilder sendTimeQueryBuilder = QueryBuilders.rangeQuery("send_time");

        sourceBuilder.query(queryBuilder);
        // 设置一个可选的超时时间，以控制允许搜索的时间。
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 指定排序
        sourceBuilder.sort(new FieldSortBuilder("send_time").order(SortOrder.DESC));
        // 每次取1000条
        sourceBuilder.size(1000);
        // 将请求体加入到请求中
        searchRequest.source(sourceBuilder);
        // 设置滚屏
        searchRequest.scroll(new TimeValue(20000));
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取第一次得游标
        String scrollId = searchResponse.getScrollId();
        // 处理命中文档
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map> mapList = new ArrayList<Map>();
        for (SearchHit hit : searchHits) {
            // 取_source字段值
            mapList.add(hit.getSourceAsMap());
        }
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(new TimeValue(20000));
            try {
                // 带着上次游标查询
                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 重新给游标赋值
            scrollId = searchResponse.getScrollId();
            // 处理命中数据
            searchHits = searchResponse.getHits().getHits();
            if (searchHits != null && searchHits.length > 0) {
                for (SearchHit searchHit : searchHits) {
                    mapList.add(searchHit.getSourceAsMap());
                }
            }
        }
        // 清除游标
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        // 最后一次得游标
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = null;
        try {
            // 清除游标 释放内存
            clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("clear scrollId error", e);
        }
        boolean succeeded = false;
        if (clearScrollResponse != null) {
            succeeded = clearScrollResponse.isSucceeded();
        }
        log.info("clear scrollId status:{}", succeeded);
        Map<String, Object> result = new HashMap(8);
        /*result.put("list", fomartClientMsg(mapList));
        return result;*/
    }

    public void getFriendRecordByEsSearchAfter(String INDEX) throws IOException {
        // 1、创建search请求
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 集合查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        RangeQueryBuilder send_timeQueryBuilder = QueryBuilders.rangeQuery("send_time");
        sourceBuilder.query(queryBuilder);
        // 设置一个可选的超时时间，以控制允许搜索的时间。
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 指定排序
        sourceBuilder.sort(new FieldSortBuilder("send_time").order(SortOrder.DESC));
        // 每次获取数据条数
        sourceBuilder.size(50);
        boolean type = true;
        Object[] objects = null;
        SearchResponse searchResponse;
        Map<String, Object> result = new HashMap(8);
        List<Map> mapList = new ArrayList<Map>();
        while (type) {
            if (objects != null) {
                sourceBuilder.searchAfter(objects);
            }
            searchRequest.source(sourceBuilder);
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            if (hits.length == 0) {
                type = false;
            }
            if (hits.length > 0) {
                objects = hits[hits.length - 1].getSortValues();
            }
            for (SearchHit searchHit : hits) {
                mapList.add(searchHit.getSourceAsMap());
            }
            //result.put("list", fomartClientMsg(mapList));
        }
        //return result;
    }
}