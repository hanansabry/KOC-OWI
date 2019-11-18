package com.android.kocowi.backend.wells;

import com.android.kocowi.model.Well;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class WellsRepositoryImpl implements WellsRepository {

    private final DatabaseReference mDatabase;

    public WellsRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("wells");
    }

    @Override
    public void retrieveWells(String gcCode, final WellsRetrievingCallback callback) {
        mDatabase.orderByChild("gcCode").equalTo(gcCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Well> wellList = new ArrayList<>();
                for (DataSnapshot gcSnapshot : dataSnapshot.getChildren()) {
                    Well well = gcSnapshot.getValue(Well.class);
                    well.setId(gcSnapshot.getKey());
                    wellList.add(well);
                }
                callback.onRetrievingWellsSuccessfully(wellList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingWellsFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addNewWell(final Well well, final WellInsertionCallback callback) {
        String wellId = mDatabase.push().getKey();
        mDatabase.child(wellId).setValue(well).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccessfullyAddingNewWell(well);
                } else {
                    callback.onAddingNewWellFailed(task.getException().getMessage());
                }
            }
        });
    }
}
