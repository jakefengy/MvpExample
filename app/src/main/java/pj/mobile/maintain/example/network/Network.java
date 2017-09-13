package pj.mobile.maintain.example.network;

import com.pujieinfo.mobile.framework.network.BaseAuthInterceptor;
import com.pujieinfo.mobile.framework.network.RetrofitUtils;

/**
 * 2017-05-22.
 */

public class Network {

    private Apis apis;

    private Network() {

        apis = RetrofitUtils.createService(Apis.class, "http://192.168.103.156:8080/pjWebAPI/rest/", new BaseAuthInterceptor("1", "1"), new LoggingInterceptor());
    }

    private static class SingletonHolder {
        private static final Network INSTANCE = new Network();
    }

    //获取单例
    public static Apis getApis() {
        return SingletonHolder.INSTANCE.apis;
    }

}
