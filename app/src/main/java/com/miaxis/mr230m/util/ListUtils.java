package com.miaxis.mr230m.util;

import java.util.List;

/**
 * @author Tank
 * @date 2021/8/6 7:26 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ListUtils {


    public static boolean isNull(List<?> list) {
        return list == null;
    }

    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }


}
