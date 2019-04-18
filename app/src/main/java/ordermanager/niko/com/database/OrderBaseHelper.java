package ordermanager.niko.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ordermanager.niko.com.database.OrderDbSchema.OrderTable;

public class OrderBaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME="orderBase.db";

    public OrderBaseHelper(Context constext){
        super(constext,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + OrderTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                OrderTable.Cols.UUID + ", " +
                OrderTable.Cols.NAME + ", " +
                OrderTable.Cols.DESCRIPTION + ", " +
                OrderTable.Cols.COST + ", " +
                OrderTable.Cols.START_TIME + ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
