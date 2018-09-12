package com.kloosin.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.kloosin.R;
import com.kloosin.adapter.FriendAdapter;
import com.kloosin.common.Common;
import com.kloosin.dialog.Loading;
import com.kloosin.dialog.ProfileMenu;
import com.kloosin.fragment.EditProfileFragment;
import com.kloosin.fragment.FeedFragment;

import com.kloosin.fragment.ProfileFragment;

import com.kloosin.service.KLRestService;
import com.kloosin.service.LocationService;
import com.kloosin.service.model.FriendModel;
import com.kloosin.service.model.FriendTrack;
import com.kloosin.utility.listener.PopupMenuListener;

import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.activeandroid.Cache.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Loading mLoader;
    private BottomSheetBehavior sheetBehavior;
    int PICK_IMAGE_PAN = 1;
    android.hardware.Camera camera;
    ImageView imageView;
    //*created by Shaid Reza


    ImageView locationView;
    private static final String TAG = MainActivity.class.getSimpleName();
    CheckBox mCheckBox;
    EmojiconEditText emojiconEditText;
    EmojiconTextView textView;
    ImageView emojiImageView;
    ImageView submitButton;
    View newrootView;
    ImageView postSubmitButton;
    ImageView postSubmit;
    ProgressDialog progressDialog;
    ImageView show_menu;
    ProgressBar pBar;
    List<FriendTrack> trackList = new ArrayList<>();


    EmojIconActions emojIcon;
    Toolbar toolbar;
    SearchView searchView;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message Main: " + message);

            if (message == null) {

                return;
            }
            if (message.contentEquals("Locationfetched")) {

                if (Common.latitude != 0.0 && Common.longitude != 0.0) {
                    try {
                        String userId = CommonHelper.getInstance().getCurrentUser(MainActivity.this).getUserId();
                        postLocationToServer(Common.latitude, Common.longitude, Integer.parseInt(userId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                processStopService(LocationService.TAG);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("locationUpdate"));
        setContentView(R.layout.activity_main);

        //  imageView = (ImageView) findViewById(R.id.image_view);
        locationView = (ImageView) findViewById(R.id.locationview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView centerCircle = findViewById(R.id.center_circle);
        ImageView chatButton = findViewById(R.id.chat_button);

        ImageView cameraButton = findViewById(R.id.id_camera);
        ImageView usernotificationbutton = findViewById(R.id.usernotifitionbuttom);
        show_menu = findViewById(R.id.show_menu);
        pBar = findViewById(R.id.pBar);

        locationView.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isLocationPermissionGranted()) {
                    if (isGPSon()) {
                        pBar.setVisibility(View.VISIBLE);
                        processStartService(LocationService.TAG);


                    }
                }

            }
        }, 1000);

        if (isMyServiceRunning(LocationService.class) && Common.latitude != 0.0 && Common.longitude != 0.0) {
            processStopService(LocationService.TAG);

        } else if (!isMyServiceRunning(LocationService.class)) {
            processStartService(LocationService.TAG);
        }

        centerCircle.setOnClickListener(this);
        chatButton.setOnClickListener(this);

        cameraButton.setOnClickListener(this);
        usernotificationbutton.setOnClickListener(this);
        show_menu.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
// button2.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


        //pushFragment( (SPHelper.getInstance(getMContext()).readBoolean(TagNameHelper.getInstance().IS_INVITATION_COMPLETED_KEY)) ? new FeedFragment() : new InvitationFragment(), false);
        pushFragment(new FeedFragment(), false);
    }

    public void pushFragment(Fragment _fragment) {
        pushFragment(_fragment, true);
    }

    public void pushFragment(Fragment _fragment, boolean _addToBackStack) {
        FragmentTransaction _transaction = getSupportFragmentManager().beginTransaction();
        if (_addToBackStack) _transaction.addToBackStack(_fragment.getClass().getSimpleName());
        _transaction.replace(R.id.container, _fragment);
        _transaction.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.center_circle:
                Intent sharePostActivity = new Intent(MainActivity.this, SharePostActivity.class);
                startActivity(sharePostActivity);

                //                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                } else {
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }

                break;

            case R.id.chat_button:

                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent);

                break;


            case R.id.id_camera:


                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent2, 0);

                break;


            case R.id.usernotifitionbuttom:

                Intent userNotificationIntent = new Intent(MainActivity.this, UserNotification.class);
                startActivity(userNotificationIntent);

                break;


            case R.id.bottom_sheet:
                hideKeyboard(view);
                break;


            case R.id.locationview:
                Intent inyetlocationView = new Intent(MainActivity.this, TrackActivity.class);
                startActivity(inyetlocationView);

                break;

            case R.id.show_menu:
                final ProfileMenu dialog = new ProfileMenu(MainActivity.this, R.style.DialogTheme);
                dialog.setListener(new PopupMenuListener() {
                    @Override
                    public void onClick(int viewId) {
                        dialog.dismiss();
                        handleProfileMenuClick(viewId);
                    }
                });
                dialog.show();
                break;


        }


    }


    /**
     * @return Application Loader
     */
    public Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }

    /**
     * @return Context
     */
    private Context getMContext() {
        return MainActivity.this;
    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void handleProfileMenuClick(int viewId) {
        switch (viewId) {
            case R.id.edit_profile_link:
                (MainActivity.this).pushFragment(new EditProfileFragment(), true);
                break;
            case R.id.profile_image:
                (MainActivity.this).pushFragment(new ProfileFragment(), true);
                break;
            case R.id.btn_logout:
                CommonHelper.getInstance().performLogout(getContext());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                i.putExtra("friendName", query);
                startActivity(i);
                return true;


            }

            @Override
            public boolean onQueryTextChange(String query) {
                //populate json function
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processStartService(final String tag) {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.addCategory(tag);
        startService(intent);
    }

    private void processStopService(final String tag) {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.addCategory(tag);
        stopService(intent);
    }

    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Message", "Permission is granted");
                return true;
            } else {

                Log.d("Message", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            Log.d("Message", "Permission is granted");
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Message", "Permission: " + permissions[0] + "was " + grantResults[0]);
            // resume tasks needing this permission
//            processStartService(LocationService.TAG);
//            sendLocationtoServer(String.valueOf(Common.Latitude),String.valueOf(Common.Longitude));
        }
    }

    private boolean isGPSon() {
        boolean isOn = false;
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOn) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        return isOn;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pBar.setVisibility(View.GONE);

    }

    private void postLocationToServer(double latitude, double longitude, int userId) throws Exception {
        pBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("latitude", latitude);
        jsonObject.put("longitude", longitude);
        jsonObject.put("deviceId", Build.SERIAL);
        Call<Void> trackFriend = KLRestService.getInstance().getService(this).postFriendPosition(jsonObject.toString());
        trackFriend.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                pBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    System.out.println("Location posted to server");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
