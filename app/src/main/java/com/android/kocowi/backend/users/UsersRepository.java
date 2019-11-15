package com.android.kocowi.backend.users;

import com.android.kocowi.model.ProductionOperation;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(ProductionOperation user, UserInsertionCallback callback);
}
