package co.chatsdk.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import wanderingdevelopment.tk.sdkbaseui.Activities.ChatSDKLocationActivity;

/**
 * Created by benjaminsmiley-andrews on 23/05/2017.
 */

public class LocationSelector {

    public static final int PICK_LOCATION = 102;

    private Result resultHandler;
    private Activity activity;

    interface Result {
        void result (String snapshotPath, LatLng latLng);
    }

    public void startChooseLocationActivity (Activity activity, Result resultHandler) throws Exception {
        this.resultHandler = resultHandler;
        this.activity = activity;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = builder.build(activity);
            activity.startActivityForResult(intent, PICK_LOCATION);
        }
        catch (GooglePlayServicesRepairableException e) {
            throw new Exception(e.getMessage());
        }

       // Intent intent = new Intent(activity, ChatSDKLocationActivity.class);
    }

    private void processPickedLocation(int resultCode, Intent data) throws Exception {
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            if (data.getExtras() == null)
                return;

            if (data.getExtras().containsKey(ChatSDKLocationActivity.ERROR)) {
                throw new Exception(data.getExtras().getString(ChatSDKLocationActivity.ERROR));
            }

        }
        else if (resultCode == AppCompatActivity.RESULT_OK) {
            // Send the message, Params Latitude, Longitude, Base64 Representation of the image of the location, threadId.
            if(resultHandler != null) {
                Place place = PlacePicker.getPlace(activity, data);
//                String filePath = bundle.getExtras().getString(ChatSDKLocationActivity.SNAP_SHOT_PATH, null);

                resultHandler.result("", place.getLatLng());
            }
        }
    }

    public void handleResult (Activity activity, int requestCode, int resultCode, Intent data) throws Exception {
        /* Pick location logic*/
        if (requestCode == PICK_LOCATION)
        {
            processPickedLocation(resultCode, data);
        }
    }


}
