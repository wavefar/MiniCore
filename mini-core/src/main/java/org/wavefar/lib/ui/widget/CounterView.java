package org.wavefar.lib.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.wavefar.lib.R;
import org.wavefar.lib.utils.ToastUtils;


/**
 * 购物车，计数
 * @author summer
 */
public class CounterView extends LinearLayout implements View.OnClickListener, TextWatcher {
    /**
     * 最大的数量
     **/
    public static final int MAX_VALUE = 100;

    /**
     * 最小的数量
     **/
    public static final int MIN_VALUE = 1;

    /**
     * 数量
     */
    private int countValue = 1;

    private ImageView addIv, delIv;

    private EditText countEt;

    private IChangeCountCallback callback;

    private int maxValue = MAX_VALUE;


    public void setCallback(IChangeCountCallback c) {
        this.callback = c;
    }

    private Context mContext;

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context, attrs);
    }

    /**
     * 功能描述：设置最大数量
     * 参数：
     */
    public void setMaxValue(int max) {
        this.maxValue = max;
    }


    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.counter_size);

        int maxValue = a.getInt(R.styleable.counter_size_count_max, 100);

        setMaxValue(maxValue);

        LayoutInflater.from(mContext).inflate(R.layout.count_view, this);

        delIv = findViewById(R.id.iv_count_minus);
        delIv.setOnClickListener(this);

        addIv =  findViewById(R.id.iv_count_add);
        addIv.setOnClickListener(this);

        countEt = findViewById(R.id.et_count);
        countEt.addTextChangedListener(this);
        a.recycle();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_count_add) {
            addAction();

        } else if (i == R.id.iv_count_minus) {
            minuAction();
        }
    }

    /**
     * 添加操
     */
    private void addAction() {
        countValue++;
        btnChangeWord();
    }


    /**
     * 删除操作
     */
    private void minuAction() {
        countValue--;
        btnChangeWord();
    }

    /**
     * 功能描述：
     * 参数：boolean 是否需要重新赋值
     */
    private void changeWord(boolean needUpdate) {
        if (needUpdate) {
            countEt.removeTextChangedListener(this);
            //不为空的时候才需要赋值
            if (!TextUtils.isEmpty(countEt.getText().toString().trim())) {
                countEt.setText(String.valueOf(countValue));
            }
            countEt.addTextChangedListener(this);
        }
        countEt.setSelection(countEt.getText().toString().trim().length());

        if (callback != null) {
            callback.change(countValue);
        }
    }

    private void btnChangeWord() {
        delIv.setEnabled(countValue > MIN_VALUE);
        addIv.setEnabled(countValue < maxValue);
        countEt.setText(String.valueOf(countValue));
        countEt.setSelection(countEt.getText().toString().trim().length());
        if (callback != null) {
            callback.change(countValue);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean needUpdate = false;
        if (!TextUtils.isEmpty(s)) {
            countValue = Integer.valueOf(s.toString());
            if (countValue <= MIN_VALUE) {
                countValue = MIN_VALUE;
                delIv.setEnabled(false);
                addIv.setEnabled(true);
                if (needUpdate) {
                    ToastUtils.showLong(String.format("最少添加%s个数量", MIN_VALUE));
                }
                needUpdate = true;
            } else if (countValue >= maxValue) {
                countValue = maxValue;
                delIv.setEnabled(true);
                addIv.setEnabled(false);
                if (needUpdate) {
                    ToastUtils.showLong(String.format("最多只能添加%s个数量", maxValue));
                }
                needUpdate = true;

            } else {
                delIv.setEnabled(true);
                addIv.setEnabled(true);
            }
        } else {  //如果编辑框被清空了，直接填1
            countValue = 1;
            delIv.setEnabled(false);
            addIv.setEnabled(true);
            needUpdate = true;
            ToastUtils.showLong(String.format("最少添加%s个数量", MIN_VALUE));

        }
        changeWord(needUpdate);
    }

    /**
     * 监听选择技能数量的变化
     * @author summer
     */
    public interface IChangeCountCallback {

        /**
         * 发生改变
         * @param count
         */
        void change(int count);

    }
}
