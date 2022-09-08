package com.miaxis.mr230m.viewmodel;

import android.graphics.Bitmap;

import com.miaxis.mr230m.event.SingleLiveEvent;
import com.miaxis.mr230m.mr990.bean.Finger;
import com.miaxis.mr230m.mr990.finger.MR990FingerStrategy;
import com.mx.finger.common.MxImage;
import com.mx.finger.utils.RawBitmapUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FingerViewModel extends ViewModel implements MR990FingerStrategy.ReadFingerCallBack {

    MutableLiveData<Boolean> StartCountdown = new MutableLiveData<>(true);
    public SingleLiveEvent<Integer> match=new SingleLiveEvent<>();
    public SingleLiveEvent<Bitmap> bit=new SingleLiveEvent<>();
    private byte[] finger0;
    private byte[] finger1;

    public FingerViewModel() {
    }

    public void readFinger() {
        MR990FingerStrategy.getInstance().readFinger(this);
    }

    public void stopRead() {
        MR990FingerStrategy.getInstance().stopRead();
    }

    public void resume() {
        MR990FingerStrategy.getInstance().resume();
        this.StartCountdown.setValue(true);
    }

    public void pause() {
        MR990FingerStrategy.getInstance().pause();
        this.StartCountdown.postValue(false);
    }

    @Override
    public void onReadFinger(MxImage finger) {

        this.StartCountdown.postValue(true);
    }

    @Override
    public void onExtractFeature(MxImage image, byte[] feature) {
        Bitmap bitmap = RawBitmapUtils.raw2Bimap(image.data, image.width, image.height);
        bit.postValue(bitmap);
        if (feature!=null){
            int match = MR990FingerStrategy.getInstance().getMxFingerAlg().match(finger0, feature, 3);
            if (match!=0){
                match= MR990FingerStrategy.getInstance().getMxFingerAlg().match(finger1, feature, 3);
            }
            this.match.postValue(match);
            pause();
        }else {

        }
    }

    private String lastUserID;
    private long lastTime;

    @Override
    public void onFeatureMatch(MxImage image, byte[] feature, Finger finger, Bitmap bitmap) {

    }

    public void setFinger(byte[] finger0,byte[] finger1){
        this.finger0=finger0;
        this.finger1=finger1;
    }

}