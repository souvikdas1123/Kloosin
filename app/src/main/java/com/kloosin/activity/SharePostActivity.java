package com.kloosin.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kloosin.R;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.edit_profile.UserPost;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.activeandroid.Cache.getContext;

public class SharePostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SharePostActivity.class.getSimpleName();
    int PICK_IMAGE_PAN = 1;
    ImageView postSubmit;
    ProgressDialog progressDialog;
    private Loading mLoader;


    EmojIconActions emojIcon;
    EmojiconEditText emojiconEditText;
    View newrootView;
    ImageView imageShow;
    ImageView emojiButton;
    ImageView attachImage;
    ImageView postButtonSubmit;
    String strImagePath;
    boolean isImageFromGoogleDrive = false;
    boolean isKitKat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    String inputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        imageShow = (ImageView) findViewById(R.id.showImage);
        attachImage = (ImageView) findViewById(R.id.attachImage);
        emojiButton = (ImageView) findViewById(R.id.emoji_logo);
        postButtonSubmit = (ImageView) findViewById(R.id.postSubmitButton);
        // imageShow.setOnClickListener(this);
        attachImage.setOnClickListener(this);
        postButtonSubmit.setOnClickListener(this);
        newrootView = findViewById(R.id.rootviewlayot);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojiconedit_text);
        emojIcon = new EmojIconActions(this, newrootView, emojiconEditText, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {

            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }


        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.attachImage:
                Intent attachIntent = new Intent();
                attachIntent.setType("image/*");
                attachIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(attachIntent, "Select Image"), PICK_IMAGE_PAN);

                break;


            case R.id.postSubmitButton:
                //sendPost()
                postImage(null == inputStream ? null : new File(inputStream));

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageShow.setImageBitmap(bitmap);
                inputStream = getNewPath(uri);
                //  postImage( new File(inputStream) );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    private void postImage(final File file) {
        UserPost userPost = new UserPost();

        userPost.setLatitude(123);
        userPost.setLongitude(654);

        userPost.setPostType(null != file ? "Image" : "");
        //   _request.setUsername(String.valueOf(((EditText) findViewById(R.id.username)).getText()));
        userPost.setPostBody(String.valueOf(((EmojiconEditText) findViewById(R.id.emojiconedit_text)).getText()));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("postImagePath", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), CommonHelper.getInstance().getCurrentUser(getContext()).getUserId());
        RequestBody r_posttype = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostType());
        RequestBody r_postbody = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostBody());
        getLoader().show();
        Call<Void> _call = KLRestService.getInstance().getService(getContext()).postImage1(description, body, r_posttype, r_postbody, userPost.getLongitude(), userPost.getLongitude());
        _call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getLoader().dismiss();
                if (response.code() == 200) {
                    onBackPressed();
                } else {
                    RequestHelper.getInstance().handleErrorResponse(getContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                getLoader().dismiss();
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });


    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getNewPath(Uri uri) {

        if (isKitKat && DocumentsContract.isDocumentUri(SharePostActivity.this, uri)) {

            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    strImagePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    Pattern DIR_SEPORATOR = Pattern.compile("/");
                    Set<String> hashSet = new HashSet<>();
                    String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                    String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                    String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                    if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                        if (TextUtils.isEmpty(rawExternalStorage)) {
                            hashSet.add("/storage/sdcard0");
                        } else {
                            hashSet.add(rawExternalStorage);
                        }
                    } else {
                        String rawUserId;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            rawUserId = "";
                        } else {
                            String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String[] folders = DIR_SEPORATOR.split(storagePath);
                            String lastFolder = folders[folders.length - 1];
                            boolean isDigit = false;
                            try {
                                Integer.valueOf(lastFolder);
                                isDigit = true;
                            } catch (NumberFormatException ignored) {
                            }
                            rawUserId = isDigit ? lastFolder : "";
                        }
                        if (TextUtils.isEmpty(rawUserId)) {
                            hashSet.add(rawEmulatedStorageTarget);
                        } else {
                            hashSet.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                        }
                    }
                    if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                        String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                        Collections.addAll(hashSet, rawSecondaryStorages);
                    }
                    String[] temp = hashSet.toArray(new String[hashSet.size()]);

                    for (int i = 0; i < temp.length; i++) {
                        File temporaryFile = new File(temp[i] + "/" + split[1]);
                        if (temporaryFile.exists()) {
                            strImagePath = temp[i] + "/" + split[1];
                        }
                    }
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                String documentId = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));

                Cursor cursor = null;
                String column = "_data";
                String[] projection = {
                        column
                };
                try {
                    cursor = getContentResolver().query(contentUri, projection, null, null,
                            null);
                    if (cursor != null && cursor.moveToFirst()) {
                        final int column_index = cursor.getColumnIndexOrThrow(column);
                        strImagePath = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{
                        split[1]
                };
                Cursor cursor = null;
                String column = "_data";
                String[] projection = {
                        column
                };
                try {
                    cursor = getContentResolver().query(contentUri, projection, selection, selectionArgs,
                            null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        strImagePath = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                isImageFromGoogleDrive = true;
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            String column = "_data";
            String[] projection = {
                    column
            };
            try {
                cursor = getContentResolver().query(uri, projection, null, null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(column);
                    strImagePath = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            strImagePath = uri.getPath();
        }


        if (isImageFromGoogleDrive) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(strImagePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        }
        return strImagePath;

    }

    public Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }

    private Context getMContext() {
        return SharePostActivity.this;
    }


}
