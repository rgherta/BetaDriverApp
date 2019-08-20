package com.ride.driverapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ride.driverapp.BuildConfig;
import com.ride.driverapp.R;
import com.ride.driverapp.model.AcceptanceContract;
import com.ride.driverapp.model.MessageContract;
import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static enum ride {
        REFRESH
    }

    private static final String TAG = "MyFirebaseMsgService";
    public static MutableLiveData chat = new MutableLiveData<String>();
    public static MutableLiveData systemBus = new MutableLiveData<String>();
    public static MutableLiveData rideBus = new MutableLiveData<AcceptanceContract>();


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.w(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //TODO: here data payload
            Log.w(TAG, "Message data payload: " + remoteMessage.getData());
            Map mydata = remoteMessage.getData();
            String mType =  mydata.get("type").toString();

            switch (mType){
                case "refresh":
                    if (systemBus.hasActiveObservers()) systemBus.postValue(ride.REFRESH);
                    break;

                case "chat":
                    MessageContract newMessage = new MessageContract( mydata.get("message").toString()
                            , mydata.get("sender").toString()
                            , mydata.get("receiver").toString()
                            , new Date((String) mydata.get("timestamp"))
                            , mydata.get("ride").toString()
                    );
                    newMessage.mType = "incomming";
                    if (chat.hasActiveObservers()) chat.postValue(newMessage);
                    break;

                case "start":
                    if (rideBus.hasActiveObservers()){
                        String mRide =  mydata.get("ride").toString();
                        String mCustomer =  mydata.get("customer").toString();
                        AcceptanceContract myContract = new AcceptanceContract(mRide, mCustomer, "", "");
                        rideBus.postValue(myContract);
                    }
                    break;

            }



            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                // scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.w(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //TODO: finish up subject
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
//        // [END dispatch_job]
//    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        SharedPreferences sharedPreferences = getSharedPreferences( getString(R.string.pref_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FcmToken", token);
        editor.commit();

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        /*
        Intent intent = new Intent(this, ResponseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "canal";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentTitle("my title")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
        */
    }






}
