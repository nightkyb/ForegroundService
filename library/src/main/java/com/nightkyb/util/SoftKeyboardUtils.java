package com.nightkyb.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;

/**
 * 软键盘工具类
 * Author: nightkyb
 */
public class SoftKeyboardUtils {

    public static void openSoftKeyboard(EditText et) {
        if (et != null) {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, 0);
        }
    }

    public static void closeSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            try {
                View view = activity.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                view.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSoftKeyboard(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isSoftKeyboardOpen(Activity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int heightDiffer = screenHeight - rect.bottom - DimensionUtils.getNavigationBarHeightIfShow(activity);
        Logger.d("高度差：" + heightDiffer);
        //当高度差大于屏幕1/4（严格上是0），认为是输入法弹出变动，可能会有特殊机型会失败
        return heightDiffer > screenHeight / 4;
    }

  /*  public static int getSoftKeyboardHeight(Activity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int heightDiffer = screenHeight - rect.bottom - DimensionUtils.getNavigationBarHeightIfShow(activity);
        LogUtils.i("高度差：" + heightDiffer);
        //当高度差大于屏幕1/4（严格上是0），认为是输入法弹出变动，可能会有特殊机型会失败
        return heightDiffer;
    }*/

    /**
     * 输入法键盘弹出时将界面滚动到指定位置<br/>
     * 要滚动的距离=控件距屏幕顶部距离+控件高度-输入法弹出后的activity高度-通知栏高度
     *
     * @param activity
     * @param lyRootID 根布局id
     * @param vID      目标视图id
     * @param svID     滚动视图id
     */
    public static void pullKeywordTop(
            final Activity activity, final int lyRootID,
            final int vID, final int svID
    ) {
        ViewGroup ly = (ViewGroup) activity.findViewById(lyRootID);
        //获取屏幕高度，根据经验，输入法弹出高度一般在屏幕1/3到1/2之间
        final int defaultHeight = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        final int mKeyHeight = defaultHeight / 4;
        ly.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View v, int left, int top, int right,
                    int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom
            ) {
                //获取根布局前后高度差
                int height = oldBottom - bottom;
                ScrollView sv = (ScrollView) activity.findViewById(svID);
                if (height > mKeyHeight) {//当高度差大于屏幕1/4，认为是输入法弹出变动，可能会有特殊机型会失败
                    final int lybottom = bottom;
                    sv.post(new Runnable() {//用post防止有时输入法会自动滚动覆盖我们手动滚动
                        @Override
                        public void run() {
                            ScrollView runSv = (ScrollView) activity.findViewById(svID);
                            //获取要滚动至的控件到屏幕顶部高度
                            View v = (View) activity.findViewById(vID);
                            int[] loca = new int[2];
                            v.getLocationOnScreen(loca);
                            //这种通知栏高度获取方法必须在布局构建完毕后才能生效，否则获取为0
                            Rect frame = new Rect();
                            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                            int statusBarHeight = frame.top;
                            // 要滚动的距离=控件距屏幕顶部距离+控件高度-输入法弹出后的activity高度-通知栏高度
                            int scrollHeight = loca[1] + v.getHeight() - lybottom - statusBarHeight;
                            if (scrollHeight > 0) {
                                runSv.scrollBy(0, scrollHeight);
                            }
                        }
                    });
                } else if (-height > mKeyHeight) {//当输入法收起，回滚回顶部
                    sv.scrollTo(0, 0);
                }
            }
        });
    }
}
