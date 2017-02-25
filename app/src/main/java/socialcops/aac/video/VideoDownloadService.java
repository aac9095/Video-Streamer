package socialcops.aac.video;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Ayush on 25-02-2017.
 */

public interface VideoDownloadService {
    @GET
    Call<ResponseBody> downloadVideo(@Url String videoUrl);
}
