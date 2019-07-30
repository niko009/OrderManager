package ordermanager.niko.com.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ordermanager.niko.com.R;
import ordermanager.niko.com.activity.OrderPagerActivity;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;

public class OrderListFragment extends Fragment {
    private static final int REQUEST_ORDER = 1;

    private final int CAMERA_PERMISSION_ID = 1;
    private RecyclerView mOrderRecyclerView;
    private OrderAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            CheckPermissions();
    }
  /*  private void requestPermission(String permission, int requestCode) {
        // запрашиваем разрешение
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
    }

    private boolean isPermissionGranted(String permission) {
        int permissionCheck = ActivityCompat.checkSelfPermission(getActivity(), permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }*/

    private void CheckPermissions() {
        String[] needAscPermArray;
        List<String> needAscPerms = new ArrayList<>();
        String[] perms = retrievePermissions(getActivity());
        for (String perm : perms) {
            if (!checkPermission(perm)) {
               // DataWorks.LogE(TAG, "Need Ask " + perm);
                needAscPerms.add(perm);
            }
        }

        if (needAscPerms.size() > 0) {
            needAscPermArray = new String[needAscPerms.size()];
            needAscPerms.toArray(needAscPermArray);
            ActivityCompat.requestPermissions(getActivity(), needAscPermArray, 1);
        }

    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getActivity(), permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public static String[] retrievePermissions(Context context) {
        try {
            return context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                    .requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            return new String[0];
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container,
                false);
        mOrderRecyclerView = (RecyclerView) view
                .findViewById(R.id.order_recycler_view);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        FloatingActionButton fb= (FloatingActionButton)view.findViewById(R.id.fabAddNewOrder);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewOrderClick();

            }
        });



        updateUI();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        OrderLab orderLab = OrderLab.get(getActivity());
        List<Order> orders = orderLab.getOrders();
        if (mAdapter == null) {
            mAdapter = new OrderAdapter(orders);
            mOrderRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }


    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
        private List<Order> mOrders;

        public OrderAdapter(List<Order> orders) {
            mOrders = orders;
        }

        @NonNull
        @Override
        public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_order, parent, false);
            GradientDrawable gd = new GradientDrawable();
            gd.setColors(new int[]{
                    Color.RED,
                    Color.GREEN

            });
            gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            gd.setGradientType(GradientDrawable.RADIAL_GRADIENT) ;
gd.setShape(GradientDrawable.RECTANGLE);
            gd.setGradientRadius(640.0f);
            gd.mutate();
            gd.setGradientCenter(.9f,.5f);
           // view.setBackgroundDrawable(gd);

            return new OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
            Order order = mOrders.get(position);
            holder.bindOrder(order);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }
    }

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mOrderName;
        public TextView mOrderCost;
        public TextView mOrderStartTime;
        public View mView;
        public Order mOrder;

        public OrderHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
            initViews();
        }

        private void initViews() {
            mOrderName = (TextView) mView.findViewById(R.id.tvOrderListName);
            mOrderCost = (TextView) mView.findViewById(R.id.tvOrderListCost);
            mOrderStartTime = (TextView) mView.findViewById(R.id.tvOrderListStartTime);
        }

        public void bindOrder(Order order) {
            mOrder = order;
            mOrderName.setText(order.Name);
            mOrderCost.setText(String.valueOf(order.Cost));
            mOrderStartTime.setText(order.getStartTimeString());
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mOrderName.getText().toString(), Toast.LENGTH_LONG).show();
            Intent intent = OrderPagerActivity.newIntent(getActivity(),
                    mOrder.getUuid());
            startActivityForResult(intent, REQUEST_ORDER);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ORDER) {
// Обработка результата
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_order_list, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_order:
                addNewOrderClick();
                return true;
            case R.id.menu_item_find_order:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewOrderClick(){
        Order order = new Order();
        OrderLab.get(getActivity()).addOrder(order);
        Intent intent = OrderPagerActivity
                .newIntent(getActivity(), order.getUuid());
        startActivity(intent);
    }
    private void updateSubtitle() {
        OrderLab orderLab= OrderLab.get(getActivity());
        int orderCount = orderLab.getOrders().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural,orderCount, orderCount);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
