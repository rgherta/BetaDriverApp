package com.ride.driverapp.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.ride.driverapp.R;
import com.ride.driverapp.model.AcceptanceContract;
import com.ride.driverapp.model.MessageContract;
import com.ride.driverapp.services.MyFirebaseMessagingService;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;
import com.ride.driverapp.ui.adapters.ChatMessagesAdapter;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.viewmodel.RegViewModel;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideConfirmationActivity extends TrackingActivity implements OnMapReadyCallback {

    private static final String TAG = RegViewModel.class.getSimpleName();
    private GoogleMap mMap;

    ArrayList<MessageContract> chatMessages;

    //Recycler
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_info);
        mapFragment.onCreate(null);
        mapFragment.getMapAsync(this);

        //Chat Messages Recycler
        recyclerView = findViewById(R.id.recycler_messages);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatMessages = new ArrayList<MessageContract>();
        mAdapter = new ChatMessagesAdapter(chatMessages); //TODO: get messages arrayluist cu mesajeel
        recyclerView.setAdapter(mAdapter);


        MyFirebaseMessagingService.chat.observe(this, o -> {
            MessageContract newMessage = (MessageContract) o;
            chatMessages.add(newMessage);
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        });

        AcceptanceContract mContract = (AcceptanceContract) MyFirebaseMessagingService.rideBus.getValue();
        sendButtonListener(mContract);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //TODO: handle this shit in trackingactivity, got null here, was ok on a second trial
//        if(mLastknownLocation.getValue() == null) {
//            mLastknownLocation = new Location("myprovider");
//            mLastknownLocation.setLatitude( mDefaultLocation.latitude );
//            mLastknownLocation.setLongitude( mDefaultLocation.longitude );
//
//        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( mLastknownLocation.getValue().getLatitude(), mLastknownLocation.getValue().getLongitude() ), DEFAULT_ZOOM));
        mMap.setPadding(50,50,50,180);
    }




    private void sendButtonListener(AcceptanceContract driverData){
        EditText sendButton = findViewById(R.id.send_button);
        sendButton.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (sendButton.getRight() - sendButton.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Log.w(TAG, "clicked send message");

                    Date currentTime = Calendar.getInstance().getTime();
                    MessageContract newMessage = new MessageContract(sendButton.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), driverData.getDriver(), currentTime, driverData.getRide() );
                    chatMessages.add(newMessage);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                    sendButton.setText("");

                    IApiService apiService = ApiServiceGenerator.createService(IApiService.class, this);
                    Call<MessageContract> call = apiService.sendMessage(newMessage);
                    call.enqueue(new Callback<MessageContract>() {

                        @Override
                        public void onResponse(Call<MessageContract> call, Response<MessageContract> response) {
                            Log.w("response", response.toString());

                        }

                        @Override
                        public void onFailure(Call<MessageContract> call, Throwable t) {
                            Log.w("responserror", t);
                        }
                    });

                    return true;
                }
            }
            return false;
        });
    }




}
