package com.heaven7.android.ui.round;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private static final int[] ATTRS_ROUND = {
            R.attr.lib_ui_round,
    };
    private static final int[] ATTRS_ROUND_XY = {
            R.attr.lib_ui_round_x,
            R.attr.lib_ui_round_y,
    };
    private static final int[] ATTRS_BORDER_XY = {
            R.attr.lib_ui_round_border_x,
            R.attr.lib_ui_round_border_y,
    };
    private static final int[] ATTRS_BORDER = {
            R.attr.lib_ui_round_border,
            R.attr.lib_ui_round_border_color,
    };
    private static final int[] ATTRS_EXTRA = {
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
    public static RoundParameters of(Context context, @Nullable AttributeSet attrs, RoundParameters defaultParam){
        //1, set default
        //2, cover by attrs
        //3, cover by theme
       /* TypedArray ta = context.obtainStyledAttributes(attrs, ATTRS);
        System.err.println("getIndexCount = " + ta.getIndexCount());

        ta = context.obtainStyledAttributes(attrs, ATTRS_ROUND);
        System.err.println("getIndexCount = " + ta.getIndexCount());

        ta = context.obtainStyledAttributes(attrs, ATTRS_ROUND_XY);
        System.err.println("getIndexCount = " + ta.getIndexCount());

        ta = context.obtainStyledAttributes(attrs, ATTRS_BORDER_XY);
        System.err.println("getIndexCount = " + ta.getIndexCount());

        ta = context.obtainStyledAttributes(attrs, ATTRS_BORDER);
        System.err.println("getIndexCount = " + ta.getIndexCount());

        ta = context.obtainStyledAttributes(attrs, ATTRS_EXTRA);
        System.err.println("getIndexCount = " + ta.getIndexCount());*/

        //why global attrs can't obtain larger than two ?
        RoundParameters p = new RoundParameters(defaultParam);
        int size = 0;
        size += getImpl(context, attrs, ATTRS_ROUND, p, defaultParam);
        size += getImpl(context, attrs, ATTRS_ROUND_XY, p, defaultParam);
        size += getImpl(context, attrs, ATTRS_BORDER_XY, p, defaultParam);
        size += getImpl(context, attrs, ATTRS_BORDER, p, defaultParam);
        size += getImpl(context, attrs, ATTRS_EXTRA, p, defaultParam);
        return size != 0 ? p : defaultParam;
    }

    private static int getImpl(Context context, @Nullable AttributeSet as, int[] attrs,
                               RoundParameters out, RoundParameters drp){
        TypedArray ta = context.obtainStyledAttributes(as, attrs);
        try {
            return getRoundParameter(ta, attrs, out, drp);
        }finally {
            ta.recycle();
        }
    }

    private static int getRoundParameter(TypedArray ta, int[] attrs, RoundParameters out, RoundParameters drp) {
        if(drp == null){
            drp = DEFAULT;
        }
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int idx = ta.getIndex(i);
            int index = attrs[idx];
            if(index == R.attr.lib_ui_round){
                int val = ta.getDimensionPixelSize(idx, (int) drp.getRadiusX());
                out.setRadiusX(val);
                out.setRadiusY(val);
            }else if(index == R.attr.lib_ui_round_x){
                out.setRadiusX(ta.getDimensionPixelSize(idx, (int) drp.getRadiusX()));
            }
            else if(index == R.attr.lib_ui_round_y){
                out.setRadiusY(ta.getDimensionPixelSize(idx, (int) drp.getRadiusY()));
            }
            else if(index == R.attr.lib_ui_round_border){
                int val = ta.getDimensionPixelSize(idx, (int) drp.getBorderWidthX());
                out.setBorderWidthX(val);
                out.setBorderWidthY(val);
            }
            else if(index == R.attr.lib_ui_round_border_x){
                out.setBorderWidthX(ta.getDimensionPixelSize(idx, (int) drp.getBorderWidthX()));
            }
            else if(index == R.attr.lib_ui_round_border_y){
                out.setBorderWidthY(ta.getDimensionPixelSize(idx, (int) drp.getBorderWidthY()));
            }
            else if(index == R.attr.lib_ui_round_border_color){
                out.setBorderColor(ta.getColor(idx, drp.getBorderColor()));
            }
            else if(index == R.attr.lib_ui_round_circle){
                out.setCircle(ta.getBoolean(idx, drp.isCircle()));
            }
            else if(index == R.attr.lib_ui_round_afterPadding){
                out.setRoundAfterPadding(ta.getBoolean(idx, drp.isRoundAfterPadding()));
            }
        }
        return indexCount;
    }

    public static void applyTheme(@NonNull Resources.Theme t, @Nullable RoundParameters rp) {
        if(rp == null){
            return;
        }
        TypedArray ta = t.obtainStyledAttributes(ATTRS);
        try {
            getRoundParameter(ta, ATTRS, rp, null);
        }finally {
            ta.recycle();
        }
    }
    public static void applyTheme(@NonNull Resources res, @Nullable Resources.Theme theme, @NonNull AttributeSet set, @Nullable RoundParameters rp) {
        if(rp == null){
            return;
        }
        TypedArray ta = obtainAttributes(res, theme, set, ATTRS);
       // TypedArray ta = res.obtainAttributes(set, ATTRS);
        try {
            getRoundParameter(ta, ATTRS,  rp, null);
        }finally {
            ta.recycle();
        }
    }

    public static TypedArray obtainAttributes(@NonNull Resources res,
                                              @Nullable Resources.Theme theme, @NonNull AttributeSet set, @NonNull int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }
}
