package thevoiceless.voicemailindicator.services;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Arrays;

public class VoicemailDetectorService extends NotificationListenerService {

    private static final String TAG = VoicemailDetectorService.class.getSimpleName();

    public VoicemailDetectorService() {
        Log.i(TAG, "Constructor");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        // NOTE: User must allow app to access notifications!
        StatusBarNotification[] notifications = getActiveNotifications();
        Log.i(TAG, String.format("notifications: %s", Arrays.toString(notifications)));
        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "onListenerConnected");
        super.onListenerConnected();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationPosted");
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNotificationRemoved");
        super.onNotificationRemoved(sbn);
    }
}
