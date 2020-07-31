package com.gis.autocompleteplace;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MapExtention {

private static Long  TASK_AWAIT = 120L;
private float MAP_CAMERA_ZOOM = 11f;
private int MAP_CAMERA_ZOOM_INT = 11;

    public static List<AutocompletePrediction>getAutoComplete(PlacesClient placesClient,CharSequence constraint){
    List<AutocompletePrediction>list =  new ArrayList<>();
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request =FindAutocompletePredictionsRequest.builder().setTypeFilter(TypeFilter.ADDRESS).setSessionToken(token).setQuery(constraint.toString()).build();
        Task<FindAutocompletePredictionsResponse> prediction =placesClient.findAutocompletePredictions(request);

        try {
            Tasks.await(prediction, TASK_AWAIT, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if (prediction.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = prediction.getResult();
            if (findAutocompletePredictionsResponse!=null) {
                list = findAutocompletePredictionsResponse.getAutocompletePredictions();
            }
            return list;
        }




        return list;
    }
}
