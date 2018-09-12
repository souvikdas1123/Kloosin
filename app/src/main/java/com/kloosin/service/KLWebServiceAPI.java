package com.kloosin.service;

import com.google.gson.JsonObject;
import com.kloosin.service.model.FriendModel;
import com.kloosin.service.model.FriendTrack;
import com.kloosin.service.model.Message;
import com.kloosin.service.model.Post;
import com.kloosin.service.model.UserDetails;
import com.kloosin.service.model.UserPostRequst;
import com.kloosin.service.model.UserRegistration;
import com.kloosin.service.model.edit_profile.UserProfileDetails;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface KLWebServiceAPI {

    @POST("/api/RegistrationOTP")
    Call<Void> requestGenerateOTP(@Body UserRegistration.Request _request);


    @POST("/api/UserRegistration")
    Call<Void> requestUserRegistration(@Body UserRegistration.Request _request);

    //////////////FOR EDIT PROFILE/////////////////
    @GET("/api/UserProfile/GetById/{userId}")
    Call<UserProfileDetails> getUserDetailsById(@Path("userId") String userId);

    //////////
    @Headers({"Content-Type: application/json;charset=UTF-8"})//this code will change by sahid
    @POST("/api/UserProfile/UpdateUserProfile")
    Call<Void> updateUserProfileDetails(@Body UserProfileDetails userProfileDetails);
    ///////////////////////////////////////////////

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    @POST("/token")
    Call<UserDetails> getUserToken(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);

    @Multipart
    @POST("/api/UserProfile/UploadProfilePicture")
    Call<Void> uploadProfilePicture(@Part("UserId") RequestBody userID, @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/UserTimeLine/PostTimeLine")
    Call<Void> addPost(@Part("userId") RequestBody userID, @Part MultipartBody.Part file, @Part("postType") RequestBody postType,
                       @Part("postBody") RequestBody postBody, @Part("longitude") int longitude, @Part("latitude") int latitude);


    @GET("/api/UserTimeLine/GetAll")
    Call<List<Post>> getAllPosts();

    @GET("/api/UserTimeLine/GetByUserId/{userId}")
    Call<List<Post>> getUserAllPosts(@Path("userId") String userId);

    @POST("/api/UserMessage/SendMessage")
    Call<Void> sendMessage(@Body Message.SendBody _message);

    @Headers({"Content-Type: application/json"})
    @POST("/api/UserProfile/SearchProfile")
    Call<List<FriendModel>> getFriends(@Body String s);

    @Headers({"Content-Type: application/json"})
    @POST("/api/UserTracking/InsertUserTracking")
    Call<Void> postFriendPosition(@Body String s);

    @GET("/api/UserMessage/GetAllByReceiver/{userId}")
    Call<List<Message.ResponseBody>> getAllByReceiver(@Path("userId") String userID);

    @GET("/api/UserMessage/GetAllBySender/{userId}")
    Call<List<Message.ResponseBody>> getAllBySender(@Path("userId") String userID);

}
