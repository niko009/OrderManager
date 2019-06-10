package ordermanager.niko.com.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import ordermanager.niko.com.ImageLab;
import ordermanager.niko.com.R;
import ordermanager.niko.com.adapter.GalleryAdapter;
import ordermanager.niko.com.order.Order;
import ordermanager.niko.com.order.OrderLab;
import ordermanager.niko.com.utils.PictureUtils;

public class OrderFragment extends Fragment {
    private static final String ARG_OREDER_ID = "order_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_PICK_PHOTO = 3;

    private File mPhotoFile;
    private Order mOrder;
    private ViewHolder mViewHolder;

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
        mPhotoFile = ImageLab.get(getActivity()).getTmpFile(mOrder.Name);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_order, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove_order:
                deleteOrder();

                return true;

            case R.id.menu_item_save_order:
                saveOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveOrder() {
        mOrder.Name = mViewHolder.getName();
        try {
            mOrder.Cost = Integer.parseInt(mViewHolder.getCost());
        } catch (Exception ex) {

        }
        if (OrderLab.get(getActivity()).updateOrder(mOrder) > 0)
            Toast.makeText(getActivity(), R.string.order_saved_successful, Toast.LENGTH_SHORT).show();


    }

    private void deleteOrder() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        OrderLab.get(getActivity()).deleteOrder(mOrder);
                        getActivity().finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.str_delete_order_dialog_title).setPositiveButton(R.string.str_delete_order_dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.str_delete_order_dialog_no, dialogClickListener).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        mViewHolder = new ViewHolder(v);
        mViewHolder.setOrder(mOrder);


        PackageManager packageManager = getActivity().getPackageManager();


        final Intent captureImage = new Intent
                (MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        };
        mViewHolder.setButtonVariable(canTakePhoto, listener);

     final   Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        mViewHolder.setPickButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivityForResult(photoPickerIntent, REQUEST_PICK_PHOTO);
            }
        });
        updatePhotoView();
        return v;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mViewHolder.setImage(null);
        } else {

            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mViewHolder.setImage(bitmap);
        }

        mViewHolder.updateAdapter();
    }

    public void returnResult() {
        Intent intent = new Intent();
        intent.putExtra(ARG_OREDER_ID, mOrder.getUuid());
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mOrder.StartTime = date;
            updateDate(mOrder.getStartTimeString());
        } else if (requestCode == REQUEST_PHOTO) {
            ImageLab.get(getActivity()).copyImage(mPhotoFile, mOrder.Name.replace(' ', '_'));
            updatePhotoView();
        }
        else
            if(requestCode==REQUEST_PICK_PHOTO){
                final Uri imageUri = data.getData();
              //  final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                ImageLab.get(getActivity()).copyImage(imageUri, mOrder.Name.replace(' ', '_'));
mViewHolder.updateAdapter();
            }
    }

    private void updateDate(String startTimeString) {
        mViewHolder.updateDate(startTimeString);
    }

    private class ViewHolder {
        private ImageButton mTakePhotoButton;
        private ImageButton mBrowsePhotoButton;
        private ImageView mPhotoView;
        private EditText medOrderName;
        private TextView mtvOrderStartTime;
        private TextView mtvOrderEndTime;
        private EditText mtvOrderCost;
        private TextView mtvOrderClient;
        private TextView mtvOrderCostString;

        private RecyclerView mRecyclerViewImages;
        private GalleryAdapter mGalleryAdapter;
        private View view;

        public ViewHolder(View view) {
            this.view = view;
            init();
        }

        private void init() {
            medOrderName = (EditText) view.findViewById(R.id.edName);

            mtvOrderStartTime = (TextView) view.findViewById(R.id.tvOrderStartTime);
            mtvOrderCost = (EditText) view.findViewById(R.id.tvCost);
            mtvOrderEndTime = (TextView) view.findViewById(R.id.tvOrderEndTime);

            mBrowsePhotoButton = (ImageButton) view.findViewById(R.id.ibOpenPhoto);
            mTakePhotoButton = (ImageButton) view.findViewById(R.id.ibTakePhoto);
            mPhotoView = (ImageView) view.findViewById(R.id.ivOrderPhoto);
            mtvOrderCostString = (TextView) view.findViewById(R.id.tvCostString);

            mtvOrderCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        int tmpCost = Integer.parseInt(charSequence.toString());
                        int profit = (int) ((tmpCost * 30.0f) / 100.0f);
                        int investments = tmpCost - profit;
                        mtvOrderCostString.setText(getString(R.string.str_order_cost) + " " + profit + " (" + investments + ")  ");
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            mGalleryAdapter = new GalleryAdapter(getActivity(), mOrder.Name);
            mRecyclerViewImages = (RecyclerView) view.findViewById(R.id.rvOrderImages);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerViewImages.setLayoutManager(mLayoutManager);
            mRecyclerViewImages.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewImages.setAdapter(mGalleryAdapter);

            mtvOrderEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = getFragmentManager();
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mOrder.StartTime);
                    dialog.setTargetFragment(OrderFragment.this, REQUEST_DATE);
                    dialog.show(manager, DIALOG_DATE);
                }
            });

            mRecyclerViewImages.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), mRecyclerViewImages, new GalleryAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    //   bundle.putSerializable("images", images);
                    bundle.putInt("position", position);
                    bundle.putString("order_name", mOrder.Name);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }

                @Override
                public void onLongClick(View view, int position) {
                        mViewHolder.setImage(PictureUtils.getScaledBitmap(mGalleryAdapter.getPath(position) , getActivity()));
                }
            }));
        }

        public void setOrder(Order order) {

            medOrderName.setText(order.Name);
            mtvOrderStartTime.setText(order.getStartTimeString());

            mtvOrderCost.setText(String.valueOf(order.Cost));
            try {
                int tmpCost = order.Cost;
                int profit = (int) ((tmpCost * 30.0f) / 100.0f);
                int investments = tmpCost - profit;
                mtvOrderCostString.setText(getString(R.string.str_order_cost) + " " + profit + " (" + investments + ")  ");
            } catch (Exception ex) {

            }


        }

        public void updateAdapter() {
            if (mGalleryAdapter != null)
                mGalleryAdapter.update();
        }

        public void updateDate(String startTimeString) {
            mtvOrderEndTime.setText(startTimeString);

        }

        public void setImage(Bitmap bitmap) {
            mPhotoView.setImageBitmap(bitmap);
        }

        public void setButtonVariable(boolean enabled, View.OnClickListener listener) {
            mTakePhotoButton.setEnabled(enabled);
            mTakePhotoButton.setOnClickListener(listener);
        }

        public void setPickButtonListener(View.OnClickListener listener){
            mBrowsePhotoButton.setOnClickListener(listener);
        }
        public String getName() {
            return medOrderName.getText().toString();
        }

        public String getCost() {
            return mtvOrderCost.getText().toString();
       }

    }
}
