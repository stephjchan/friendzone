package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsListTab extends Fragment {

    private EditText mSearchField;
    private Button mSearchBtn;

    private RecyclerView myRecyclerview;
    private RecyclerViewAdapterUser recyclerAdapter;
    private List<FriendItem> listFriend = new ArrayList<>();
    private List<String> curFriends = new ArrayList<>();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference mFriendRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends_list, container, false);
        myRecyclerview = (RecyclerView)rootView.findViewById(R.id.friend_recyclerView);
        recyclerAdapter = new RecyclerViewAdapterUser(getContext(), listFriend);
        myRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerview.setAdapter(recyclerAdapter);

        mAuth = FirebaseAuth.getInstance();
        userEmail = user.getEmail();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String x = user.getUid();
        mFriendRef = mDatabase.getReference("/Friends/"+user.getUid()); //get curUser friendsList
        mFriendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curFriends = new ArrayList<>();
                for (DataSnapshot firebaseUser: dataSnapshot.getChildren()){
                    curFriends.add(firebaseUser.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FRIEND_LIST", "Failed to read existing friends.", databaseError.toException());
            }
        });

        mRef = mDatabase.getReference("/Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listFriend = new ArrayList<>();

                for (DataSnapshot firebaseUser: dataSnapshot.getChildren()){
                    FriendItem value = firebaseUser.getValue(FriendItem.class);
                    if (userEmail.equals(value.getEmail())){
                        continue;
                    }
                    FriendItem mFriend = new FriendItem();
                    mFriend.setUsername(value.getUsername());
                    mFriend.setEmail(value.getEmail());
                    mFriend.setID(firebaseUser.getKey());
                    if (curFriends.contains(firebaseUser.getKey())){
                        mFriend.setIsFriend(true);
                    }
                    else
                        mFriend.setIsFriend(false);
                    listFriend.add(mFriend);

                    recyclerAdapter = new RecyclerViewAdapterUser(getContext(), listFriend);
                    myRecyclerview.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("FRIEND_TAB", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
