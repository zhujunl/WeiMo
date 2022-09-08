package org.zz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Tank
 * @date 2021/5/19 9:04
 * @des
 * @updateAuthor
 * @updateDes
 */
public class FaceRectView extends View {

    private final Paint mPaint = new Paint();

    public FaceRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(0xDDDDDDDD);
        mPaint.setStrokeWidth(3F);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private RectF mRectF;
    private boolean mirror = false;

    public void setRect(RectF rect, boolean mirror) {
        this.mRectF = rect;
        this.mirror = mirror;
        //如果出现人脸框与视频中人脸移动方向不对，需进行位置转换，改变mRectF的值
        //float left, float top, float right, float bottom
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRectF == null) {
            canvas.save();
            canvas.restore();
        } else {
            canvas.drawRect(
                    new RectF(this.mRectF.left * 2, this.mRectF.top * 2-240,
                    this.mRectF.right * 2, this.mRectF.bottom * 2-240
            ), this.mPaint);

            //            float left = this.mRectF.left * 2F;
            //            float top = (this.mRectF.top * 2F - 120) * 5 / 8F;
            //            float right = this.mRectF.right * 2F;
            //            float bottom = (this.mRectF.bottom * 2F + 120) * 5 / 8F;
            //            RectF rectF = new RectF(left, top, right, bottom);
            //            if (rectF.height() >= 1.2F*rectF.width()) {
            //                rectF.bottom = rectF.top + 1.2F*rectF.width();
            //            }
            //            canvas.drawRect(rectF, this.mPaint);

        }
    }
}
