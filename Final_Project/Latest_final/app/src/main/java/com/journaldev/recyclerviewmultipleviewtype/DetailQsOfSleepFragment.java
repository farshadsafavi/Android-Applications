package com.journaldev.recyclerviewmultipleviewtype;


import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailQsOfSleepFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.details_activity, container, false);


        Button button = (Button) rootView.findViewById(R.id.btn_details_done);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Realm realm = Realm.getDefaultInstance();

                RealmQuery<Resource> query = realm.where(Resource.class);
                query.equalTo("component","sleep");

                query.beginGroup(); //Group for Answers
                boolean orFlag=false;
                CheckBox checkBox=(CheckBox) rootView.findViewById(R.id.sleep_detail_1);
                if (checkBox.isChecked()) {
                    query.contains("criteria","anxious");
                    orFlag=true;
                }
                checkBox=(CheckBox) rootView.findViewById(R.id.sleep_detail_2);
                if (checkBox.isChecked()) {
                    if (orFlag)
                        query.or();
                    else
                        orFlag=true;
                    query.contains("criteria","midnight");
                }
                checkBox=(CheckBox) rootView.findViewById(R.id.sleep_detail_3);
                if (checkBox.isChecked()) {
                    if (orFlag)
                        query.or();
                    else
                        orFlag=true;
                    query.contains("criteria","busy");
                }
                checkBox=(CheckBox) rootView.findViewById(R.id.sleep_detail_4);
                if (checkBox.isChecked()) {
                    if (orFlag)
                        query.or();
                    else
                        orFlag=true;
                    query.contains("criteria","general");
                }
                checkBox=(CheckBox) rootView.findViewById(R.id.sleep_detail_5);
                if (checkBox.isChecked()) {
                    if (orFlag)
                        query.or();
                    query.contains("criteria","roommate");
                }
                query.endGroup();
                query.contains("criteria","grad");

                RealmResults<Resource> resultsAll = query.findAll();

                rootView.findViewById(R.id.scroll_view_detail_activity_tips).setVisibility(View.VISIBLE);
                LinearLayout mainLL=(LinearLayout) rootView.findViewById(R.id.linear_layout_main_for_tips);
                int j=0;
                for (final Resource resource:resultsAll) {
                    j++;

                    LinearLayout tipLL=new LinearLayout(getActivity());
                    tipLL.setBackgroundColor(getResources().getColor(R.color.colorBackgroundWhite));
                    tipLL.setPadding(30,30,30,30);
                    tipLL.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 20, 0, 20);
                    tipLL.setLayoutParams(lp);
                    tipLL.setElevation(10);
                    tipLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity mainActivity=(MainActivity) getActivity();
                            mainActivity.resourceToShowTitle=resource.title;
                            mainActivity.changeFragment(new ShowResourceFragment());
                            //showResource(resource.title);
                        }
                    });

                    TextView tipTitle=new TextView(getActivity());
                    tipTitle.setText(j + "- " + resource.getTitle());
                    tipTitle.setTextSize(20);
                    tipTitle.setTextColor(getResources().getColor(R.color.colorHexSleep));
                    tipTitle.setTypeface(null, Typeface.BOLD);

                    TextView tipContent=new TextView(getActivity());
                    tipContent.setText(resource.getContent());
                    tipTitle.setTextSize(18);


                    tipLL.addView(tipTitle);
                    tipLL.addView(tipContent);
                    mainLL.addView(tipLL);

                }

                LinearLayout chkboxes= (LinearLayout) rootView.findViewById(R.id.detail_activity_checkboxes_ll);
                // Prepare the View for the animation
                mainLL.setVisibility(View.VISIBLE);
                mainLL.setAlpha(0.0f);
                mainLL.setTranslationY(chkboxes.getHeight());

                // Start the animation
                mainLL.animate().translationY(0).alpha(1.0f).setDuration(1000);

                chkboxes.animate().translationY(chkboxes.getHeight()).alpha(0f).setDuration(1000);
            }
        });
        return rootView;
    }

    /*public void showResource(String title){
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Resource> realmResults = realm.where(Resource.class).equalTo("title",title).findAll();
        Toast.makeText(getActivity(),realmResults.get(0).getContent(),Toast.LENGTH_LONG).show();
    }*/

}
