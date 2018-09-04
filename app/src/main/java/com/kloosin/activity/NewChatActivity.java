package com.kloosin.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.kloosin.R;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.Message;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NewChatActivity.class.getSimpleName();

    EmojiconEditText emojicon_EditText;
    EmojiconTextView emojiicon_textView;
    ImageView emoji_ImageView;
    ImageView submitButton;
    View newroot_View;
    ImageView postSubmit_Button;

    private ImageView imageView6;


    EmojIconActions emojIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(this);
        newroot_View = findViewById(R.id.my_view);
        emoji_ImageView = (ImageView) findViewById(R.id.submit_view);
        emojicon_EditText = (EmojiconEditText) findViewById(R.id.edittext_emoji);
        emojIcon = new EmojIconActions(this, newroot_View, emojicon_EditText, emoji_ImageView);

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

        getExistingChatConversation();
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.imageView6:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        String _message = String.valueOf(emojicon_EditText.getText());
        Message.SendBody _m = new Message.SendBody();
        _m.setMessageBody( _message );
        _m.setFromUserId(Integer.parseInt(CommonHelper.getInstance().getCurrentUser(getMContext()).getUserId()));
        // todo need to changes hardcoded IDs
        _m.setToUserId( 1 );

        Call<Void> _call = KLRestService.getInstance().getService(getMContext()).sendMessage(_m);
        _call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if ( response.code() != 200 ) {
                    RequestHelper.getInstance().handleErrorResponse(getMContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });

    }

    private void getExistingChatConversation() {
        String userID = CommonHelper.getInstance().getCurrentUser(getMContext()).getUserId();
        Call<List<Message.ResponseBody>> _call_one = KLRestService.getInstance().getService(getMContext()).getAllBySender(userID);
        Call<List<Message.ResponseBody>> _call_two = KLRestService.getInstance().getService(getMContext()).getAllByReceiver(userID);
        _call_one.enqueue(new Callback<List<Message.ResponseBody>>() {
            @Override
            public void onResponse(Call<List<Message.ResponseBody>> call, Response<List<Message.ResponseBody>> response) {
                if ( response.code() == 200 ) {

                } else {
                    RequestHelper.getInstance().handleErrorResponse(getMContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Message.ResponseBody>> call, Throwable t) {
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });

        _call_two.enqueue(new Callback<List<Message.ResponseBody>>() {
            @Override
            public void onResponse(Call<List<Message.ResponseBody>> call, Response<List<Message.ResponseBody>> response) {
                if ( response.code() == 200 ) {

                } else {
                    RequestHelper.getInstance().handleErrorResponse(getMContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Message.ResponseBody>> call, Throwable t) {
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }


    Context getMContext() {
        return NewChatActivity.this;
    }
}
