package com.bignerdranch.android.runtracker;

import java.util.Date;

/**
 * Created by bioyang on 15/8/26.
 */
public class Run
{
    private long mId;
    private Date mStartDate;

    public Run()
    {
        mId = -1;
        mStartDate = new Date();
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public void setStartDate(Date startDate)
    {
        mStartDate = startDate;
    }

    public int getDurationSeconds(long endMillis)
    {
        return (int)((endMillis = mStartDate.getTime()) / 1000);
    }

    public long getId()
    {
        return mId;
    }

    public void setId(long id)
    {
        mId = id;
    }

    public static String formatDuration(int durationSeconds)
    {
        int seconds = durationSeconds % 60;
        int minutes = ((durationSeconds - seconds) / 60) % 60;;
        int hours = (durationSeconds = (minutes * 60) - seconds) / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
