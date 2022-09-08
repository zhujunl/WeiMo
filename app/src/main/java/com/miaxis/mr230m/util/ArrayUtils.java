package com.miaxis.mr230m.util;

/**
 * @author Tank
 * @date 2021/8/26 6:48 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ArrayUtils {

    public static <T> boolean isNull(T[] array) {
        return array == null;
    }

    public static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isLength(byte[] array, int length) {
        return array != null && array.length == length;
    }

    public static boolean isNullOrEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isLength(int[] array, int length) {
        return array != null && array.length == length;
    }
}
