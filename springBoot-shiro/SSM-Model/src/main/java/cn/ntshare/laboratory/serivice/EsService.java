package cn.ntshare.laboratory.serivice;

import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

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

    /**
     * 默认排序
     * @param indexName 索引
     * @param typeName 类型
     * @param field
     * @param keyWord
     * @throws IOException
     */
    void queryMatch(String indexName, String typeName, String field,String keyWord)throws IOException;

    /**
     * 条件排序
     * @param indexName
     * @param typeName
     * @param field
     * @param keyWord
     * @param sort
     * @param sortOrder
     * @throws IOException
     */
    void sortQuery(String indexName, String typeName, String field, String keyWord, String sort, SortOrder sortOrder)throws IOException;

    /**
     * 多条件排序
     * @param indexName
     * @param typeName
     * @param field
     * @param keyWord
     * @param sort1
     * @param sort2
     * @param sortOrder
     * @throws IOException
     */
    void multSortQuery(String indexName, String typeName, String field,String keyWord,String sort1,String sort2,SortOrder sortOrder)throws IOException;

    /**
     * 去重
     * @param indexName
     * @param typeName
     * @param field
     */
    void cardinalityAggregations(String indexName, String typeName, String field)throws IOException;

    /**
     * extended_stats统计聚合
     * @param indexName
     * @param typeName
     * @param field
     * @throws IOException
     */
    void extendedStatsAggregation(String indexName, String typeName, String field)throws IOException;
}
