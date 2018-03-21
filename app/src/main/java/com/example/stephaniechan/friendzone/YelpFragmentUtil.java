package com.example.stephaniechan.friendzone;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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

/**
 * Created by Jansen on 3/19/2018.
 */

public class FragmentUtil {
    private Context mContext;
    public static String apiKey = "uLpgcL9A3hILUX7a4lzLe_JT63aHhO8Y2IDQ1uoJ0MdYc_o_1sWQnblPfHyqamS0-e2kXCY97dj0n_mTJZ3bpGVM6LTq3uOyzLDSGLCVVY7WxS8KXgZrMji4gZapWnYx";
    public FragmentUtil(Context c){

    }
    public static String generateDisplayString(Map<String, String> params){

        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
        YelpFusionApi yelpFusionApi = null;
        try {
            yelpFusionApi = apiFactory.createAPI(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
        SearchResponse searchResponse = null;
        try {
            searchResponse = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalNumberOfResult = searchResponse.getTotal();  // 3

        ArrayList<Business> businesses = searchResponse.getBusinesses();
        String businessName = businesses.get(0).getName();  // "JapaCurry Truck"
        Double rating = businesses.get(0).getRating();  // 4.0
//        Toast.makeText(getContext(), "" + businessName,
//                Toast.LENGTH_SHORT).show()
        // = getParentFragment().getView().findViewById(R.id.place_details);
        URI url = URI.create(businesses.get(0).getUrl());
        CharSequence cs= businesses.get(0).getLocation().getAddress1() +", " +  businesses.get(0).getLocation().getCity() + ", "+businesses.get(0).getLocation() + ", "+ businesses.get(0).getLocation().getState();
        //        + " " +businesses.get(0).getLocation().getZipCode() + ", "+ businesses.get(0).getLocation().getCountry();
        //Jansen Yan: Edited to include the params.get("term"), which is the intent within the textview, we will use this as the event name
        // Original Code:
        // mPlaceDetailsText.setText( businesses.get(0).getName() + "\n"+ businesses.get(0).getLocation().getAddress1() + businesses.get(0).getLocation().getAddress2()+businesses.get(0).getLocation().getAddress3()+", "+businesses.get(0).getLocation().getCity()+ ", " );

        String intent = params.get("term");
        String locationAddress = businesses.get(0).getLocation().getAddress1();
        String city = businesses.get(0).getLocation().getCity();
        String state = businesses.get(0).getLocation().getState();
        String zipCode = businesses.get(0).getLocation().getZipCode();
        String country = businesses.get(0).getLocation().getCountry();
        String displayString = intent + "\n" + businessName + "\n" + locationAddress + ", " + city + " " + state + " " + zipCode + ", " + country;
        //System.out.println("char sequence: " + cs);
        return displayString;
    }
}
