package org.wavefar.lib.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.telephony.SmsManager;
import android.text.TextUtils;

import org.wavefar.lib.base.ContainerActivity;
import org.wavefar.lib.ui.fragment.agentweb.AgentWebFragment;
import org.wavefar.lib.ui.fragment.agentweb.SmartRefreshWebFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.SEND_SMS;

/**
 * 常用意图工具类
 */
public final class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the intent is available.
     *
     * @param intent The intent.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isIntentAvailable(final Intent intent) {
        return Utils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() > 0;
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final String filePath) {
        return getInstallAppIntent(getFileByPath(filePath), false);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final File file) {
        return getInstallAppIntent(file, false);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath  The path of file.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final String filePath, final boolean isNewTask) {
        return getInstallAppIntent(getFileByPath(filePath), isNewTask);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file      The file.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        if (file == null) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = Utils.getApp().getPackageName() + ".util.provider";
            data = FileProvider.getUriForFile(Utils.getApp(), authority, file);
        }
        intent.setDataAndType(data, type);
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of uninstall app.
     *
     * @param packageName The name of the package.
     * @return the intent of uninstall app
     */
    public static Intent getUninstallAppIntent(final String packageName) {
        return getUninstallAppIntent(packageName, false);
    }

    /**
     * Return the intent of uninstall app.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of uninstall app
     */
    public static Intent getUninstallAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of launch app.
     *
     * @param packageName The name of the package.
     * @return the intent of launch app
     */
    public static Intent getLaunchAppIntent(final String packageName) {
        return getLaunchAppIntent(packageName, false);
    }

    /**
     * Return the intent of launch app.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of launch app
     */
    public static Intent getLaunchAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = Utils.getApp().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param packageName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String packageName) {
        return getLaunchAppDetailsSettingsIntent(packageName, false);
    }

    /**
     * Return the intent of launch app details settings.
     *
     * @param packageName The name of the package.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String packageName,
                                                           final boolean isNewTask) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of share text.
     *
     * @param content The content.
     * @return the intent of share text
     */
    public static Intent getShareTextIntent(final String content) {
        return getShareTextIntent(content, false);
    }

    /**
     * Return the intent of share text.
     *
     * @param content   The content.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share text
     */

    public static Intent getShareTextIntent(final String content, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imagePath The path of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content, final String imagePath) {
        return getShareImageIntent(content, imagePath, false);
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param imagePath The path of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content,
                                             final String imagePath,
                                             final boolean isNewTask) {
        if (imagePath == null || imagePath.length() == 0){ return null;}
        return getShareImageIntent(content, new File(imagePath), isNewTask);
    }

    /**
     * Return the intent of share image.
     *
     * @param content The content.
     * @param image   The file of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content, final File image) {
        return getShareImageIntent(content, image, false);
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param image     The file of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content,
                                             final File image,
                                             final boolean isNewTask) {
        if (image == null || !image.isFile()) {
            return null;
        }
        return getShareImageIntent(content, file2Uri(image), isNewTask);
    }

    /**
     * Return the intent of share image.
     *
     * @param content The content.
     * @param uri     The uri of image.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content, final Uri uri) {
        return getShareImageIntent(content, uri, false);
    }

    /**
     * Return the intent of share image.
     *
     * @param content   The content.
     * @param uri       The uri of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content,
                                             final Uri uri,
                                             final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content    The content.
     * @param imagePaths The paths of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content, final LinkedList<String> imagePaths) {
        return getShareImageIntent(content, imagePaths, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content    The content.
     * @param imagePaths The paths of images.
     * @param isNewTask  True to add flag of new task, false otherwise.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content,
                                             final LinkedList<String> imagePaths,
                                             final boolean isNewTask) {
        if (imagePaths == null || imagePaths.isEmpty()){ return null;}
        List<File> files = new ArrayList<>();
        for (String imagePath : imagePaths) {
            files.add(new File(imagePath));
        }
        return getShareImageIntent(content, files, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content The content.
     * @param images  The files of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content, final List<File> images) {
        return getShareImageIntent(content, images, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content   The content.
     * @param images    The files of images.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content,
                                             final List<File> images,
                                             final boolean isNewTask) {
        if (images == null || images.isEmpty()) {return null;}
        ArrayList<Uri> uris = new ArrayList<>();
        for (File image : images) {
            if (!image.isFile()) {continue;}
            uris.add(file2Uri(image));
        }
        return getShareImageIntent(content, uris, isNewTask);
    }

    /**
     * Return the intent of share images.
     *
     * @param content The content.
     * @param uris    The uris of images.
     * @return the intent of share images
     */
    public static Intent getShareImageIntent(final String content, final ArrayList<Uri> uris) {
        return getShareImageIntent(content, uris, false);
    }

    /**
     * Return the intent of share images.
     *
     * @param content   The content.
     * @param uris      The uris of image.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of share image
     */
    public static Intent getShareImageIntent(final String content,
                                             final ArrayList<Uri> uris,
                                             final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String packageName, final String className) {
        return getComponentIntent(packageName, className, null, false);
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String packageName,
                                            final String className,
                                            final boolean isNewTask) {
        return getComponentIntent(packageName, className, null, isNewTask);
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param bundle      The Bundle of extras to add to this intent.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String packageName,
                                            final String className,
                                            final Bundle bundle) {
        return getComponentIntent(packageName, className, bundle, false);
    }

    /**
     * Return the intent of component.
     *
     * @param packageName The name of the package.
     * @param className   The name of class.
     * @param bundle      The Bundle of extras to add to this intent.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of component
     */
    public static Intent getComponentIntent(final String packageName,
                                            final String className,
                                            final Bundle bundle,
                                            final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) {intent.putExtras(bundle);}
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of shutdown.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     *
     * @return the intent of shutdown
     */
    public static Intent getShutdownIntent() {
        return getShutdownIntent(false);
    }

    /**
     * Return the intent of shutdown.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     *
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of shutdown
     */
    public static Intent getShutdownIntent(final boolean isNewTask) {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of dial.
     *
     * @param phoneNumber The phone number.
     * @return the intent of dial
     */
    public static Intent getDialIntent(final String phoneNumber) {
        return getDialIntent(phoneNumber, false);
    }

    /**
     * Return the intent of dial.
     *
     * @param phoneNumber The phone number.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of dial
     */
    public static Intent getDialIntent(final String phoneNumber, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     * @return the intent of call
     */
    @RequiresPermission(CALL_PHONE)
    public static Intent getCallIntent(final String phoneNumber) {
        return getCallIntent(phoneNumber, false);
    }

    /**
     * Return the intent of call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of call
     */
    @RequiresPermission(CALL_PHONE)
    public static Intent getCallIntent(final String phoneNumber, final boolean isNewTask) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        return getIntent(intent, isNewTask);
    }

    /**
     * Return the intent of send SMS.
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     * @return the intent of send SMS
     */
    public static Intent getSendSmsIntent(final String phoneNumber, final String content) {
        return getSendSmsIntent(phoneNumber, content, false);
    }

    /**
     * Return the intent of send SMS.
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     * @param isNewTask   True to add flag of new task, false otherwise.
     * @return the intent of send SMS
     */
    public static Intent getSendSmsIntent(final String phoneNumber,
                                          final String content,
                                          final boolean isNewTask) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return getIntent(intent, isNewTask);
    }

    /**
     * Send sms silently.
     * <p>Must hold {@code <uses-permission android:name="android.permission.SEND_SMS" />}</p>
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    @RequiresPermission(SEND_SMS)
    public static void sendSmsSilent(final String phoneNumber, final String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, new Intent("send"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri The uri of output.
     * @return the intent of capture
     */
    public static Intent getCaptureIntent(final Uri outUri) {
        return getCaptureIntent(outUri, false);
    }

    /**
     * Return the intent of capture.
     *
     * @param outUri    The uri of output.
     * @param isNewTask True to add flag of new task, false otherwise.
     * @return the intent of capture
     */
    public static Intent getCaptureIntent(final Uri outUri, final boolean isNewTask) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) {return true;}
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Uri file2Uri(final File file) {
        if (file == null){ return null;}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Utils.getApp().getPackageName() + ".util.provider";
            return FileProvider.getUriForFile(Utils.getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    //页面跳转方法
    ////////////////////////////////////////////////////////////////////////
    /**
     * 重定向页面无参数
     *
     * @param activity
     */
    public static void  redirect(Class<?> activity) {
        redirectAndPrameter(activity,null);
    }

    /**
     * 重定向页面并带参数
     *
     * @param activity
     * @param extras
     */
    public static void redirectAndPrameter(Class<?> activity, Bundle extras) {

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (activity == null) {
            throw new RuntimeException("Acticity or Bundle cannot be null!");
        }
        intent.setClass(Utils.getApp(), activity);
        if (extras != null) {
            intent.putExtras(extras);
        }
        Utils.getApp().startActivity(intent);
    }

    /**
     * 重定向页面并带参数和回调
     *
     * @param context     当前Activity
     * @param activity    要访问的Activity
     * @param extras      可选 不需要传null
     * @param requestCode
     */
    public static void redirectAndPrameterResult(Activity context, Class<?> activity, Bundle extras, int
            requestCode) {

        Intent intent = new Intent();
        if (activity == null || context == null) {
            throw new RuntimeException("Acticity cannot or current Activity  be null!");
        }
        intent.setClass(context, activity);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转Fragment公共容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getName()
     * @param bundle        跳转所携带的信息
     */
    public static  void startContainerActivity(String canonicalName, Bundle bundle) {
        Bundle bd = new Bundle();
        if (bundle != null){
            bd.putBundle(ContainerActivity.BUNDLE, bundle);
        }
        bd.putString(ContainerActivity.FRAGMENT,canonicalName);
        redirectAndPrameter(ContainerActivity.class,bd);
    }

    /**
     * 跳转Fragment公共容器页面
     * @param canonicalName 规范名 : Fragment.class.getName()
     */
    public static void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName,null);
    }

    /**
     * 跳转Fragment公共容器页面，带回调监听
     *
     * @param activity 当前Activity
     * @param canonicalName 规范名 : Fragment.class.getName()
     * @param bundle        跳转所携带的信息
     * @param requestCode 请求code
     */
    public static  void startContainerActivity(Activity activity,String canonicalName, Bundle bundle,int requestCode) {
        Bundle bd = new Bundle();
        if (bundle != null){
            bd.putBundle(ContainerActivity.BUNDLE, bundle);
        }
        bd.putString(ContainerActivity.FRAGMENT,canonicalName);
        redirectAndPrameterResult(activity,ContainerActivity.class,bd,requestCode);
    }


    /**
     * 获取Bundle 数据对象
     *
     * @return
     */
    public static Bundle getBundle() {
        Bundle bundle = null;
        Intent intent = ((Activity)Utils.getTopActivityOrApp()).getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
        }
        return bundle;
    }

    /**
     * 启动网页
     * @param url 访问路径
     * @param title 自定义导航标题
     */
    public static  void startWeb(String url,String title)
    {
        Bundle bundle = new Bundle();
        bundle.putString(AgentWebFragment.URL_KEY,url);
        if (!TextUtils.isEmpty(title)) {
            bundle.putString(AgentWebFragment.TITLE_KEY, title);
        }
        startContainerActivity(SmartRefreshWebFragment.class.getName(),bundle);
    }

    /**
     * 启动网页
     * @param url 访问路径
     */
    public static  void startWeb(String url)
    {
        startWeb(url,null);
    }
}
