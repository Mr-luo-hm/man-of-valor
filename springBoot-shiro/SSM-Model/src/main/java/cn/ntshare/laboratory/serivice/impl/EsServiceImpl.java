package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.serivice.EsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // 1?????????search??????
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // searchRequest.types(EsConstant.TYPE);
        // 2??????SearchSourceBuilder???????????????????????? ,?????????????????????????????????????????????????????????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // ????????????
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        RangeQueryBuilder sendTimeQueryBuilder = QueryBuilders.rangeQuery("send_time");

        sourceBuilder.query(queryBuilder);
        // ?????????????????????????????????????????????????????????????????????
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // ????????????
        sourceBuilder.sort(new FieldSortBuilder("send_time").order(SortOrder.DESC));
        // ?????????1000???
        sourceBuilder.size(1000);
        // ??????????????????????????????
        searchRequest.source(sourceBuilder);
        // ????????????
        searchRequest.scroll(new TimeValue(20000));
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ????????????????????????
        String scrollId = searchResponse.getScrollId();
        // ??????????????????
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map> mapList = new ArrayList<Map>();
        for (SearchHit hit : searchHits) {
            // ???_source?????????
            mapList.add(hit.getSourceAsMap());
        }
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(new TimeValue(20000));
            try {
                // ????????????????????????
                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ?????????????????????
            scrollId = searchResponse.getScrollId();
            // ??????????????????
            searchHits = searchResponse.getHits().getHits();
            if (searchHits != null && searchHits.length > 0) {
                for (SearchHit searchHit : searchHits) {
                    mapList.add(searchHit.getSourceAsMap());
                }
            }
        }
        // ????????????
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        // ?????????????????????
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = null;
        try {
            // ???????????? ????????????
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
        // 1?????????search??????
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 2??????SearchSourceBuilder???????????????????????? ,?????????????????????????????????????????????????????????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // ????????????
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        RangeQueryBuilder send_timeQueryBuilder = QueryBuilders.rangeQuery("send_time");
        sourceBuilder.query(queryBuilder);
        // ?????????????????????????????????????????????????????????????????????
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // ????????????
        sourceBuilder.sort(new FieldSortBuilder("send_time").order(SortOrder.DESC));
        // ????????????????????????
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

    public void queryMatch(String indexName, String typeName, String field, String keyWord) throws IOException {
        SearchRequest request = new SearchRequest();
        request.types(typeName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, keyWord));
        searchSourceBuilder.sort("replyTotal");
        request.source(searchSourceBuilder);
        log.info("source:" + request.source());
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("count:{}", hits.getHits().length);
        SearchHit[] h = hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("??????" + hit.getSourceAsMap() + ",score:" + hit.getScore());
        }
    }

    public void sortQuery(String indexName, String typeName, String field, String keyWord, String sort, SortOrder sortOrder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, keyWord));
        searchSourceBuilder.sort(sort, sortOrder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("count:{}", hits.getHits().length);
        SearchHit[] h = hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("??????" + hit.getSourceAsMap() + ",score:" + hit.getScore());
        }
    }

    public void multSortQuery(String indexName, String typeName, String field, String keyWord, String sort1, String sort2, SortOrder sortOrder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, keyWord));
        searchSourceBuilder.sort(sort1, sortOrder);
        searchSourceBuilder.sort(sort2, sortOrder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:" + hits.getHits().length);
        SearchHit[] h = hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("??????" + hit.getSourceAsMap() + ",score:" + hit.getScore());
        }
    }

    public void cardinalityAggregations(String indexName, String typeName, String field) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        CardinalityAggregationBuilder agg1 = AggregationBuilders.cardinality("agg").field(field);
        sourceBuilder.aggregation(agg1);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Cardinality agg = searchResponse.getAggregations().get("agg");
        double value = agg.getValue();
        log.info(field + " cardinalityAggregation value ???" + value);
    }

    public void extendedStatsAggregation(String indexName, String typeName, String field) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        ExtendedStatsAggregationBuilder agg1 = AggregationBuilders.extendedStats("agg").field(field);
        sourceBuilder.aggregation(agg1);
        searchRequest.source(sourceBuilder);
        log.info("source :" + searchRequest.source());
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        ExtendedStats agg = searchResponse.getAggregations().get("agg");
        double min = agg.getMin();
        double max = agg.getMax();
        double avg = agg.getAvg();
        double sum = agg.getSum();
        long count = agg.getCount();
        double stdDeviation = agg.getStdDeviation();
        double sumOfSquares = agg.getSumOfSquares();
        double variance = agg.getVariance();
        System.out.println("min ???" + min);
        System.out.println("max ???" + max);
        System.out.println("avg ???" + avg);
        System.out.println("sum ???" + sum);
        System.out.println("count ???" + count);
        System.out.println("stdDeviation ???" + stdDeviation);
        System.out.println("sumOfSquares ???" + sumOfSquares);
        System.out.println("variance ???" + variance);
    }
}
