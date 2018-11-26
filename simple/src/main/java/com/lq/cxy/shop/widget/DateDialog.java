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
import org.wavefar.lib.utils.TimeUtil;

import java.util.ArrayList;

/**
 * 日期格式化，当前时间基础上添加N天数显示
 * @author Administrator
 */
public class DateDialog extends Dialog implements OnClickListener {

    private Context mContext;

    private AbstractWheel mTimeAbstractWheel;

    private ArrayList<String> arrayList = new ArrayList<>();

    private static final int DAY_NUM = 7;

    private OnItemChangeListener mChangeListener;


    public DateDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
        initDate();
        initView();
    }

    private void initDate() {
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i< DAY_NUM; i++) {
            String  pattern = "MM月dd日 E";
            if (i==0) {
                pattern = "MM月dd日 E 今天";
            }else if (i==1) {
                pattern = "MM月dd日 E 明天";
            }else if (i==2) {
                pattern = "MM月dd日 E 后天";
            }
            String timeStr = TimeUtil.getTime2String(currentTime + i * 86400000L,pattern);
            arrayList.add(timeStr);
        }

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
