package com.journaldev.recyclerviewmultipleviewtype;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.journaldev.recyclerviewmultipleviewtype.Resource;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Farshad on 11/6/2016.
 */
public class ResourceItemView extends RelativeLayout {


    @Bind(R.id.search_title)
    TextView title;

    @Bind(R.id.search_content)
    TextView content;

    @Bind(R.id.search_keywords)
    TextView sleep_type;


    public ResourceItemView(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {
        inflate(context, R.layout.resource_item_view, this);
        ButterKnife.bind(this);
    }

    public void bind(final Resource sleepResource) {
//        emoji.setText(blog.getEmoji());
        title.setText(sleepResource.getTitle());
        content.setText(sleepResource.getContent());
        sleep_type.setText(sleepResource.getKeywords());
        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getContext();
                mainActivity.resourceToShowTitle=sleepResource.getTitle();
                mainActivity.changeFragment(new ShowResourceFragment());
            }
        });
    }
}