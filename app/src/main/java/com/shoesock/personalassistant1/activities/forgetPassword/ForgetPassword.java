package com.shoesock.personalassistant1.activities.forgetPassword;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.password_utils.PasswordUtils;
import com.shoesock.personalassistant1.models.UserModel;

public class ForgetPassword extends AppCompatActivity {

    EditText firstName, lastName, phone, password;
    String userName, userLastName, userPhone, userPassword;
    Button resetPasswordButton;
    Context context;

    RealTimeDataBase realTimeDataBase;
    Functions functions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        context = this;
        setIds();
    } // close the onCreate function

    private void setIds() {
        firstName = findViewById(R.id.editTextUsername_forgetPassword);
        lastName = findViewById(R.id.editTextUserLastName_forgetPassword);
        phone = findViewById(R.id.editTextPhoneNumber_forgetPassword);
        password = findViewById(R.id.editTextPassword_forgetPassword);
        resetPasswordButton = findViewById(R.id.buttonRegister_forgetPassword);


        classesInit();
    } // close setIds function

    private void classesInit() {
        functions = new Functions(this);
        realTimeDataBase = new RealTimeDataBase();
        setPointer();
    } // close classesInit function

    private void setPointer() {
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = firstName.getText().toString();
                userLastName = lastName.getText().toString();
                userPhone = phone.getText().toString();
                userPassword = password.getText().toString();
                checkParams();
            }
        });


    } // close setPointer function

    private void checkParams() {
        if (!userName.isEmpty() || !userName.equals("")) {
            // userName not empty
            if (!userLastName.isEmpty() || !userLastName.equals("")) {
                // last name is not empty
                if (!userPhone.isEmpty() || !userPhone.equals("")) {
                    // phone is not empty
                    if (!userPassword.isEmpty() || userPassword.equals("")) {
                        // password is not empty
                        if (userPassword.length() < 6) {
                            password.setError(getString(R.string.passwordLength6Char));
                        } else {
                            // password length is more than 6
                            checkIfUserExist();
                        }
                    } else {
                        // password is empty
                        password.setError("שדה חובה");
                    }
                } else {
                    // phone is empty
                    phone.setError("שדה חובה");
                }
            } else {
                // last name is empty
                lastName.setError("שדה חובה");
            }
        } else {
            // userName is empty
            firstName.setError("שדה חובה");
        }
    } // close checkParams function


    private void checkIfUserExist() {
        String hashedUserName = PasswordUtils.hashString(userName, "");
        String hashedLastName = PasswordUtils.hashString(userLastName, "");
        realTimeDataBase.getUserByHashedUserName(hashedUserName, new RealTimeDataBase.OnUserFetchListener() {
            @Override
            public void onUserFetchSuccess(UserModel user) {
                if(hashedLastName.equals(user.getUserLastName())){
                    if (userPhone.equals(user.getUserPhone())){
                        // the first and last name and phone is ok
                        String hashedPassword = PasswordUtils.hashString(userPassword, user.getSlat());
                        realTimeDataBase.userPasswordUpdate(hashedUserName, hashedPassword);
                        functions.ToastFunction(context, "הסיסמה עודכנה בהצלחה!");
                    }else {
                        phone.setError(getString(R.string.userNotExist));
                    }
                }else {
                    lastName.setError(getString(R.string.userNotExist));
                }
            }

            @Override
            public void onUserFetchFailure() {
                firstName.setError(getString(R.string.userNotExist));
            }
        });


    } // check checkIfUserExist function


} // close ForgetPassword class
