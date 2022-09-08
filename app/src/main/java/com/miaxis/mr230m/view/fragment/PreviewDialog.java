package com.miaxis.mr230m.view.fragment;

import android.app.Dialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.miaxis.mr230m.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author ZJL
 * @date 2022/9/8 15:55
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PreviewDialog extends DialogFragment {

    SurfaceView surface;
    Button btnCancel;
    String TAG="PreviewDialog";
    Camera camera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_prewview, container, false);
        surface=view.findViewById(R.id.surface);
        btnCancel=view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> Log.e(TAG, "btnCancel" ));
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
