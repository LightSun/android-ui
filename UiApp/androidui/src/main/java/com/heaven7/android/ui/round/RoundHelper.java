package com.heaven7.android.ui.round;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * the round helper. help fast set round parameters
 * @author heaven7
 */
public final class RoundHelper {

    private static final String TAG = "RoundHelper";
    private final Path mPath = new Path();
    //private final Path mBorderPath = new Path();
    private final RectF mRect = new RectF();
    private Paint mPaint;

    private final View mView;
    private final Callback mCallback;
    private boolean mDirty;

    private RoundParameters mParams;

    public RoundHelper(View view, Callback callback) {
        this.mView = view;
        this.mCallback = callback;
    }
    /**
     * get the round parameters from known attrs. order is 'attrs -> theme'
     * @param context the context
     * @param attrs the attrs
     * @param dp the default parameters , can be null.
     * @return the round parameters. may be null if no attr-value define in theme or attrs And dp is null.
     */
    public static RoundParameters of(Context context, AttributeSet attrs, RoundParameters dp){
        return Utils.of(context, attrs, dp);
    }

    public RoundParameters getRoundParameters() {
        return mParams;
    }
    public void setRoundParameters(RoundParameters mParams) {
        this.mParams = mParams;
    }
    /**
     * called on apply round parameters. this can called in layout or measure.
     */
    public void apply(){
        if(mDirty || mParams == null){
            return;
        }
        mDirty = true;
        mView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mView.getViewTreeObserver().removeOnPreDrawListener(this);
                if(mDirty){
                    mDirty = false;
                    apply0(mView.getWidth(), mView.getHeight());
                }
                return true;
            }
        });
    }

    /**
     * apply direct without on pre-draw listener
     */
    public void applyDirect(){
        apply0(mView.getWidth(), mView.getHeight());
    }

    /**
     * called on size changed
     * @param w the width
     * @param h the height
     */
    public void onSizeChanged(int w, int h) {
        apply0(w, h);
    }

    /**
     * draw view with default callback
     * @param canvas the canvas
     */
    public void draw(Canvas canvas) {
        draw(canvas, mCallback);
    }

    /**
     * draw view with target callback
     * @param canvas the canvas
     * @param callback the callback
     */
    public void draw(Canvas canvas, Callback callback) {
        if(mParams != null){
            if(mParams.hasBorder()){
                mPaint.setColor(mParams.getBorderColor());
                canvas.save();
                canvas.drawRoundRect(mRect,  getRadius(true), getRadius(false), mPaint);
                canvas.restore();
            }

            int save = canvas.save();
            canvas.clipPath(this.mPath);
            callback.draw0(mView, canvas);
            canvas.restoreToCount(save);
        }else {
            callback.draw0(mView, canvas);
        }
    }

    private float getRadius(boolean x){
        if(x){
            return mParams.isCircle() ? mView.getWidth()*1f / 2 : mParams.getRadiusX();
        }else {
            return mParams.isCircle() ? mView.getHeight()*1f / 2 : mParams.getRadiusY();
        }
    }

    private void apply0(int w, int h){
        if(mParams == null){
            return;
        }
        Log.d(TAG, "apply0:  w = " + w + ", h = " + h);
        int padLeft = mView.getPaddingLeft();
        int padTop = mView.getPaddingTop();
        int padRight = mView.getPaddingRight();
        int padBottom = mView.getPaddingBottom();
        //
        mPath.reset();
        if(mParams.isRoundAfterPadding()){
            mRect.set(padLeft, padTop, w - padRight, h - padBottom);
        }else {
            mRect.set(0, 0, w,  h);
        }
        if(mParams.hasBorder()){
            mRect.inset(mParams.getBorderWidthX(), mParams.getBorderWidthY());
            if(mPaint == null){
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setStyle(Paint.Style.FILL);
            }
        }
        mPath.addRoundRect(mRect, getRadius(true), getRadius(false), Path.Direction.CW);
        mPath.close();

        //mBorderPath
        if(mParams.hasBorder()){
            if(mParams.isRoundAfterPadding()){
                mRect.set(padLeft, padTop, w - padRight, h - padBottom);
            }else {
                mRect.set(0, 0, w,  h);
            }
        }
    }

    /**
     * called on save instance state
     * @param superState the super state
     * @return the state
     */
    public Parcelable onSaveInstanceState(Parcelable superState) {
        Bundle b = new Bundle();
        b.putParcelable("super", superState);
        b.putParcelable("round_param", getRoundParameters());
        return b;
    }

    /**
     * called on restore state
     * @param state the state
     * @return super state
     */
    public Parcelable onRestoreInstanceState(Parcelable state) {
        Bundle b = (Bundle) state;
        RoundParameters rp = b.getParcelable("round_param");
        setRoundParameters(rp);
        apply();
        return b.getParcelable("super");
    }

    public interface Callback{
        void draw0(View view, Canvas canvas);
    }
}
