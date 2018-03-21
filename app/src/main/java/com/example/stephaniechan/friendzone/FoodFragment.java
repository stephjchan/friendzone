package com.example.stephaniechan.friendzone;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;

import static android.content.ContentValues.TAG;

/**
 * Created by mosheyang on 3/7/18.
 */

public class FoodFragment extends Fragment{
    private String title;
    private int page;
    private String longitude;
    private String latitude;
    private Integer[] mThumbIDs = {
        R.drawable.asian_noodles, R.drawable.beer_drinking,
                R.drawable.burger, R.drawable.burrito,
                R.drawable.chinese_food, R.drawable.donut,
                R.drawable.hotdog, R.drawable.pizza,
                R.drawable.sandwich, R.drawable.tacos,

    };
    private String[] termMap = {
         "Noodles", "Bars", "Burgers", "Burritos", "Chinese Takeout",
            "Donuts", "Hotdogs", "Pizza", "Sandwiches", "Tacos"
    };
    //public static String apiKey = "uLpgcL9A3hILUX7a4lzLe_JT63aHhO8Y2IDQ1uoJ0MdYc_o_1sWQnblPfHyqamS0-e2kXCY97dj0n_mTJZ3bpGVM6LTq3uOyzLDSGLCVVY7WxS8KXgZrMji4gZapWnYx";

    public static FoodFragment newInstance( int page, String title, double longitude, double latitude){
        FoodFragment fragmentFirst = new FoodFragment();
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
        longitude = Double.toString(getArguments().getDouble("longitude"));
        latitude = Double.toString(getArguments().getDouble("latitude"));

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
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
//                    params.put("latitude", "33.647163");
//                    params.put("longitude", "-117.841129");
//                    System.out.println("latitude in food fragment: " + Double.toString(MainActivity.latitude));
//                    System.out.println("longitude in food fragment" + Double.toString(MainActivity.longitude));
                    params.put("latitude",Double.toString(MainActivity.latitude));
                    params.put("longitude", Double.toString(MainActivity.longitude));
                    System.out.println("Gridview.getAdapter().getItem code: " + gridview.getAdapter().getItem(i));
                    params.put("term" , (String)gridview.getAdapter().getItem(i));
                    // Jansen Yan: mapped the position to the Term so if statement would no longer be necessary
//                    if (i == 0)
//                        params.put("term", "Noodles");
//                    if (i == 1)
//                        params.put("term", "Bars");
//                    if (i == 2)
//                        params.put("term", "Burgers");
//                    if (i == 3)
//                        params.put("term", "Burritos");
//                    if (i == 4)
//                        params.put("term", "Chinese Takeout");
//                    if (i == 5)
//                        params.put("term", "Donuts");
//                    if (i == 6)
//                        params.put("term", "Hot Dogs");
//                    if (i == 7)
//                        params.put("term", "Pizza");
//                    if (i == 8)
//                        params.put("term", "Sandwiches");
//                    if (i == 9)
//                        params.put("term", "Tacos");


//                    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
//                    YelpFusionApi yelpFusionApi = null;
//                    try {
//                        yelpFusionApi = apiFactory.createAPI(apiKey);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//
//                    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
//                    SearchResponse searchResponse = null;
//                    try {
//                        searchResponse = call.execute().body();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//
//                    int totalNumberOfResult = searchResponse.getTotal();  // 3
//
//                    ArrayList<Business> businesses = searchResponse.getBusinesses();
                    // Jansen Yan: moved the above code into the YelpFragmentUtil helper
                    ArrayList<Business> businesses = YelpFragmentUtil.generateYelpSearch(params);
                    String businessName = businesses.get(0).getName();  // "JapaCurry Truck"
                    Double rating = businesses.get(0).getRating();  // 4.0
//                    Toast.makeText(getContext(), "" + businessName,
//                            Toast.LENGTH_SHORT).show();

                    ConstraintLayout lay = (ConstraintLayout) getView().getParent().getParent();

                        if (lay.findViewById(R.id.place_details)== null)
                            System.out.println("lay is null");

                       TextView mPlaceDetailsText = lay.findViewById(R.id.place_details);
                    // = getParentFragment().getView().findViewById(R.id.place_details);
                    URI url = URI.create(businesses.get(0).getUrl());
                    CharSequence cs= businesses.get(0).getLocation().getAddress1() +", " +  businesses.get(0).getLocation().getCity() + ", "+businesses.get(0).getLocation() + ", "+ businesses.get(0).getLocation().getState();
                    //        + " " +businesses.get(0).getLocation().getZipCode() + ", "+ businesses.get(0).getLocation().getCountry();
                    //Jansen Yan: Edited to include the params.get("term"), which is the intent within the textview, we will use this as the event name
                    // Original Code:
                    // mPlaceDetailsText.setText( businesses.get(0).getName() + "\n"+ businesses.get(0).getLocation().getAddress1() + businesses.get(0).getLocation().getAddress2()+businesses.get(0).getLocation().getAddress3()+", "+businesses.get(0).getLocation().getCity()+ ", " );

//                    String intent = params.get("term");
//                    String locationAddress = businesses.get(0).getLocation().getAddress1();
//                    String city = businesses.get(0).getLocation().getCity();
//                    String state = businesses.get(0).getLocation().getState();
//                    String zipCode = businesses.get(0).getLocation().getZipCode();
//                    String country = businesses.get(0).getLocation().getCountry();
//                    String displayString = intent + "\n" + businessName + "\n" + locationAddress + ", " + city + " " + state + " " + zipCode + ", " + country;
                    //System.out.println("char sequence: " + cs);
                    // Jansen Yan: Edited in string
                    //mPlaceDetailsText.setText( params.get("term") + "\n" + businesses.get(0).getName() + "\n"+ businesses.get(0).getLocation().getAddress1() + businesses.get(0).getLocation().getAddress2()+businesses.get(0).getLocation().getAddress3()+", "+businesses.get(0).getLocation().getCity()+ ", " );

                    mPlaceDetailsText.setText(YelpFragmentUtil.generateDisplayString(params.get("term"), businesses.get(0)));
                }

            });
        }
        //CONTENT_URI= Uri.parse("content://"+iconsProvider.class.getCanonicalName());
        return view;
     //   TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
      //  tvLabel.setText(page + " -- " + title);

    }
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, URI websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }




}