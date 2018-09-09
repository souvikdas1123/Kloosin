package com.kloosin.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.kloosin.R;
import com.kloosin.common.Common;
import com.kloosin.dialog.Loading;
import com.kloosin.dialog.ProfileMenu;
import com.kloosin.fragment.EditProfileFragment;
import com.kloosin.fragment.FeedFragment;

import com.kloosin.fragment.ProfileFragment;

import com.kloosin.utility.listener.PopupMenuListener;

import com.kloosin.utility.u.CommonHelper;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


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


    EmojIconActions emojIcon;
    Toolbar toolbar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // MAKE THE SPLASH SCREEN A FULL SCREEN VIEW

        // SET THE VIEW
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

        locationView.setOnClickListener(this);


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
                Intent inyetlocationView = new Intent(MainActivity.this, MapsActivity.class);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}
