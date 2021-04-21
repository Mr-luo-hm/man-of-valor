package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.serivice.EsService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
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
}
