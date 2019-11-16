package com.android.kocowi.backend.fieldheaders;

import com.android.kocowi.model.FieldHeader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class FieldHeaderRepositoryImpl implements FieldHeaderRepository {
    
    private final DatabaseReference mDatabase;

    public FieldHeaderRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("fieldheaders");
    }

    @Override
    public void retrieveFieldHeaders(final FieldHeaderRepository.FieldHeaderRepositoryCallback callback) {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FieldHeader> fieldHeaders = new ArrayList<>();
                for(DataSnapshot fieldHeaderSnapshot : dataSnapshot.getChildren()) {
                    FieldHeader fieldHeader = fieldHeaderSnapshot.getValue(FieldHeader.class);
                    fieldHeader.setCode(fieldHeaderSnapshot.getKey());
                    fieldHeaders.add(fieldHeader);
                }
                callback.onRetrievingFiledHeadersSuccessfully(fieldHeaders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingFiledHeadersFailed(databaseError.getMessage());
            }
        });
    }
}
