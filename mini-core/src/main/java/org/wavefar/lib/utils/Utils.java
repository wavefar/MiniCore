package org.wavefar.lib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 获取APP上下文及生命周期管理
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(@NonNull final Context context) {
        init((Application) context.getApplicationContext());
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(@NonNull final Application app) {
        if (sApplication == null) {
            Utils.sApplication = app;
            Utils.sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        }
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null){ return sApplication;}
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            init((Application) app);
            return sApplication;
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("未初始化库异常，在使用之前，你需要在'Application.onCreate()'\" +\n" +
                "                    \"(或者其他位置)中调用'Utils.init(Context)初始化。");
    }

    public static ActivityLifecycleImpl getActivityLifecycle() {
        return ACTIVITY_LIFECYCLE;
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIFECYCLE.mActivityList;
    }

    static Context getTopActivityOrApp() {
        if (isAppForeground()) {
            Activity topActivity = ACTIVITY_LIFECYCLE.getTopActivity();
            return topActivity == null ? Utils.getApp() : topActivity;
        } else {
            return Utils.getApp();
        }
    }

    static boolean isAppForeground() {
        ActivityManager am =
                (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {return false;}
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {return false;}
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(Utils.getApp().getPackageName());
            }
        }
        return false;
    }

    public static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

        final LinkedList<Activity>                        mActivityList      = new LinkedList<>();
        final HashMap<Object, OnAppStatusChangedListener> mStatusListenerMap = new HashMap<>();

        private int mForegroundCount = 0;
        private int mConfigCount     = 0;

        void addListener(final Object object, final OnAppStatusChangedListener listener) {
            mStatusListenerMap.put(object, listener);
        }

        void removeListener(final Object object) {
            mStatusListenerMap.remove(object);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivity(activity);
            if (mForegroundCount <= 0) {
                postStatus(true);
            }
            if (mConfigCount < 0) {
                ++mConfigCount;
            } else {
                ++mForegroundCount;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {/**/}

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity.isChangingConfigurations()) {
                --mConfigCount;
            } else {
                --mForegroundCount;
                if (mForegroundCount <= 0) {
                    postStatus(false);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityList.remove(activity);
        }

        private void postStatus(final boolean isForeground) {
            if (mStatusListenerMap.isEmpty()){
                return;
            }
            for (OnAppStatusChangedListener onAppStatusChangedListener : mStatusListenerMap.values()) {
                if (onAppStatusChangedListener == null) {
                    return;
                }
                if (isForeground) {
                    onAppStatusChangedListener.onForeground();
                } else {
                    onAppStatusChangedListener.onBackground();
                }
            }
        }

        private void setTopActivity(final Activity activity) {
            if (activity.getClass() == PermissionUtils.PermissionActivity.class){ return;}
            if (mActivityList.contains(activity)) {
                if (!mActivityList.getLast().equals(activity)) {
                    mActivityList.remove(activity);
                    mActivityList.addLast(activity);
                }
            } else {
                mActivityList.addLast(activity);
            }
        }

       public Activity getTopActivity() {
            if (!mActivityList.isEmpty()) {
                final Activity topActivity = mActivityList.getLast();
                if (topActivity != null) {
                    return topActivity;
                }
            }
            // using reflect to get top activity
            try {
                @SuppressLint("PrivateApi")
                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
                Field activitiesField = activityThreadClass.getDeclaredField("mActivityList");
                activitiesField.setAccessible(true);
                Map activities = (Map) activitiesField.get(activityThread);
                if (activities == null) {return null;}
                for (Object activityRecord : activities.values()) {
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        Activity activity = (Activity) activityField.get(activityRecord);
                        setTopActivity(activity);
                        return activity;
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
                e.printStackTrace();
            }
           return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnAppStatusChangedListener {
        void onForeground();

        void onBackground();
    }

    /**
     * 是否是第一次访问app
     * @return
     */
    static  boolean firstStartAPP() {
        try {
            if (!FileUtils.checkFileIsExists(getApp(), "lock")) {
                FileUtils.writeLocalFile(getApp(), "lock", "isFrist".getBytes());
                return true;
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static long lastClickTime;

    /**
     * 判断在一定时间内范围点击重复点击
     * @param milliseconds 毫秒数
     */
    public static boolean isFastDoubleClick(int milliseconds) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < milliseconds) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断类中是否包含EventBus的注解
     * @param classz
     * @return
     */
    public static boolean hasEventBus(Class<?> classz) {
        Method[] methods = classz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定类中是否包含指定的注解
     * @param classz 指定的类
     * @param annotation 指定的注解
     * @return
     */
    public static boolean hasEventBus(Class<?> classz,Class<? extends Annotation> annotation) {
        Method[] methods = classz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
}