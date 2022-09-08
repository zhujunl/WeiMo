package com.miaxis.mr230m.view.fragment;

import android.os.Bundle;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.FragmentHomeBinding;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.viewmodel.DemoViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author ZJL
 * @date 2022/9/6 14:50
 * @des
 * @updateAuthor
 * @updateDes
 */
public class HomeFragment extends BaseBindingFragment<FragmentHomeBinding> {
    private DemoViewModel viewModel;
    String TAG="HomeFragment";

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull FragmentHomeBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel=new ViewModelProvider(getActivity()).get(DemoViewModel.class);
        viewModel.resultLiveData.observe(this, result -> {
            binding.result.setText("操作结果:"+result.getMsg());
        });
        viewModel.IDCardLiveData.observe(this, idCardRecord -> {
            binding.setCardInfo(idCardRecord);
            binding.imageIdcard.setImageBitmap(idCardRecord.getCardBitmap());
        });

        String token = mkUtil.getInstance().decodeString("token", "");
        String weiIp = mkUtil.getInstance().decodeString("weiIp","");
        binding.HomeSetting.setOnClickListener(v -> nvTo(new SettingFragment()));
        binding.btnReadVerify.setOnClickListener(v -> viewModel.UsbReadIDCardMsgVerify());
        binding.btnReadFull.setOnClickListener(v -> viewModel.UsbReadIDCardMsg(token,weiIp));

    }


}
