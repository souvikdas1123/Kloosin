package com.kloosin.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.activity.MapsActivity;
import com.kloosin.activity.TrackActivity;
import com.kloosin.service.model.FriendModel;
import com.kloosin.utility.u.CommonHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;
    private List<FriendModel> values;
    private boolean isTrack;

    public FriendAdapter(Context ctx, List<FriendModel> values, boolean isTrack) {
        this.context = ctx;
        this.values = values;
        this.isTrack = isTrack;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.friend_adapter, viewGroup, false);
        return new FriendAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        final FriendModel fObj = values.get(position);
        holder.friendNameTxt.setText(fObj.getFullName());
        if (isTrack) {
            holder.trackPic.setVisibility(View.VISIBLE);
            holder.bellImage.setVisibility(View.GONE);
            holder.trackPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MapsActivity.class);
                    i.putExtra("userId", fObj.getUserId());
                    i.putExtra("friendName", fObj.getFullName());
                    context.startActivity(i);
                }
            });
        } else {
            holder.trackPic.setVisibility(View.GONE);
            holder.bellImage.setVisibility(View.VISIBLE);
        }

        //Picasso.get().load().into(holder.profileImage);
        CommonHelper.getInstance().setImageFromExternalSource(context, holder.profileImage, fObj.getProfilePicturePath(), false);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView friendNameTxt;
        private CircleImageView profileImage;
        ImageView bellImage, trackPic;

        public ViewHolder(View itemView) {
            super(itemView);
            friendNameTxt = itemView.findViewById(R.id.friendNameTxt);
            profileImage = itemView.findViewById(R.id.profile_image);
            bellImage = itemView.findViewById(R.id.bellImage);
            trackPic = itemView.findViewById(R.id.trackPic);
        }
    }


}
