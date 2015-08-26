package com.bignerdranch.android.runtracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by bioyang on 15/8/26.
 */
public class RunManager
{
    private static final String TAG = "RunManager";

    private static final String PREFS_FILE = "runs";
    private static final String PREFS_CURRENT_RUN_ID = "RunManager.currentId";

    public static final String ACTION_LOCATION = "com.bignerdranch.android.runtracker.ACTION_LOCATION";

    private static RunManager sRunManager;
    private Context mAppContext;
    private LocationManager mLocationManager;

    private static final String TEST_PROVIDER = "TEST_PROVIDER";

    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    private RunManager(Context context)
    {
        mAppContext = context;
        mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);

        mHelper = new RunDatabaseHelper(context);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        mCurrentRunId = mPrefs.getLong(PREFS_CURRENT_RUN_ID, -1);
    }

    public static RunManager get(Context c)
    {
        if (sRunManager == null)
        {
            sRunManager = new RunManager(c.getApplicationContext());
        }

        return sRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate)
    {
        Intent broadcast = new Intent(ACTION_LOCATION);

        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;

        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    public void startLocationUpdates()
    {
        String provider = LocationManager.GPS_PROVIDER;

        if (mLocationManager.getProvider(TEST_PROVIDER) != null && mLocationManager.isProviderEnabled(TEST_PROVIDER))
        {
            provider = TEST_PROVIDER;
        }

        Log.d(TAG, "Using provider: " + provider);

        Location lastKnown = mLocationManager.getLastKnownLocation(provider);

        if (lastKnown != null)
        {
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    private void broadcastLocation(Location lastKnown)
    {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, lastKnown);
        mAppContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates()
    {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null)
        {
            mLocationManager.removeUpdates(pi);
            pi.cancel();;
        }
    }

    public boolean isTrackingRun()
    {
        return getLocationPendingIntent(false) != null;
    }

    public Run startNewRun()
    {
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    public void startTrackingRun(Run run)
    {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong(PREFS_CURRENT_RUN_ID, mCurrentRunId).commit();

        startLocationUpdates();
    }

    public void stopRun()
    {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREFS_CURRENT_RUN_ID).commit();
    }

    private Run insertRun()
    {
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    public RunDatabaseHelper.RunCursor queryRuns()
    {
        return mHelper.queryRuns();
    }

    public void insertLocation(Location location)
    {
        if (mCurrentRunId != -1)
        {
            mHelper.insertLocation(mCurrentRunId, location);
        }
        else
        {
            Log.e(TAG, "Location received with no trackeing run; ignoring.");
        }
    }
}
