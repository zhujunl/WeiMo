package com.miaxis.mr230m.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.miaxis.mr230m.R;
import com.miaxis.mr230m.databinding.FragmentHomeBinding;
import com.miaxis.mr230m.util.mkUtil;
import com.miaxis.mr230m.viewmodel.DemoViewModel;
import com.miaxis.mr230m.viewmodel.FingerViewModel;

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
    private IDCardRecord idCardRecord;
    private ProgressDialog mProgressDialog;
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
        fingerModel.match.observe(this, integer -> {
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            if (integer==0){
                binding.result.setText("操作结果:指纹比对成功");
            }else {
                binding.result.setText("操作结果:指纹比对失败");
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
            this.idCardRecord=idCardRecord;
            binding.setCardInfo(idCardRecord);
            binding.finger1.setText("指纹1："+ Base64.encodeToString(idCardRecord.getFingerprint0(),Base64.DEFAULT));
            binding.finger2.setText("指纹2："+Base64.encodeToString(idCardRecord.getFingerprint1(),Base64.DEFAULT));
            fingerModel.setFinger(idCardRecord.getFingerprint0(),idCardRecord.getFingerprint1());
            binding.imageIdcard.setImageBitmap(idCardRecord.getCardBitmap());
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
//            PreviewDialogFragment fragment=new PreviewDialogFragment();
//            fragment.show();
            PreviewDialog previewDialog=new PreviewDialog();
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

}
