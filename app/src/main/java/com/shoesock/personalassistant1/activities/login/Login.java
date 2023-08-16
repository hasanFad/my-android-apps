package com.shoesock.personalassistant1.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.activities.register.RegisterNewUser;
import com.shoesock.personalassistant1.activities.splashScreen.SplashScreen;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.password_functions.PasswordUtils;
import com.shoesock.personalassistant1.interfaces.OnUserFetchListener;
import com.shoesock.personalassistant1.models.UserModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class Login extends AppCompatActivity {

    private Context context;

    private EditText userNameET, userPasswordET;
    private Button newUserBtn, forgetPasswordBtn, sendBtn;

    private String userName, userPassword;

    private RealTimeDataBase realTimeDataBase;
    private Functions functions;
    private SharedPreferencesAssistant preferencesAssistant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setIds();
    } // close onCreate function

    private void setIds() {
        context = this;
        userNameET = findViewById(R.id.editTextUserName_login);
        userPasswordET = findViewById(R.id.editTextUserPassword_login);
        newUserBtn = findViewById(R.id.buttonNewUser_login);
        forgetPasswordBtn = findViewById(R.id.buttonForgetPassword_login);
        sendBtn = findViewById(R.id.buttonEnter_login);

        classesInit();
    } // close setIds function

    private void classesInit() {
        realTimeDataBase = new RealTimeDataBase();
        preferencesAssistant = new SharedPreferencesAssistant(context);
        functions = new Functions(Login.this);

        setPointer();
    } // close classesInit function

    private void setPointer() {
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegisterNewUser.class);
                startActivity(intent);
                finish();
            }
        }); // close newUserBtn.setOnClickListener

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTexts();

            }
        }); // close sendBtn.setOnClickListener

    } // close setPointer function

    private void checkTexts() {
        userName = userNameET.getText().toString();
        userPassword = userPasswordET.getText().toString();
        if (!userName.isEmpty()) {
            // username not empty
            if (!userPassword.isEmpty()) {
                // useName and password not empty will check from DB
                if (userPassword.length() >= 6) {
                    // userPassword is ok
                    CheckingUserExists();
                } else {
                    // userPassword not ok
                    userPasswordET.setError(getString(R.string.passwordLength6Char));
                }
            } else {
                // userPassword is empty
                userPasswordET.setError(getString(R.string.thisFeildEmpty));

            }
        } else {
            // username is empty
            userNameET.setError(getString(R.string.thisFeildEmpty));
        }

    } // close checkTexts function

    private void CheckingUserExists() {
        String hashedUserName = PasswordUtils.hashString(userName, "");

        // Fetch the user by hashedUserName
        realTimeDataBase.getUserByHashedUserName(hashedUserName, new RealTimeDataBase.OnUserFetchListener() {
            @Override
            public void onUserFetchSuccess(UserModel user) {
                // User found, verify the password
                String hashedPassword = PasswordUtils.hashString(userPassword, user.getSlat());
                if (hashedPassword.equals(user.getUserHashedPassword())) {
                    // Password matches, login successful, handle accordingly (e.g., navigate to MainActivity)
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();


                    preferencesAssistant.saveSharedPreferences("loginPreferences","userName", userName);
                   Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Password does not match, handle accordingly (e.g., display an error message)
                    Toast.makeText(context, getString(R.string.passwordNotOk), Toast.LENGTH_SHORT).show();
                    userPasswordET.setError(getString(R.string.passwordNotOk));
                }
            }

            @Override
            public void onUserFetchFailure() {
                // User not found, handle accordingly (e.g., display an error message)
                userNameET.setError(getString(R.string.userNotExist));
            }




        });


    } // close CheckingUserExists function









} // close Login class
