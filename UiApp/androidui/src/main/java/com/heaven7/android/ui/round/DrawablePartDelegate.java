package com.heaven7.android.ui.round;


import android.graphics.drawable.Drawable;

public class DrawablePartDelegate implements RoundPartDelegate {

    private final Drawable drawable;

    public DrawablePartDelegate(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public int getWidth() {
        return drawable.getBounds().width();
    }
    @Override
    public int getHeight() {
        return drawable.getBounds().height();
    }
    @Override
    public int getPaddingLeft() {
        return 0;
    }
    @Override
    public int getPaddingTop() {
        return 0;
    }

    @Override
    public int getPaddingRight() {
        return 0;
    }
    @Override
    public int getPaddingBottom() {
        return 0;
    }
}
