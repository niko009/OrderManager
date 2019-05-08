package ordermanager.niko.com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ordermanager.niko.com.R;
import ordermanager.niko.com.activity.OrderPagerActivity;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;

public class OrderListFragment extends Fragment {
    private static final int REQUEST_ORDER = 1;

    private RecyclerView mOrderRecyclerView;
    private OrderAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container,
                false);
        mOrderRecyclerView = (RecyclerView) view
                .findViewById(R.id.order_recycler_view);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        return view;
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
}
