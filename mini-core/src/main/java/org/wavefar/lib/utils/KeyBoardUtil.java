package org.wavefar.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘工具类
 * @author summer
 */
public class KeyBoardUtil {

    private KeyBoardUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 动态显示软键盘
     *
     * @param edit 输入框
     */
    public static void showSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(edit, 0);
        }
    }

    /**
     * 切换键盘显示与否状态
     * @param context 上下文
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}