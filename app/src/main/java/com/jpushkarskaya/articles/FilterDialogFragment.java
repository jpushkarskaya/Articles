package com.jpushkarskaya.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.parceler.Parcels;

/**
 * Created by epushkarskaya on 10/23/16.
 */
public class FilterDialogFragment extends DialogFragment {

    private ImageButton btnExit;
    private Button btnFilter;

    private DatePicker pickerBeginDate;
    private Spinner spinnerSort;
    private CheckBox checkboxArts;
    private CheckBox checkboxStyle;
    private CheckBox checkboxSports;

    FilterDialogListener listener;

    // empty constructor is required
    public FilterDialogFragment() {
        listener = null;
    }

    public void setFilterDialogListener(FilterDialogListener listener) {
        this.listener = listener;
    }

    public static FilterDialogFragment newInstance(Filter filter) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filter", Parcels.wrap(filter));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // handle buttons
        btnExit = (ImageButton) view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnFilter = (Button) view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new Filter();
                filter.setDate(pickerBeginDate.getYear(), pickerBeginDate.getMonth(), pickerBeginDate.getDayOfMonth());
                filter.setOrder(spinnerSort.getSelectedItemPosition());
                filter.setDesks(checkboxArts.isChecked(), checkboxStyle.isChecked(), checkboxSports.isChecked());
                listener.onFilter(filter);
                dismiss();
            }
        });

        // handle other views
        pickerBeginDate = (DatePicker) view.findViewById(R.id.pickerBeginDate);
        spinnerSort = (Spinner) view.findViewById(R.id.spinnerSort);
        checkboxArts = (CheckBox) view.findViewById(R.id.checkboxArts);
        checkboxStyle = (CheckBox) view.findViewById(R.id.checkboxStyle);
        checkboxSports = (CheckBox) view.findViewById(R.id.checkboxSports);

        Filter filter = Parcels.unwrap(getArguments().getParcelable("filter"));

        if (filter != null) {
            pickerBeginDate.updateDate(filter.getBeginYear(), filter.getBeginMonth(), filter.getBeginDay());
            spinnerSort.setSelection(filter.getSortOrder(), true);
            checkboxArts.setChecked(filter.isArtsChecked());
            checkboxStyle.setChecked(filter.isStyleChecked());
            checkboxSports.setChecked(filter.isSportsChecked());
        }

        // remove title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public interface FilterDialogListener {

        void onFilter(Filter filter);

    }

}
