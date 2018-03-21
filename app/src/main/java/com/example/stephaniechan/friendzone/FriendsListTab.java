package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FriendsListTab extends Fragment {

    private Button addFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends_list, container, false);
        addFriend = (Button) rootView.findViewById(R.id.add_friend);
        return rootView;
    }
}
