package com.bignerdranch.android.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by 安軻 on 2015/8/31.
 */
public class RunMapActivity extends SingleFragmentActivity
{
    public static final String EXTRA_RUN_ID = "com.bignerdranch.android.runtracker.run_id";
    @Override
    protected Fragment createFragment()
    {
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);

        if (runId != -1)
        {
           return RunMapFragment.newInstance(runId);
        }
        else
        {
            return new RunMapFragment();
        }
    }
}
