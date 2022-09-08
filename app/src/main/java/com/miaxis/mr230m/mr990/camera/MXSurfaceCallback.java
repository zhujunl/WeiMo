package com.miaxis.mr230m.mr990.camera;

import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

/**
 * @author Tank
 * @date 2021/8/26 4:00 下午
 * @des
 * @updateAuthor
 * @updateDes
 */
public abstract class MXSurfaceCallback implements SurfaceHolder.Callback{

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
