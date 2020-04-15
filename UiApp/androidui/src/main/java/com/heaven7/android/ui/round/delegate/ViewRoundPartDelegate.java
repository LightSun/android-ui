package com.heaven7.android.ui.round.delegate;

import android.graphics.Rect;
import android.view.View;

import com.heaven7.android.ui.round.RoundPartDelegate;

public class ViewRoundPartDelegate implements RoundPartDelegate {

    private final View view;

    public ViewRoundPartDelegate(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
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
    public void getPadding(Rect rect) {
        rect.set(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }
}
