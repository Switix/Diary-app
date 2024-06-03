package com.maciej_switalski.projekt;

import android.provider.BaseColumns;

public class EntryContract {
    private EntryContract() {}

    public static final class EntryConst implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";
    }

}
