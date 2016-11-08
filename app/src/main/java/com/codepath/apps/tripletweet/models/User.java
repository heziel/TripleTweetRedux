package com.codepath.apps.tripletweet.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public long   uniqueId;
    public String screenName;
    public String profileImageUrl;
    public String profileBackgroundImageUrl;
    public String description;
    public int    followersCount;
    public int    followingCount;

    // Getters
    public String getName() {
        return name;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() { return profileBackgroundImageUrl; }

    public String getDescription() { return description; }

    public int getFollowersCount() { return followersCount; }

    public int getFollowingCount() { return followingCount; }

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();

        // extract and fill the values
        try {
            user.name = jsonObject.getString("name");
            user.uniqueId = jsonObject.getLong("id");
            user.screenName = "@" + jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            user.description = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}