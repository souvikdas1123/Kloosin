package com.kloosin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;


import org.json.JSONObject;
import com.kloosin.R;
import com.kloosin.adapter.FriendAdapter;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.FriendModel;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**Author SD
 * This should be the way to make an activity**/

public class FriendsActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    RecyclerView friendsList;
    FriendAdapter friendAdapter;
    List<FriendModel> friendList = new ArrayList<>();
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String userName = getIntent().getStringExtra("friendName");
        String userId= CommonHelper.getInstance().getCurrentUser(this).getUserId();
        initWidgets();
        try {
            populateFriendList(userName,userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initWidgets(){
        friendsList=findViewById(R.id.friendslist);
        pbar=findViewById(R.id.pBar);
    }

    // Method to create user details JSON & post as a raw JSON in request body & gnerate friendList
    private void populateFriendList(String keyword, String userId) throws  Exception{
        pbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", keyword);
        jsonObject.put("userId", userId);
        //RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(jsonObject.toString()));
        Call<List<FriendModel>> callfriendList = KLRestService.getInstance().getService(this).getFriends(jsonObject.toString());
        callfriendList.enqueue(new Callback<List<FriendModel>>() {
            @Override
            public void onResponse(Call<List<FriendModel>> call, Response<List<FriendModel>> response) {
                pbar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    friendList.addAll(response.body());
                    if(friendList.size()!=0){
                        friendAdapter= new FriendAdapter(FriendsActivity.this, friendList);
                        final LinearLayoutManager llm = new LinearLayoutManager(FriendsActivity.this);

                        llm.setOrientation(LinearLayoutManager.VERTICAL);

                        friendsList.setLayoutManager(llm);
                        friendsList.setAdapter(friendAdapter);
                        friendAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(FriendsActivity.this,"Unable to fetch friends list, try again",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    RequestHelper.getInstance().handleErrorResponse(FriendsActivity.this, response.code(), response.errorBody());
                    Toast.makeText(FriendsActivity.this,String.valueOf(response.errorBody()),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<FriendModel>> call, Throwable t) {
                Toast.makeText(FriendsActivity.this,"Some error occurred, try again",Toast.LENGTH_SHORT).show();
            }
        });

    }




}
