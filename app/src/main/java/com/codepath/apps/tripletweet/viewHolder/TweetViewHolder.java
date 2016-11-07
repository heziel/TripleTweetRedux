package com.codepath.apps.tripletweet.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.activity.DetailActivity;
import com.codepath.apps.tripletweet.activity.ProfileActivity;
import com.codepath.apps.tripletweet.models.Tweet;
import com.codepath.apps.tripletweet.models.User;
import com.codepath.apps.tripletweet.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tvTweetFeed) TextView tvTweetFeed;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.ivProfilePic) ImageView ivProfilePic;
    @BindView(R.id.tvTimeStamp) TextView tvTimeStamp;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.ivFeedImage) ImageView ivFeedImage;

    private List<Tweet> tweetList;
    private Context context;

    // Getters
    public ImageView getIvFeedImage() { return ivFeedImage; }

    public TextView getTvUserName() { return tvUserName; }

    public TextView getTvTimeStamp() { return tvTimeStamp; }

    public ImageView getIvProfilePic() { return ivProfilePic; }

    public TextView getTvName() { return tvName; }

    public TextView getTvTweetFeed() {
        return tvTweetFeed;
    }

    // ViewHolder Constructor

    public TweetViewHolder(Context context, final View itemView, final List<Tweet> tweetList) {

        super(itemView);

        this.tweetList = tweetList;
        this.context = context;

        itemView.setOnClickListener(this);
        ButterKnife.bind(this,itemView);

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getLayoutPosition();
                Tweet tweet = tweetList.get(position);
                User user = tweet.getUser();
                Intent intent = new Intent(itemView.getContext(),ProfileActivity.class);
                intent.putExtra("userId", user.getUniqueId());
                itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {

        int position = getLayoutPosition();
        // get the tweet
        Tweet tweet = tweetList.get(position);

        // send the url to DetailActivity
         Intent i = new Intent(context, DetailActivity.class);
          i.putExtra("tweet", Parcels.wrap(tweet));
          context.startActivity(i);
    }
}
