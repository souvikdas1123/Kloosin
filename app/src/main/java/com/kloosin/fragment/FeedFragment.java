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

import com.kloosin.R;
import com.kloosin.activity.MainActivity;
import com.kloosin.adapter.FeedAdapter;
import com.kloosin.dialog.ProfileMenu;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.Post;
import com.kloosin.utility.listener.PopupMenuListener;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View _view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView feed_list_view;

    private boolean is_day_mode_onn = true;
    private List<Post> _feed_list = new ArrayList<>();
    private FeedAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        handleNavigationBAR();
        if (null == _view)
            _view = inflater.inflate(R.layout.feed_fragment_layout, container, false);

        initComponent();
        return _view;
    }

    private void initComponent() {

        _view.findViewById(R.id.show_menu).setOnClickListener(this);
        mSwipeRefreshLayout = _view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                getAllPostFromServer();
            }
        });

        feed_list_view = _view.findViewById(R.id.feed_list_view);
        _adapter = new FeedAdapter(getContext(), _feed_list);
        feed_list_view.setAdapter(_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        feed_list_view.setLayoutManager(linearLayoutManager);

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 18 || calendar.get(Calendar.HOUR_OF_DAY) <= 6) {
            is_day_mode_onn = false;
            feed_list_view.setBackgroundResource(R.drawable.night_bg);
        } else {
            is_day_mode_onn = true;
            feed_list_view.setBackgroundResource(R.drawable.day_bg);
        }
        /*feed_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed_list_view.setBackground(ContextCompat.getDrawable( getContext(), is_day_mode_onn ? R.drawable.night_bg : R.drawable.day_bg ));
                is_day_mode_onn = !is_day_mode_onn;
            }
        });*/
    }

    private void getAllPostFromServer() {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<List<Post>> _call = KLRestService.getInstance().getService(getContext()).getAllPosts();
        _call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    _feed_list.addAll(response.body());
                    _adapter.notifyDataSetChanged();
                } else {
                    RequestHelper.getInstance().handleErrorResponse(getContext(), response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_menu:
                final ProfileMenu dialog = new ProfileMenu(getContext(), R.style.DialogTheme);
                dialog.setListener(new PopupMenuListener() {
                    @Override
                    public void onClick(int viewId) {
                        dialog.dismiss();
                        handleProfileMenuClick(viewId);
                    }
                });
                dialog.show();
                break;
        }
    }

    private void handleProfileMenuClick(int viewId) {
        switch (viewId) {
            case R.id.edit_profile_link:
                ((MainActivity) getContext()).pushFragment(new EditProfileFragment(), true);
                break;
            case R.id.profile_image:
                ((MainActivity) getContext()).pushFragment(new ProfileFragment(), true);
                break;
            case R.id.btn_logout:
                CommonHelper.getInstance().performLogout(getContext());
                break;
        }
    }

    private void handleNavigationBAR() {
        ((MainActivity) getContext()).findViewById(R.id.bottom_navigation_section).setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        _feed_list.clear();
        getAllPostFromServer();
    }
}
