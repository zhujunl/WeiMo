package com.miaxis.mr230m.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.FragmentSettingBinding;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.viewmodel.DemoViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author ZJL
 * @date 2022/9/6 14:36
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SettingFragment extends BaseBindingFragment<FragmentSettingBinding> {
    private DemoViewModel viewModel;
    String TAG="SettingFragment";

    @Override
    protected int initLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(@NonNull FragmentSettingBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel=new ViewModelProvider(getActivity()).get(DemoViewModel.class);
        viewModel.resultLiveData.observe(this, result -> {
            binding.result.setText("结果："+result.getMsg());
        });
        String weiIp = mkUtil.getInstance().decodeString("weiIp","");
        String jkmIp = mkUtil.getInstance().decodeString("jkmIp","");
        String token = mkUtil.getInstance().decodeString("token", "");
        binding.back.setOnClickListener(v -> finish());
        binding.save.setOnClickListener(v -> {
            mkUtil.getInstance().encode("weiIp",binding.WeiIp.getText().toString().trim());
            mkUtil.getInstance().encode("jkmIp",binding.JkmIp.getText().toString().trim());
            finish();
        });
        binding.WeiIp.setText(TextUtils.isEmpty(weiIp)?"":weiIp);
        binding.JkmIp.setText(TextUtils.isEmpty(jkmIp)?"":jkmIp);
        binding.btnActiveinfo.setOnClickListener(v -> viewModel.ActiveInfo(token,weiIp));
        binding.btnDeactiveinfo.setOnClickListener(v -> viewModel.ActRel(token,weiIp));
        binding.btnOnlineauthinfo.setOnClickListener(v -> viewModel.OnlineAuth(token,weiIp));
        binding.btnActiveState.setOnClickListener(v -> viewModel.ActiveState());
        binding.btnCertificate.setOnClickListener(v -> viewModel.getSign());
    }
}
