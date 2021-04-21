package cn.ntshare.laboratory.serivice;

import java.io.IOException;
import java.util.Map;

public interface EsService {
    Boolean createIndex(String index,String type) throws IOException;

    /**
     * 游标查询
     */
    void getFriendRecordByEsScroll(String INDEX);

    /**
     * SearchAfter 查询拿10条会导致数据丢失 未详细测试
     * @param INDEX
     * @throws IOException
     */
    void getFriendRecordByEsSearchAfter(String INDEX) throws IOException;
}
