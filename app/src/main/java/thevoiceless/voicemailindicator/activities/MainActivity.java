package thevoiceless.voicemailindicator.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
    private static final String KEY_SERVICE = "key_service";
    private static final String KEY_LISTENER = "key_listener";

    private static int sNotificationId = 1;

    private Button mStartServiceButton;
    private Button mStopServiceButton;
    private Button mAddPhoneStateListenerButton;
    private Button mRemovePhoneStateListenerButton;
    private Button mTriggerNotificationButton;
    private TextView mEmptyText;
    // TODO: RecyclerView
    private ListView mList;

    private boolean mServiceRunning;
    private boolean mAddedListener;

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            Log.i(TAG, String.format("'Message waiting' indicator changed: %s", String.valueOf(mwi)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mServiceRunning = savedInstanceState.getBoolean(KEY_SERVICE);
            mAddedListener = savedInstanceState.getBoolean(KEY_LISTENER);
        }

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_SERVICE, mServiceRunning);
        outState.putBoolean(KEY_LISTENER, mAddedListener);
    }

    private void init() {
        mStartServiceButton = ((Button) findViewById(R.id.start_service));
        mStopServiceButton = ((Button) findViewById(R.id.stop_service));
        mAddPhoneStateListenerButton = ((Button) findViewById(R.id.add_state_listener));
        mRemovePhoneStateListenerButton = ((Button) findViewById(R.id.remove_state_listener));
        mTriggerNotificationButton = ((Button) findViewById(R.id.trigger_notification));

        mEmptyText = ((TextView) findViewById(R.id.empty));
        mList = ((ListView) findViewById(R.id.list));

        mList.setEmptyView(mEmptyText);

        updateButtons();
    }

    private void updateButtons() {
        if (mServiceRunning) {
            mStartServiceButton.setEnabled(false);
            mAddPhoneStateListenerButton.setEnabled(false);
            mRemovePhoneStateListenerButton.setEnabled(false);

            mStopServiceButton.setEnabled(true);
        } else {
            mStartServiceButton.setEnabled(true);
            mAddPhoneStateListenerButton.setEnabled(true);
            mRemovePhoneStateListenerButton.setEnabled(true);

            mStopServiceButton.setEnabled(false);
        }

        if (mAddedListener) {
            mAddPhoneStateListenerButton.setEnabled(false);
            mStartServiceButton.setEnabled(false);
            mStopServiceButton.setEnabled(false);

            mRemovePhoneStateListenerButton.setEnabled(true);
        } else {
            mAddPhoneStateListenerButton.setEnabled(true);
            mStartServiceButton.setEnabled(true);
            mStopServiceButton.setEnabled(true);

            mRemovePhoneStateListenerButton.setEnabled(false);
        }
    }

    public void onStartServiceClicked(View v) {
        Log.i(TAG, "Starting service");
        startService(new Intent(this, VoicemailDetectorService.class));
        mServiceRunning = true;
        updateButtons();
    }

    public void onStopServiceClicked(View v) {
        Log.i(TAG, "Stopping service");
        stopService(new Intent(this, VoicemailDetectorService.class));
        mServiceRunning = false;
        updateButtons();
    }

    public void onAddListenerClicked(View v) {
        Log.i(TAG, "Adding phone state listener");
        TelephonyManager tm = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        if (tm != null) {
            tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
            mAddedListener = true;
            updateButtons();
        }
    }

    public void onRemoveListenerClicked(View v) {
        Log.i(TAG, "Removing phone state listener");
        TelephonyManager tm = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        if (tm != null) {
            tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            mAddedListener = false;
            updateButtons();
        }
    }

    public void onTriggerNotificationClicked(View v) {
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
}
