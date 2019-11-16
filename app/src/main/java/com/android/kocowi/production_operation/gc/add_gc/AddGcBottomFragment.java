package com.android.kocowi.production_operation.gc.add_gc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.kocowi.BaseView;
import com.android.kocowi.Injection;
import com.android.kocowi.R;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepository;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.FieldHeader;
import com.android.kocowi.model.GC;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddGcBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener, BaseView<AddGcPresenter>, GcRepository.GcInsertionCallback, FieldHeaderRepository.FieldHeaderRepositoryCallback {

    private AddGcPresenter mPresenter;
    private TextInputLayout codeTextInput, notesNameTextInput;
    private EditText codeEditText, notesNameEditText;
    private ProgressBar progressBar;
    private Button doneButton;
    private Spinner fieldHeaderSpinner;
    private String selectedFieldHeader;

    public AddGcBottomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_gc_bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new AddGcPresenter(this, Injection.provideGcUseCaseHandler());
        initializeView(view);
        mPresenter.retrieveFieldHeader(this);
    }

    private void initializeView(View view) {
        codeTextInput = view.findViewById(R.id.code_text_input_edittext);
        notesNameTextInput = view.findViewById(R.id.notes_text_input_edittext);


        codeEditText = view.findViewById(R.id.code_edit_text);
        codeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                codeTextInput.setError(null);
                codeTextInput.setErrorEnabled(false);
            }
        });
        notesNameEditText = view.findViewById(R.id.notes_edit_text);
        notesNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                notesNameTextInput.setError(null);
                notesNameTextInput.setErrorEnabled(false);
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        doneButton = view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    private void setupFiledHeaderSpinner(ArrayList<FieldHeader> fieldHeaders) {
        FieldHeaderAdapter fieldHeaderAdapter = new FieldHeaderAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        fieldHeaderAdapter.add("Field Header");

        for (FieldHeader fieldHeader : fieldHeaders) {
            fieldHeaderAdapter.add(fieldHeader.getName());
        }

        Spinner fieldHeaderSpinner = getView().findViewById(R.id.field_header_spinner);
        fieldHeaderSpinner.setAdapter(fieldHeaderAdapter);
        fieldHeaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fieldHeader = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    selectedFieldHeader = fieldHeader;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        showProgressBar();
        GC gc = getGcInputDate();
        if (mPresenter.validateUserData(gc)) {
            mPresenter.addNewGc(gc, this);
        } else {
            hideProgressBar();
        }
    }

    private GC getGcInputDate() {
        GC gc = new GC();
        gc.setCode(codeEditText.getText().toString().trim());
        gc.setNotes(notesNameEditText.getText().toString());
        gc.setFiledHeader(selectedFieldHeader);
        return gc;
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
    public void setPresenter(AddGcPresenter presenter) {
        mPresenter = presenter;
    }

    public void setCodeInputTextErrorMessage() {
        codeEditText.setError(getString(R.string.name_empty_err_msg));
    }

    public void setFieldHeaderErrorMessage(String s) {
        Toast.makeText(getContext(), "You must select Field Header", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfullyAddingNewGc(GC gc) {
        dismiss();
        Toast.makeText(getContext(), "GC is added successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddingNewGcFailed(String err) {
        dismiss();
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRetrievingFiledHeadersSuccessfully(ArrayList<FieldHeader> fieldheaders) {
        setupFiledHeaderSpinner(fieldheaders);
    }

    @Override
    public void onRetrievingFiledHeadersFailed(String err) {

    }
}
