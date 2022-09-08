package com.miaxis.mr230m.view.fragment;

import android.app.Dialog;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.mr990.camera.CameraConfig;
import com.miaxis.mr230m.mr990.camera.CameraHelper;
import com.miaxis.mr230m.viewmodel.PreviewViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author ZJL
 * @date 2022/9/8 15:55
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PreviewDialog extends DialogFragment implements TextureView.SurfaceTextureListener{

    CameraTextureView surface;
    Button btnCancel;
    String TAG="PreviewDialog";
    Camera camera;
    private PreviewViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView" );
        View view = inflater.inflate(R.layout.dialog_fragment_prewview, container, false);
        mViewModel=new ViewModelProvider(getActivity()).get(PreviewViewModel.class);
        surface=view.findViewById(R.id.surface);
        btnCancel=view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = 800;   //设置宽度充满屏幕
        lp.height = 500;
        window.setAttributes(lp);
        return dialog;
    }

    public void openCamera(){
        surface.setSurfaceTextureListener(this);
        surface.setRotationY(CameraConfig.Camera_RGB.mirror ? 180 : 0); // 镜面对称
        surface.setRawPreviewSize(new CameraTextureView.Size(CameraConfig.Camera_RGB.height, CameraConfig.Camera_RGB.width));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart" );
    }


    @Override
    public void onResume() {
        super.onResume();
        openCamera();
        Log.e(TAG, "onResume" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStart" );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView" );
        CameraHelper.getInstance().stop();
        mViewModel.destroy();
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        showCameraPreview(surface);
    }

    private void showCameraPreview(SurfaceTexture surface) {
        mViewModel.showRgbCameraPreview(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        CameraHelper.getInstance().stop();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }
}
