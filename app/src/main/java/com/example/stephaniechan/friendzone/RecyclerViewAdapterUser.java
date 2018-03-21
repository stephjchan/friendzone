package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Janice on 3/19/2018.
 */

public class RecyclerViewAdapterUser extends RecyclerView.Adapter<RecyclerViewAdapterUser.MyViewHolder> {
    Context mContext;
    List<FriendItem> mData;

    public RecyclerViewAdapterUser(Context mContext, List<FriendItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemUsername.setText(mData.get(position).getUsername());
        holder.itemEmail.setText(mData.get(position).getEmail());
    }


    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemUsername,itemEmail;
        private Button buttonAdd;
        private FirebaseAuth mAuth;
        private DatabaseReference mDataBase;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemUsername = itemView.findViewById(R.id.item_username);
            itemEmail = itemView.findViewById(R.id.item_email);

            mAuth = FirebaseAuth.getInstance();
            mDataBase = FirebaseDatabase.getInstance().getReference();


            buttonAdd = itemView.findViewById(R.id.button_add);
            buttonAdd.setTag(0); // initialize button as "Add"
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int status = (Integer) v.getTag();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Integer> account = new HashMap<>();

                    account.put(user.getEmail(), 1);
                    //CHANGE THIS!!!
                    switch (status) {
                        case 0:
                            //change to JOINED if User clicks button
                            //TODO: add user to Joined branch of database
                            //mDataBase.child("Joined").child("Email").child("event_"+String.valueOf(1)).setValue(account);
                            buttonAdd.setText("REMOVE");
                            buttonAdd.setBackgroundColor(Color.parseColor("#43a047"));
                            v.setTag(1);
                            break;
                        case 1:
                            //change to JOIN? if User clicks button again
                            //TODO: delete user from Joined branch of database
                            buttonAdd.setText("ADD");
                            buttonAdd.setBackgroundColor(Color.parseColor("#4d2c91"));
                            v.setTag(0);
                            break;
                    }
                }
            });


        }
    }
}