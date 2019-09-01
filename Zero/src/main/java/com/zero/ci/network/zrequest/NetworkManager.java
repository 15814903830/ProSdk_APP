package com.zero.ci.network.zrequest;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description: 网络管理
 * -------------------------------
 */
public class NetworkManager {
    private static NetworkConfig mNetworkConfig;

//    private static NetworkManager instance;
//
//    public static NetworkManager getInstance() {
//        if (instance == null)
//            synchronized (NetworkManager.class) {
//                instance = new NetworkManager();
//            }
//        myreturn instance;
//    }

    public NetworkManager() {
    }

    private static class NetworkManagerHolder {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManagerHolder.INSTANCE;
    }


    public void InitializationConfig(NetworkConfig config) {
        mNetworkConfig = config;
    }

    public NetworkConfig getInitializeConfig() {
        return mNetworkConfig;
    }

}
