package com.heaven7.android.ui.round;

import android.view.View;

public class ViewRoundPartDelegate implements RoundPartDelegate {

    private final View view;

    public ViewRoundPartDelegate(View view) {
        this.view = view;
    }

    @Override
    public int getWidth() {
        return view.getWidth();
    }

    @Override
    public int getHeight() {
        return view.getHeight();
    }

    @Override
    public int getPaddingLeft() {
        return view.getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return view.getPaddingTop();
    }

    @Override
    public int getPaddingRight() {
        return view.getPaddingStart();
    }

    @Override
    public int getPaddingBottom() {
        return view.getPaddingBottom();
    }
}
