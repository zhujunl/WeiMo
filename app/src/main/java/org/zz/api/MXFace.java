package org.zz.api;

import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

/**
 * @author Tank
 * @date 2021/8/20 11:34 上午
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MXFace {

    private final int[] bInfo;
    private final MXFaceInfoEx pFaceInfo;

    public MXFace(@NonNull int[] bInfo, @NonNull MXFaceInfoEx pFaceInfo) {
        int length = bInfo.length;
        this.bInfo = new int[length];
        System.arraycopy(bInfo, 0, this.bInfo, 0, length);
        this.pFaceInfo = MXFaceInfoEx.Copy(pFaceInfo);
    }

    public int[] getFaceData() {
        return this.bInfo;
    }

    public MXFaceInfoEx getFaceInfo() {
        return this.pFaceInfo;
    }

    public Rect getFaceRect() {
        return new Rect(this.pFaceInfo.x, this.pFaceInfo.y,
                this.pFaceInfo.x + this.pFaceInfo.width,
                this.pFaceInfo.y + this.pFaceInfo.height);
    }

    public RectF getFaceRectF() {
        return new RectF(this.pFaceInfo.x, this.pFaceInfo.y,
                this.pFaceInfo.x + this.pFaceInfo.width,
                this.pFaceInfo.y + this.pFaceInfo.height);
    }

    @Override
    public String toString() {
        return "MXFace{" +
                "bInfo=" + (this.bInfo == null ? null : this.bInfo.length) +
                ", pFaceInfo=" + this.pFaceInfo +
                '}';
    }
}
