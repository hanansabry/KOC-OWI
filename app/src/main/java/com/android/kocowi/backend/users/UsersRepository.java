package com.android.kocowi.backend.users;

import com.android.kocowi.model.User;

public interface UsersRepository {

    interface UserInsertionCallback {
        void onUserInsertedSuccessfully();

        void onUserInsertedFailed(String errmsg);
    }

    interface UsersRetrievingCallback {
        void onUserRetrievedSuccessfully(User user);

        void onUserRetrievedFailed(String err);
    }

    void insertNewUser(User user, UserInsertionCallback callback);

    void getCurrentUserData(UsersRetrievingCallback callback);
}
