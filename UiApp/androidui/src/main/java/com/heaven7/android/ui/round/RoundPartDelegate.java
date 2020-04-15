package com.heaven7.android.ui.round;

import android.graphics.Rect;

/**
 * the round part delegate
 * @author heven7
 * @since 1.0.2
 */
public interface RoundPartDelegate {

    int getWidth();
    int getHeight();

    void getPadding(Rect rect);
}
