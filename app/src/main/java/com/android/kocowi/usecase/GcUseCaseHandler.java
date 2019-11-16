package com.android.kocowi.usecase;

import com.android.kocowi.backend.fieldheaders.FieldHeaderRepository;
import com.android.kocowi.backend.gc.GcRepository;
import com.android.kocowi.model.GC;

public class GcUseCaseHandler {

    private final FieldHeaderRepository fieldHeaderRepository;
    private final GcRepository gcRepository;

    public GcUseCaseHandler(FieldHeaderRepository fieldHeaderRepository, GcRepository gcRepository) {
        this.fieldHeaderRepository = fieldHeaderRepository;
        this.gcRepository = gcRepository;
    }

    public void retrieveFieldHeaders(FieldHeaderRepository.FieldHeaderRepositoryCallback callback) {
        fieldHeaderRepository.retrieveFieldHeaders(callback);
    }

    public void addNewGc(GC gc, GcRepository.GcInsertionCallback callback) {
        gcRepository.addNewGc(gc, callback);
    }

    public void retrieveGcs(GcRepository.GcRetrievingCallback callback) {
        gcRepository.retrieveGcs(callback);
    }
}
