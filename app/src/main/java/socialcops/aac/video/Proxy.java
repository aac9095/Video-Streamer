package socialcops.aac.video;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by Ayush on 25-02-2017.
 */

public class Proxy extends Application {
    private HttpProxyCacheServer httpProxyCacheServer;

    public static HttpProxyCacheServer getProxy(Context context) {
        Proxy proxy = (Proxy) context.getApplicationContext();
        return proxy.httpProxyCacheServer == null ? (proxy.httpProxyCacheServer = proxy.newProxy())
                                            : proxy.httpProxyCacheServer;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
