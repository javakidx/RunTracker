package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Javakid on 2015/8/31.
 */
public class LocationListCursorLoader extends SQLiteCursorLoader
{
    private long mRunId;

    public LocationListCursorLoader(Context context, long runId)
    {
        super(context);
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor()
    {
        return RunManager.get(getContext()).getLocationsForRun(mRunId);
    }
}
