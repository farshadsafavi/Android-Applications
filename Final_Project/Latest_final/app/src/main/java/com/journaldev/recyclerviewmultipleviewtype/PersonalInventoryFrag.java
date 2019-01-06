package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owlia on 11/17/2016.
 */

public class PersonalInventoryFrag extends Fragment {
    String TAG="OOO";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView= inflater.inflate(R.layout.frag_personal_inventory, container, false);

        rootView.findViewById(R.id.personal_inventory_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Realm realm = Realm.getDefaultInstance();
                //obtain the results of a query
                final RealmResults<PersonalInventory> results = realm.where(PersonalInventory.class).findAll();
                // All changes to data must happen in a transaction
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // Delete all matches
                        results.deleteAllFromRealm();
                    }
                });


                realm.beginTransaction();

                PersonalInventory personalInventory = realm.createObject(PersonalInventory.class); // Create a new object

                String campus="STG";
                if (((RadioButton)rootView.findViewById(R.id.campus_mississauga)).isChecked())
                    campus="MSG";
                if (((RadioButton)rootView.findViewById(R.id.campus_scarborough)).isChecked())
                    campus="SCB";

                String affiliation="Undergrad";
                if (((RadioButton)rootView.findViewById(R.id.affiliation_grad)).isChecked())
                    affiliation="Grad";

                String living="Home";
                if (((RadioButton)rootView.findViewById(R.id.live_housing)).isChecked())
                    living="Housing";
                if (((RadioButton)rootView.findViewById(R.id.live_residence)).isChecked())
                    living="Residence";

                personalInventory.setCampus(campus);
                personalInventory.setAffiliation(affiliation);
                personalInventory.setLiving(living);

                personalInventory.setAccessibilty(((CheckBox)rootView.findViewById(R.id.is_accessibility)).isChecked());
                personalInventory.setlGBTQ(((CheckBox)rootView.findViewById(R.id.is_lgbtq)).isChecked());
                personalInventory.setInternational(((CheckBox)rootView.findViewById(R.id.is_international)).isChecked());
                personalInventory.setHaveChild(((CheckBox)rootView.findViewById(R.id.is_child)).isChecked());

                realm.commitTransaction();
                Log.d(TAG,personalInventory.toString());
                realm.close();
            }
        });
        return rootView;
    }
}
