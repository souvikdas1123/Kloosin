package com.kloosin.utility.u;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kloosin.activity.LoginScreenActivity;
import com.kloosin.activity.MainActivity;
import com.kloosin.service.model.UserDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommonHelper {

    private static CommonHelper mInstance;

    public static synchronized CommonHelper getInstance() {
        if ( mInstance == null ) mInstance = new CommonHelper();
        return mInstance;
    }

    public void setCurrentUser(Context context, UserDetails userDetails) {
        SPHelper.getInstance(context).write(TagNameHelper.getInstance().LOGIN_RESPONSE_KEY, new Gson().toJson(userDetails));
    }

    public UserDetails getCurrentUser(Context context) {
        if (!SPHelper.getInstance( context ).read(TagNameHelper.getInstance().LOGIN_RESPONSE_KEY).equals("")) {
            return new Gson().fromJson(SPHelper.getInstance(context).read(TagNameHelper.getInstance().LOGIN_RESPONSE_KEY), UserDetails.class);
        }
        return null;
    }

    public void updateCurrentUserProfilePicture(Context context, String URL) {
        UserDetails _user_details = CommonHelper.getInstance().getCurrentUser(context);
        if ( null != _user_details ) {
            _user_details.profileImage = URL;
            CommonHelper.getInstance().setCurrentUser(context, _user_details);
        }
    }

    public void performLogout(Context context) {
        SPHelper.getInstance(context).clear(TagNameHelper.getInstance().LOGIN_RESPONSE_KEY);
        SPHelper.getInstance(context).writeBoolean(TagNameHelper.getInstance().NEED_TO_REBOOT_RETROFIT_SERVICE_KEY, true);
        context.startActivity(new Intent(context, LoginScreenActivity.class));
        ((MainActivity) context).finish();
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return uri.getPath();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public String getUserAuthToken(Context context) {
        if ( null != getCurrentUser(context) )
            return String.format("Bearer %s", getCurrentUser(context).getAccessToken());
        return "";
    }

    public void setImageFromExternalSource(final Context context, final View view, String url, final boolean is_background_pic) {
        try {
            Target _t = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if ( is_background_pic ) {
                        view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    } else {
                        ((ImageView) view).setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            view.setTag(_t);
            Picasso.get().setLoggingEnabled(true);
            if ( url.startsWith("/") && !is_background_pic ) {
                ((ImageView) view).setImageBitmap( BitmapFactory.decodeFile(new File(url).getAbsolutePath()) );
            } else {
                if (url.startsWith("http")) {
                    Picasso.get().load(url).into(_t);
                } else {
                    Picasso.get().load(String.format("http://%s", url)).into(_t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
