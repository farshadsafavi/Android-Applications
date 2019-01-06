package com.example.farshad.gridviewasynch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Farshad on 10/7/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private int layoutResourceId;
    private Context mContext;
    ArrayList<String> itemList = new ArrayList<String>();

    public ImageAdapter(Context c, int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        mContext = c;
    }

    void add(String path) {
        itemList.add(path);
    }

    void clear() {
        itemList.clear();
    }

    void remove(int index){
        itemList.remove(index);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       // View row = convertView;
        ViewHolder holder;
        ImageView imageView;
        View cv = convertView;
        if (cv == null) {
            // іf іt's nοt recycled, initialize ѕοmе attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            cv = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.Exif = (TextView) cv.findViewById(R.id.text);
            holder.image = (ImageView) cv.findViewById(R.id.image);
            holder.progress = (ProgressBar) cv.findViewById(R.id.progress_spinner);
            holder.position = position;
            cv.setTag(holder);
        } else {
            holder = (ViewHolder) cv.getTag();
        }
//        Picasso
//                .with


//        holder.image.setImageBitmap(bm);


        new AsyncTask<ViewHolder, Void, Bitmap>() {
            private ViewHolder v;

            @Override
            protected Bitmap doInBackground(ViewHolder... params) {
                v = params[0];
                Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);

                if (v.position == position) {
                    // If this item hasn't been recycled already, hide the
                    // progress and set and show the image
                  //  v.image.setVisibility(View.VISIBLE);
                    v.Exif.setText(ReadExif((itemList.get(position))));
                    v.progress.setVisibility(View.GONE);
                    v.image.setVisibility(View.VISIBLE);
                    v.image.setImageBitmap(result);
                }
            }
        }.execute(holder);
        return cv ;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                             int reqHeight) {
        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height
                        / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    String ReadExif(String file){
        String exif="";

        try {
            ExifInterface exifInterface = new ExifInterface(file);
            GeoDegree degree= new GeoDegree(exifInterface);
            exif = degree.toString();
//            Toast.makeText(mContext,
//                    "" + a,
//                    Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            Toast.makeText(mContext,
//                    e.toString(),
//                    Toast.LENGTH_LONG).show();
        }

        return exif;
    }

    static class ViewHolder  {
        TextView Exif;
        ImageView image;
        ProgressBar progress;
        int position;
    }

}