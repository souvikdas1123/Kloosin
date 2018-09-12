package com.kloosin.activity;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kloosin.R;
import com.kloosin.adapter.FriendAdapter;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.FriendModel;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends AppCompatActivity {
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
        //String userName = getIntent().getStringExtra("friendName");
        String userId= CommonHelper.getInstance().getCurrentUser(this).getUserId();
        initWidgets();
        try {
            populateFriendList("",userId);
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
                        friendAdapter= new FriendAdapter(TrackActivity.this, friendList,true);
                        final LinearLayoutManager llm = new LinearLayoutManager(TrackActivity.this);

                        llm.setOrientation(LinearLayoutManager.VERTICAL);

                        friendsList.setLayoutManager(llm);
                        friendsList.setAdapter(friendAdapter);
                        friendAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(TrackActivity.this,"Unable to fetch friends list, try again",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    RequestHelper.getInstance().handleErrorResponse(TrackActivity.this, response.code(), response.errorBody());
                    Toast.makeText(TrackActivity.this,String.valueOf(response.errorBody()),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<FriendModel>> call, Throwable t) {
                Toast.makeText(TrackActivity.this,"Some error occurred, try again",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
