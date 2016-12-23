package com.tran.jakphotodemo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;


// ContentProvider is not necessary but I choose to use it to add an extra level of abstraction
public class PhotoProvider extends ContentProvider {

    // content provider
    static final String PROVIDER_NAME = "com.tran.jakphotodemo.database.PhotoProvider";
    static final String DIRECTORY = "/cpphotos";
    static final String URL = "content://" + PROVIDER_NAME + DIRECTORY;
    public static final Uri CONTENT_URL = Uri.parse(URL);

    // columns
    public static String UID = "_id";

    public static final String id = "id";
    public static final String albumId = "albumId";
    public static final String title = "title";
    public static final String url = "url";
    public static final String thumbnailUrl = "thumbnailUrl";

    static final int uriCode = 1;

    private static HashMap<String, String> values;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DIRECTORY, uriCode);
    }

    // database
    private SQLiteDatabase sqLiteDatabase;
    static final String DATABASE_NAME = "myPhotos";
    static final String TABLE_PHOTO = "photos";
    static final int DATABASE_VERSION = 1;

    // database init
    private static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_PHOTO + " ("
                    + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + id + " INTEGER, "
                    + albumId + " INTEGER, "
                    + title + " VARCHAR(255), "
                    + url + " VARCHAR(255), "
                    + thumbnailUrl + " VARCHAR(255));";

    @Override
    public boolean onCreate() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        if(sqLiteDatabase != null) return true;
        else return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_PHOTO);

        switch (uriMatcher.match(uri)) {

            case uriCode:
                sqLiteQueryBuilder.setProjectionMap(values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, strings, s, strings1, s1, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vdn.android.cursor.dir" + DIRECTORY;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        long rowID = sqLiteDatabase.insert(TABLE_PHOTO, null, contentValues);

        if(rowID > 0) {

            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        } else {
            // message failed
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        // TODO

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
            onCreate(sqLiteDatabase);
        }
    }
}
