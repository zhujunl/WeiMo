package com.miaxis.weomosdk.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Handler辅助类（UI线程和非UI线程 操作）
 *
 * @Author SunYi
 * @Date on 2020/5/13 13:17
 * @Version 1.0
 * @Describe
 **/
public final class HandlerHelper {
    /**
     * 异步线程
     */
    private static final HandlerThread mAsyncHandlerThread = new HandlerThread("AsyncHandler");
    public static final Handler mAsyncHandler;
    /**
     * 主线程（Ui线程）
     */
    private static final Handler mMainHandler;

    static {
        mAsyncHandlerThread.start();
        mAsyncHandler = new Handler(mAsyncHandlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    private HandlerHelper() {
    }

    /**
     * 异步
     *
     * @param r
     */
    public static void asyncPost(Runnable r) {
        if (r != null) {
            mAsyncHandler.post(r);
        }
    }

    /**
     * 异步
     *
     * @param r
     * @param delayMillis
     */
    public static void asyncPostDelayed(Runnable r, long delayMillis) {
        if (r != null) {
            mAsyncHandler.postDelayed(r, delayMillis);
        }
    }

    /**
     * @param token
     */
    public static void asyncRemoveCallbacksAndMessages(Object token) {
        mAsyncHandler.removeCallbacksAndMessages(token);
    }

    /**
     * @param r
     */
    public static void asyncRemoveCallbacks(Runnable r) {
        if (r != null) {
            mAsyncHandler.removeCallbacks(r);
        }
    }

    /**
     * Ui线程
     *
     * @param r
     */
    public static void post(Runnable r) {
        if (r != null) {
            mMainHandler.post(r);
        }
    }

    /**
     * Ui线程
     *
     * @param r
     * @param delayMillis
     */
    public static void postDelayed(Runnable r, long delayMillis) {
        if (r != null) {
            mMainHandler.postDelayed(r, delayMillis);
        }
    }

    /**
     * @param token
     */
    public static void removeCallbacksAndMessages(Object token) {
        mMainHandler.removeCallbacksAndMessages(token);
    }

    /**
     * @param r
     */
    public static void removeCallbacks(Runnable r) {
        if (r != null) {
            mMainHandler.removeCallbacks(r);
        }
    }

    /**
     * 返回true则说明运行在主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 返回true则说明运行在后台线程
     *
     * @return
     */
    public static boolean isBackgroundThread() {
        return !isMainThread();
    }
}
