package com.example.stephaniechan.friendzone;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.fusion.client.models.Business;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mosheyang on 3/7/18.
 */


public class ActivitiesFragment extends Fragment {
    private String title;
    private int page;
    private Integer[] mThumbIDs = {
            R.drawable.basketball, R.drawable.movie, R.drawable.studying, R.drawable.bowling,
           R.drawable.shopping, R.drawable.biking,
            R.drawable.running, R.drawable.swimming, R.drawable.weight_lifting
    };

    private String[] termMap = {"Basketball Courts", "Movie Theatre", "Study Area", "Bowling Alley",
        "Shopping", "Biking Trail", "Running Trail", "Swimming Pool", "Gym"};
    public static ActivitiesFragment newInstance( int page, String title){
        ActivitiesFragment fragmentFirst = new ActivitiesFragment();
        Bundle args = new Bundle();
        args.putInt("someInt",page);
        args.putString("someString", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        //   TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        //  tvLabel.setText(page + " -- " + title);

        super.onCreate(savedInstanceState);
        int iconSize=getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            final GridView gridview = (GridView) view.findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(getContext(), mThumbIDs, termMap));


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(getContext(), "" + i,
//                            Toast.LENGTH_SHORT).show();
                    Map<String, String> params = new HashMap<>();

                    // general params
                    // Jansen Yan: use last known location for these values from MainActivity
                    params.put("latitude",Double.toString(MainActivity.latitude));
                    params.put("longitude", Double.toString(MainActivity.longitude));
                    //System.out.println("Gridview.getAdapter().getItem code: " + gridview.getAdapter().getItem(i));
                    params.put("term" , (String)gridview.getAdapter().getItem(i));
                    // Jansen Yan: mapped the position to the Term so if statement would no longer be necessary
                    // Jansen Yan: moved the above code into the YelpFragmentUtil helper
                    ArrayList<Business> businesses = YelpFragmentUtil.generateYelpSearch(params);
                    String businessName = businesses.get(0).getName();  // "JapaCurry Truck"
                    //Double rating = businesses.get(0).getRating();  // 4.0
//                    Toast.makeText(getContext(), "" + businessName,
//                            Toast.LENGTH_SHORT).show();

                    ConstraintLayout lay = (ConstraintLayout) getView().getParent().getParent();

                    if (lay.findViewById(R.id.place_details)== null)
                        System.out.println("lay is null");

                    TextView mPlaceDetailsText = lay.findViewById(R.id.place_details);
                    // = getParentFragment().getView().findViewById(R.id.place_details);
                    URI url = URI.create(businesses.get(0).getUrl());
                    CharSequence cs= businesses.get(0).getLocation().getAddress1() +", " +  businesses.get(0).getLocation().getCity() + ", "+businesses.get(0).getLocation() + ", "+ businesses.get(0).getLocation().getState();
                    mPlaceDetailsText.setText(YelpFragmentUtil.generateDisplayString(params.get("term"), businesses.get(0)));
                }

            });
        }
        //CONTENT_URI= Uri.parse("content://"+iconsProvider.class.getCanonicalName());
        return view;
    }
}