package com.heaven7.android.ui.round;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.heaven7.android.ui.round.delegate.ViewRoundPartDelegate;

/**
 * the round helper. help fast set round parameters
 * <p>Note: currently not compat with 'android:clipchildren' for view. but part is support.</p>
 * @author heaven7
 */
public final class RoundHelper {

    private static final String TAG = "RoundHelper";
    private final Path mPath = new Path();
    //private final Path mBorderPath = new Path();
    private final RectF mRectF = new RectF();
    private final Rect mRect = new Rect();
    private Paint mPaint;

    private final View mView;
    private final Resources mResource;
    private final RoundPartDelegate mPartDelegate;
    private final Callback mCallback;
    private boolean mDirty;

    private RoundParameters mParams;
    private Calculator mCalculator;

    /**
     * create round helper by target round-part
     * @param view the view
     * @param delegate the round-part
     * @param callback the callback to draw. can be null.
     * @since 1.0.2
     */
    public RoundHelper(View view, RoundPartDelegate delegate, @Nullable Callback callback) {
        this(view.getResources(), view, delegate, callback);
    }
    /**
     * create round helper by default view round-part
     * @param view the view
     * @param callback the callback to draw. can be null.
     */
    public RoundHelper(View view, @Nullable Callback callback) {
       this(view.getResources(), view, new ViewRoundPartDelegate(view), callback);
    }

    /**
     * create round helper by delegate and callback
     * @param res the resource
     * @param delegate the delegate
     * @param callback the callback
     * @since 1.0.3
     */
    public RoundHelper(Resources res, RoundPartDelegate delegate, @Nullable Callback callback){
        this(res, null, delegate, callback);
    }

    private RoundHelper(Resources res, View view, RoundPartDelegate delegate, @Nullable Callback callback) {
        this.mResource = res;
        this.mView = view;
        this.mPartDelegate = delegate;
        this.mCallback = callback;
    }
    /**
     * get view . may be null if not used from view.
     * @return view
     * @since 1.0.2
     */
    public View getView(){
        return mView;
    }
    /**
     * get Context. may be null . if have no view
     * @return Context
     * @since 1.0.2
     */
    public Context getContext(){
        return mView != null ? mView.getContext() : null;
    }
    /**
     * get Resources
     * @return Resources
     * @since 1.0.2
     */
    public Resources getResource(){
        return mResource;
    }
    public RoundParameters getRoundParameters() {
        return mParams;
    }
    public void setRoundParameters(RoundParameters mParams) {
        this.mParams = mParams;
    }
    /**
     * called on apply round parameters. this can called in layout or measure.
     * call this will add a OnPreDrawListener for view tree.
     */
    public void apply(){
        if(mDirty || mParams == null){
            return;
        }
        //if not used for view. apply direct
        if(mView == null){
            applyDirect();
            return;
        }
        mDirty = true;
        mView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mView.getViewTreeObserver().removeOnPreDrawListener(this);
                if(mDirty){
                    mDirty = false;
                    apply0(mPartDelegate.getWidth(), mPartDelegate.getHeight());
                }
                return true;
            }
        });
    }

    /**
     * the calculator
     * @return the calculator
     * @since 1.0.3
     */
    public Calculator getCalculator() {
        return mCalculator;
    }

    /**
     * set the calculator
     * @param calculator the calculator
     * @since 1.0.3
     */
    public void setCalculator(Calculator calculator) {
        this.mCalculator = calculator;
    }

    /**
     * apply direct without on pre-draw listener
     */
    public void applyDirect(){
        apply0(mPartDelegate.getWidth(), mPartDelegate.getHeight());
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
               // canvas.save();
                canvas.drawRoundRect(mRectF, getRadius(true), getRadius(false), mPaint);
               // canvas.restore();
            }

            int save = canvas.save();
            canvas.clipPath(this.mPath);
            callback.draw0(mPartDelegate, canvas);
            canvas.restoreToCount(save);
        }else {
            callback.draw0(mPartDelegate, canvas);
        }
    }

    private float getRadius(boolean x){
        if(mCalculator != null){
            return mCalculator.getRoundSize(mPartDelegate, mParams, x);
        }
        if(x){
            return mParams.isCircle() ? mPartDelegate.getWidth()*1f / 2 : mParams.getRadiusX();
        }else {
            return mParams.isCircle() ? mPartDelegate.getHeight()*1f / 2 : mParams.getRadiusY();
        }
    }
    private float getBorderSize(boolean x){
        if(mCalculator != null){
            return mCalculator.getBorderSize(mPartDelegate, mParams, x);
        }
        return x ? mParams.getBorderWidthX() : mParams.getBorderWidthY();
    }

    private void apply0(int w, int h){
        if(mParams == null){
            return;
        }
        Log.d(TAG, "apply0:  w = " + w + ", h = " + h);
        mPartDelegate.getPadding(mRect);
        Log.d(TAG, "apply0:  padding = " + mRect);

        int padLeft = mRect.left;
        int padTop = mRect.top;
        int padRight = mRect.right;
        int padBottom = mRect.bottom;
        //
        mPath.reset();
        if(mParams.isRoundAfterPadding()){
            mRectF.set(padLeft, padTop, w - padRight, h - padBottom);
        }else {
            Log.d(TAG, "apply0:  isRoundAfterPadding = false");
            mRectF.set(0, 0, w,  h);
        }
        if(mParams.hasBorder()){
            mRectF.inset(getBorderSize(true), getBorderSize(false));
            initBorderPaintIfNeed();
        }
        mPath.addRoundRect(mRectF, getRadius(true), getRadius(false), Path.Direction.CW);
        mPath.close();

        //mBorderPath
        if(mParams.hasBorder()){
            if(mParams.isRoundAfterPadding()){
                mRectF.set(padLeft, padTop, w - padRight, h - padBottom);
            }else {
                mRectF.set(0, 0, w,  h);
            }
        }
    }
    private void initBorderPaintIfNeed(){
        if(mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            switch (mParams.getStyle()){
                case RoundParameters.STYLE_STROKE:
                    mPaint.setStyle(Paint.Style.STROKE);
                    break;
                case RoundParameters.STYLE_STROKE_AND_FILL:
                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    break;

                default:
                case RoundParameters.STYLE_FILL:
                    mPaint.setStyle(Paint.Style.FILL);
                    break;
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
    public void setAlpha(int alpha) {
        initBorderPaintIfNeed();
        mPaint.setAlpha(alpha);
    }
    public void setColorFilter(ColorFilter filter) {
        initBorderPaintIfNeed();
        mPaint.setColorFilter(filter);
    }
    /**
     * the callback
     */
    public interface Callback{
        /**
         * called on draw round-part
         * @param delegate the round part delegate
         * @param canvas the canvas to draw
         */
        void draw0(RoundPartDelegate delegate, Canvas canvas);
    }

    /**
     * the calculator which can used for animate drawable.
     * @since 1.0.3
     */
    public interface Calculator{
        /**
         * get the round size
         * @param delegate the round part delegate
         * @param rp the round parameter
         * @param x true if as x-axis, false as y-axis
         * @return the round size
         */
        float getRoundSize(RoundPartDelegate delegate,RoundParameters rp, boolean x);
        /**
         * get the border size
         * @param delegate the round part delegate
         * @param rp the round parameter
         * @param x true if as x-axis, false as y-axis
         * @return the border size
         */
        float getBorderSize(RoundPartDelegate delegate,RoundParameters rp, boolean x);
    }
}
