package com.bignerdranch.android.runtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by bioyang on 15/8/26.
 */
public class RunListFragment extends ListFragment implements LoaderManager.LoaderCallbacks
{
    //private RunDatabaseHelper.RunCursor mCursor;
    private static final int REQUEST_NEW_RUN = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        mCursor = RunManager.get(getActivity()).queryRuns();
//
//        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), mCursor);
//
//        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

//    @Override
//    public void onDestroy()
//    {
//        mCursor.close();
//        super.onDestroy();
//    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent i = new Intent(getActivity(), RunActivity.class);
        i.putExtra(RunActivity.EXTRA_RUN_ID, id);

        startActivity(i);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        return new RunListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader loader, Object data)
    {
        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), (RunDatabaseHelper.RunCursor)data);

        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader)
    {
        setListAdapter(null);
    }

    private static class RunListCursorLoader extends SQLiteCursorLoader
    {
        public RunListCursorLoader(Context context)
        {
            super(context);
        }

        @Override
        protected Cursor loadCursor()
        {
            return RunManager.get(getContext()).queryRuns();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_run:
                Intent i = new Intent(getActivity(), RunActivity.class);

                startActivityForResult(i, REQUEST_NEW_RUN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (REQUEST_NEW_RUN == requestCode)
        {
//            mCursor.requery();
//            ((RunCursorAdapter)getListAdapter()).notifyDataSetChanged();
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    private static class RunCursorAdapter extends CursorAdapter
    {
        private RunDatabaseHelper.RunCursor mRunCursor;

        public RunCursorAdapter(Context context, RunDatabaseHelper.RunCursor cursor)
        {
            super(context, cursor, 0);
            mRunCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            Run run = mRunCursor.getRun();

            TextView startDateTextView = (TextView)view;

            String cellText = context.getString(R.string.cell_text, run.getStartDate());
            startDateTextView.setText(cellText);
        }
    }
}
