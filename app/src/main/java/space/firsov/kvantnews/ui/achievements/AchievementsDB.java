package space.firsov.kvantnews.ui.achievements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AchievementsDB {

    private static final String DATABASE_NAME = "achievement.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "achievements";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACHIEVEMENT = "achievement";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_ACHIEVEMENT = 1;
    private static int maxId = 0;

    private SQLiteDatabase mDataBase;

    public AchievementsDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String achievement) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ACHIEVEMENT, achievement);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }



    public ArrayList<Achievement> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Achievement> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String achievement = mCursor.getString(NUM_COLUMN_ACHIEVEMENT);
                arr.add(new Achievement(achievement));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ACHIEVEMENT+ " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}