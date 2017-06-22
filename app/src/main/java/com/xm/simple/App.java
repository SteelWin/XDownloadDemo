package com.xm.simple;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.xm.simple.interfac.IConstantPool;
import com.xm.xdownload.net.common.RetrofitClient;
import com.xm.xdownload.net.header.NetRequestParamsListener;

import java.util.HashMap;

/**
 * 功能:
 * 作者：小民
 * 创建时间：2017/5/26ww
 */
public class App extends Application {
    // 入口函数
    private static App application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化网络请求
        RetrofitClient.init(this)
                .setDebug(true)              //是否输出调试日志
                .setBaseUrl(IConstantPool.sCommonUrl)
                // 网友们可不要弄错了。主要是 7.0 权限问题 ，安装 apk 时候需要用到
                // 是 主工程的 BuildConfig
                .setApplictionId(BuildConfig.APPLICATION_ID)
                /** 以下都是按需设置 */
                .setDownloadsQueueCount(5)                 //下载最大数量
                .setDbName("net_buffer_db")                //数据库表名
                .setConnectionTimeout(6)                   //普通请求连接超时
                .setReadTimeout(6)                         //普通请求读取超时
                .setDownConnectionTime(6)                  //下载连接超时   6秒
                .setNetBufferTime(60)                      //有网络的情况下缓存  60秒
                .setNoNetBufferTime(24 * 60 * 60 * 7)      //无网络的时候，缓存
                /** 头部参数 */
                .setNetRequestParamsListener(new NetRequestParamsListener() {
                    @Override
                    public HashMap<String, String> getHeaderParams() {
                        //需要请传,不需要返回 null
                        //                        return null;
                        HashMap<String, String> map = new HashMap<>();
                        map.put("userId", "2745329043");
                        return map;
                    }
                })
                /** 设置完，记得Buid */
                .build();
    }

    /**
     * 方便其他地方调用
     */
    public static App getInstance() {
        return application;
    }


}
