package org.wavefar.lib.binding.viewadapter.webview;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;


/**
 * 简单WebView实现
 * @author summer
 */
public class ViewAdapter {
    @BindingAdapter({"loadHtml"})
    public static void loadHtml(WebView webView, String html) {
        if (!TextUtils.isEmpty(html)) {
            webSetting(webView);
            html = String.format("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0\">%s",html);
            html = html.replaceAll("<img", "<img style=\"height:auto;width:100%;\"");
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView webView, String url) {
        if (!TextUtils.isEmpty(url)) {
            webSetting(webView);
            webView.loadUrl(url);
        }
    }

    /**
     * WebView设置参数
     * @param webView
     */
    private static void  webSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }
}
