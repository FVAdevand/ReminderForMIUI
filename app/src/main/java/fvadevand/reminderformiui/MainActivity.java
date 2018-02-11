package fvadevand.reminderformiui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fvadevand.reminderformiui.data.NotificationContract.NotificationEntry;
import fvadevand.reminderformiui.service.NotificationIntentService;
import fvadevand.reminderformiui.service.NotificationTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTIFICATIONS_LOADER_ID = 145;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    NotificationAdapter mCursorAdapter;


    // TODO: add list notification (saved and planned notification)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditorActivity.class));
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        ListView notificationListView = findViewById(R.id.lv_notifications);
        View emptyView = findViewById(R.id.empty_view);
        notificationListView.setEmptyView(emptyView);
        mCursorAdapter = new NotificationAdapter(this, null);
        notificationListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(NOTIFICATIONS_LOADER_ID, null, this);

        notificationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditorActivity.class);
                editIntent.setData(ContentUris.withAppendedId(NotificationEntry.CONTENT_URI, id));
                startActivity(editIntent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_resend:
                Intent serviceResendIntent = new Intent(this, NotificationIntentService.class);
                serviceResendIntent.setAction(NotificationTask.ACTION_RESEND_NOTIFICATIONS);
                startService(serviceResendIntent);
                return true;

            case R.id.action_delete_all:
                Intent serviceDeleteIntent = new Intent(this, NotificationIntentService.class);
                serviceDeleteIntent.setAction(NotificationTask.ACTION_DELETE_ALL_NOTIFICATIONS);
                startService(serviceDeleteIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NotificationEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
