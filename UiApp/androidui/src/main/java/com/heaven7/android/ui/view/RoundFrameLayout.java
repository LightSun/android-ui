package com.heaven7.android.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.heaven7.android.ui.round.RoundHelper;
import com.heaven7.android.ui.round.RoundAttacher;
import com.heaven7.android.ui.round.RoundPartDelegate;

/**
 * the round frame-layout
 * @author heaven7
 * @since 1.0.3
 */
public class RoundFrameLayout extends FrameLayout implements RoundAttacher, RoundHelper.Callback {

    private final RoundHelper mHelper = new RoundHelper(this, this);

    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHelper.setRoundParameters(RoundHelper.of(context, attrs, null));
        mHelper.apply();
    }

    public RoundHelper getRoundHelper() {
        return mHelper;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHelper.onSizeChanged(w, h);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        mHelper.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mHelper.draw(canvas, mDispatch);
    }
    @Override
    public void draw0(RoundPartDelegate delegate,Canvas canvas) {
        RoundFrameLayout.super.draw(canvas);
    }
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return mHelper.onSaveInstanceState(super.onSaveInstanceState());
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(mHelper.onRestoreInstanceState(state));
    }
    private final RoundHelper.Callback mDispatch = new RoundHelper.Callback() {
        @Override
        public void draw0(RoundPartDelegate delegate, Canvas canvas) {
            RoundFrameLayout.super.dispatchDraw(canvas);
        }
    };
}
