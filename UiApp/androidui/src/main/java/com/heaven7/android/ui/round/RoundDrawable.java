package com.heaven7.android.ui.round;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.heaven7.android.ui.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static androidx.core.content.res.TypedArrayUtils.obtainAttributes;

public class RoundDrawable extends Drawable implements RoundAttacher,Drawable.Callback {

    private Drawable mTarget;
    private RoundHelper mRoundHelper;
    private State mState;

    public RoundDrawable(){}
    private RoundDrawable(Drawable base, RoundHelper mRoundHelper){
        this.mTarget = base;
        this.mRoundHelper = mRoundHelper;
    }

    public void initialize(Resources res, Drawable base){
        this.mTarget = base;
        this.mRoundHelper = new RoundHelper(res, new DrawablePartDelegate(this), new RoundHelper.Callback() {
            @Override
            public void draw0(RoundPartDelegate delegate, Canvas canvas) {
                mTarget.draw(canvas);
            }
        });
    }

    public void applyRound(){
        if(mRoundHelper != null){
            mRoundHelper.applyDirect();
        }
    }
    public Drawable getDrawable() {
        return mTarget;
    }
    @Override
    public RoundHelper getRoundHelper() {
        return mRoundHelper;
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        if(mRoundHelper != null){
            mRoundHelper.draw(canvas);
        }
    }
    @Override
    public void setAlpha(int alpha) {
        if(mTarget != null && mRoundHelper != null){
            int old = mTarget.getAlpha();
            if(old != alpha){
                mTarget.setAlpha(alpha);
                mRoundHelper.setAlpha(alpha);
                invalidateSelf();
            }
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if(mTarget != null && mRoundHelper != null){
            mTarget.setColorFilter(colorFilter);
            mRoundHelper.setColorFilter(colorFilter);
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return mTarget != null ? mTarget.getOpacity() : PixelFormat.OPAQUE;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Rect getDirtyBounds() {
        return mTarget != null ? mTarget.getDirtyBounds() : super.getDirtyBounds();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public int getLayoutDirection() {
        return mTarget != null ? mTarget.getLayoutDirection() : View.LAYOUT_DIRECTION_LTR;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(@NonNull Outline outline) {
        if(mTarget != null){
            mTarget.getOutline(outline);
        }else {
            super.getOutline(outline);
        }
    }
    @Override
    public boolean setState(@NonNull int[] stateSet) {
        return super.setState(stateSet) || (mTarget != null && mTarget.setState(stateSet));
    }
    @Override
    public void jumpToCurrentState() {
        mTarget.jumpToCurrentState();
    }
    @NonNull
    @Override
    public Drawable mutate() {
        if(mRoundHelper != null && mTarget != null){
            RoundDrawable rd = new RoundDrawable();
            rd.initialize(mRoundHelper.getResource(), mTarget.mutate());
            //copy round params
            if(mRoundHelper.getRoundParameters() != null){
                rd.mRoundHelper.setRoundParameters(new RoundParameters(mRoundHelper.getRoundParameters()));
            }
            rd.applyRound();
            return rd;
        }
        return this;
    }
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || (mTarget != null && mTarget.setVisible(visible, restart));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void applyTheme(@NonNull Resources.Theme t) {
        if (mTarget != null && mTarget.canApplyTheme()) {
            mTarget.applyTheme(t);
        }
        if(mRoundHelper != null){
            mRoundHelper.setRoundParameters(Utils.applyTheme(t, mRoundHelper.getRoundParameters()));
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean canApplyTheme() {
        return (mRoundHelper != null && mRoundHelper.getRoundParameters() !=null)
                || (mTarget != null && mTarget.canApplyTheme());
    }
    @Override
    public int getAlpha() {
        return mTarget != null ? mTarget.getAlpha() : super.getAlpha();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public ColorFilter getColorFilter() {
        return mTarget != null ? mTarget.getColorFilter() : null;
    }
    @Nullable
    @Override
    public ConstantState getConstantState() {
        if(mTarget == null){
            return null;
        }
        if(mState == null){
            mState = new State(mTarget, mRoundHelper);
        }
        return mState;
    }
    @Override
    public int getIntrinsicWidth() {
        return mTarget != null ? mTarget.getIntrinsicWidth() : -1;
    }
    @Override
    public int getIntrinsicHeight() {
        return mTarget != null ? mTarget.getIntrinsicHeight() : -1;
    }
    @NonNull
    @Override
    public Insets getOpticalInsets() {
        return mTarget != null ? mTarget.getOpticalInsets() : super.getOpticalInsets();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        return mTarget != null ? mTarget.getPadding(padding) : super.getPadding(padding);
    }
    @Nullable
    @Override
    public Region getTransparentRegion() {
        return mTarget != null ? mTarget.getTransparentRegion() : null;
    }

    //====================

    @Override
    public void inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs)
            throws IOException, XmlPullParserException {
        super.inflate(r, parser, attrs);
    }

    @Override
    public void inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs,
                        @Nullable Resources.Theme theme) throws IOException, XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        final TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.RoundDrawable);
        try {
           a.getDrawable(R.styleable.RoundDrawable_drawable);
           //TODO
        }finally {
            a.recycle();
        }
    }

    //===================
    @Override
    public boolean isAutoMirrored() {
        return mTarget != null && mTarget.isAutoMirrored();
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        if(mTarget != null){
            mTarget.setAutoMirrored(mirrored);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean isFilterBitmap() {
        return mTarget != null && mTarget.isFilterBitmap();
    }
    @Override
    public void setFilterBitmap(boolean filter) {
        if(mTarget != null){
            mTarget.setFilterBitmap(filter);
        }
    }
    @Override
    public boolean isProjected() {
        return mTarget != null && mTarget.isProjected();
    }
    @Override
    public boolean isStateful() {
        return mTarget != null && mTarget.isStateful();
    }

    @Override
    public void setChangingConfigurations(int configs) {
        if(mTarget != null){
            mTarget.setChangingConfigurations(configs);
        }
    }

    @Override
    public void setColorFilter(int color, @NonNull PorterDuff.Mode mode) {
        setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setHotspot(float x, float y) {
        if(mTarget != null){
            mTarget.setHotspot(x, y);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if(mTarget != null){
            mTarget.setHotspotBounds(left, top, right, bottom);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTint(int tintColor) {
        if(mTarget != null){
            mTarget.setTint(tintColor);
        }
    }

    @Override
    public void setTintBlendMode(@Nullable BlendMode blendMode) {
        if(mTarget != null){
            mTarget.setTintBlendMode(blendMode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        if(mTarget != null){
            mTarget.setTintList(tint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTintMode(@Nullable PorterDuff.Mode tintMode) {
        if(mTarget != null){
            mTarget.setTintMode(tintMode);
        }
    }

    //==========================
    @Override
    protected boolean onStateChange(int[] state) {
        if (mTarget != null && mTarget.isStateful()) {
            final boolean changed = mTarget.setState(state);
            if (changed) {
                onBoundsChange(getBounds());
            }
            return changed;
        }
        return false;
    }
    @Override
    protected boolean onLevelChange(int level) {
        return mTarget != null && mTarget.setLevel(level);
    }
    @Override
    protected void onBoundsChange(Rect bounds) {
        if (mTarget != null) {
            mTarget.setBounds(bounds);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return mTarget != null && mTarget.setLayoutDirection(layoutDirection);
    }
    //======================================
    @Override
    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public static TypedArray obtainAttributes(@NonNull Resources res,
                                              @Nullable Resources.Theme theme, @NonNull AttributeSet set, @NonNull int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    static class State extends ConstantState{

        final RoundHelper mRoundHelper;
        final Drawable base;

        /*public*/ State(Drawable base, RoundHelper mRoundHelper) {
            this.mRoundHelper = mRoundHelper;
            this.base = base;
        }
        @NonNull
        @Override
        public Drawable newDrawable() {
            return newDrawable(null, null);
        }
        @NonNull
        @Override
        public Drawable newDrawable(@Nullable Resources res, @Nullable Resources.Theme theme) {
            RoundDrawable rd = new RoundDrawable(base, mRoundHelper);
            if(theme != null){
                rd.applyTheme(theme);
            }
            rd.applyRound();
            return rd;
        }
        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }

}
