package com.bignerdranch.android.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by 安軻 on 2015/8/31.
 */
public class RunMapActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new RunMapFragment();
    }
}
