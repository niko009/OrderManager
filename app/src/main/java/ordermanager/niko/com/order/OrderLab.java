package ordermanager.niko.com.order;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ordermanager.niko.com.database.OrderBaseHelper;
import ordermanager.niko.com.database.OrderCursorWrapper;
import ordermanager.niko.com.database.OrderDbSchema.OrderTable;

public class OrderLab {
    private static OrderLab sOrderLab;
    private List<Order> mOrders;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static OrderLab get(Context context) {
        if (sOrderLab == null) {
            sOrderLab = new OrderLab(context);
        }
        return sOrderLab;
    }
public void testAddOrders(){
        for (int i =0;i<10;i++){
            Order order = new Order();
            order.Name = "order  "+ i;
            order.StartTime = new Date();
            order.Description = i+  "  description";
            addOrder(order);
        }
}
    private OrderLab(Context context) {
        mContext = context;
        mDatabase = new OrderBaseHelper(mContext).getWritableDatabase();
        mOrders = new ArrayList<>();
      /*  for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.Name = "Order #" + i;
            mOrders.add(order);
        }*/

    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        OrderCursorWrapper cursor = queryOrders(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orders.add(cursor.getOrder());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return orders;
    }


    public Order getOrder(UUID id) {
        OrderCursorWrapper cursor = queryOrders(OrderTable.Cols.UUID + " =?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getOrder();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.Cols.UUID, order.getUuid().toString());
        values.put(OrderTable.Cols.NAME, order.Name);
        values.put(OrderTable.Cols.DESCRIPTION, order.Description);
        values.put(OrderTable.Cols.COST, order.Cost);
        values.put(OrderTable.Cols.START_TIME, order.getStartTimeString());
        return values;
    }

    public void addOrder(Order order) {
        ContentValues values = getContentValues(order);
        mDatabase.insert(OrderTable.NAME, null, values);
    }

    public int updateOrder(Order order) {

        String uuidString = order.getUuid().toString();
        ContentValues values = getContentValues(order);
     return   mDatabase.update(OrderTable.NAME, values,
                OrderTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteOrder(Order order){
        String uuidString = order.getUuid().toString();
        mDatabase.delete(OrderTable.NAME,OrderTable.Cols.UUID + " = ?",new String[]{uuidString});
    }

    private OrderCursorWrapper queryOrders(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                OrderTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new OrderCursorWrapper(cursor);
    }
}
