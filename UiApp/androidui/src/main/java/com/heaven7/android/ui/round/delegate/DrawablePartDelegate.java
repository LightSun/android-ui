package com.heaven7.android.ui.round.delegate;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.heaven7.android.ui.round.RoundPartDelegate;

public class DrawablePartDelegate implements RoundPartDelegate {

    private final Drawable drawable;

    public DrawablePartDelegate(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
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
    public void getPadding(Rect rect) {
        drawable.getPadding(rect);
    }
}
