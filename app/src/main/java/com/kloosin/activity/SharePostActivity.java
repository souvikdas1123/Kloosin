package com.kloosin.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.kloosin.R;
import com.kloosin.common.Common;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.edit_profile.UserPost;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
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

    // Added by SD photo upload stuffs
    File fileSDImage;
    Uri imageToUploadUri;
    int CAMERA_PHOTO = 111;
    int GALLERY_PHOTO = 112;
    String imageName = "";
    Toolbar toolbar;
    EditText editTextPost;
    TextView userNameTxt;
    CircleImageView profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setSupportActionBar(toolbar);
        imageShow = (ImageView) findViewById(R.id.showPic);
        attachImage = (ImageView) findViewById(R.id.attachImage);
        // emojiButton = (ImageView) findViewById(R.id.emoji_logo);
        postButtonSubmit = (ImageView) findViewById(R.id.postSubmitButton);
        // imageShow.setOnClickListener(this);
        attachImage.setOnClickListener(this);
        postButtonSubmit.setOnClickListener(this);
        editTextPost = findViewById(R.id.editTextPost);
        userNameTxt = findViewById(R.id.user_name);
        profile_image = findViewById(R.id.profile_image);

        CommonHelper commonHelper = CommonHelper.getInstance();
        if (commonHelper.getCurrentUser(this).fullName != null)
            userNameTxt.setText(commonHelper.getCurrentUser(this).fullName);
        else
            userNameTxt.setText("Unknown user");
       /* if(commonHelper.getCurrentUser(this).getProfileImage()!=null)
            Picasso.get().load(commonHelper.getCurrentUser(this).getProfileImage()).into(profile_image);*/

