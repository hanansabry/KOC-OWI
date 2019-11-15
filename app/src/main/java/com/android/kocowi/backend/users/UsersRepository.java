package com.android.kocowi.backend.users;

import com.android.kocowi.model.User;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    void insertNewUser(User user, UserInsertionCallback callback);
}
