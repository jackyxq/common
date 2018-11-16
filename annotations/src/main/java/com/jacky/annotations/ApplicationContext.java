package com.jacky.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Used to generate PreferenceUtils and ToastUtil files, only for static methods of obtaining Application objects<br><br>
 *
 *
 * <code> @ApplicationContext({ApplicationContext.Toast,...})</code><br>
 * <code> public static Application  get() {</code><br>
 * <code>     return ....</code><br>
 * <code> } </code><br>
 *
 * @author Created by jacky on 2018/11/6.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ApplicationContext {

    String[] value();

    String SharePrefence = "SharePrefence";
    String Toast = "Toast";
}