//        newrootView = findViewById(R.id.rootviewlayot);
//        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojiconedit_text);
//        emojIcon = new EmojIconActions(this, newrootView, emojiconEditText, emojiButton);
//        emojIcon.ShowEmojIcon();
//        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
//        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
//
//            @Override
//            public void onKeyboardOpen() {
//                Log.e(TAG, "Keyboard opened!");
//            }
//
//            @Override
//            public void onKeyboardClose() {
//                Log.e(TAG, "Keyboard closed");
//            }
//
//
//        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.attachImage:
                showImageUploadPopUp(view);
                break;

            case R.id.postSubmitButton:
                addPost();
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == PICK_IMAGE_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageShow.setImageBitmap(bitmap);
                inputStream = getNewPath(uri);
                //  addPost( new File(inputStream) );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PHOTO) {

                InputStream myImage = null;
                boolean isSaved = false;

                try {
                    myImage = new FileInputStream(fileSDImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (myImage == null) {
                    try {
                        imageToUploadUri = data.getData();
                        myImage = getContentResolver().openInputStream(imageToUploadUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else
                    isSaved = true; // indicates a hard copy has been saved

                // added SND - storing in external storage
                String fullName = Environment.getExternalStorageDirectory() + File.separator + "Kloosin" + File.separator + "KL_" + Build.SERIAL + imageName;
                Common.imagePath = fullName;
                // ImageView img = (ImageView) findViewById(R.id.imgPhoto);

                // copy the image to the internal directory as a bitmap
                if (myImage != null) {
                    try {
                        Bitmap bmNew = null;
                        FileOutputStream fout = new FileOutputStream(Common.imagePath);
                        Bitmap bm = BitmapFactory.decodeStream(myImage);

                        // resize
                        int w = bm.getWidth();
                        int h = bm.getHeight();
                        double ratio = (double) w / (double) h;
                        double newW = 0;
                        double newH = 0;

                        if (w > 1024) {

                            newW = 1024.0;
                            newH = Math.round((double) 1024 / (double) w * (double) h);

                            if (newH > 1024) {
                                newH = 1024.0;
                                newW = Math.round((double) 1024 / (double) h * (double) w);

                            }
                            bmNew = Bitmap.createScaledBitmap(bm, (int) newW, (int) newH, true);
                            bmNew.compress(Bitmap.CompressFormat.JPEG, 60, fout);
                            bmNew = rotateImageIfRequired(SharePostActivity.this, bmNew, imageToUploadUri);

                            imageShow.setImageBitmap(bmNew);

                            // showPic.setRotation(90.0f);

                            bm.recycle();
                        } else {
                            bm.compress(Bitmap.CompressFormat.JPEG, 60, fout);
                            imageShow.setImageBitmap(bm);

                        }

                        fout.close();
                        imageShow.setVisibility(View.VISIBLE);
                        // bm.recycle();

                        // release myImage
                        myImage.close();
                        myImage = null;

                        // delete the image from the SD directory
                        if (isSaved) {
                            fileSDImage.delete();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageShow.setVisibility(View.GONE);
                    Toast.makeText(SharePostActivity.this, "Error in capturing image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == GALLERY_PHOTO && null != data) {
                imageToUploadUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imageToUploadUri, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagepathName = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmNew = null;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] data1 = bos.toByteArray();
                Common.imagePath = new String(Base64.encode(data1, GALLERY_PHOTO));
                Bitmap bm = BitmapFactory.decodeFile(imagepathName);
                int w = bm.getWidth();
                int h = bm.getHeight();
                double ratio = (double) w / (double) h;
                double newW = 0;
                double newH = 0;

                if (w > 1024) {

                    newW = 1024.0;
                    newH = Math.round((double) 1024 / (double) w * (double) h);

                    if (newH > 1024) {
                        newH = 1024.0;
                        newW = Math.round((double) 1024 / (double) h * (double) w);

                    }
                    bmNew = Bitmap.createScaledBitmap(bm, (int) newW, (int) newH, true);
                    bmNew.compress(Bitmap.CompressFormat.JPEG, 60, bos);


                    imageShow.setImageBitmap(bmNew);

                    // showPic.setRotation(90.0f);

                    bm.recycle();
                } else {
                    bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                    imageShow.setImageBitmap(bm);

                }
                imageShow.setVisibility(View.VISIBLE);
                File fimage = new File(imagepathName);
                if (fimage.exists()) {
                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    String formattedDate = df.format(c.getTime());
                    imageName = formattedDate + ".jpg";
                    Common.imagePath = Environment.getExternalStorageDirectory() + File.separator + "Kloosin" + File.separator + "KL_" + Build.SERIAL + imageName;
                    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Kloosin" + File.separator);
                    root.mkdirs();
                    fileSDImage = new File(root, imageName);
                }
                try {
                    copyFile(fimage, new File(Common.imagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imageShow.setVisibility(View.GONE);
                Toast.makeText(SharePostActivity.this, "Error in capturing image", Toast.LENGTH_SHORT).show();
            }

        }


    }


    private void addPost() {
        if (!Common.imagePath.equals("") && editTextPost.getText().toString().trim().length() != 0) {
            File file = new File(Common.imagePath);
            UserPost userPost = new UserPost();

           /* userPost.setLatitude(Common.latitude);
            userPost.setLongitude(Common.longitude);*/

            userPost.setLati(22);
            userPost.setLongi(88);

            userPost.setPostType("Image");
            //   _request.setUsername(String.valueOf(((EditText) findViewById(R.id.username)).getText()));
            userPost.setPostBody(editTextPost.getText().toString());


            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("postImagePath", file.getPath(), requestFile);
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), CommonHelper.getInstance().getCurrentUser(this).getUserId());
            RequestBody r_posttype = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostType());
            RequestBody r_postbody = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostBody());
            getLoader().show();
            Call<Void> _call = KLRestService.getInstance().getService(getContext()).addPost(description, body, r_posttype, r_postbody, userPost.getLati(), userPost.getLongi());
            _call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getLoader().dismiss();
                    if (response.code() == 200) {
                        startActivity(new Intent(SharePostActivity.this, MainActivity.class));
                        Common.imagePath = "";
                        finish();
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

        } else if (Common.imagePath.equals("") && editTextPost.getText().toString().trim().length() != 0) {
            File file = new File(Common.imagePath);
            UserPost userPost = new UserPost();

            /*userPost.setLatitude(Common.latitude);
            userPost.setLongitude(Common.longitude);*/

            userPost.setLati(22);
            userPost.setLongi(88);

            userPost.setPostType("Text");
            //   _request.setUsername(String.valueOf(((EditText) findViewById(R.id.username)).getText()));
            userPost.setPostBody(editTextPost.getText().toString().trim());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            MultipartBody.Part body = MultipartBody.Part.createFormData("postImagePath", "", requestFile);
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), CommonHelper.getInstance().getCurrentUser(this).getUserId());
            RequestBody r_posttype = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostType());
            RequestBody r_postbody = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostBody());
            getLoader().show();
            Call<Void> _call = KLRestService.getInstance().getService(getContext()).addPost(description, body, r_posttype, r_postbody, userPost.getLati(), userPost.getLongi());
            _call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getLoader().dismiss();
                    if (response.code() == 200) {
                        startActivity(new Intent(SharePostActivity.this, MainActivity.class));
                        Common.imagePath = "";
                        finish();
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

        } else if (!Common.imagePath.equals("") && editTextPost.getText().toString().trim().length() == 0) {
            File file = new File(Common.imagePath);
            UserPost userPost = new UserPost();

            /*userPost.setLatitude(Common.latitude);
            userPost.setLongitude(Common.longitude);*/
            userPost.setLati(22);
            userPost.setLongi(88);
            userPost.setPostType("Image");

            //   _request.setUsername(String.valueOf(((EditText) findViewById(R.id.username)).getText()));
            userPost.setPostBody("");

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("postImagePath", file.getName(), requestFile);
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), CommonHelper.getInstance().getCurrentUser(getContext()).getUserId());
            RequestBody r_posttype = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostType());
            RequestBody r_postbody = RequestBody.create(MediaType.parse("multipart/form-data"), userPost.getPostBody());
            getLoader().show();
            Call<Void> _call = KLRestService.getInstance().getService(getContext()).addPost(description, body, r_posttype, r_postbody, userPost.getLati(), userPost.getLongi());
            _call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getLoader().dismiss();
                    if (response.code() == 200) {
                        startActivity(new Intent(SharePostActivity.this, MainActivity.class));
                        Common.imagePath = "";
                        finish();
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

        } else if (Common.imagePath.equals("") && editTextPost.getText().toString().trim().length() == 0) {
            Toast.makeText(SharePostActivity.this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }


    }


    public Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }

    private Context getMContext() {
        return SharePostActivity.this;
    }


    // Method to show photo upload pop up
    private void showImageUploadPopUp(View view) {
        PopupMenu popup = new PopupMenu(SharePostActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.pop_up_image, popup.getMenu());
        if (isPermissionGranted()) {
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    try {
                        if (item.getTitle().equals("Camera")) {

                            if (Build.VERSION.SDK_INT >= 24) {
                                try {
                                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                    m.invoke(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            captureCameraImage();
                        } else if (item.getTitle().equals("Gallery"))
                            pickFromGallery();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }

            });
            popup.show();
        }


    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Message", "Permission is granted");
                return true;
            } else {

                Log.d("Message", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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

        }
    }

    // Method to invoke camera for image Upload
    private void captureCameraImage() throws Exception {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Kloosin" + File.separator);
        root.mkdirs();
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(c.getTime());
        // imageName = Long.toString(System.currentTimeMillis()) + ".jpg";
        imageName = formattedDate + ".jpg";

        fileSDImage = new File(root, imageName);
        imageToUploadUri = Uri.fromFile(fileSDImage);
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToUploadUri);
        // imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, CAMERA_PHOTO);
    }

    // Method to invoke gallery for image Upload
    public void pickFromGallery() throws Exception {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_PHOTO);
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
