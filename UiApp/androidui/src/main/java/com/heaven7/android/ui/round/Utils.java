package com.heaven7.android.ui.round;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.heaven7.android.ui.R;

/*public*/ final class Utils {

    private static final int[] ATTRS = {
            R.attr.lib_ui_round,
            R.attr.lib_ui_round_x,
            R.attr.lib_ui_round_y,
            R.attr.lib_ui_round_border,
            R.attr.lib_ui_round_border_x,
            R.attr.lib_ui_round_border_y,
            R.attr.lib_ui_round_border_color,
            R.attr.lib_ui_round_circle,
            R.attr.lib_ui_round_afterPadding,
    };
    private static final RoundParameters DEFAULT = new RoundParameters();

    /**
     * get the round parameters from known attrs. order is 'attrs -> theme'
     * @param context the context
     * @param attrs the attrs
     * @param defaultParam the default parameters
     * @return the round parameters
     */
    public static RoundParameters of(Context context, AttributeSet attrs, RoundParameters defaultParam){
        //1, set default
        //2, cover by attrs
        //3, cover by theme
        TypedArray ta = context.obtainStyledAttributes(attrs, ATTRS);
        int indexCount = ta.getIndexCount();
        //no attribute and default is null
        if(indexCount == 0 && defaultParam == null){
            return null;
        }
        RoundParameters p = new RoundParameters(defaultParam);
        if(defaultParam == null){
            defaultParam = DEFAULT;
        }
        try {
            for (int i = 0; i < indexCount; i++) {
                int idx = ta.getIndex(i);
                int index = ATTRS[idx];
                if(index == R.attr.lib_ui_round){
                    int val = ta.getDimensionPixelSize(idx, (int) defaultParam.getRadiusX());
                    p.setRadiusX(val);
                    p.setRadiusY(val);
                }else if(index == R.attr.lib_ui_round_x){
                    p.setRadiusX(ta.getDimensionPixelSize(idx, (int) defaultParam.getRadiusX()));
                }
                else if(index == R.attr.lib_ui_round_y){
                    p.setRadiusY(ta.getDimensionPixelSize(idx, (int) defaultParam.getRadiusY()));
                }
                else if(index == R.attr.lib_ui_round_border){
                    int val = ta.getDimensionPixelSize(idx, (int) defaultParam.getBorderWidthX());
                    p.setBorderWidthX(val);
                    p.setBorderWidthY(val);
                }
                else if(index == R.attr.lib_ui_round_border_x){
                    p.setBorderWidthX(ta.getDimensionPixelSize(idx, (int) defaultParam.getBorderWidthX()));
                }
                else if(index == R.attr.lib_ui_round_border_y){
                    p.setBorderWidthY(ta.getDimensionPixelSize(idx, (int) defaultParam.getBorderWidthY()));
                }
                else if(index == R.attr.lib_ui_round_border_color){
                    p.setBorderColor(ta.getColor(idx, defaultParam.getBorderColor()));
                }
                else if(index == R.attr.lib_ui_round_circle){
                    p.setCircle(ta.getBoolean(idx, defaultParam.isCircle()));
                }
                else if(index == R.attr.lib_ui_round_afterPadding){
                    p.setRoundAfterPadding(ta.getBoolean(idx, defaultParam.isRoundAfterPadding()));
                }
            }
        }finally {
            ta.recycle();
        }
        return p;
    }

    public static RoundParameters applyTheme(Resources.Theme t, RoundParameters parameters) {
        //TODO

        return null;
    }
}
