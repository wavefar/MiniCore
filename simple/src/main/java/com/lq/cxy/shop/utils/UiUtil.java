package com.lq.cxy.shop.utils;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lq.cxy.shop.R;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.wavefar.lib.base.BaseApplication;

import java.util.List;

public class UiUtil {
    public static void showOptionDialog(Context context, List data, OnOptionsSelectListener l) {
        OptionsPickerView cartOptionView = new OptionsPickerBuilder(context, l)
                .setSubmitColor(context.getResources().getColor(R.color.colorAccent))
                .setCancelColor(context.getResources().getColor(R.color.colorAccent))
                .isDialog(true)
                .build();
        cartOptionView.setPicker(data);
        cartOptionView.setDialogOutSideCancelable();
        cartOptionView.show();
    }

    public static void showDateSelect(Context context, boolean isDialog, OnTimeSelectListener l) {
        TimePickerView endPickerView = new TimePickerBuilder(context, l)
                .setSubmitColor(context.getResources().getColor(R.color.colorAccent))
                .setCancelColor(context.getResources().getColor(R.color.colorAccent))
                .isDialog(isDialog)
                .build();
        endPickerView.show();
    }

    public static ISListConfig getImageSelectorConfig(Context context, int aspectX, int aspectY, int outputX, int outputY) {
        // 自由配置选项
        return new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(true)
                // “确定”按钮背景色
                .btnBgColor(BaseApplication.getInstance().getResources().getColor(R.color.colorAccent))
                // “确定”按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(BaseApplication.getInstance().getResources().getColor(R.color.colorAccent))
                // 返回图标ResId
                .backResId(R.drawable.back_white)
                // 标题
                .title("图片选择")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(BaseApplication.getInstance().getResources().getColor(R.color.colorAccent))
//            // 裁剪大小。needCrop为true的时候配置
                .cropSize(aspectX, aspectY, outputX, outputY)
                .needCrop(true)
                // 最大选择图片数量，默认9
                .maxNum(1)
                .build();
    }

    public static CityConfig CITY_CONFIG = new CityConfig.Builder().title("选择城市")//标题
            .build();
}
