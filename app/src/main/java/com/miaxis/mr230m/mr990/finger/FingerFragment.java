//package com.miaxis.mr230m.mr990.finger;
//
//import android.os.Bundle;
//
//import com.miaxis.mr230m.R;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.lifecycle.ViewModelProvider;
//
//public class FingerFragment extends BaseBindingFragment<FragmentFingerBinding> {
//
//    private com.miaxis.attendance.ui.finger.FingerViewModel mViewModel;
//    private MainViewModel mMainViewModel;
//
//    public static FingerFragment newInstance() {
//        return new FingerFragment();
//    }
//
//    @Override
//    protected int initLayout() {
//        return R.layout.fragment_finger;
//    }
//
//    @Override
//    protected void initView(@NonNull FragmentFingerBinding binding, @Nullable Bundle savedInstanceState) {
//        this.mViewModel = new ViewModelProvider(this).get(com.miaxis.attendance.ui.finger.FingerViewModel.class);
//        this.mMainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
//        this.mViewModel.mAttendance.observe(this, attendanceBeanZZResponse ->
//                mMainViewModel.mAttendance.setValue(attendanceBeanZZResponse));
//        this.mViewModel.StartCountdown.observe(this, aBoolean -> mMainViewModel.timeOutReset(aBoolean));
//        this.mViewModel.readFinger();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        this.mViewModel.resume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        this.mViewModel.pause();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        this.mViewModel.stopRead();
//    }
//}