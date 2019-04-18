package ordermanager.niko.com.order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ordermanager.niko.com.database.OrderBaseHelper;

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

    private OrderLab(Context context) {
        mContext=context;
        mDatabase=new OrderBaseHelper(mContext).getWritableDatabase();
        mOrders = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.Name ="Order #" + i;
            mOrders.add(order);
        }
    }

    public List<Order> getOrders() {
        return mOrders;
    }

    public Order getOrder(UUID id) {
        for (Order order : mOrders) {
            if (order.getUuid().equals(id)) {
                return order;
            }
        }
        return null;
    }



}
