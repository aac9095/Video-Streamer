package socialcops.aac.video;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ayush on 26-02-2017.
 */

public class Utils {
    public static void setIsLocallyAvailable(Context context, boolean choice) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();
        e.putBoolean(context.getString(R.string.isLocallyAvailable), choice);
        e.apply();
    }

    public static boolean getIsLocallyAvailable(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        return preferences.getBoolean(context.getString(R.string.isLocallyAvailable),false);
    }

    public static void setFilePath(Context context, String path) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();
        e.putString(context.getString(R.string.filePath), path);
        e.apply();
    }

    public static String getFilePath(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.filePath),"");
    }

    public static void setProxyUrl(Context context, String proxyUrl) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();
        e.putString(context.getString(R.string.proxyUrl), proxyUrl);
        e.apply();
    }

    public static String getProxyUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(String.valueOf(R.string.app_name),
                Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.proxyUrl),"");
    }
}
