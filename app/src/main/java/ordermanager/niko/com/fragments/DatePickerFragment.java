package ordermanager.niko.com.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import ordermanager.niko.com.R;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        DatePicker dp = new DatePicker(getActivity());
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);
        return new AlertDialog.Builder(getActivity())
                .setView(dp)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
