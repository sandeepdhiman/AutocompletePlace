package com.gis.autocompleteplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.gis.autocompleteplace.adapter.PlaceAdapter;
import com.gis.autocompleteplace.model.PlaceDataModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private GoogleMap mMap;
    private PlaceAdapter placeAdapter;
    private  PlacesClient mPlacesClient;
    private AutoCompleteTextView autoCompleteEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteEditText =  findViewById(R.id.autoCompleteEditText);
        String apiKey = ""; // put your api key here
        Places.initialize(this, apiKey);
        mPlacesClient = Places.createClient(this);

        placeAdapter = new PlaceAdapter(this, R.layout.layout_item_places, mPlacesClient);
        autoCompleteEditText.setAdapter(placeAdapter);

       autoCompleteEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               PlaceDataModel placeDataModel = (PlaceDataModel) autoCompleteEditText.getAdapter().getItem(position);
               autoCompleteEditText.setText(placeDataModel.getFullText());
               findPlaceDetail(placeDataModel.getPlaceId());
           }
       });

    }
    private void findPlaceDetail(String placeid){
        // Define a Place ID.
        final String placeId = placeid;//"INSERT_PLACE_ID_HERE";

// Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        final ProgressDialog progressDialog = ProgressDialog.show(this,"","Please wait...",false);
        mPlacesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                progressDialog.dismiss();
                Place place = response.getPlace();
                Log.i("TAG", "Place found: " + place.getName());
                if (place.getLatLng()!=null){
                    Log.v("Latitude",""+place.getLatLng().latitude);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            }
        });

    }
}
