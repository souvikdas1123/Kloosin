package com.kloosin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kloosin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserNotification extends AppCompatActivity implements View.OnClickListener {

    ImageView back_button;

    String[] listviewTitle = new String[]{
            "ListView Title 1", "List" +
            "View Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };

    int[] listviewProfileImage = new int[]{
            R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image,
            R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image,
    };




    int[] listviewImage = new int[]{
            R.drawable.notification_bell, R.drawable.notification_bell, R.drawable.notification_bell, R.drawable.notification_bell,
            R.drawable.notification_bell, R.drawable.notification_bell, R.drawable.notification_bell, R.drawable.notification_bell,
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);
        back_button =(ImageView) findViewById(R.id.back_button) ;

        back_button.setOnClickListener(this);


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_profile_image", Integer.toString(listviewProfileImage[i]));
            hm.put("listview_image", Integer.toString(listviewImage[i]));

            aList.add(hm);
        }

        String[] from = {"listview_image","listview_profile_image", "listview_title"};
        int[] to = {R.id.notition_bellImage, R.id.user_image,R.id.title_name};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.single_page_user_notification, from, to);
        ListView androidListView = (ListView) findViewById(R.id.id_list_view_userNotification);
        // layoutparams = view.getLayoutParams();

        //Define your height here.
        //layoutparams.height = 70;


        androidListView.setAdapter(simpleAdapter);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.back_button:
                Intent userNotificationIntent = new Intent(UserNotification.this,MainActivity.class);
                startActivity(userNotificationIntent);
        }


    }
}
