package com.lq.cxy.shop.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.PublicApi;
import com.lq.cxy.shop.model.entity.VersionInfoEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.json.JSONObject;
import org.wavefar.lib.net.DownLoadManager;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.net.download.ProgressCallBack;
import org.wavefar.lib.utils.AppUtils;
import org.wavefar.lib.utils.FileUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.PermissionUtils;
import org.wavefar.lib.utils.ToastUtils;
import org.wavefar.lib.utils.constant.PermissionConstants;
import org.wavefar.lib.utils.helper.DialogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * APP版本检查及下载
 * @author Administrator
 */
public class AppDownloadManager {


    private File tempFile = null;
    /**
     * 更新应用版本标记
      */
    private static final int UPDARE_TOKEN = 0x29;

    /**
     * 准备安装新版本应用标记
      */
    private static final int INSTALL_TOKEN = 0x31;

    private Context mContext;

    /**
     * 下载应用的对话框
     */
    private Dialog dialog;

    /**
     * 下载应用的进度条
      */
    private ProgressBar progressBar;

    /**
     * 进度条的当前刻度值
     */
    private int curProgress;

    /**
     * 用户是否取消下载
     */
    private boolean isCancel;


    private PublicApi mPublicApi;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDARE_TOKEN:
                    progressBar.setProgress(curProgress);
                    break;
                case INSTALL_TOKEN:
                    installApp();
                    break;
                    default:break;
            }
        }
    };

    /**
     * 显示下载进度对话框
     */
    private void showDownloadDialog() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.progress_bar, null);
        progressBar = view.findViewById(R.id.progressBar);
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isCancel = true;
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //请求动态权限
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        downloadApp();
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();
    }

    /**
     * 下载新版本应用
     */
    private void downloadApp() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(versionInfoEntity.getVersionUrl());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    in = conn.getInputStream();
                    File dirFile = FileUtils.getSDRootPath();
                    // 判断sdcard是否存在,不存在放到手机内置内存中
                    if (null != dirFile) {
                        File rootFile = new File(dirFile, "/"
                                + Constant.CACHE_ROOT);
                        if (!rootFile.exists() && !rootFile.isDirectory()) {
                            rootFile.mkdir();
                        }
                        tempFile = new File(dirFile,
                                "/" + Constant.CACHE_ROOT
                                        + "app.apk");
                    } else {
                        // 在内存中存储
                        tempFile = new File(Constant.CACHE_ROOT,"app.apk");
                    }
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                    tempFile.createNewFile();
                    out = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if (isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDARE_TOKEN);
                        if (readedLength >= fileLength) {
                            dialog.dismiss();
                            // 下载完毕，通知安装
                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        if (!tempFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = IntentUtils.getInstallAppIntent(tempFile);
        mContext.startActivity(intent);
    }

    public AppDownloadManager(Context context, boolean showDialog) {
        mContext = context;
        mShowDialog = showDialog;
        mPublicApi = new PublicApi();
    }

    private VersionInfoEntity versionInfoEntity;
    private boolean mShowDialog;

    public void update() {
        mPublicApi.getVersionInfo(new BaseResultCallback<BaseResponseEntity<VersionInfoEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<VersionInfoEntity> response) {
                if (response.isSuccess()) {
                    versionInfoEntity = response.getContent();
                    if (versionInfoEntity != null) {
                        int versionCode = Integer.parseInt(versionInfoEntity.getVersionCode());
                        if (AppUtils.getAppVersionCode() < versionCode) {
                            showUpdataDialog();
                        }else {
                            if (mShowDialog) {
                                ToastUtils.showShort("已是最新版本");
                            }
                        }
                    }
                }
            }
        });
    }

    private void showUpdataDialog() {
        Builder builer = new Builder(mContext);
        builer.setTitle("版本更新");
        builer.setMessage(versionInfoEntity.getVersionDesc());
        builer.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDownloadDialog();
            }
        });
        builer.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                AppUtils.exitApp();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
