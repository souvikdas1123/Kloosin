package com.kloosin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.service.model.Post;
import com.kloosin.utility.u.CommonHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final List<Post> feedList;
    private Context _context;

    public FeedAdapter(Context context, List<Post> _feed_list) {
        _context = context;
        feedList = _feed_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(_context);
        View view = inflater.inflate(R.layout.item_feed_layout, parent, false);
        return new ViewHolder(view);
    }

    public Post getItem(int position) {
        return feedList.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post single_post = getItem(position);
        holder.tv_name.setText(single_post.getUserName());
        String date = single_post.getPostlocalTime().substring(0, 10);
        String time = single_post.getPostlocalTime().substring(11, 19);
        holder.tv_date.setText(date + "\n" + time);
        holder.tv_description.setText(single_post.getPostBody());
        if (!single_post.getPostImagePath().equals("") && single_post.getPostBody().equals("")) {
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_description.setVisibility(View.GONE);
            holder.iv_post_image.setVisibility(View.VISIBLE);
            CommonHelper.getInstance().setImageFromExternalSource(_context, holder.iv_post_image, single_post.getPostImagePath(), false);
        } else if (!single_post.getPostImagePath().equals("") && !single_post.getPostBody().equals("")) {
            holder.iv_post_image.setVisibility(View.VISIBLE);
            CommonHelper.getInstance().setImageFromExternalSource(_context, holder.iv_post_image, single_post.getPostImagePath(), false);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_description.setVisibility(View.VISIBLE);

        } else if (single_post.getPostImagePath().equals("") && !single_post.getPostBody().equals("")) {
            holder.iv_post_image.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_description.setVisibility(View.VISIBLE);
        }
        CommonHelper.getInstance().setImageFromExternalSource(_context, holder.iv_profile_image, single_post.getUserProfileImagePath(), false);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_date, tv_title, tv_description;
        private ImageView iv_profile_image, iv_star_icon, iv_comment_icon, iv_share, iv_favourite, iv_post_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);

            iv_profile_image = itemView.findViewById(R.id.iv_profile_image);
            iv_star_icon = itemView.findViewById(R.id.iv_star_icon);
            iv_comment_icon = itemView.findViewById(R.id.iv_comment_icon);
            iv_share = itemView.findViewById(R.id.iv_share);
            iv_favourite = itemView.findViewById(R.id.iv_favourite);
            iv_post_image = itemView.findViewById(R.id.iv_post_image);
        }
    }
}
