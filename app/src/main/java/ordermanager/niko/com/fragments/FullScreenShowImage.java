package ordermanager.niko.com.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import ordermanager.niko.com.R;
import ordermanager.niko.com.activity.OrderPagerActivity;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;

public class FullScreenShowImage extends AppCompatActivity {
    public static final String EXTRA_ORDER_NAME =
            "ordermanager.niko.com.order_name";
    private ViewPager mViewPager;
    private List<Order> mOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_slider);
        String orderName =   getIntent().getSerializableExtra(EXTRA_ORDER_NAME).toString();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
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
            if (mOrders.get(i).getUuid().equals(orderName)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID orderId) {
        Intent intent = new Intent(packageContext, OrderPagerActivity.class);
        intent.putExtra(EXTRA_ORDER_NAME, orderId);
        return intent;
    }
}
