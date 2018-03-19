package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephanie Chan on 3/14/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<MicroReportItem> mData;

    public RecyclerViewAdapter(Context mContext, List<MicroReportItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_microreport, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemImage.setImageResource(mData.get(position).getLocationPhoto());

        holder.itemEvent.setText(mData.get(position).getEventName());
        holder.itemUser.setText(mData.get(position).getEventUsername());
        holder.itemPlaceName.setText(mData.get(position).getEventLocationName());
        holder.itemPlaceAddr.setText(mData.get(position).getEventLocationAddress());
        holder.itemTime.setText(mData.get(position).getEventStartTime());

    }

    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemEvent, itemUser, itemPlaceName, itemPlaceAddr, itemTime;
        private Button buttonJoin;
        private FirebaseAuth mAuth;
        private DatabaseReference mDataBase;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);

            itemEvent = itemView.findViewById(R.id.item_event);
            itemUser = itemView.findViewById(R.id.item_user);
            itemPlaceName = itemView.findViewById(R.id.item_place_name);
            itemPlaceAddr = itemView.findViewById(R.id.item_place_addr);
            itemTime = itemView.findViewById(R.id.item_time);

            mAuth = FirebaseAuth.getInstance();
            mDataBase = FirebaseDatabase.getInstance().getReference();


            buttonJoin = itemView.findViewById(R.id.button_join);
            buttonJoin.setTag(0); // initialize button as "JOIN"
            buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int status = (Integer) v.getTag();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    Map<String, Integer> account = new HashMap<>();

                    account.put(user.getEmail(), 1);

                    switch (status) {
                        case 0:
                            //change to JOINED if User clicks button
                            //TODO: add user to Joined branch of database
                            //mDataBase.child("Joined").child("email").child("event_"+String.valueOf(1)).setValue(account);
                            buttonJoin.setText("JOINED");
                            buttonJoin.setBackgroundColor(Color.parseColor("#43a047"));
                            v.setTag(1);
                            break;
                        case 1:
                            //change to JOIN? if User clicks button again
                            //TODO: delete user from Joined branch of database
                            buttonJoin.setText("JOIN?");
                            buttonJoin.setBackgroundColor(Color.parseColor("#4d2c91"));
                            v.setTag(0);
                            break;
                    }
                }
            });


        }
    }

}
