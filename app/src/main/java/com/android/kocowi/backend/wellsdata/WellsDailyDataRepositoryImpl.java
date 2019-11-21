package com.android.kocowi.backend.wellsdata;

import com.android.kocowi.model.WellDailyData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class WellsDailyDataRepositoryImpl implements WellsDailyDataRepository {

    private final static String WELLS_DAILY_DATA = "wells_daily_data";
    private final DatabaseReference mDatabase;

    public WellsDailyDataRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(WELLS_DAILY_DATA);
    }

    @Override
    public void retrieveAllWellsData(final WellsDataRetrievingCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<WellDailyData> wellsData = new ArrayList<>();
                for (DataSnapshot wellDailyDataSnapshot : dataSnapshot.getChildren()) {
                    WellDailyData welldata = wellDailyDataSnapshot.getValue(WellDailyData.class);
                    welldata.setId(wellDailyDataSnapshot.getKey());
                    wellsData.add(welldata);
                }
                callback.onRetrievingWellsDaraSuccessfully(wellsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingWellsDataFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addNewWellDailyData(final WellDailyData well, final WellDailyDataInsertionCallback callback) {
        String wellId = mDatabase.push().getKey();
        mDatabase.child(wellId).setValue(well).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccessfullyAddingNewWellData(well);
                } else {
                    callback.onAddingNewWellDataFailed(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void approveWellData(String wellDataId, boolean checked) {
        mDatabase.child(wellDataId).child("approved").setValue(checked);
    }
}
