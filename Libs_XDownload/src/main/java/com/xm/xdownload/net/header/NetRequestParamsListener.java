package com.xm.xdownload.net.header;

import java.util.HashMap;

/**
 * @author: yedanmin
 * @date: 2017-06-20
 * @time: 16:18
 * @说明:
 */
public interface NetRequestParamsListener {
    //请求的时候 携带头部参数,如果没有 返回null
    HashMap<String,String> getHeaderParams();
}
