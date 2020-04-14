package com.heaven7.android.ui.round;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

/**
 * the round helper. help fast set round parameters
 * <p>Note: currently not compat with 'android:clipchildren' for view. but part is support.</p>
 * @author heaven7
 */
public final class RoundHelper {

    private static final String TAG = "RoundHelper";
    private final Path mPath = new Path();
    //private final Path mBorderPath = new Path();
    private final RectF mRect = new RectF();
    private Paint mPaint;

    private final View mView;
    private final Resources mResource;
    private final RoundPartDelegate mPartDelegate;
    private final Callback mCallback;
    private boolean mDirty;

    private RoundParameters mParams;

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
     * get the round parameters from known attrs. order is 'attrs -> theme'
     * @param context the context
     * @param attrs the attrs
     * @param dp the default parameters , can be null.
     * @return the round parameters. may be null if no attr-value define in theme or attrs And dp is null.
     */
    public static RoundParameters of(Context context, AttributeSet attrs, RoundParameters dp){
        return Utils.of(context, attrs, dp);
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
                canvas.drawRoundRect(mRect,  getRadius(true), getRadius(false), mPaint);
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
        if(x){
            return mParams.isCircle() ? mPartDelegate.getWidth()*1f / 2 : mParams.getRadiusX();
        }else {
            return mParams.isCircle() ? mPartDelegate.getHeight()*1f / 2 : mParams.getRadiusY();
        }
    }

    private void apply0(int w, int h){
        if(mParams == null){
            return;
        }
        Log.d(TAG, "apply0:  w = " + w + ", h = " + h);
        int padLeft = mPartDelegate.getPaddingLeft();
        int padTop = mPartDelegate.getPaddingTop();
        int padRight = mPartDelegate.getPaddingRight();
        int padBottom = mPartDelegate.getPaddingBottom();
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
    private void initBorderPaintIfNeed(){
        if(mPaint == null){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
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

    public int getAlpha() {
        initBorderPaintIfNeed();
        return mPaint.getAlpha();
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
     * the
     */
    public interface RoundCalculator{
        float getRadius(RoundParameters rp, boolean x);
    }
}
