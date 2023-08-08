package com.shoesock.personalassistant1.interfaces;

import com.shoesock.personalassistant1.models.UserModel;

public interface OnUserFetchListener {
    void onUserFetchSuccess(UserModel user);
    void onUserFetchFailure();
}