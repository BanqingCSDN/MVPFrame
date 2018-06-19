package com.mvpframe.util;

import android.content.Context;
import android.widget.Toast;

import com.mvpframe.app.MApplication;
import com.mvpframe.constant.Constants;

/**
 * <提示公共类>
 */
public final class ToastUtil {
    private static Toast toast;

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextShort(Context context, String msg) {
        if (MApplication.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextLong(Context context, String msg) {
        if (MApplication.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    /**
     * <显示失败信息 >
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextError(Context context) {
        makeTextShort(context, Constants.ERROR_MESSAGE);
    }

    /**
     * <显示失败信息 >
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextErrorLong(Context context) {
        makeTextLong(context, Constants.ERROR_MESSAGE);
    }

    public static void destory() {
        toast = null;
    }
}
