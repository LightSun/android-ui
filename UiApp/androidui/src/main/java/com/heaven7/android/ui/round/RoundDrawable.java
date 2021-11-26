package com.heaven7.android.ui.round;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
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
import com.heaven7.android.ui.round.delegate.DrawablePartDelegate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static com.heaven7.android.ui.round.Utils.obtainAttributes;

/**
 * the round drawable. wrap the round helper
 * @see RoundHelper
 * @since 1.0.3
 */
public class RoundDrawable extends Drawable implements RoundAttacher, Drawable.Callback {

    private Drawable mDrawable;
    private RoundHelper mRoundHelper;
    private State mState;

    public RoundDrawable(){}

    private RoundDrawable(Drawable base, RoundHelper mRoundHelper){
        this.mDrawable = base;
        this.mRoundHelper = mRoundHelper;
    }

    /**
     * create round drawable from target context and base drawable.
     * @param context the context
     * @param base the base drawable
     * @return the round drawable
     */
    public static RoundDrawable create(Context context, Drawable base){
        RoundDrawable rd = new RoundDrawable();
        rd.setUpRoundHelper(context.getResources());
        rd.setDrawable(base);
        return rd;
    }

    public void applyRound(){
        if(mRoundHelper != null){
            mRoundHelper.applyDirect();
        }
    }
    public void setDrawable(Drawable dr) {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);

