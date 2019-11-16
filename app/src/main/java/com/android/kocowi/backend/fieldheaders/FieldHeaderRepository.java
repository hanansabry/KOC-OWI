package com.android.kocowi.backend.fieldheaders;

import com.android.kocowi.model.FieldHeader;

import java.util.ArrayList;

public interface FieldHeaderRepository {

    interface FieldHeaderRepositoryCallback {
        void onRetrievingFiledHeadersSuccessfully(ArrayList<FieldHeader> fieldheaders);

        void onRetrievingFiledHeadersFailed(String err);
    }

    void retrieveFieldHeaders(FieldHeaderRepository.FieldHeaderRepositoryCallback callback);
}
