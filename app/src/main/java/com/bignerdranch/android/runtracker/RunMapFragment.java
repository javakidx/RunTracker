package com.bignerdranch.android.runtracker;

import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

/**
 * Created by 安軻 on 2015/8/31.
 */
public class RunMapFragment extends SupportMapFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap mGoogleMap;
    private RunDatabaseHelper.LocationCursor mLocationCursor;

    public static RunMapFragment newInstance(long runId)
    {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);

        RunMapFragment runMapFragment = new RunMapFragment();
        runMapFragment.setArguments(args);

        return runMapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null)
        {
            long runId = args.getLong(ARG_RUN_ID, -1);

            if (runId != -1)
            {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mGoogleMap = getMap();
        mGoogleMap.setMyLocationEnabled(true);

        return v;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        long runId = args.getLong(ARG_RUN_ID, -1);
        return new LocationListCursorLoader(getActivity(), runId);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor)
    {
        mLocationCursor = (RunDatabaseHelper.LocationCursor)cursor;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader loader)
    {
        mLocationCursor.close();
        mLocationCursor = null;
    }

    private void updateUI()
    {
        if (mGoogleMap == null || mLocationCursor == null)
        {
            return;
        }

        //Set up an overlay on the map for this run's locations;
        PolylineOptions line = new PolylineOptions();

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        //Iterate the locations
        mLocationCursor.moveToFirst();

        while (!mLocationCursor.isAfterLast())
        {
            Location location = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            Resources r = getResources();

            if (mLocationCursor.isFirst())
            {
                String startDate = new Date(location.getTime()).toString();

                MarkerOptions startMarkOptions = new MarkerOptions()
                        .position(latLng)
                        .title(r.getString(R.string.run_start))
                        .snippet(r.getString(R.string.run_finished_at_format, startDate));

                mGoogleMap.addMarker(startMarkOptions);
            }
            else if (mLocationCursor.isLast())
            {
                String endDate = new Date(location.getTime()).toString();
                MarkerOptions finalMarkOptions = new MarkerOptions()
                        .position(latLng)
                        .title(r.getString(R.string.run_finish))
                        .snippet(r.getString(R.string.run_finished_at_format, endDate));

                mGoogleMap.addMarker(finalMarkOptions);
            }
            line.add(latLng);
            latLngBuilder.include(latLng);

            mLocationCursor.moveToNext();
        }

        //Add the polyline to the map
        mGoogleMap.addPolyline(line);

        //Make the map zoom to show the track, with some padding
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        //Construct a movement instruction for the map camera
        LatLngBounds latLngBounds = latLngBuilder.build();

        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds, display.getWidth(), display.getHeight(), 15);
        mGoogleMap.moveCamera(movement);
    }
}
