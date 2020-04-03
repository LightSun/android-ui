package com.heaven7.android.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.heaven7.android.ui.round.RoundHelper;
import com.heaven7.android.ui.round.RoundPartDelegate;

/**
 * the round text view. which can use round parameter to set
 *
 * @author heaven7
 * @since 1.0.3
 */
public class RoundTextView extends AppCompatTextView implements RoundHelper.Callback {

    private final RoundHelper mHelper = new RoundHelper(this, this);

    public RoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public void draw0(RoundPartDelegate delegate, Canvas canvas) {
        super.draw(canvas);
    }

    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        return mHelper.onSaveInstanceState(super.onSaveInstanceState());
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(mHelper.onRestoreInstanceState(state));
    }
}
