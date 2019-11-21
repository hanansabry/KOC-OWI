package com.android.kocowi.operator.well_day_data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.Well;
import com.android.kocowi.model.WellDailyData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddWellDataBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener, WellsDailyDataRepository.WellDailyDataInsertionCallback {

    private TextInputLayout whpTextInput, flpTextInput, tmpTextInput;
    private EditText whpEditText, flpEditText, tmpEditText;
    private ProgressBar progressBar;
    private Button doneButton;
    private AddWellDayDataPresenter mPresenter;
    private WellDailyData.WellStatus selectedWellStatus = WellDailyData.WellStatus.OPEN;
    private Well currentWell;

    public AddWellDataBottomFragment() {
    }

    public static AddWellDataBottomFragment getInstance(Well well) {
        AddWellDataBottomFragment fragment = new AddWellDataBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Well.class.getName(), well);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.well_daily_input_data_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new AddWellDayDataPresenter(this, Injection.provideWellsDataRepository());
        currentWell = (Well) getArguments().get(Well.class.getName());
        initializeView(view);
    }

    private void initializeView(View view) {
        whpTextInput = view.findViewById(R.id.whp_input_edittext);
        flpTextInput = view.findViewById(R.id.flp_input_edittext);
        tmpTextInput = view.findViewById(R.id.tmp_input_edittext);

        whpEditText = view.findViewById(R.id.whp_edit_text);
        whpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                whpTextInput.setError(null);
                whpTextInput.setErrorEnabled(false);
            }
        });
        flpEditText = view.findViewById(R.id.flp_edit_text);
        flpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                flpTextInput.setError(null);
                flpTextInput.setErrorEnabled(false);
            }
        });

        tmpEditText = view.findViewById(R.id.tmp_edit_text);
        tmpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tmpTextInput.setError(null);
                tmpTextInput.setErrorEnabled(false);
            }
        });

        RadioGroup wellStatus = view.findViewById(R.id.well_status_group);
        wellStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedWellStatus = checkedId == R.id.open ? WellDailyData.WellStatus.OPEN : WellDailyData.WellStatus.CLOSE;
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        doneButton = view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showProgressBar();
        WellDailyData wellDailyData = getWellInputDate();
        if (mPresenter.validateWellData(wellDailyData)) {
            mPresenter.addTodayWellData(wellDailyData, this);
        } else {
            hideProgressBar();
        }
    }

    private WellDailyData getWellInputDate() {
        WellDailyData dayData = new WellDailyData(
                whpEditText.getText().toString().isEmpty() ? 0 : Double.parseDouble(whpEditText.getText().toString().trim()),
                flpEditText.getText().toString().isEmpty() ? 0 : Double.parseDouble(flpEditText.getText().toString().trim()),
                tmpEditText.getText().toString().isEmpty() ? 0 : Double.parseDouble(tmpEditText.getText().toString().trim()),
                selectedWellStatus.name(),
                mPresenter.getCurrentDate(),
                mPresenter.getCurrentTime()
        );
        dayData.setWell(currentWell);
        return dayData;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);
    }

    public void setWhpInputTextErrorMessage() {
        whpEditText.setError("WHP can't be empty");
    }

    public void setFlpInputTextErrorMessage() {
        flpEditText.setError("FLP can't be empty");
    }

    public void setTempInputTextErrorMessage() {
        tmpEditText.setError("Temperature can't be empty");
    }

    @Override
    public void onSuccessfullyAddingNewWellData(WellDailyData well) {
        dismiss();
        Toast.makeText(getContext(), "Well data is added successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddingNewWellDataFailed(String err) {
        dismiss();
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }
}
