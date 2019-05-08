package ordermanager.niko.com.activity;

import android.support.v4.app.Fragment;

import ordermanager.niko.com.fragments.OrderListFragment;

public class OrderListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {

        return new OrderListFragment();
    }
}
