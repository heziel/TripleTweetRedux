package com.codepath.apps.tripletweet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.tripletweet.R;
import com.codepath.apps.tripletweet.network.TwitterApplication;
import com.codepath.apps.tripletweet.network.TwitterClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowingFragment extends Fragment {
    @BindView(R.id.textViewTest)
    TextView textView;

    private TwitterClient client;
    private Long userId;
    private Long nextCursor;

    public static FollowingFragment newInstance(Long userId) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        userId = getArguments().getLong("userId");
        //populateWithUsers();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, parent, false);
        ButterKnife.bind(this,view);
        textView.setText("Following...");
        return view;
    }
}
