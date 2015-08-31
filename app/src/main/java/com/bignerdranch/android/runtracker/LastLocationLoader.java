package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.location.Location;

/**
 * Created by 安軻 on 2015/8/31.
 */
public class LastLocationLoader extends DataLoader<Location>
{
    private long mRunId;

    public LastLocationLoader(Context context, long runId)
    {
        super(context);
        mRunId = runId;
    }

    @Override
    public Location loadInBackground()
    {
        return RunManager.get(getContext()).getLastLocationForRun(mRunId);
    }
}
