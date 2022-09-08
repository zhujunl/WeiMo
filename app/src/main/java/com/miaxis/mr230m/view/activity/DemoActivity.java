package com.miaxis.mr230m.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.ActivityMian2Binding;
import com.miaxis.mr230m.service.TokenService;
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
        mkUtil.init(this);
        nvTo(new HomeFragment());
        viewModel=new ViewModelProvider(this).get(DemoViewModel.class);
        viewModel.UsbConnect(this);
        viewModel.getToken();
        Intent intent=new Intent(this, TokenService.class);
        startService(intent);
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
        Intent intent=new Intent(this, TokenService.class);
        stopService(intent);
        super.onDestroy();
    }
}