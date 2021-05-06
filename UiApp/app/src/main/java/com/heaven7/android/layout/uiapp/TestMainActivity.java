package com.heaven7.android.layout.uiapp;

import com.heaven7.android.layout.uiapp.sample.TestRoundActivity;
import com.heaven7.android.layout.uiapp.sample.TestRoundActivity2;
import com.heaven7.android.layout.uiapp.sample.TestRoundDrawableActivity;
import com.heaven7.android.layout.uiapp.sample.TestSpecialTextAc;

import java.util.List;

/**
 * this class help we test ui.
 * Created by heaven7 on 2017/3/24 0024.
 */

public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(TestRoundActivity.class, "TestRoundActivity"));
        list.add(new ActivityInfo(TestRoundActivity2.class, "TestRoundActivity2"));
        list.add(new ActivityInfo(TestRoundDrawableActivity.class, "TestRoundDrawableActivity"));
        list.add(new ActivityInfo(TestSpecialTextAc.class, "TestSpecialTextAc"));
    }
}

