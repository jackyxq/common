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
            "import android.os.Build;\n" +
            "import android.os.Handler;\n" +
            "import android.os.Message;\n" +
            "import android.util.Log;\n" +
            "import android.view.Gravity;\n" +
            "import android.view.View;\n" +
            "import android.view.WindowManager;\n" +
            "import android.widget.TextView;\n" +
            "import android.widget.Toast;\n" +
            "\n" +
            "import androidx.annotation.StringRes;\n" +
            "import androidx.fragment.app.Fragment;\n" +
            "\n" +
            "import com.google.android.material.snackbar.Snackbar;\n" +
            "import java.lang.reflect.Field;\n" +
            "\n" +
            "import static android.content.Context.WINDOW_SERVICE;\n" +
            "\n" +
            "/**\n" +
            " * Created by jacky on 2017-07-10.\n" +
            " */\n" +
            "\n" +
            "public final class ToastUtil {\n" +
            "\n" +
            "    private static boolean mIsHookInit = false;\n" +
            "    private static Field sField_TN;\n" +
            "    private static Field sField_TN_Handler;\n" +
            "    private static final String FIELD_NAME_TN = \"mTN\";\n" +
            "    private static final String FIELD_NAME_HANDLER = \"mHandler\";\n" +
            "    private static Toast mToast;\n" +
            "\n" +
            "    //只有android 7.* 系统会\n" +
            "    //参考文档： https://blog.csdn.net/tksnail/article/details/103841070\n" +
            "    private static boolean isNeedCheckToast() {\n" +
            "        int cur = Build.VERSION.SDK_INT;\n" +
            "        return Build.VERSION_CODES.N == cur || Build.VERSION_CODES.N_MR1 == cur;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 通过hook进行替换Toast.TN.Handler\n" +
            "     */\n" +
            "    private static Toast hookToast(Toast toast) {\n" +
            "        try {\n" +
            "            if(!mIsHookInit) {\n" +
            "                sField_TN = Toast.class.getDeclaredField(FIELD_NAME_TN);\n" +
            "                sField_TN.setAccessible(true);\n" +
            "                sField_TN_Handler = sField_TN.getType().getDeclaredField(FIELD_NAME_HANDLER);\n" +
            "                sField_TN_Handler.setAccessible(true);\n" +
            "                mIsHookInit = true;\n" +
            "            }\n" +
            "            //替换对象\n" +
            "            Object tn = sField_TN.get(toast);\n" +
            "            Handler originalHandler = (Handler)sField_TN_Handler.get(tn);\n" +
            "            if(!(originalHandler instanceof SafelyHandlerWrapper)) {\n" +
            "                sField_TN_Handler.set(tn, new SafelyHandlerWrapper(originalHandler));\n" +
            "            }\n" +
            "        }catch (Exception e){\n" +
            "            Log.e(\"logger\",\"hook toast exception:\" + e);\n" +
            "        }\n" +
            "        return toast;\n" +
            "    }\n" +
            "\n" +
            "    public static void show(Toast toast) {\n" +
            "        if(toast == null) return;\n" +
            "        if(isNeedCheckToast()) {\n" +
            "            hookToast(toast);\n" +
            "        }\n" +
            "        toast.show();\n" +
            "    }\n" +
            "\n" +
            "    public static void showMsg(@StringRes int resId) {\n" +
            "        get(Toast.LENGTH_SHORT);\n" +
            "        mToast.setText(resId);\n" +
            "        show(mToast);\n" +
            "    }\n" +
            "\n" +
            "    public static void showMsg(CharSequence text) {\n" +
            "        get(Toast.LENGTH_SHORT);\n" +
            "        mToast.setText(text);\n" +
            "        show(mToast);\n" +
            "    }\n" +
            "\n" +
            "    public static void showLongMsg(@StringRes int resId) {\n" +
            "        get(Toast.LENGTH_LONG);\n" +
            "        mToast.setText(resId);\n" +
            "        show(mToast);\n" +
            "    }\n" +
            "\n" +
            "    public static void showLongMsg(CharSequence text) {\n" +
            "        get(Toast.LENGTH_LONG);\n" +
            "        mToast.setText(text);\n" +
            "        show(mToast);\n" +
            "    }\n" +
            "\n" +
            "    public static void cancel() {\n" +
            "        if(mToast != null) {\n" +
            "            mToast.cancel();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private static void get(int time) {\n" +
            "        if(mToast != null) {\n" +
            "            mToast.cancel();\n" +
            "        }\n" +
            "        mToast = Toast.makeText(%s, \"\", time);\n" +
            "        mToast.setGravity(Gravity.CENTER, 0, 0);\n" +
            "        mToast.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {\n" +
            "            @Override\n" +
            "            public void onViewAttachedToWindow(View v) {}\n" +
            "\n" +
            "            @Override\n" +
            "            public void onViewDetachedFromWindow(View v) {\n" +
            "                mToast = null;\n" +
            "            }\n" +
            "        });\n" +
            "    }\n" +
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
            "    /**\n" +
            "     * 用于对Toast内部类TN中的内部对象mHandler，进行装饰\n" +
            "     */\n" +
            "    private static class SafelyHandlerWrapper extends Handler {\n" +
            "        private Handler mOriginalHandler;\n" +
            "\n" +
            "        SafelyHandlerWrapper(Handler originalHandler){\n" +
            "            this.mOriginalHandler = originalHandler;\n" +
            "        }\n" +
            "\n" +
            "        @Override\n" +
            "        public void dispatchMessage(Message msg) {\n" +
            "            //发生异常代码的执行顺序：Handler.dispatchMessage()->Toast$TN$Handler.handleMessage()->Toast$TN.handleShow()，该处可能发生BadTokenException\n" +
            "            //内部handler异常，外部可以捕获\n" +
            "            try {\n" +
            "                super.dispatchMessage(msg);\n" +
            "            }catch (Exception e){\n" +
            "                Log.d(\"logger\",\"catch Toast exception:\" + e);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        @Override\n" +
            "        public void handleMessage(Message msg) {\n" +
            "            //委托给原Handler执行\n" +
            "            if(mOriginalHandler != null) {\n" +
            "                mOriginalHandler.handleMessage(msg);\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
