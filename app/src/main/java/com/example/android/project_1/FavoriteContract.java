package com.example.android.project_1;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by amr5aled on 3/10/2018.
 */

public class FavoriteContract {
    public static final String AUTHORITY = "com.example.android.project_1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String FAVORITE_PATH = "favorite";

    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITE_PATH).build();
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
    }
}