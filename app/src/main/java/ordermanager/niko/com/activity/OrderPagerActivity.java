package ordermanager.niko.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

import ordermanager.niko.com.R;
import ordermanager.niko.com.fragments.OrderFragment;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;

public class OrderPagerActivity extends FragmentActivity {
    public static final String EXTRA_ORDER_ID =
            "ordermanager.niko.com.order_id";
    private ViewPager mViewPager;
    private List<Order> mOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pager);
        UUID orderId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ORDER_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_order_pager_view_pager);
        mOrders = OrderLab.get(this).getOrders();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Order order = mOrders.get(position);
                return OrderFragment.newInstance(order.getUuid());
            }

            @Override
            public int getCount() {
                return mOrders.size();
            }
        });
        for (int i = 0; i < mOrders.size(); i++) {
            if (mOrders.get(i).getUuid().equals(orderId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID orderId) {
        Intent intent = new Intent(packageContext, OrderPagerActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }
}
