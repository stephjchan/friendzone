package com.example.stephaniechan.friendzone;

/**
 * Created by Stephanie Chan on 3/5/2018.
 */

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsListTab extends Fragment {

//    private EditText mSearchField;
//    private ImageButton mSearchBtn;
//    private RecyclerView mResultList;
//    FirebaseRecyclerAdapter<FriendItem, UsersViewHolder> mAdapter;


//    private DatabaseReference mUserDatabase;

//    ListView user_list;
//    ArrayAdapter<String> adapter2;

    //
    private RecyclerView myRecyclerview;
    private RecyclerViewAdapterUser recyclerAdapter;
    private List<FriendItem> listFriend = new ArrayList<>();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
//    MyAdapter adapter;
//    List<FriendItem> listData;
//    FirebaseDatabase FDB;
//    DatabaseReference DBR;

    private Button addFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends_list, container, false);


        myRecyclerview = (RecyclerView)rootView.findViewById(R.id.friend_recyclerView);
//        myRecyclerview.setHasFixedSize(true);
        recyclerAdapter = new RecyclerViewAdapterUser(getContext(), listFriend);
        myRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerview.setAdapter(recyclerAdapter);
//
//        RecyclerView.LayoutManager LM = new LinearLayoutManager(getActivity().getApplicationContext());
//
//        myRecyclerview.setLayoutManager(LM);
//        myRecyclerview.setItemAnimator(new DefaultItemAnimator());
//        myRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),
//                LinearLayoutManager.VERTICAL));
//        listData = new ArrayList<>();
//        adapter = new MyAdapter(listData);
//
//        FDB = FirebaseDatabase.getInstance();
//        GetDataFirebase();
        addFriend = (Button) rootView.findViewById(R.id.add_friend);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("/Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listFriend = new ArrayList<>();
                for (DataSnapshot firebaseUser: dataSnapshot.getChildren()){
                    FriendItem value = firebaseUser.getValue(FriendItem.class);
                    FriendItem mFriend = new FriendItem();

                    mFriend.setUsername(value.getUsername());
                    mFriend.setEmail(value.getEmail());
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

//    void GetDataFirebase(){
//        DBR = FDB.getReference("users");
//        DBR.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                FriendItem data = dataSnapshot.getValue(FriendItem.class);
//                //add to arraylist
//                listData.add(data);
//                //add list to adapter/recyclerview
//                myRecyclerview.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
//        List<FriendItem> listArray;
//        public MyAdapter(List<FriendItem> list){
//            this.listArray = list;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
//            return new ViewHolder(view);
//        }
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            FriendItem data = listArray.get(position);
//            holder.MyText.setText(data.getEmail());
//
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder{
//            TextView MyText;
//            public ViewHolder(View itemView) {
//                super(itemView);
//                MyText = (TextView)itemView.findViewById(R.id.email_text);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return listArray.size();
//        }
//    }

    // View Holder CLass
//    public class UsersViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public UsersViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//        }
//        public void setDetails(String userEmail){
//            TextView user_email = (TextView) mView.findViewById(R.id.email_text);
//            user_email.setText(userEmail);
//        }
//    }


}
