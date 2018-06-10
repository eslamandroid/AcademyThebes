package com.eaapps.thebesacademy.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by EslamAndroid on 05/02/2018.
 */
public class PermissionUtil {
    // check if version is marshmallow and above
    // used in deciding to ask runtime permission


    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static void checkPermission(Context context, String permission, PermissionAskListener permissionAskListener) {

        if (shouldAskPermission(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                permissionAskListener.onNeedPermission();
            } else {
                permissionAskListener.onPermissionPreviouslyDenied();
            }
        } else {
            permissionAskListener.onPermissionGranted();
        }
    }

    public static void canDrawOverlay(Context context, PermissionAskListener permissionAskListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                permissionAskListener.onNeedPermission();
            } else {
                permissionAskListener.onPermissionGranted();
            }
        } else {
            permissionAskListener.onPermissionGranted();
        }
    }

    public static boolean isMyFlyRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isServiceRunning?", false + "");
        return false;
    }

    public interface PermissionAskListener {
        // callback to ask permission
        void onNeedPermission();

        // callback on permission denied

        void onPermissionPreviouslyDenied();

        // callback on permission 'never show again' check and denied

        void onPermissionDisabled();

        // callback on permission granted

        void onPermissionGranted();
    }


}
