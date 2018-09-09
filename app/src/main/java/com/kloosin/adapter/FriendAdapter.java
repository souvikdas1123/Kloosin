package com.kloosin.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.service.model.FriendModel;
import com.kloosin.utility.u.CommonHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;
    private List<FriendModel> values;

    public FriendAdapter(Context ctx, List<FriendModel> values) {
        this.context = ctx;
        this.values = values;
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
        //Picasso.get().load().into(holder.profileImage);
        CommonHelper.getInstance().setImageFromExternalSource(context, holder.profileImage, fObj.getProfilePicturePath(),false);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView friendNameTxt;
        private CircleImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            friendNameTxt = itemView.findViewById(R.id.friendNameTxt);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
