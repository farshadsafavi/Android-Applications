package com.journaldev.recyclerviewmultipleviewtype.Strategy;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.journaldev.recyclerviewmultipleviewtype.AlarmClock.StrategyAlarmActivity;
import com.journaldev.recyclerviewmultipleviewtype.R;
import com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking.SleepGameActivity;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class StrategyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StrategyModel> dataSet;
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


    private boolean fabStateVolume = false;

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {


        TextView txtType;
        CardView cardView;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }


    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView image;


        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.image = (ImageView) itemView.findViewById(R.id.background);
        }

    }

    public static class SpinnerTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView image;


        public SpinnerTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.image = (ImageView) itemView.findViewById(R.id.background);
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


    public StrategyAdapter(ArrayList<StrategyModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case StrategyModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new TextTypeViewHolder(view);
            case StrategyModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type, parent, false);
                return new ImageTypeViewHolder(view);
            case StrategyModel.SPINNER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type, parent, false);
                return new SpinnerTypeViewHolder(view);
            case StrategyModel.SAVE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_type, parent, false);
                return new SaveTypeViewHolder(view);
        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return StrategyModel.TEXT_TYPE;
            case 1:
                return StrategyModel.IMAGE_TYPE;
            case 2:
                return StrategyModel.SPINNER_TYPE;
            case 4:
                return StrategyModel.SAVE_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        StrategyModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case StrategyModel.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).txtType.setText(object.text);

                    break;

                case StrategyModel.IMAGE_TYPE:

                    ((ImageTypeViewHolder) holder).txtType.setText(object.text);
                    ((ImageTypeViewHolder) holder).image.setImageResource(object.data);
                    ((ImageTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(mContext, StrategyAlarmActivity.class);
                            mContext.startActivity(myIntent);
                        }

                    });

                    break;
                case StrategyModel.SPINNER_TYPE:

                    ((SpinnerTypeViewHolder) holder).txtType.setText(object.text);
                    ((SpinnerTypeViewHolder) holder).image.setImageResource(object.data);
                    ((SpinnerTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(mContext, SleepGameActivity.class);
                            mContext.startActivity(myIntent);
//                            CalanderFragment mainActivity = (CalanderFragment) mContext;
//                            mainActivity.changeFragment(new SleepGameFragment());
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

}
