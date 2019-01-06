package com.journaldev.recyclerviewmultipleviewtype;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG="OOO";

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;

    // Time of sleep variables
    TimePicker tb;
    String time_of_sleep =null;

    // hours of sleep variables
    Spinner sp;
    Integer hours_of_sleep;

    // mode of sleepiness
    RadioGroup rd_sleepness;
    String sleepiness;

    // Needs suggestions
    boolean need_suggestions;



    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        CardView cardView;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }



    public static class TimePickerTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TimePicker timePicker;


        public TimePickerTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.timePicker = (TimePicker) itemView.findViewById(R.id.timePicker);
            ArrayList<String> hours = new ArrayList<String>();

        }

    }

    public static class SpinnerTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        Spinner spinnerType;

        public SpinnerTypeViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.spinnerType = (Spinner) itemView.findViewById(R.id.timeToBed);

        }

    }

    public static class RadioButtonTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        RadioGroup radioGroupType;
        public RadioButtonTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.radioGroupType = (RadioGroup) itemView.findViewById(R.id.radio_group);
        }

    }

    public static class SaveTypeViewHolder extends RecyclerView.ViewHolder {
        TextView txtType;
        Button saveType;

        public SaveTypeViewHolder(View itemView) {
            super(itemView);
            this.saveType = (Button) itemView.findViewById(R.id.save_info);
        }

    }

    public static class RadioButtonReflectTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        RadioGroup radioGroupReflectType;

        public RadioButtonReflectTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.radioGroupReflectType = (RadioGroup) itemView.findViewById(R.id.reflect_radio_group);
        }

    }

    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new TextTypeViewHolder(view);
            case Model.TIME_PICKER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timepicker_type, parent, false);
                return new TimePickerTypeViewHolder(view);
            case Model.SPINNER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_type, parent, false);
                return new SpinnerTypeViewHolder(view);
            case Model.RADIO_BUTTON:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radiobutton_type, parent, false);
                return new RadioButtonTypeViewHolder(view);
            case Model.SAVE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_type, parent, false);
                return new SaveTypeViewHolder(view);
            case Model.RADIO_BUTTON_REFLECT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radiobuttonreflect_type, parent, false);
                return new RadioButtonReflectTypeViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.TEXT_TYPE;
            case 1:
                return Model.TIME_PICKER;
            case 2:
                return Model.SPINNER_TYPE;
            case 3:
                return Model.RADIO_BUTTON;
            case 4:
                return Model.SAVE_TYPE;
            case 5:
                return Model.RADIO_BUTTON_REFLECT;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).txtType.setText(object.text);

                    break;
                case Model.TIME_PICKER:

                    ((TimePickerTypeViewHolder) holder).txtType.setText(object.text);
                    tb = ((TimePickerTypeViewHolder) holder).timePicker;

                    break;
                case Model.SPINNER_TYPE:

                    ((SpinnerTypeViewHolder) holder).txtType.setText(object.text);

                    ((SpinnerTypeViewHolder) holder).spinnerType.setAdapter(object.adapter);

                    sp = ((SpinnerTypeViewHolder) holder).spinnerType;

                    sp.setSelection(5);
                    break;

                case Model.RADIO_BUTTON:

                    ((RadioButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((RadioButtonTypeViewHolder) holder).radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        public void onCheckedChanged(RadioGroup group,
                                                     int checkedId) {
                            Integer pos = (Integer) group.getTag();

                            switch (checkedId) {
                                case R.id.radio_very:
                                    sleepiness = "Very";
                                case R.id.radio_somewhat:
                                    sleepiness = "Somewhat";
                                    break;
                                case R.id.radio_notAtAll:
                                    sleepiness = "Not at all";
                                    break;
                            }
                        }
                    });
                    break;
                case Model.SAVE_TYPE:

                        ((SaveTypeViewHolder) holder).saveType.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                hours_of_sleep = Integer.valueOf(sp.getSelectedItem().toString());
                                if (hours_of_sleep==null || sleepiness==null) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage("Please fill out questions");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                                else {

                                    setTime(tb);
                                    //Log.v(TAG, time_of_sleep + "; " + hours_of_sleep + "; " + sleepiness + "; " + need_suggestions);

                                    saveToDB(time_of_sleep, sleepiness, hours_of_sleep, need_suggestions);

                                    // custom dialog
                                    if (need_suggestions) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext);
                                        alertDialogBuilder.setMessage("You clicked 'I need more sleep'. Do you tell reasons that you need more sleep? ");

                                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                MainActivity mainActivity = (MainActivity) mContext;
                                                mainActivity.changeFragment(new DetailQsOfSleepFragment());
                                            }

                                        });

                                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                MainActivity mainActivity = (MainActivity) mContext;
                                                mainActivity.changeFragment(new MainFrag());
                                            }
                                        });
                                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    } else {
                                        MainActivity mainActivity = (MainActivity) mContext;
                                        mainActivity.changeFragment(new MainFrag());
                                    }
                                }
                            }
                        });
                    break;
                case Model.RADIO_BUTTON_REFLECT:

                    ((RadioButtonReflectTypeViewHolder) holder).txtType.setText(object.text);
                    ((RadioButtonReflectTypeViewHolder) holder).radioGroupReflectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        public void onCheckedChanged(RadioGroup group,
                                                     int checkedId) {
                            Log.v("FarshadSafavi","; " + checkedId);
                            switch (checkedId) { //set the Model to hold the answer the user picked

                                case R.id.radio_needMoreSleep:
                                    need_suggestions = true;
                                    Log.v("FarshadSafavi","; " + need_suggestions);
                                    break;
                                case R.id.radio_enoughSleep:
                                    need_suggestions = false;
                                    Log.v("FarshadSafavi","; " + need_suggestions);
                                    break;


                            }
                        }
                    });
                    break;
            }

        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setTime(TimePicker view) {
        int hour = view.getCurrentHour();
        int min = view.getCurrentMinute();
        time_of_sleep = showTime(hour, min);
    }

    public String showTime(int hour, int min) {
        String format = "";
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return "" + hour + ":" + min + format;
    }


    private void saveToDB(String timeOfSleep,String sleepiness,int hoursOfSleep,boolean needSuggestions) {
        //clearDB();

        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        SleepData sleepData = realm.createObject(SleepData.class); // Create a new object
        sleepData.setTimeOfSleep(timeOfSleep);
        sleepData.setSleepiness(sleepiness);
        sleepData.setHoursOfSleep(hoursOfSleep);
        sleepData.setNeedSuggestions(needSuggestions);


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        sleepData.setYear(year);
        sleepData.setMonth(month);
        sleepData.setDay(day);

        realm.commitTransaction();
        Log.d(TAG,sleepData.toString());
        realm.close();
    }

    private void clearDB() {

        Realm realm = Realm.getDefaultInstance();

        //obtain the results of a query
        final RealmResults<SleepData> results = realm.where(SleepData.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });

    }



}
