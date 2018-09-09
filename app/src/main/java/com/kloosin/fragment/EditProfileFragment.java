package com.kloosin.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.activity.MainActivity;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.UserDetails;
import com.kloosin.service.model.edit_profile.UserProfileDetails;
import com.kloosin.utility.u.AlertInfoHelper;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.PermissionRequestHelper;
import com.kloosin.utility.u.RequestHelper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_PHOTO_FOR_AVATAR = 1111;

    private Fragment _this_fragment;
    private View _view;
    private Loading mLoader;
    UserDetails _user_details;
    private ImageView profile_image;
    private FrameLayout profile_cover_pic;
    private RelativeLayout btn_male, btn_female, btn_other;
    EditText edtNickName,edtShortBio,edtEmailId,edtPhoneNumber,edtCompanyName,edtDesignation,edtEducation,edtAddress,edtCurrentCity,edtInterestArea,edtFriendSearchKeyword;
    TextView txtDOB;

    private int ACTIVE_SEX_BTN_INDEX    =   -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( null == _view )
            _view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        _this_fragment = this;
        initActionButton();

        _user_details = CommonHelper.getInstance().getCurrentUser(getActivity());
        getUserDetailsForEditApiCall();

        return _view;
    }


    ////////////////////////////////////////use post method

    private void getUserDetailsForEditApiCall() {
        if (!KLRestService.getInstance().isNetworkAvailable(getActivity())) {
            AlertInfoHelper.getInstance().alertNetworkError(getActivity());
            return;
        }
        getLoader().show();
        Call<UserProfileDetails> _call = KLRestService.getInstance().getService(getActivity()).getUserDetailsById(_user_details.getUserId());
        _call.enqueue(new Callback<UserProfileDetails>() {
            @Override
            public void onResponse(Call<UserProfileDetails> call, Response<UserProfileDetails> response) {
                getLoader().dismiss();
                if(response.code() == 200) {

                    populateWidgets(response.body());
                } else {
                    RequestHelper.getInstance().handleErrorResponse( getContext(), response.code(), response.errorBody() );
                }
            }

            @Override
            public void onFailure(Call<UserProfileDetails> call, Throwable t) {
                getLoader().dismiss();
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }





    private void populateWidgets(UserProfileDetails user_details) {

        if ( null == user_details ) return;

        if ( null != user_details.getProfilePicturePath() && !user_details.getProfilePicturePath().isEmpty() ) {
            CommonHelper.getInstance().setImageFromExternalSource(getContext(), profile_image, user_details.getProfilePicturePath(), false);
            CommonHelper.getInstance().updateCurrentUserProfilePicture(getContext(), user_details.getProfilePicturePath());
        }

        if ( null != user_details.getCoverPicturePath() && !user_details.getCoverPicturePath().isEmpty() ) {
            CommonHelper.getInstance().setImageFromExternalSource(getContext(), profile_cover_pic, user_details.getCoverPicturePath(), true);
        }

        edtNickName.setText( user_details.getNickName() );
        edtShortBio.setText( user_details.getShortBioData() );
        edtEmailId.setText( user_details.getEmailId() );
        edtPhoneNumber.setText( user_details.getMobileNo() );
        edtCompanyName.setText( user_details.getCurrentCompaneName() );
        edtDesignation.setText( user_details.getCurrentDesignation() );
        edtEducation.setText( user_details.getLastEducation() );
        edtAddress.setText( user_details.getAddress() );
        edtCurrentCity.setText( user_details.getCurrentCity() );
        edtInterestArea.setText( user_details.getInterestArea() );


    }

    private void initActionButton() {

        profile_cover_pic = _view.findViewById(R.id.profile_cover_pic);
        profile_image = _view.findViewById(R.id.profile_image);

        btn_male = _view.findViewById(R.id.btn_male);
        btn_female = _view.findViewById(R.id.btn_female);
        btn_other = _view.findViewById(R.id.btn_other);

        edtNickName = _view.findViewById(R.id.edtNickName);
        edtShortBio = _view.findViewById(R.id.edtShortBio);
        edtEmailId = _view.findViewById(R.id.edtEmailId);
        edtPhoneNumber = _view.findViewById(R.id.edtPhoneNumber);
        edtCompanyName = _view.findViewById(R.id.edtCompanyName);
        edtDesignation = _view.findViewById(R.id.edtDesignation);
        edtEducation = _view.findViewById(R.id.edtEducation);
        edtAddress = _view.findViewById(R.id.edtAddress);
        edtCurrentCity = _view.findViewById(R.id.edtCurrentCity);
        edtInterestArea = _view.findViewById(R.id.edtInterestArea);
        edtFriendSearchKeyword = _view.findViewById(R.id.edtFriendSearchKeyword);

        txtDOB = _view.findViewById(R.id.txtDOB);

        _view.findViewById(R.id.btn_edit_pp).setOnClickListener(this);
        /* _view.findViewById(R.id.profile_nxt_btn).setOnClickListener(this);*/
        _view.findViewById(R.id.profile_finish_btn).setOnClickListener(this);
        _view.findViewById(R.id.back_btn).setOnClickListener(this);
        /*_view.findViewById(R.id.imgSecondScreenBack).setOnClickListener(this);*/

        btn_male.setOnClickListener(this);
        btn_female.setOnClickListener(this);
        btn_other.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.btn_edit_pp:
                pickImage();
                break;
            /*case R.id.profile_nxt_btn :
                display_second_form();
                break;
            case R.id.profile_finish_btn :
                update_user_details();
                break;*/
            case R.id.back_btn :
                getActivity().onBackPressed();
                break;
            /*case R.id.imgSecondScreenBack :
                display_first_form();
                break;*/
            case R.id.btn_male :
                reflectSexView(1); // ;)
                break;
            case R.id.btn_female :
                reflectSexView(2);
                break;
            case R.id.btn_other :
                reflectSexView(3);
                break;
        }
    }

    // don't change physically ;)
    private void reflectSexView(int i) {
        ((TextView)btn_male.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        ((TextView)btn_female.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        ((TextView)btn_other.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        ACTIVE_SEX_BTN_INDEX = i;
        switch (i) {
            case 1:
                ((TextView)btn_male.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.golden));
                break;
            case 2:
                ((TextView)btn_female.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.golden));
                break;
            case 3:
                ((TextView)btn_other.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.golden));
                break;
        }
    }

    private void update_user_details() {

        if (!KLRestService.getInstance().isNetworkAvailable(getActivity())) {
            AlertInfoHelper.getInstance().alertNetworkError(getActivity());
            return;
        }

        UserProfileDetails ud = new UserProfileDetails();
        ud.setNickName(String.valueOf(edtNickName.getText()));
        ud.setShortBioData(String.valueOf(edtShortBio.getText()));
        ud.setEmailId(String.valueOf(edtEmailId.getText()));
        ud.setMobileNo(String.valueOf(edtPhoneNumber.getText()));
        // todo calendar integration
        ud.setDateOfBirth(String.valueOf(txtDOB.getText()));
        ud.setCurrentCompaneName(String.valueOf(edtCompanyName.getText()));
        ud.setCurrentDesignation(String.valueOf(edtDesignation.getText()));
        ud.setLastEducation(String.valueOf(edtEducation.getText()));
        ud.setAddress(String.valueOf(edtAddress.getText()));
        ud.setCurrentCity(String.valueOf(edtCurrentCity.getText()));
        ud.setInterestArea(String.valueOf(edtInterestArea.getText()));
        ud.setFriendSearchKeyword(String.valueOf(edtFriendSearchKeyword.getText()));

        ud.setGender( ACTIVE_SEX_BTN_INDEX == 3 ? "Other" : ACTIVE_SEX_BTN_INDEX == 2 ? "Female" : "Male" );
        // todo need to set HomeTown & Country
        ud.setHomeTown("Kolkata");
        ud.setCountry("India");

        ud.setUserId(Integer.parseInt(CommonHelper.getInstance().getCurrentUser(getContext()).getUserId()));
        ud.setOpMode("string");
        // todo need to change lat lng
        ud.setLatitude(130);
        ud.setLongitude(120);

        getLoader().show();

        Call<Void> _ec = KLRestService.getInstance().getService(getContext()).updateUserProfileDetails(ud);
        _ec.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getLoader().dismiss();
                if ( response.code() == 200 ) {
                    getActivity().onBackPressed();
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

    @TargetApi(Build.VERSION_CODES.M)
    public void pickImage() {
        if (!PermissionRequestHelper.getInstance().checkPermission(getContext(), PermissionRequestHelper.Permission.READ_STORAGE_PERMISSION)) {
            PermissionRequestHelper.getInstance().requestPermission(getContext(), PermissionRequestHelper.Permission.READ_STORAGE_PERMISSION, PermissionRequestHelper.Permission.READ_STORAGE_PERMISSION_CODE, _this_fragment);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
        }
    }

   /* private void display_first_form() {
        _view.findViewById(R.id.first_screen).setVisibility(View.VISIBLE);
        _view.findViewById(R.id.second_screen).setVisibility(View.GONE);
    }

    private void display_second_form() {
        _view.findViewById(R.id.first_screen).setVisibility(View.GONE);
        _view.findViewById(R.id.second_screen).setVisibility(View.VISIBLE);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                String inputStream = CommonHelper.getInstance().getPath(getContext(), data.getData());
                uploadUserProfilePicture( new File(inputStream) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadUserProfilePicture(final File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), CommonHelper.getInstance().getCurrentUser(getContext()).getUserId());
        ((MainActivity) getActivity()).getLoader().show();
        Call<Void> _call = KLRestService.getInstance().getService(getContext()).uploadProfilePicture(description, body);
        _call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                ((MainActivity) getActivity()).getLoader().dismiss();
                CommonHelper.getInstance().setImageFromExternalSource(getContext(), profile_image, file.getAbsolutePath(), false);
                CommonHelper.getInstance().updateCurrentUserProfilePicture(getContext(), file.getAbsolutePath());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ((MainActivity) getActivity()).getLoader().dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case PermissionRequestHelper.Permission.READ_STORAGE_PERMISSION_CODE:
                    boolean _is_all_permission_granted  =   true;
                    for (int index = 0; index < grantResults.length; index++) {
                        if (grantResults[ index ] != PackageManager.PERMISSION_GRANTED) {
                            _is_all_permission_granted  =   false;
                            break;
                        }
                    }
                    if ( _is_all_permission_granted ) {
                        pickImage();
                    } else {
                        AlertInfoHelper.getInstance().alertIt(getContext(), getString(R.string.error_dialog_heading), getString(R.string.no_permission_msg));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getActivity());
        return mLoader;
    }
}