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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedTab extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerViewAdapter recyclerAdapter;

    private List<MicroReportItem> listMReport = new ArrayList<>();

    private FirebaseDatabase mDataBase;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        myRecyclerView = rootView.findViewById(R.id.feed_recyclerview);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), listMReport);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance();
        myRef = mDataBase.getReference("/Microreports");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this
                // location is updated.

                listMReport = new ArrayList<>();

                for (DataSnapshot firebaseUser : dataSnapshot.getChildren()) {
                    for (DataSnapshot microReport : firebaseUser.getChildren()) {
                        MicroReportItem value = microReport.getValue(MicroReportItem.class);

                        if (!value.getEventLocationAddress().equals(" ")) {
                            MicroReportItem mReport = new MicroReportItem();

                            //TODO: get location image/event image
                            mReport.setLocationPhoto(R.drawable.web_hi_res_512);
                            mReport.setEventName(value.getEventName());
                            mReport.setEventLocationName(value.getEventLocationName());
                            mReport.setEventLocationAddress(value.getEventLocationAddress());
                            mReport.setEventUsername(value.getEventUsername());
                            mReport.setEventStartTime(value.getEventStartTime());

                            listMReport.add(mReport);
                            //TODO: sort microreports by event time
                            recyclerAdapter = new RecyclerViewAdapter(getContext(), listMReport);
                            myRecyclerView.setAdapter(recyclerAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FEED_TAB", "Failed to read value.", error.toException());
            }
        });
    }
}