package com.bignerdranch.android.runtracker;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by 安軻 on 2015/8/31.
 */
public class RunMapFragment extends SupportMapFragment
{
    private static final String ARG_RUN_ID = "RUN_ID";

    private GoogleMap mGoogleMap;

    public static RunMapFragment newInstance(long runId)
    {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);

        RunMapFragment runMapFragment = new RunMapFragment();
        runMapFragment.setArguments(args);

        return runMapFragment;
    }
}
