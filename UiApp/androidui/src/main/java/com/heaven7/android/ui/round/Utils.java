package com.heaven7.android.ui.round;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*public*/ final class Utils {

    public static int[] getAttrArray(TypedArray a){
        //a.extractThemeAttrs()
        try {
            Method method = TypedArray.class.getMethod("extractThemeAttrs");
            return (int[]) method.invoke(a);
        }catch (Exception e){
            return null;
        }
    }

    //like: ColorDrawable
    public static void applyTheme(@NonNull Resources.Theme t, @Nullable RoundParameters rp, int[] res, int[] styleRes) {
        if(rp == null){
            return;
        }
        Method method;
        try {
            method = t.getClass().getMethod("resolveAttributes", int[].class, int[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        try {
            TypedArray ta = (TypedArray)method.invoke(t, res, styleRes);

            if(ta != null){
                ta.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
//        TypedArray ta = t.obtainStyledAttributes(styleRes);
//        try {
//            method.invoke(t, styleRes);
//            TypedValue typedValue = new TypedValue();
//            typedValue.data = 0;
//            t.resolveAttribute(R.attr.lib_ui_round, typedValue, true);
//            rp.setRadiusX(typedValue.getFloat());
//            rp.setRadiusY(typedValue.getFloat());
//
//        }finally {
//            ta.recycle();
//        }
    }
    public static TypedArray obtainAttributes(@NonNull Resources res,
                                              @Nullable Resources.Theme theme, @NonNull AttributeSet set, @NonNull int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }
}
