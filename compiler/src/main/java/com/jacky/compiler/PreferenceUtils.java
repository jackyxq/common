package com.jacky.compiler;

/**
 * Created by jacky on 2018/11/16.
 */

@interface PreferenceUtils {
    String value = "package com.jacky.util;\n" +
            "\n" +
            "import android.content.SharedPreferences;\n" +
            "import android.support.v4.util.Pair;\n" +
            "import android.content.Context;\n" +
            "\n" +
            "/**\n" +
            " * A simple encapsulation tool class for SharePrefence\n" +
            " */\n" +
            "public final class PreferenceUtils {\n" +
            "\n" +
            "    public static int getInt(String fileName, String key) {\n" +
            "        return getInt(fileName, key, 0);\n" +
            "    }\n" +
            "\n" +
            "    public static int getInt(String fileName, String key, int defaultValue) {\n" +
            "        return get(fileName).getInt(key, defaultValue);\n" +
            "    }\n" +
            "\n" +
            "    public static void put(String fileName, String key, int value) {\n" +
            "        getEditor(fileName).putInt(key, value).apply();\n" +
            "    }\n" +
            "\n" +
            "    public static String getString(String fileName, String key) {\n" +
            "        return getString(fileName, key, \"\");\n" +
            "    }\n" +
            "\n" +
            "    public static String getString(String fileName, String key, String defaultValue) {\n" +
            "        return get(fileName).getString(key, defaultValue);\n" +
            "    }\n" +
            "\n" +
            "    public static void put(String fileName, String key, String value) {\n" +
            "        getEditor(fileName).putString(key, value).apply();\n" +
            "    }\n" +
            "\n" +
            "    public static boolean getBoolean(String fileName, String key) {\n" +
            "        return getBoolean(fileName, key, false);\n" +
            "    }\n" +
            "\n" +
            "    public static boolean getBoolean(String fileName, String key, boolean defaultValue) {\n" +
            "        return get(fileName).getBoolean(key, defaultValue);\n" +
            "    }\n" +
            "\n" +
            "    public static void put(String fileName, String key, boolean value) {\n" +
            "        getEditor(fileName).putBoolean(key, value).apply();\n" +
            "    }\n" +
            "\n" +
            "    public static void putInt(String fileName, Pair<String, Integer>... pairs) {\n" +
            "        if(pairs != null) {\n" +
            "            SharedPreferences.Editor editor = getEditor(fileName);\n" +
            "            for(Pair<String, Integer> pair : pairs) {\n" +
            "                editor.putInt(pair.first, pair.second);\n" +
            "            }\n" +
            "            editor.commit();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static void putBoolean(String fileName, Pair<String, Boolean>... pairs) {\n" +
            "        if(pairs != null) {\n" +
            "            SharedPreferences.Editor editor = getEditor(fileName);\n" +
            "            for(Pair<String, Boolean> pair : pairs) {\n" +
            "                editor.putBoolean(pair.first, pair.second);\n" +
            "            }\n" +
            "            editor.commit();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static void putString(String fileName, Pair<String, String>... pairs) {\n" +
            "        if(pairs != null) {\n" +
            "            SharedPreferences.Editor editor = getEditor(fileName);\n" +
            "            for(Pair<String, String> pair : pairs) {\n" +
            "                editor.putString(pair.first, pair.second);\n" +
            "            }\n" +
            "            editor.commit();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static void remove(String fileName, String key) {\n" +
            "        SharedPreferences.Editor editor = getEditor(fileName);\n" +
            "        editor.remove(key);\n" +
            "        editor.commit();\n" +
            "    }\n" +
            "\n" +
            "    public static void clearAll(String fileName) {\n" +
            "        SharedPreferences.Editor editor = getEditor(fileName);\n" +
            "        editor.clear();\n" +
            "        editor.commit();\n" +
            "    }\n" +
            "\n" +
            "    private PreferenceUtils(){}\n" +
            "\n" +
            "    public static SharedPreferences get(String name) {\n" +
            "        Context context = %s;\n" +
            "        return context.getSharedPreferences(name, Context.MODE_PRIVATE);\n" +
            "    }\n" +
            "\n" +
            "    public static SharedPreferences.Editor getEditor(String name) {\n" +
            "        return get(name).edit();\n" +
            "    }\n" +
            "}";
}
