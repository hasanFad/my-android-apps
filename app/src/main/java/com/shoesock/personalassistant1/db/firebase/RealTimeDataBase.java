package com.shoesock.personalassistant1.db.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoesock.personalassistant1.interfaces.OnUserFetchListener;
import com.shoesock.personalassistant1.models.ReminderModel;
import com.shoesock.personalassistant1.models.UserModel;


public class RealTimeDataBase {


    private DatabaseReference databaseUsersReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public RealTimeDataBase() {
        // Initialize the database reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseUsersReference = firebaseDatabase.getReference().child("users");
    }


    // login the user
    public void getUserByHashedUserName(String hashedUserName, OnUserFetchListener listener) {
        databaseUsersReference.orderByChild("hashedUserName").equalTo(hashedUserName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            UserModel user = userSnapshot.getValue(UserModel.class);
                            if (user != null) {
                                listener.onUserFetchSuccess(user);
                                return;
                            }
                        }
                        listener.onUserFetchFailure();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onUserFetchFailure();
                    }
                });


    }



    public interface OnUserFetchListener {
        void onUserFetchSuccess(UserModel user);

        void onUserFetchFailure();
    }




    public void registerNewUser(UserModel userModel, OnListener listener) {


        databaseUsersReference = database.getReference().child("users");
        databaseUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseUsersReference.child(userModel.getHashedUserName()).setValue(userModel);
                listener.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });

    }


    public void insertReminderToDB(ReminderModel reminderModel, OnListener listener) {
        databaseUsersReference = database.getReference().child("reminders");
        databaseUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseUsersReference.child(reminderModel.userName).setValue(reminderModel);
                listener.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });


    }




    public interface OnListener {
        void onSuccess();
        void onError(String errorMessage);
    } // close interface OnListener




} // close the class
