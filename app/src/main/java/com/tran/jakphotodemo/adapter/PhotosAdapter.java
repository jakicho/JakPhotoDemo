package com.tran.jakphotodemo.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tran.jakphotodemo.R;
import com.tran.jakphotodemo.models.Photo;
import com.tran.jakphotodemo.utils.PicassoTrustAll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Photo> mDataset = Collections.emptyList();

    public PhotosAdapter(Context context, List<Photo> mDataset){

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;
        view = inflater.inflate(R.layout.item_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Photo current = mDataset.get(position);
        holder.txtTitle.setText(current.getTitle());
        holder.txtUrl.setText(current.getUrl());

        if(isNetworkAvailable()) {

            // saving locally the image that is access
            Picasso.with(context)
                    .load(current.getUrl())
                    .into(picassoImageTarget(context, "imageDir", mDataset.get(position).getId()+ ".jpeg"));

            // displaying the image
            PicassoTrustAll.getInstance(context)
                    .load(current.getUrl())
                    .into(holder.imgPict);

        } else {

            // loading image locally if any
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, current.getId()+ ".jpeg");
            Picasso.with(context).load(myImageFile).into(holder.imgPict);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtTitle;
        TextView txtUrl;
        ImageView imgPict;

        public ViewHolder(View itemView) {

            super(itemView);

            imgPict = (ImageView) itemView.findViewById(R.id.img_pict);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtUrl = (TextView) itemView.findViewById(R.id.txt_url);
        }
    }

    public void setDataset(List<Photo> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    private Target picassoImageTarget(final Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
