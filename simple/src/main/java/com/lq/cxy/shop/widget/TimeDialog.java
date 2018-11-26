package com.lq.cxy.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.widget.spinnerwheel.AbstractWheel;
import com.lq.cxy.shop.widget.spinnerwheel.adapters.ArrayWheelAdapter;

import org.wavefar.lib.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * 日期格式化，当前时间基础上加N时间段显示
 * @author Administrator
 */
public class TimeDialog extends Dialog implements OnClickListener {

    private Context mContext;

    private AbstractWheel mTimeAbstractWheel;

    private ArrayList<String> arrayList = new ArrayList<>();


    private OnItemChangeListener mChangeListener;


    public TimeDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
        initDate();
        initView();
    }

    private void initDate() {
            arrayList.add("08:00-10:00");
            arrayList.add("10:00-12:00");
            arrayList.add("12:00-14:00");
            arrayList.add("14:00-16:00");
            arrayList.add("16:00-18:00");
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_public, null);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_ok).setOnClickListener(this);

        mTimeAbstractWheel = findViewById(R.id.name);
        mTimeAbstractWheel.setViewAdapter(new ArrayWheelAdapter<>(mContext,arrayList.toArray(new String[]{}) ));
        mTimeAbstractWheel.setCurrentItem(0);

        Window window = getWindow();
        /**
         * 设置显示动画
         */
        window.setWindowAnimations(R.style.DialogBottomAnim);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ScreenUtils.getScreenHeight();
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(wl);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_ok:
                mChangeListener.onItemChange(arrayList.get(mTimeAbstractWheel.getCurrentItem()));
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface OnItemChangeListener {
        void onItemChange(String date);
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        mChangeListener = listener;
    }

}
