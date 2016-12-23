package com.tran.jakphotodemo;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tran.jakphotodemo.adapter.PhotosAdapter;
import com.tran.jakphotodemo.database.PhotoProvider;
import com.tran.jakphotodemo.models.Photo;
import com.tran.jakphotodemo.network.PhotoAPI;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainFragment extends Fragment {

    @Bind(R.id.recycler_view) public RecyclerView recyclerView;
    @Inject Observable<List<Photo>> mObservable;

    private Subscription mSubscription;
    private List<Photo> mDataset;
    private PhotosAdapter mAdapter;
    private ContentResolver mResolver;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        Application.app().photoComponent().inject(this);
        setRecyclerView();
        getPhotos();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSubscription != null &&  !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe(); // avoid leak
        }
    }

    private void setRecyclerView() {

        if(mDataset == null) mDataset = Collections.emptyList();
        mAdapter = new PhotosAdapter(getContext(), mDataset);
        recyclerView.setAdapter(mAdapter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false));
        }
    }

    // we check if the data has already been saved locally
    private void getPhotos() {

        mResolver = getActivity().getContentResolver();
        String[] projection = new String[]{PhotoProvider.id, PhotoProvider.title, PhotoProvider.url};
        Cursor cursor = mResolver.query(PhotoProvider.CONTENT_URL, projection, null, null, null);
        List<Photo> photoList = new ArrayList<>();

        if(cursor!=null && cursor.moveToFirst()) {
            do{
                int id = cursor.getInt(cursor.getColumnIndex(PhotoProvider.id));
                String title = cursor.getString(cursor.getColumnIndex(PhotoProvider.title));
                String url = cursor.getString(cursor.getColumnIndex(PhotoProvider.url));

                Photo current = new Photo();
                current.setId(id);
                current.setTitle(title);
                current.setUrl(url);
                photoList.add(current);

            } while (cursor.moveToNext());

            cursor.close();
        }


        if(!photoList.isEmpty()) mAdapter.setDataset(photoList);
        else downloadPhotos();
    }


    // Network is only access once. The data is then save locally in the Photoprovider
    private void downloadPhotos() {

        // Network asynchronous call using Retrofit & RX
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PhotoAPI.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoAPI photoApi = retrofit.create(PhotoAPI.class);

        mObservable = photoApi.getPhotosListRX(PhotoAPI.APP_ID, PhotoAPI.ACTION);

        mSubscription = mObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Photo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Photo> photos) {

                        mDataset = photos;
                        new SaveDataTask(getContext()).execute();
                        mAdapter.setDataset(photos);
                    }
                });
    }


    // data is being saved asynchronously
    class SaveDataTask extends AsyncTask<Void, Photo, Void> {

        private final WeakReference<Context> contextReference;

        SaveDataTask(Context context) {
            this.contextReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final Context context = this.contextReference.get();

            if(context != null) {
                for (Photo item : mDataset) {
                    ContentValues values = new ContentValues();
                    values.put(PhotoProvider.id, item.getId());
                    values.put(PhotoProvider.albumId, item.getAlbumId());
                    values.put(PhotoProvider.title, item.getTitle());
                    values.put(PhotoProvider.url, item.getUrl());
                    values.put(PhotoProvider.thumbnailUrl, item.getThumbnailUrl());
                    context.getContentResolver().insert(PhotoProvider.CONTENT_URL, values);
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            //Message.message(this.contextReference.get(), "DONE!!");
        }
    }

}
