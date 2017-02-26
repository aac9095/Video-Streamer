package socialcops.aac.video;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

/**
 * Created by Ayush on 26-02-2017.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;


    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
    }

    public class MyFileNameGenerator implements FileNameGenerator {

        public String generate(String url) {
            return MainActivity.videoName;
        }
    }

}