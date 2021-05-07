import cn.ntshare.laboratory.serivice.EsService;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
@SpringBootTest
public class EsTest {
    @Autowired
    EsService esService;

    @Test
    public void testSortQueryByDefault() throws IOException {
        esService.queryMatch("indexName", "type", "smsContent", "中国银行");
    }

    //条件排序
    @Test
    public void testSortQueryBySort() throws IOException {
        esService.sortQuery("indexName", "type", "smsContent", "中国银行", "replyTotal", SortOrder.DESC);
    }

    //多条件排序
    @Test
    public void testSortQueryByMultSort() throws IOException {
        esService.multSortQuery("indexName", "type", "smsContent", "中国银行", "replyTotal", "province", SortOrder.DESC);
    }
    // cardinality去重计数
    @Test
    public void cardinalityAggregationsTest()throws IOException{
        // 统计去重后的手机数量
        esService.cardinalityAggregations("indexName","type","mobile");
    }
}
