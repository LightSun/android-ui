package com.heaven7.android.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.heaven7.android.ui.round.RoundHelper;

/**
 * the round image view. which can use round parameter to set
 *
 * @author heaven7
 */
public class RoundImageView extends AppCompatImageView implements RoundHelper.Callback {

    private final RoundHelper mHelper = new RoundHelper(this, this);

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @SuppressLint("WrongCall")
    @Override
    public void draw0(View view, Canvas canvas) {
        super.draw(canvas);
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
}
