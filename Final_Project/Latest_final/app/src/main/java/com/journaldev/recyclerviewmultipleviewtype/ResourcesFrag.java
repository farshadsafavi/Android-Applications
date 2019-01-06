package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Owlia on 11/15/2016.
 */

public class ResourcesFrag extends Fragment {
    private Realm realm;
    private String TAG="OOO";

    private RealmSearchView realmSearchView;
    private ResourceRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frag_resources, container, false);

        realm = Realm.getDefaultInstance();




        realmSearchView = (RealmSearchView) rootView.findViewById(R.id.search_view);
        adapter = new ResourceRecyclerViewAdapter(getActivity(), realm, "keywords");
        adapter.setUseContains(true);

        realmSearchView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    public class ResourceRecyclerViewAdapter
            extends RealmSearchAdapter<Resource, ResourceRecyclerViewAdapter.ViewHolder> {

        public ResourceRecyclerViewAdapter(
                android.content.Context context,
                Realm realm,
                String filterColumnName) {
            super(context, realm, filterColumnName);
        }

        public class ViewHolder extends RealmSearchViewHolder {

            private ResourceItemView resourceItemView;

            public ViewHolder(FrameLayout container, TextView footerTextView) {
                super(container, footerTextView);
            }

            public ViewHolder(ResourceItemView resourceItemView) {
                super(resourceItemView);
                this.resourceItemView = resourceItemView;
            }
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            ViewHolder vh = new ViewHolder(new ResourceItemView(viewGroup.getContext()));
            return vh;
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final Resource sleep = realmResults.get(position);
            viewHolder.resourceItemView.bind(sleep);
        }

        @Override
        public ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
            View v = inflater.inflate(R.layout.footer_view, viewGroup, false);
            return new ViewHolder(
                    (FrameLayout) v,
                    (TextView) v.findViewById(R.id.footer_text_view));
        }

        @Override
        public void onBindFooterViewHolder(ViewHolder holder, int position) {
            super.onBindFooterViewHolder(holder, position);
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }
            );
        }
    }
}
