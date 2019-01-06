package com.example.farshad.assignmentp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int count = 0;
    boolean isToggled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    public void incrementCounter(View view) {
        count++;
        TextView textView = (TextView) findViewById(R.id.text_view_1);
        textView.setText("Clicked " + count + " Times");
    }

    public void toggleImage(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_1);
        if (isToggled) {
            isToggled = false;
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade);
            imageView.startAnimation(myFadeInAnimation);
            imageView.setVisibility(view.GONE);
        } else {
            isToggled = true;
            imageView.setVisibility(view.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            count = 0;
            TextView textView = (TextView) findViewById(R.id.text_view_1);
            textView.setText("No Clicks Yet");
            isToggled = false;
            ImageView imageView = (ImageView) findViewById(R.id.image_view_1);
            imageView.setVisibility(View.GONE);
        }
        return true;
    }
}
