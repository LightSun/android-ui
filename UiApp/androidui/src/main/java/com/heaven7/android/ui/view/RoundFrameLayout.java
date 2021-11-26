package com.heaven7.android.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.heaven7.android.ui.R;
import com.heaven7.android.ui.round.RoundHelper;
import com.heaven7.android.ui.round.RoundAttacher;
import com.heaven7.android.ui.round.RoundParameters;
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
        RoundParameters out = new RoundParameters();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);
        try {
            int val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round, (int) out.getRadiusX());
            out.setRadiusX(val);
            out.setRadiusY(val);
            val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round_x, (int) out.getRadiusX());
            out.setRadiusX(val);
            val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round_y, (int) out.getRadiusY());
            out.setRadiusY(val);
            val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round_border, (int) out.getBorderWidthX());
            out.setBorderWidthX(val);
            out.setBorderWidthY(val);
            val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round_border_x, (int) out.getBorderWidthX());
            out.setBorderWidthX(val);
            val = ta.getDimensionPixelSize(R.styleable.RoundFrameLayout_lib_ui_round_border_y, (int) out.getBorderWidthY());
            out.setBorderWidthY(val);

            val = ta.getColor(R.styleable.RoundFrameLayout_lib_ui_round_border_color, Color.TRANSPARENT);
            out.setBorderColor(val);
            out.setCircle(ta.getBoolean(R.styleable.RoundFrameLayout_lib_ui_round_circle, false));
            out.setRoundAfterPadding(ta.getBoolean(R.styleable.RoundFrameLayout_lib_ui_round_afterPadding, false));
            out.setStyle(ta.getInt(R.styleable.RoundFrameLayout_lib_ui_round_style, RoundParameters.STYLE_STROKE));
        }finally {
            ta.recycle();
        }
        mHelper.setRoundParameters(out);
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
