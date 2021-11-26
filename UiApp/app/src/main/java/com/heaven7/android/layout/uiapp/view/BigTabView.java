package com.heaven7.android.layout.uiapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.heaven7.android.layout.uiapp.R;
import com.heaven7.android.ui.view.RoundLinearLayout;
import com.heaven7.core.util.DimenUtil;

public class BigTabView extends RoundLinearLayout {

    private TextView mTv_count;

    public BigTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        TextView tv_title = new TextView(context);
        tv_title.setTextColor(Color.WHITE);
        tv_title.setTextSize(13);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DimenUtil.dip2px(context, 6);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(HORIZONTAL);
        ll.setLayoutParams(lp);
        ll.setGravity(Gravity.CENTER);

        TextView tv_count = new TextView(context);
        tv_count.setTextColor(Color.WHITE);
        tv_count.setTextSize(15);

        TextView tv_count_unit = new TextView(context);
        tv_count_unit.setTextColor(Color.WHITE);
        tv_count_unit.setTextSize(12);

        ll.addView(tv_count);
        ll.addView(tv_count_unit);

        addView(tv_title);
        addView(ll);

        mTv_count = tv_count;

        String title = "";
        String count_str = "";
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BigTabView);
        try {
            title = ta.getString(R.styleable.BigTabView_btv_title);
            count_str = ta.getString(R.styleable.BigTabView_btv_count_unit);
        }finally {
            ta.recycle();
        }
        tv_title.setText(title);
        tv_count_unit.setText(count_str);
    }

    public void setCount(int count){
        mTv_count.setText(String.valueOf(count));
    }
}
