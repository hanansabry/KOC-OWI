package com.android.kocowi.backend.gc;

import com.android.kocowi.model.GC;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class GcRepositoryImpl implements GcRepository {

    private final DatabaseReference mDatabase;

    public GcRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("gcs");
    }

    @Override
    public void retrieveGcs(final GcRetrievingCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GC> gcList = new ArrayList<>();
                for (DataSnapshot gcSnapshot : dataSnapshot.getChildren()) {
                    GC gc = gcSnapshot.getValue(GC.class);
                    gc.setId(gcSnapshot.getKey());
                    gcList.add(gc);
                }
                callback.onRetrievingGcSuccessfully(gcList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievingGcFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addNewGc(final GC gc, final GcInsertionCallback callback) {
        String gcId = mDatabase.push().getKey();
        mDatabase.child(gcId).setValue(gc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccessfullyAddingNewGc(gc);
                } else {
                    callback.onAddingNewGcFailed(task.getException().getMessage());
                }
            }
        });
    }
}
