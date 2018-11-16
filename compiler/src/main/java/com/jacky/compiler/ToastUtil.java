package com.jacky.compiler;

/**
 * Created by jacky on 2018/11/16.
 */

@interface ToastUtil {
    String value="package com.jacky.util;\n" +
            "\n" +
            "import android.app.Activity;\n" +
            "import android.content.Context;\n" +
            "import android.graphics.PixelFormat;\n" +
            "import android.support.annotation.StringRes;\n" +
            "import android.support.design.widget.Snackbar;\n" +
            "import android.support.v4.app.Fragment;\n" +
            "import android.view.Gravity;\n" +
            "import android.view.View;\n" +
            "import android.view.WindowManager;\n" +
            "import android.widget.TextView;\n" +
            "import android.widget.Toast;\n" +
            "\n" +
            "import com.jacky.log.Logger;\n" +
            "\n" +
            "import static android.content.Context.WINDOW_SERVICE;\n" +
            "\n" +
            "/**\n" +
            " * Created by jacky on 2017-07-10.\n" +
            " */\n" +
            "\n" +
            "public final class ToastUtil {\n" +
            "\n" +
            "    private static Toast mToast;\n" +
            "\n" +
            "    public static void showMsg(@StringRes int resId) {\n" +
            "        get(Toast.LENGTH_SHORT);\n" +
            "        mToast.setText(resId);\n" +
            "        mToast.show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showMsg(CharSequence text) {\n" +
            "        get(Toast.LENGTH_SHORT);\n" +
            "        mToast.setText(text);\n" +
            "        mToast.show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showLongMsg(@StringRes int resId) {\n" +
            "       get(Toast.LENGTH_LONG);\n" +
            "        mToast.setText(resId);\n" +
            "        mToast.show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showLongMsg(CharSequence text) {\n" +
            "       get(Toast.LENGTH_LONG);\n" +
            "        mToast.setText(text);\n" +
            "        mToast.show();\n" +
            "    }\n" +
            "\n" +
            "    private static void get(int time) {\n" +
            "            if(mToast != null) {\n" +
            "                mToast.cancel();\n" +
            "            }\n" +
            "            mToast = Toast.makeText(%s, \"\", time);\n" +
            "            mToast.setGravity(Gravity.CENTER, 0, 0);\n" +
            "            mToast.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {\n" +
            "                        @Override\n" +
            "                        public void onViewAttachedToWindow(View v) {}\n" +
            "\n" +
            "                        @Override\n" +
            "                        public void onViewDetachedFromWindow(View v) {\n" +
            "                               mToast = null;\n" +
            "                        }\n" +
            "                    });\n" +
            "        }\n" +
            "\n" +
            "    public static void showSnackBar(Activity activity, CharSequence text) {\n" +
            "        View view = activity.getWindow().getDecorView();\n" +
            "        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showLongSnackBar(Activity activity, CharSequence text) {\n" +
            "        View view = activity.getWindow().getDecorView();\n" +
            "        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showSnackBar(Fragment fragment, CharSequence text) {\n" +
            "        View view = fragment.getView();\n" +
            "        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();\n" +
            "    }\n" +
            "\n" +
            "    public static boolean showToast(Context context, String string) {\n" +
            "        //这个方法在小米mx5上不兼容\n" +
            "        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);\n" +
            "        WindowManager.LayoutParams params = new WindowManager.LayoutParams();\n" +
            "        params.type = WindowManager.LayoutParams.TYPE_TOAST;\n" +
            "        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;\n" +
            "        params.format = PixelFormat.TRANSLUCENT;\n" +
            "        params.width = WindowManager.LayoutParams.WRAP_CONTENT;\n" +
            "        params.height = WindowManager.LayoutParams.WRAP_CONTENT;\n" +
            "        params.gravity = Gravity.CENTER;\n" +
            "\n" +
            "        final TextView view = new TextView(context);\n" +
            "        view.setText(string);\n" +
            "        view.setTextColor(0xffffffff);\n" +
            "        view.setBackgroundColor(0x7f000000);\n" +
            "        view.postDelayed(new Runnable() {\n" +
            "            @Override\n" +
            "            public void run() {\n" +
            "                wm.removeView(view);\n" +
            "            }\n" +
            "        }, 1000);\n" +
            "        try {\n" +
            "            wm.addView(view, params);\n" +
            "        } catch (Exception e) {\n" +
            "            Logger.w(e);\n" +
            "            return false;\n" +
            "        }\n" +
            "        return true;\n" +
            "    }\n" +
            "}";
}
