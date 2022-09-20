package com.miaxis.mr230m.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.FragmentSettingBinding;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.viewmodel.DemoViewModel;
import com.zzreader.zzStringTrans;

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
    private ProgressDialog mProgressDialog;
    boolean mode;
    long clickTime= 0L;

    @Override
    protected int initLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(@NonNull FragmentSettingBinding binding, @Nullable Bundle savedInstanceState) {
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setMessage("请稍后");
        mProgressDialog.setCancelable(false);
        viewModel=new ViewModelProvider(getActivity()).get(DemoViewModel.class);
        viewModel.resultLiveData.observe(this, result -> {
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            long timeDiff=result.getDeviceTime()-clickTime;
            binding.result.setText("结果："+ result.getMsg()+(clickTime==0F?"":"\n读卡耗时："+timeDiff+"毫秒")+(result.getNetTime()==0F?"":"\n联网耗时："+result.getNetTime()+"毫秒"));
        });
        String weiIp = mkUtil.getInstance().decodeString("weiIp","https://183.129.171.153:8080");
        String jkmIp = mkUtil.getInstance().decodeString("jkmIp","");
        mode=mkUtil.getInstance().decodeBool("jkm",false);
        binding.back.setOnClickListener(v -> {finish();});
        binding.save.setOnClickListener(v -> {
            mkUtil.getInstance().encode("weiIp",binding.WeiIp.getText().toString().trim());
            mkUtil.getInstance().encode("jkmIp",binding.JkmIp.getText().toString().trim());
            mkUtil.getInstance().encode("jkm",mode);
            finish();
        });
        binding.WeiIp.setText(TextUtils.isEmpty(weiIp)?"":weiIp);
        binding.JkmIp.setText(TextUtils.isEmpty(jkmIp)?"":jkmIp);
        binding.jkmSwitch.setChecked(mode);
        binding.btnActiveinfo.setOnClickListener(v -> {
            mProgressDialog.show();
            clickTime=System.currentTimeMillis();
            if (mode){
                viewModel.ActiveInfo(jkmIp);
            }else {
                viewModel.ActiveInfo(weiIp);
            }
        });
        binding.btnDeactiveinfo.setOnClickListener(v -> {
            mProgressDialog.show();
            clickTime=System.currentTimeMillis();
            if (mode) {
                viewModel.ActRel(jkmIp);
            }else {
                viewModel.ActRel(weiIp);
            }
        });
        binding.btnOnlineauthinfo.setOnClickListener(v -> {
            mProgressDialog.show();
            clickTime=System.currentTimeMillis();
            if (mode) {
                viewModel.OnlineAuth(jkmIp);
            }else {
                viewModel.OnlineAuth(weiIp);
            }
        });
        binding.btnActiveState.setOnClickListener(v -> {
            clickTime=System.currentTimeMillis();
            viewModel.ActiveState();
        });
        binding.btnCertificate.setOnClickListener(v -> {
            binding.result.setText("证书："+ zzStringTrans.hex2str(viewModel.jdkBase64Decode(viewModel.getSign().getBytes())));
        });
        binding.jkmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> mode=isChecked);
    }
}
