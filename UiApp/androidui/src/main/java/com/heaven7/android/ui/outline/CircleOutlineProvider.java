package com.heaven7.android.ui.outline;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by heaven7 on 2018/6/7 0007.
 * call {@linkplain View#invalidateOutline()} to redraw outline
 */
@TargetApi(21)
public class CircleOutlineProvider extends ViewOutlineProvider {

    private final int margin;

    public CircleOutlineProvider(int margin) {
        this.margin = margin;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setOval(margin, margin, view.getWidth() - margin, view.getHeight() - margin);
    }
}
