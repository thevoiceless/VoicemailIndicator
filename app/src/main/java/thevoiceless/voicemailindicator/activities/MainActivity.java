package thevoiceless.voicemailindicator.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import thevoiceless.voicemailindicator.R;
import thevoiceless.voicemailindicator.services.VoicemailDetectorService;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NOTIFICATION_TAG = "voicemail_indicator_notification";

    private static int sNotificationId = 1;

    private Button mTriggerNotificationButton;
    private TextView mEmptyText;
    // TODO: RecyclerView
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTriggerNotificationButton = ((Button) findViewById(R.id.trigger_notification));
        mEmptyText = ((TextView) findViewById(R.id.empty));
        mList = ((ListView) findViewById(R.id.list));

        init();

        startService(new Intent(this, VoicemailDetectorService.class));
    }

    private void init() {
        mTriggerNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Creating test notification");
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (nm != null) {
                    Log.i(TAG, "Created notification manager");
                    Notification notification = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Test notification")
                            .setContentText("This is a test")
                            .build();
                    if (notification != null) {
                        Log.i(TAG, "Created notification, calling notify()");
                        nm.notify(NOTIFICATION_TAG, sNotificationId++, notification);
                    }
                }
            }
        });
        mList.setEmptyView(mEmptyText);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
