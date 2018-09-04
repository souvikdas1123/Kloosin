package com.kloosin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.kloosin.R;
import com.kloosin.activity.MainActivity;
import com.kloosin.adapter.FeedAdapter;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.Post;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View _view;
    private ArrayList<Post> _feed_list = new ArrayList<>();
    private FeedAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if ( null == _view )
            _view   =   inflater.inflate( R.layout.profile_fragment_layout, container, false );

        initComponent();

        return _view;
    }

    private void initComponent( ) {

        ((RecyclerView) _view.findViewById(R.id.feed_list)).setNestedScrollingEnabled(false);
        ((RecyclerView) _view.findViewById(R.id.feed_list)).setLayoutManager(new LinearLayoutManager( getContext() ));
        _adapter = new FeedAdapter( getContext(), _feed_list);
        ((RecyclerView) _view.findViewById(R.id.feed_list)).setAdapter( _adapter );

        /*_view.findViewById( R.id.btn_track ).setOnClickListener( this );*/
        getAllPostFromServer();
    }

    private void getAllPostFromServer() {
        ((MainActivity) getContext()).getLoader().show();
        Call<List<Post>> _call = KLRestService.getInstance().getService(getContext()).getUserAllPosts(CommonHelper.getInstance().getCurrentUser(getContext()).getUserId());
        _call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                ((MainActivity) getContext()).getLoader().dismiss();
                if (response.code() == 200) {
                    _feed_list.addAll(response.body());
                    _adapter.notifyDataSetChanged();
                } else {
                    RequestHelper.getInstance().handleErrorResponse(getContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                ((MainActivity) getContext()).getLoader().dismiss();
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() ) {
            /*case R.id.btn_track :
                ((MainActivity) getContext()).pushFragment( new TrackFragment(), true );
                break;*/
        }
    }
}
