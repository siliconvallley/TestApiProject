package com.dh.testproject.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    private static final String AUTHORITIES = "com.dh.MyContentProvider";
    private static final String USER = "user";
    private static final int USER_CODE = 1;
    private static UriMatcher uriMatcher;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;
    private static final String TAG = "MyContentProvider";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, USER, USER_CODE);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        db.execSQL("delete from " + USER);
        db.execSQL("insert into " + USER + " values(1, '张三')");
        db.execSQL("insert into " + USER + " values(2, '李四')");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        return db.query(getTabName(uri), projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        db.insert(getTabName(uri), null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delete = db.delete(getTabName(uri), selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int update = db.update(getTabName(uri), values, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return update;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (extras == null) return null;
        String call = extras.getString("call");
        Log.e(TAG, "method: " + method + "++++>arg: " + arg + "++++>call: " + call);
        return super.call(method, arg, extras);
    }

    private String getTabName(Uri uri) {
        String tabName = null;
        switch (uriMatcher.match(uri)) {
            case USER_CODE:
                tabName = USER;
                break;
        }
        return tabName;
    }
}
