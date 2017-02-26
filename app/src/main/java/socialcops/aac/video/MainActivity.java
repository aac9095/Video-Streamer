package socialcops.aac.video;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.fabric.sdk.android.Fabric;

import static socialcops.aac.video.R.id.video;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "Ayush";
    public static String videoName = "header-img-background_video-1920-480.mp4";
    private String VIDEO_URL = "https://socialcops.com/images/spec/home/header-img-background_video-1920-480.mp4";
    private String proxyUrl = "";
    private VideoView videoView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        try{
            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);
        } catch (Exception e){
            Log.e(TAG,"Exception",e);
        }

        // Create a progressbar
        pDialog = new ProgressDialog(this);
        // Set progressbar title
        pDialog.setTitle("SocialCops Video");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        videoView = (VideoView) findViewById(video);
        playVideo();
        if(savedInstanceState != null){
            int position = savedInstanceState.getInt("position");
            boolean isPlaying = savedInstanceState.getBoolean("isPlaying?",false);
            videoView.seekTo(position);
            if(!isPlaying) {
                videoView.pause();
            }
        }
    }

    private void playVideo() {
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            // Get the proxy URL from String VideoURL
            proxyUrl = Utils.getProxyUrl(this);
            if(!Utils.getIsLocallyAvailable(this) || !check()){
                if(checkForNetwork()){
                    HttpProxyCacheServer proxy = App.getProxy(this);
                    proxyUrl = proxy.getProxyUrl(VIDEO_URL);
                    Utils.setIsLocallyAvailable(this,true);
                    Utils.setProxyUrl(this,proxyUrl);
                } else {
                    networkToast();
                }
            } else if(Utils.getFilePath(this).length()<4) {
                File cacheFile = new File(proxyUrl.substring(7));
                String localFilePath = getDir().getPath() + File.separator + videoName;
                File localFile = new File(localFilePath);
                copy(cacheFile,localFile);
                Utils.setFilePath(this,localFilePath);
                proxyUrl = localFilePath;
            } else {
                proxyUrl = Utils.getFilePath(this);
            }

            Log.d(TAG,proxyUrl);

        } catch (Exception e) {
            Log.e(TAG, "Error",e);
        }

        if (proxyUrl.length()!=0){
            videoView.setVideoPath(proxyUrl);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoView.start();
                }
            });
        } else {
            pDialog.dismiss();
        }
    }

    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        sdDir =  new File(sdDir, "SocialCops");
        if (!sdDir.exists() && !sdDir.mkdirs()) {
            Log.d(TAG, "Can't create directory to save video.");
            return null;
        } else {
            return sdDir;
        }
    }

    private void checkPermissions() {
        int hasStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasStoragePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        }
    }

    private boolean check(){
        if(proxyUrl.length()<4){
            return false;
        } else {
            File file = new File(proxyUrl.substring(7));
            return file.isFile();
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public boolean checkForNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void networkToast(){
        Toast.makeText(this, getString(R.string.network_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",videoView.getCurrentPosition());
        outState.putBoolean("isPlaying?",videoView.isPlaying());
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.pause();
        pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        pDialog.dismiss();
    }
}
