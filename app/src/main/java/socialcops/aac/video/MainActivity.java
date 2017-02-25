package socialcops.aac.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.danikula.videocache.HttpProxyCacheServer;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Android Video Task";
    private final String VIDEO_URL = "https://socialcops.com/images/spec/home/header-img-background_video-1920-480.mp4";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);
        } catch (Exception e){
            Log.e(TAG,"Exception",e);
        }

        videoView = (VideoView) findViewById(R.id.video);

        HttpProxyCacheServer proxy = Proxy.getProxy(this);
        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
        videoView.setVideoPath(proxyUrl);
    }
}
