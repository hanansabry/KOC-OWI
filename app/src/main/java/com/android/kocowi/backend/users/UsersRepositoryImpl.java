package com.android.kocowi.backend.users;

import com.android.kocowi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class UsersRepositoryImpl implements UsersRepository {

    private static final String ROLES = "roles";
    public static String USERS_COLLECTION = "users";
    private final FirebaseDatabase mDatabase;

    public UsersRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void insertNewUser(User user, final UserInsertionCallback callback) {
        String userId = user.getId();
        mDatabase.getReference(USERS_COLLECTION).child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onUserInsertedSuccessfully();
                } else {
                    callback.onUserInsertedFailed(task.getException().getMessage());
                }
            }
        });

        HashMap<String, Object> userRoles = new HashMap<>();
        userRoles.put(userId, user.getRole().name());
        mDatabase.getReference(ROLES).updateChildren(userRoles);
    }
}
