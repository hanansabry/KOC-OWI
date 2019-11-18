package com.android.kocowi.production_operation.wells.add_well;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.kocowi.BaseView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.wells.WellsRepository;
import com.android.kocowi.model.Well;
import com.android.kocowi.production_operation.wells.WellsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddWellBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener, BaseView<AddWellPresenter>, WellsRepository.WellInsertionCallback {

    private AddWellPresenter mPresenter;
    private TextInputLayout nameTextInput, notesTextInput, longitudeTextInput, latitudeTextInput;
    private EditText nameEditText, notesEditText, longitudeEditText, latitudeEditText;
    private ProgressBar progressBar;
    private Button doneButton;
    private Spinner fieldHeaderSpinner;
    private String selectedFieldHeader;

    public AddWellBottomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_well_bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new AddWellPresenter(this, Injection.provideWellsRepository());
        initializeView(view);
    }

    private void initializeView(View view) {
        nameTextInput = view.findViewById(R.id.name_text_input_edittext);
        notesTextInput = view.findViewById(R.id.notes_text_input_edittext);
        latitudeTextInput = view.findViewById(R.id.latitude_text_input_edittext);
        longitudeTextInput = view.findViewById(R.id.longitude_text_input_edittext);


        nameEditText = view.findViewById(R.id.name_edit_text);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextInput.setError(null);
                nameTextInput.setErrorEnabled(false);
            }
        });
        notesEditText = view.findViewById(R.id.notes_edit_text);
        notesEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                notesTextInput.setError(null);
                notesTextInput.setErrorEnabled(false);
            }
        });

        latitudeEditText = view.findViewById(R.id.latitude_edit_text);
        latitudeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                latitudeTextInput.setError(null);
                latitudeTextInput.setErrorEnabled(false);
            }
        });

        longitudeEditText = view.findViewById(R.id.longitude_edit_text);
        longitudeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                longitudeTextInput.setError(null);
                longitudeTextInput.setErrorEnabled(false);
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        doneButton = view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showProgressBar();
        Well well = getWellInputDate();
        if (mPresenter.validateUserData(well)) {
            mPresenter.addNewWell(well, this);
        } else {
            hideProgressBar();
        }
    }

    private Well getWellInputDate() {
        Well well = new Well();
        well.setName(nameEditText.getText().toString().trim());
        well.setNotes(notesEditText.getText().toString());
        well.setLongitude(longitudeEditText.getText().toString().isEmpty() ? 0
                : Double.parseDouble(longitudeEditText.getText().toString().trim()));
        well.setLatitude(latitudeEditText.getText().toString().isEmpty() ? 0
                : Double.parseDouble(latitudeEditText.getText().toString().trim()));
        well.setGcCode(((WellsActivity) getActivity()).getCurrentGc().getCode());
        return well;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(AddWellPresenter presenter) {
        mPresenter = presenter;
    }

    public void setNameInputTextErrorMessage() {
        nameEditText.setError(getString(R.string.name_empty_err_msg));
    }

    public void setLatitudeInputTextErrorMessage() {
        latitudeEditText.setError("Latitude can't be empty");
    }

    public void setLongitudeInputTextErrorMessage() {
        longitudeEditText.setError("Longitude can't be empty");
    }


    @Override
    public void onSuccessfullyAddingNewWell(Well well) {
        dismiss();
        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , "New Well is added successfully", Snackbar.LENGTH_LONG);
    }

    @Override
    public void onAddingNewWellFailed(String err) {
        dismiss();
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }
}
