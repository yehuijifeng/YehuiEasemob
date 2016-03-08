package com.yehui.utils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import com.yehui.utils.application.ActivityCollector;
import com.yehui.utils.contacts.SettingContact;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Luhao on 2016/3/1.
 */
public class AppUtil {

    /**
     * 防止被实例化
     */
    private AppUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得app的包名
     *
     * @param pID
     * @return
     */
    public static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断是否第一次进入应用
     *
     * @param context
     * @return
     */
    public static boolean isOneStart(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, SettingContact.YEHUI_SHARE);
        Boolean isOneStart = sharedPreferencesUtil.getBoolean(SettingContact.IS_ONE_START, true);//获取这个值，如果没有这个值则去第二个参数，即取默认值
        if (isOneStart) {//第一次
            sharedPreferencesUtil.saveBoolean(SettingContact.IS_ONE_START, false);
            return true;
        }
        return false;
    }

    /**
     * 判断该activity是否处于栈顶
     * android 5.0以后弃用，有时候判断不准确，慎用！
     *
     * @param activty
     * @return
     */
    public static boolean isTopActivity(Activity activty) {
        ActivityManager activityManager = (ActivityManager) activty.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = activityManager.getRunningTasks(1);
        if (runningTaskInfoList != null) {
            String topActivity = runningTaskInfoList.get(0).topActivity.toString();
            return topActivity.equals(activty.getComponentName().toString());
        } else
            return false;
    }


    private static Handler handler = new Handler();

    /**
     * 重启app
     */
    public static void reStartApp(final Context context, final Class activityClass) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(context, activityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent restartIntent = PendingIntent.getActivity(
                        context, 0, intent, 0);
                //退出程序
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                        restartIntent); // 1秒钟后重启应用
                ActivityCollector.finishAll();
            }
        }, 1000);
    }

    /**
     * 获得sdk版本号
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            LogUtil.e(e.toString());
        }
        return version;
    }


    /*
    * 获取程序的签名
    */
    public static String getAppSignature(Context context, String packname) {
        try {
            PackageManager pm;
            pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得应用报名
     *
     * @param context
     * @return
     */
    public static String getAppPackName(Context context) {
        PackageInfo info;
        String packageNames = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            //String versionName = info.versionName;
            // 当前版本的版本号
            //int versionCode = info.versionCode;
            // 当前版本的包名
            packageNames = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return packageNames;
    }
}
