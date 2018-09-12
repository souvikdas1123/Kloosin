package com.kloosin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kloosin.R;
import com.kloosin.common.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Map;

import retrofit2.http.Url;

public class MapsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String trackUrl = "http://kloosin.ezoneindiaportal.com:80/api/UserTracking/GetUserTracking";
    ProgressBar pBar;
    int userId;
    String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pBar = findViewById(R.id.pBar);

        userId = getIntent().getIntExtra("userId", 0);
        friendName = getIntent().getStringExtra("friendName");
        if (userId != 0) {
            fetchFriendLocation(userId, friendName);
        }

    }

    private void fetchFriendLocation(int userId, final String friendName) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.setTimeout(30000);
        params.put("userId", userId);
        client.get(trackUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(MapsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String response) {
                pBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    double latitude = jsonObject.getDouble("latitude");
                    double longitude = jsonObject.getDouble("longitude");
                    String profileImage = jsonObject.getString("profilePicturePath");
                    plotTomap(latitude, longitude, profileImage, friendName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void plotTomap(final double latitude, final double longitude, final String profileImage, final String friendName) {
        try {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting GoogleMap object from the fragment
            fm.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {
                        try {
                            MarkerOptions opt = new MarkerOptions();
                            opt.position(new LatLng(latitude, longitude));
                           // URL url = new URL(profileImage);
                           // Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                           // opt.icon(BitmapDescriptorFactory.fromBitmap(bmp));
                            opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round));
                            opt.title(friendName);
                            googleMap.addMarker(opt);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userId != 0) {
                    fetchFriendLocation(userId, friendName);
                }

            }
        }, 1 * 60 * 1000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
