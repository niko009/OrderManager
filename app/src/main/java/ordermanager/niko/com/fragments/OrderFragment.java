package ordermanager.niko.com.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

import ordermanager.niko.com.R;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;

public class OrderFragment extends Fragment {
    private static final String ARG_OREDER_ID = "order_id";
    private static final String DIALOG_DATE = "DialogDate";

    private Order mOrder;
    private TextView mtvOrderName;
private TextView mtvOrderStartTime;
private TextView mtvOrderEndTime;
private TextView mtvOrderCost;
private TextView mtvOrderClient;
    public static OrderFragment newInstance(UUID orderId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_OREDER_ID, orderId);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID orderId = (UUID) getArguments().getSerializable(ARG_OREDER_ID);
        mOrder = OrderLab.get(getActivity()).getOrder(orderId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        mtvOrderName = (TextView) v.findViewById(R.id.tvName);
        mtvOrderName.setText(mOrder.Name);
        mtvOrderStartTime = (TextView) v.findViewById(R.id.tvOrderStartTime);
        mtvOrderCost = (TextView) v.findViewById(R.id.tvCost);
        mtvOrderEndTime=(TextView)v.findViewById(R.id.tvOrderEndTime) ;
        mtvOrderEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(manager, DIALOG_DATE);
            }
        });
        return v;
    }


    public void returnResult() {
        Intent intent=new Intent();
        intent.putExtra(ARG_OREDER_ID,mOrder.getUuid());
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
