package com.miaxis.mr230m.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    String TAG = "HomeFragment";
    TextView result, finger1, finger2, name, time, number;
    ImageView imageIdcard;
    long clickTime = 0L;

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull FragmentHomeBinding binding, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "initView");
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("请稍后");
        mProgressDialog.setCancelable(false);
        viewModel = new ViewModelProvider(getActivity()).get(DemoViewModel.class);
        fingerModel = new ViewModelProvider(this).get(FingerViewModel.class);
        previewModel = new ViewModelProvider(getActivity()).get(PreviewViewModel.class);
        result = binding.getRoot().findViewById(R.id.result);
        finger1 = binding.getRoot().findViewById(R.id.finger1);
        finger2 = binding.getRoot().findViewById(R.id.finger2);
        name = binding.getRoot().findViewById(R.id.nameTxt);
        time = binding.getRoot().findViewById(R.id.timeTxt);
        number = binding.getRoot().findViewById(R.id.numTxt);
        imageIdcard = binding.getRoot().findViewById(R.id.imageIdcard);
        previewModel.VerifyResult.observe(this, result -> {
            previewDialog.dismiss();
            if (result.isFlag()) {
                setEnabled(false);
                this.result.setText("人脸核验成功");
            } else {
                this.result.setText(result.getMsg());
            }
        });
        fingerModel.match.observe(this, result -> {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
            if (result.isFlag()) {
                setEnabled(false);
                this.result.setText("操作结果:指纹比对成功");
            } else {
                this.result.setText("操作结果:指纹比对失败" + result.getMsg());
            }
        });
        fingerModel.bit.observe(this, bitmap -> {
            this.imageIdcard.setImageBitmap(bitmap);
        });
        viewModel.resultLiveData.observe(this, result -> {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
            long timeDiff = result.getDeviceTime() - clickTime;
            this.result.setText("操作结果:" + result.getMsg() + (clickTime == 0F ? "" : "\n读卡耗时：" + timeDiff + "毫秒") + (result.getNetTime() == 0F ? "" : "\n联网耗时：" + result.getNetTime() + "毫秒"));
        });
        viewModel.IDCardLiveData.observe(this, idCardRecord -> {
            Log.d(TAG, "IDCardLiveData==" + idCardRecord.toString());
            if (mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
            if (idCardRecord != null) {
                result.setText("操作结果:读卡成功");
            }
            this.idCardRecord = idCardRecord;
            binding.setCardInfo(idCardRecord);
            this.name.setText(idCardRecord.getName());
            this.number.setText(String.valueOf(idCardRecord.getCardNumber()));
            this.time.setText(idCardRecord.getValidateStart() + "——" + idCardRecord.getValidateEnd());
            setEnabled(idCardRecord.getFingerprint0() != null);
            if (idCardRecord.getFingerprint0() != null) {
                this.finger1.setText("指纹1：" + Base64.encodeToString(idCardRecord.getFingerprint0(), Base64.DEFAULT));
                this.finger2.setText("指纹2：" + Base64.encodeToString(idCardRecord.getFingerprint1(), Base64.DEFAULT));
                this.imageIdcard.setImageBitmap(idCardRecord.getCardBitmap());
                fingerModel.setFinger(idCardRecord.getFingerprint0(), idCardRecord.getFingerprint1());
            }else {
                this.finger1.setText("");
                this.finger2.setText("");
                this.imageIdcard.setImageResource(R.drawable.card);
            }
        });
        viewModel.ActiveInfoResult.observe(this, result -> {
            binding.weiTip.setText(result.getMsg());
        });

        String weiIp = mkUtil.getInstance().decodeString("weiIp", "https://183.129.171.153:8080");
        String jkmIp = mkUtil.getInstance().decodeString("jkmIp", "");
        binding.HomeSetting.setOnClickListener(v -> {
            this.fingerModel.stopRead();
            nvTo(new SettingFragment());
        });
        binding.btnReadVerify.setOnClickListener(v -> {
            mProgressDialog.show();
            this.fingerModel.stopRead();
            clickTime = System.currentTimeMillis();
//            viewModel.UsbReadIDCardMsgVerify(weiIp);
            viewModel.UsbReadIDCardMsgMultipe(weiIp);
        });
        binding.btnReadFull.setOnClickListener(v -> {
            mProgressDialog.show();
            this.fingerModel.stopRead();
            clickTime = System.currentTimeMillis();
            viewModel.UsbReadIDCardMsg(weiIp);
            //            viewModel.UsbReadIDCardMsgTCP();
        });
        binding.btnHealthVerify.setOnClickListener(v -> {
            mProgressDialog.show();
            this.fingerModel.stopRead();
            clickTime = System.currentTimeMillis();
            //            viewModel.UsbJkm(jkmIp);
        });
        binding.btnFingerVerify.setOnClickListener(v -> {
            this.result.setText("请按压手指");
            this.fingerModel.readFinger();
        });
        binding.btnFaceVerify.setOnClickListener(v -> {
            this.fingerModel.stopRead();
            previewDialog = new PreviewDialog(idCardRecord);
            previewDialog.show(getFragmentManager(), "prewView");
        });

        binding.btnReadFor.setOnClickListener(v -> viewModel.ForRead());
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

    void setEnabled(boolean flag) {
        binding.btnFaceVerify.setEnabled(flag);
        binding.btnFingerVerify.setEnabled(flag);
    }
}
