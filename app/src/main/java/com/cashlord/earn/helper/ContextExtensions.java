package com.cashlord.earn.helper;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

public class ContextExtensions {

    public static boolean isAndroid13(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public static void showLongToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showShortToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