            // Only call setters for data that's stored in the base Drawable.
            dr.setVisible(isVisible(), true);
            dr.setState(getState());
            dr.setLevel(getLevel());
            dr.setBounds(getBounds());
            if(Build.VERSION.SDK_INT >= 23){
                dr.setLayoutDirection(getLayoutDirection());
            }
        }
       // applyRound();
        invalidateSelf();
    }
    public Drawable getDrawable() {
        return mDrawable;
    }
    private void setUpRoundHelper(Resources res){
        this.mRoundHelper = new RoundHelper(res, new DrawablePartDelegate(this), new RoundHelper.Callback() {
            @Override
            public void draw0(RoundPartDelegate delegate, Canvas canvas) {
                mDrawable.draw(canvas);
            }
        });
        applyRound();
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
        if(mDrawable != null && mRoundHelper != null){
            int old = mDrawable.getAlpha();
            if(old != alpha){
                mDrawable.setAlpha(alpha);
                mRoundHelper.setAlpha(alpha);
                invalidateSelf();
            }
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if(mDrawable != null && mRoundHelper != null){
            mDrawable.setColorFilter(colorFilter);
            mRoundHelper.setColorFilter(colorFilter);
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return mDrawable != null ? mDrawable.getOpacity() : PixelFormat.OPAQUE;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Rect getDirtyBounds() {
        return mDrawable != null ? mDrawable.getDirtyBounds() : super.getDirtyBounds();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public int getLayoutDirection() {
        return mDrawable != null ? mDrawable.getLayoutDirection() : View.LAYOUT_DIRECTION_LTR;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getOutline(@NonNull Outline outline) {
        if(mDrawable != null){
            mDrawable.getOutline(outline);
        }else {
            super.getOutline(outline);
        }
    }
    @NonNull
    @Override
    public Drawable mutate() {
        if(mRoundHelper != null && mDrawable != null){
            RoundDrawable rd = new RoundDrawable();
            rd.setUpRoundHelper(mRoundHelper.getResource());
            rd.setDrawable(mDrawable.mutate());
            //set calculator
            rd.mRoundHelper.setCalculator(mRoundHelper.getCalculator());
            //copy round params
            if(mRoundHelper.getRoundParameters() != null){
                rd.mRoundHelper.setRoundParameters(new RoundParameters(mRoundHelper.getRoundParameters()));
                rd.applyRound();
            }
            return rd;
        }
        return this;
    }
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        final boolean superChanged = super.setVisible(visible, restart);
        final boolean changed = mDrawable != null && mDrawable.setVisible(visible, restart);
        return superChanged | changed;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void applyTheme(@NonNull Resources.Theme t) {
        if (mDrawable != null && mDrawable.canApplyTheme()) {
            mDrawable.applyTheme(t);
        }
//        if(mRoundHelper != null){
//            Utils.applyTheme(t, mRoundHelper.getRoundParameters());
//        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean canApplyTheme() {
        return (mRoundHelper != null) || (mDrawable != null && mDrawable.canApplyTheme());
    }
    @Override
    public int getAlpha() {
        return mDrawable != null ? mDrawable.getAlpha() : super.getAlpha();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public ColorFilter getColorFilter() {
        return mDrawable != null ? mDrawable.getColorFilter() : null;
    }
    @Nullable
    @Override
    public ConstantState getConstantState() {
        if(mDrawable == null){
            return null;
        }
        if(mState == null){
            mState = new State(mDrawable, mRoundHelper);
        }
        return mState;
    }
    @Override
    public int getIntrinsicWidth() {
        return mDrawable != null ? mDrawable.getIntrinsicWidth() : -1;
    }
    @Override
    public int getIntrinsicHeight() {
        return mDrawable != null ? mDrawable.getIntrinsicHeight() : -1;
    }
    @NonNull
    @Override
    public Insets getOpticalInsets() {
        if(mDrawable != null && Build.VERSION.SDK_INT >= 29){
            return mDrawable.getOpticalInsets();
        }
        return super.getOpticalInsets();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        return mDrawable != null ? mDrawable.getPadding(padding) : super.getPadding(padding);
    }
    @Nullable
    @Override
    public Region getTransparentRegion() {
        return mDrawable != null ? mDrawable.getTransparentRegion() : null;
    }

    //====================
    @Override
    public void inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs,
                        @Nullable Resources.Theme theme) throws IOException, XmlPullParserException {
        super.inflate(r, parser, attrs, theme);
        //set up round
        RoundParameters rp = new RoundParameters();
        setUpRoundHelper(r);
        //setup self-attrs
        int roundSize;
        int borderSize;
        int color;
        boolean circle;
        Drawable base;
        boolean afterPadding;
        String cn;

        final TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.RoundDrawable);
        try {
            roundSize = a.getDimensionPixelSize(R.styleable.RoundDrawable_round_size, 0);
            borderSize = a.getDimensionPixelSize(R.styleable.RoundDrawable_border_size, 0);
            color = a.getColor(R.styleable.RoundDrawable_border_color, Color.BLACK);
            circle = a.getBoolean(R.styleable.RoundDrawable_circle, false);
            afterPadding = a.getBoolean(R.styleable.RoundDrawable_afterPadding, false);
            base = a.getDrawable(R.styleable.RoundDrawable_drawable);
            cn = a.getString(R.styleable.RoundDrawable_calculator);
        }finally {
            a.recycle();
        }
        rp.setRadiusX(roundSize);
        rp.setRadiusY(roundSize);
        rp.setBorderWidthX(borderSize);
        rp.setBorderWidthY(borderSize);
        rp.setBorderColor(color);
        rp.setCircle(circle);
        rp.setRoundAfterPadding(afterPadding);
        if(cn != null){
            try {
                RoundHelper.Calculator calculator = (RoundHelper.Calculator) Class.forName(cn).newInstance();
                mRoundHelper.setCalculator(calculator);
            } catch (Exception e) {
                throw new RuntimeException("calculator error", e);
            }
        }
        if(rp.isValid()){
            mRoundHelper.setRoundParameters(rp);
            mRoundHelper.applyDirect();
        }
        setDrawable(base);
    }

    //===================
    @Override
    public boolean isAutoMirrored() {
        return mDrawable != null && mDrawable.isAutoMirrored();
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        if(mDrawable != null){
            mDrawable.setAutoMirrored(mirrored);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean isFilterBitmap() {
        return mDrawable != null && mDrawable.isFilterBitmap();
    }
    @Override
    public void setFilterBitmap(boolean filter) {
        if(mDrawable != null){
            mDrawable.setFilterBitmap(filter);
        }
    }
    @Override
    public boolean isProjected() {
        if(mDrawable != null && Build.VERSION.SDK_INT >= 29){
            return mDrawable.isProjected();
        }
        return super.isProjected();
    }
    @Override
    public boolean isStateful() {
        return mDrawable != null && mDrawable.isStateful();
    }

    @Override
    public void setChangingConfigurations(int configs) {
        if(mDrawable != null){
            mDrawable.setChangingConfigurations(configs);
        }
    }

    @Override
    public void setColorFilter(int color, @NonNull PorterDuff.Mode mode) {
        setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setHotspot(float x, float y) {
        if(mDrawable != null){
            mDrawable.setHotspot(x, y);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if(mDrawable != null){
            mDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }
    @Override
    public void getHotspotBounds(Rect outRect) {
        if (mDrawable != null && Build.VERSION.SDK_INT >= 23) {
            mDrawable.getHotspotBounds(outRect);
        } else {
            outRect.set(getBounds());
        }
    }

    @Override
    public void setTintBlendMode(@Nullable BlendMode blendMode) {
       // super.setTintBlendMode(blendMode);
        if(mDrawable != null && Build.VERSION.SDK_INT >= 29){
            mDrawable.setTintBlendMode(blendMode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        if(mDrawable != null){
            mDrawable.setTintList(tint);
        }
    }
    //==========================
    @Override
    protected boolean onStateChange(int[] state) {
        if (mDrawable != null && mDrawable.isStateful()) {
            final boolean changed = mDrawable.setState(state);
            if (changed) {
                onBoundsChange(getBounds());
            }
            return changed;
        }
        return false;
    }
    @Override
    protected boolean onLevelChange(int level) {
        return mDrawable != null && mDrawable.setLevel(level);
    }
    @Override
    protected void onBoundsChange(Rect bounds) {
        applyRound();
        if (mDrawable != null) {
            mDrawable.setBounds(bounds);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return mDrawable != null && mDrawable.setLayoutDirection(layoutDirection);
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

    static class State extends ConstantState{
       // int[] mThemeAttrs;
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
                rd.applyRound();
            }
            return rd;
        }
        @Override
        public int getChangingConfigurations() {
            return 0;
        }
        @Override
        public boolean canApplyTheme() {
            return mRoundHelper != null;
        }
    }

}
