package cn.ntshare.laboratory.serivice;

import java.io.IOException;

public interface EsService {
    Boolean createIndex(String index,String type) throws IOException;
}
