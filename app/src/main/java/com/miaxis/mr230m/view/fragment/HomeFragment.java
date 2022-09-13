package com.miaxis.mr230m.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.FragmentHomeBinding;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.viewmodel.DemoViewModel;
import com.miaxis.mr230m.viewmodel.FingerViewModel;
import com.miaxis.mr230m.viewmodel.PreviewViewModel;

import org.zz.bean.IDCardRecord;

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
    private FingerViewModel fingerModel;
    private PreviewViewModel previewModel;
    private IDCardRecord idCardRecord;
    private ProgressDialog mProgressDialog;
    PreviewDialog previewDialog;
    String TAG="HomeFragment";

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull FragmentHomeBinding binding, @Nullable Bundle savedInstanceState) {
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setMessage("请稍后");
        mProgressDialog.setCancelable(false);
        viewModel=new ViewModelProvider(getActivity()).get(DemoViewModel.class);
        fingerModel=new ViewModelProvider(this).get(FingerViewModel.class);
        previewModel=new ViewModelProvider(getActivity()).get(PreviewViewModel.class);
        previewModel.VerifyResult.observe(this, result -> {
            previewDialog.dismiss();
            if (result.isFlag()){
                setEnabled(false);
                binding.result.setText("人脸核验成功");
            }else {
                binding.result.setText(result.getMsg());
            }
        });
        fingerModel.match.observe(this, result -> {
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            if (result.isFlag()){
                setEnabled(false);
                binding.result.setText("操作结果:指纹比对成功");
            }else {
                binding.result.setText("操作结果:指纹比对失败"+result.getMsg());
            }
        });
        fingerModel.bit.observe(this, bitmap -> {
            binding.imageIdcard.setImageBitmap(bitmap);
        });
        viewModel.resultLiveData.observe(this, result -> {
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            binding.result.setText("操作结果:"+result.getMsg());
        });
        viewModel.IDCardLiveData.observe(this, idCardRecord -> {
            Log.d(TAG, "IDCardLiveData==" +idCardRecord.toString() );
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            if(idCardRecord!=null){
                binding.result.setText("操作结果:读卡成功");
            }
            this.idCardRecord=idCardRecord;
            binding.timeTxt.setVisibility(View.VISIBLE);
            binding.setCardInfo(idCardRecord);
            binding.finger1.setText("指纹1："+ Base64.encodeToString(idCardRecord.getFingerprint0(),Base64.DEFAULT));
            binding.finger2.setText("指纹2："+Base64.encodeToString(idCardRecord.getFingerprint1(),Base64.DEFAULT));
            binding.imageIdcard.setImageBitmap(idCardRecord.getCardBitmap());
            fingerModel.setFinger(idCardRecord.getFingerprint0(),idCardRecord.getFingerprint1());
            setEnabled(true);
        });
        viewModel.ActiveInfoResult.observe(this, result -> {
            binding.weiTip.setText(result.getMsg());
        });
        String token = mkUtil.getInstance().decodeString("token", "");
        String weiIp = mkUtil.getInstance().decodeString("weiIp","");
        String jkmIp = mkUtil.getInstance().decodeString("jkmIp","");
        binding.HomeSetting.setOnClickListener(v -> nvTo(new SettingFragment()));
        binding.btnReadVerify.setOnClickListener(v -> {
            mProgressDialog.show();
            viewModel.UsbReadIDCardMsgVerify();
        });
        binding.btnReadFull.setOnClickListener(v -> {
            mProgressDialog.show();
            viewModel.UsbReadIDCardMsg(token, weiIp);
        });
        binding.btnHealthVerify.setOnClickListener(v-> {
            mProgressDialog.show();
            viewModel.UsbJkm(jkmIp);
        });
        binding.btnFingerVerify.setOnClickListener(v -> {
            binding.result.setText("请按压手指");
            this.fingerModel.readFinger();
        });
        binding.btnFaceVerify.setOnClickListener(v -> {
            previewDialog=new PreviewDialog(idCardRecord);
            previewDialog.show(getFragmentManager(),"prewView");
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        this.fingerModel.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.fingerModel.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.fingerModel.stopRead();
    }

    void setEnabled(boolean flag){
        binding.btnFaceVerify.setEnabled(flag);
        binding.btnFingerVerify.setEnabled(flag);
    }
}
