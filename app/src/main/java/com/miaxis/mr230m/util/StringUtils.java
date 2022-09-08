package com.miaxis.mr230m.util;

import android.text.TextUtils;

/**
 * @author Tank
 * @date 2021/8/23 7:17 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class StringUtils {

    public static boolean isNull(String string) {
        return string == null;
    }

    public static boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string) || TextUtils.isEmpty(string.trim());
    }


}
