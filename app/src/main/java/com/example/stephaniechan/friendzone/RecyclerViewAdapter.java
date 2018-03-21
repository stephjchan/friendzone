package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        holder.itemImage.setImageBitmap(mData.get(position).getLocationPhoto());

        holder.itemEvent.setText(mData.get(position).getEventName());
        holder.itemUser.setText(mData.get(position).getEventUsername());
        holder.itemPlaceName.setText(mData.get(position).getEventLocationName());
        holder.itemPlaceAddr.setText(mData.get(position).getEventLocationAddress());
        holder.itemTime.setText(mData.get(position).getEventStartTime());

        holder.joinFunc(mData.get(position).getID());

    }

    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemEvent, itemUser, itemPlaceName, itemPlaceAddr, itemTime;
        private Button buttonJoin;
        private FirebaseAuth mAuth;
        private DatabaseReference mRef;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);

            itemEvent = itemView.findViewById(R.id.item_event);
            itemUser = itemView.findViewById(R.id.item_user);
            itemPlaceName = itemView.findViewById(R.id.item_place_name);
            itemPlaceAddr = itemView.findViewById(R.id.item_place_addr);
            itemTime = itemView.findViewById(R.id.item_time);

            mAuth = FirebaseAuth.getInstance();

            buttonJoin = itemView.findViewById(R.id.button_join);
            buttonJoin.setTag(0); // initialize button as "JOIN"
        }

        public void joinFunc(final String mReportID) {
            buttonJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int status = (Integer) view.getTag();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    final Map<String, Integer> account = new HashMap<>();

                    mRef = FirebaseDatabase.getInstance().getReference("/Joined/" + mReportID);

                    switch (status) {
                        case 0:
                            //change to JOINED if User clicks button
                            account.put(user.getUid(), 1);

                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot joinedFriend : dataSnapshot.getChildren()) {
                                        if (joinedFriend.getKey().equals(" ")) {
                                            joinedFriend.getRef().setValue(null);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("RECYCLER_VIEW_ADAPTER", "Failed to read value.",
                                            error.toException());
                                }
                            });
                            mRef.setValue(account);
                            buttonJoin.setText("JOINED");
                            buttonJoin.setBackgroundColor(Color.parseColor("#43a047"));
                            view.setTag(1);
                            break;

                        case 1:
                            //change to JOIN? if User clicks button again
                            account.put(user.getUid(), 0);
                            mRef.setValue(account);
                            buttonJoin.setText("JOIN?");
                            buttonJoin.setBackgroundColor(Color.parseColor("#4d2c91"));
                            view.setTag(0);
                            break;
                    }
                }
            });
        }
    }

}

