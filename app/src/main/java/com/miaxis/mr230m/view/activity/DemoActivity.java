package com.miaxis.mr230m.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.miaxis.mr230m.App;
import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.ActivityMian2Binding;
import com.miaxis.mr230m.service.ServerManager;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.view.fragment.HomeFragment;
import com.miaxis.mr230m.viewmodel.DemoViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class DemoActivity extends BaseBindingActivity<ActivityMian2Binding> {
    private FragmentManager fragmentManager;
    private DemoViewModel viewModel;

    @Override
    protected int initLayout() {
        return R.layout.activity_mian2;
    }

    @Override
    protected void initView(@NonNull ActivityMian2Binding binding, @Nullable Bundle savedInstanceState) {
        fragmentManager=getSupportFragmentManager();
        App.getInstance().init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 0x99);
            }
        }
        nvTo(new HomeFragment());
        viewModel=new ViewModelProvider(this).get(DemoViewModel.class);
        viewModel.UsbConnect(this);
        viewModel.getToken();
        viewModel.isConnect.observe(this, aBoolean -> {
            String weiIp = mkUtil.getInstance().decodeString("weiIp","https://183.129.171.153:8080");
            viewModel.ActiveInfoAuto(weiIp);
//            viewModel.ActiveInfoAutoTCP();
        });
        ServerManager.getInstance().startRefresh();
    }

    @Override
    protected void initData(@NonNull ActivityMian2Binding binding, @Nullable Bundle savedInstanceState) {

    }


    public void nvTo(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        ServerManager.getInstance().stopRefresh();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (fragmentManager.getBackStackEntryCount()<2){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}