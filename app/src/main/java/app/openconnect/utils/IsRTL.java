package app.openconnect.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;


public class IsRTL {
    public static void ifSupported(Activity mContext) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mContext.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

    }
}
