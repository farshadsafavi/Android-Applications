package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.journaldev.recyclerviewmultipleviewtype.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ScreenoffAdapter extends ArrayAdapter<Screenoff> {
    private static final String LOCATION_SEPARATOR = " of ";
    String[] separated;
    public ScreenoffAdapter(Activity context, ArrayList<Screenoff> screenoffs) {
        super(context, 0, screenoffs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.screenoff_list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        final Screenoff currentScreenOff = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.version_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        // Set the proper background color on the magnitude circle.

        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) nameTextView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude

        int minute_1 = (int) Math.floor(currentScreenOff.getScreenOffDuration()/60000);
        int target = (int ) Math.ceil(((currentScreenOff.getTargetSleep()*60) ));
        double percent = ((minute_1* 10)/target) ;

        int magnitudeColor = getMagnitudeColor((int) percent );
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        nameTextView.setText("" + (int) (percent*10));

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView location1TextView = (TextView) listItemView.findViewById(R.id.location_offset);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        location1TextView.setText("The goal set to " +  currentScreenOff.getTargetSleep() + " Hours");

        TextView location2TextView = (TextView) listItemView.findViewById(R.id.primary_location);
        long timediff = currentScreenOff.getScreenOffDuration();
        String time_diff = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timediff),
                TimeUnit.MILLISECONDS.toMinutes(timediff) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timediff)),
                TimeUnit.MILLISECONDS.toSeconds(timediff) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timediff)));
        location2TextView.setText(time_diff);
//        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
//        // Get the version number from the current AndroidFlavor object and
//        // set this text on the number TextView
        Date dateObject = new Date(currentScreenOff.getCurrentTime());
        dateTextView.setText(formatDate(dateObject));
//
        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        timeTextView.setText(formatTime(dateObject));
//

        return listItemView;
    }
//
    private String formatDate(Date dateObject)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.getDateInstance().format(dateObject);
        return dateToDisplay;
    }
//
    private String formatTime(Date dateObject)
    {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormatter.format(dateObject);
        return timeToDisplay;
    }

    private int getMagnitudeColor(long magnitude){
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
                magnitudeColorResourceId = R.color.magnitude0;
                break;
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            case 10:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }

}
