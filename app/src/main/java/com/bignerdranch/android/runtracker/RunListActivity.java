package com.bignerdranch.android.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by bioyang on 15/8/26.
 */
public class RunListActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new RunListFragment();
    }
}
