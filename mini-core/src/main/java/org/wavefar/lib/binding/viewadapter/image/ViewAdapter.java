package org.wavefar.lib.binding.viewadapter.image;


import android.app.Activity;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.wavefar.lib.utils.Utils;

/**
 * 基于Glide封装图片加载
 * @author summer
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Activity activity = Utils.getActivityLifecycle().getTopActivity();
            if (activity != null && !activity.isDestroyed()) {
                Glide.with(activity)
                        .load(url)
                        .apply(new RequestOptions().placeholder(placeholderRes))
                        .into(imageView);
            }
        }
    }
}

