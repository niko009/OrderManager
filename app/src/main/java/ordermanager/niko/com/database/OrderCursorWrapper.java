package ordermanager.niko.com.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import ordermanager.niko.com.database.OrderDbSchema.OrderTable;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.utils.Utils;

public class OrderCursorWrapper extends CursorWrapper {

    public OrderCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Order getOrder() {
        String uuidString = getString(getColumnIndex(OrderTable.Cols.UUID));
        String name = getString(getColumnIndex(OrderTable.Cols.NAME));
        String description = getString(getColumnIndex(OrderTable.Cols.DESCRIPTION));
        int cost = getInt(getColumnIndex(OrderTable.Cols.COST));
        String startDate=getString(getColumnIndex(OrderTable.Cols.START_TIME));

        Order order = new Order(UUID.fromString(uuidString));
        order.Cost=cost;
        order.Description= description;
        order.Name=name;

        order.StartTime= Utils.getDate("yyyyMMdd  kk:mm",startDate);
        return order;
    }
}
