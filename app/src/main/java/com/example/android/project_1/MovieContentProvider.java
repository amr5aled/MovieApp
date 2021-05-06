package com.example.android.project_1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by amr5aled on 23/03/18.
 */

public class MovieContentProvider extends ContentProvider {
    private static final int FAVORITES = 100;
    private static final int FAVORITES_WITH_ID = 101;
    private FavoriteDbHelper mFavoriteDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private Context context;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.FAVORITE_PATH, FAVORITES);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.FAVORITE_PATH + "/#", FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        mFavoriteDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (match) {
            case FAVORITES:
                cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = "_id=?";
                selectionArgs = new String[]{id};
                cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        if (cursor == null)
            return null;
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri retUri;
        switch (match) {
            case FAVORITES:

                long id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(uri, id);
                } else
                    throw new SQLException("Failed to insert new row into: " + uri);

                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case FAVORITES:
                rowsDeleted = db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_WITH_ID:
                rowsDeleted = db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                Toast.makeText(context, "DEEEDDD", Toast.LENGTH_SHORT).show();
                context.getContentResolver().notifyChange(uri, null);
                if (rowsDeleted <= 0)
                    throw new SQLException("Failed to delete row into: " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case FAVORITES:
                rowsUpdated = db.update(FavoriteContract.FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = "_id=?";
                selectionArgs = new String[]{id};
                rowsUpdated = db.update(FavoriteContract.FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                if (!(rowsUpdated > 0))
                    throw new SQLException("Failed to update row into: " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);
        }

        return rowsUpdated;
    }
}
