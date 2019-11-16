package com.android.kocowi.production_operation.gc.add_gc;

import com.android.kocowi.BasePresenter;
import com.android.kocowi.backend.fieldheaders.FieldHeaderRepository;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.GC;
import com.android.kocowi.usecase.GcUseCaseHandler;

public class AddGcPresenter implements BasePresenter {

    private final AddGcBottomFragment addGcBottomFragment;
    private final GcUseCaseHandler useCaseHandler;

    public AddGcPresenter(AddGcBottomFragment view, GcUseCaseHandler useCaseHandler) {
        this.useCaseHandler = useCaseHandler;
        addGcBottomFragment = view;
        addGcBottomFragment.setPresenter(this);
    }

    public boolean validateUserData(GC gc) {
        boolean validate = true;
        if (gc.getCode() == null || gc.getCode().isEmpty()) {
            validate = false;
            addGcBottomFragment.setCodeInputTextErrorMessage();
        }

        if (gc.getFiledHeader() == null || gc.getFiledHeader().isEmpty()) {
            validate = false;
            addGcBottomFragment.setFieldHeaderErrorMessage("Password can't be empty");
        }

        return validate;
    }

    public void addNewGc(GC gc, GcRepository.GcInsertionCallback callback) {
        useCaseHandler.addNewGc(gc, callback);
    }

    public void retrieveFieldHeader(FieldHeaderRepository.FieldHeaderRepositoryCallback callback) {
        useCaseHandler.retrieveFieldHeaders(callback);
    }

    @Override
    public void start() {

    }
}
