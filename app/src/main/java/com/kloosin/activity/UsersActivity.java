package com.kloosin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.kloosin.R;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        CardView cardView= findViewById(R.id.card_package1);
        CardView cardView1= findViewById(R.id.card_package2);
        CardView cardView2= findViewById(R.id.card_package3);
        CardView cardView3= findViewById(R.id.card_package4);


        cardView.setOnClickListener(this);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.card_package1:
                Intent intent= new Intent(UsersActivity.this,NewChatActivity.class);
                startActivity(intent);

                break;

            case R.id.card_package2:
                Intent intent1= new Intent(UsersActivity.this,NewChatActivity.class);
                startActivity(intent1);

                break;


            case R.id.card_package3:
                Intent intent2= new Intent(UsersActivity.this,NewChatActivity.class);
                startActivity(intent2);

                break;

            case R.id.card_package4:
                Intent intent3= new Intent(UsersActivity.this,NewChatActivity.class);
                startActivity(intent3);

                break;

        }

    }
}
