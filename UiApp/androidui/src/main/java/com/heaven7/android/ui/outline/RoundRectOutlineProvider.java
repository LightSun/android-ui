package com.heaven7.android.ui.outline;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * the round rect outline provider
 * Created by heaven7 on 2018/6/6 0006.
 */
@TargetApi(21)
public class RoundRectOutlineProvider extends ViewOutlineProvider {

    private int margin;
    private float radius;

    public RoundRectOutlineProvider(int margin, float radius) {
        this.margin = margin;
        this.radius = radius;
    }
    public RoundRectOutlineProvider(float radius) {
        this.radius = radius;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(margin, margin,
                view.getWidth() - margin,
                view.getHeight() - margin,
                radius);
    }
}
