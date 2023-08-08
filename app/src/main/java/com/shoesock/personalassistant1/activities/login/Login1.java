package com.shoesock.personalassistant1.activities.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.activities.register.RegisterNewUser;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.password_functions.PasswordUtils;
import com.shoesock.personalassistant1.interfaces.OnUserFetchListener;
import com.shoesock.personalassistant1.models.UserModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class Login1 extends Activity {

    private Context context;
    private EditText userNameET, userPasswordET;
    private   Button newUserBtn, forgetPasswordBtn, enterBtn;
    private String name, pass;
    private RealTimeDataBase realTimeDataBase;
    private Functions functions;
    private SharedPreferencesAssistant preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setIds();
    } // close the onCreate function

    // set the id for all
    private void setIds() {

        context = this;
        userNameET = findViewById(R.id.editTextUserName_login);
        userPasswordET = findViewById(R.id.editTextUserPassword_login);
        newUserBtn = findViewById(R.id.buttonNewUser_login);
        forgetPasswordBtn = findViewById(R.id.buttonForgetPassword_login);
        enterBtn = findViewById(R.id.buttonEnter_login);

        classesInit();

    } // close the setIds function

    private void classesInit() {
        realTimeDataBase = new RealTimeDataBase();
        preferences = new SharedPreferencesAssistant(context);
        functions = new Functions(Login1.this);

        setPointer();
    } // close the classesInit function

    private void setPointer() {

        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login1.this, RegisterNewUser.class);
                startActivity(intent);
                finish();
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTexts();
            }
        });

    } // close the setPointer function

    private void checkTexts() {

        name = userNameET.getText().toString().trim();
        pass = userPasswordET.getText().toString().trim();

        if (name.isEmpty() || pass.isEmpty()){

            userNameET.setError(getString(R.string.oneOfBoxsEmpty));
            userPasswordET.setError(getString(R.string.oneOfBoxsEmpty));

        } else if (pass.length() < 6) {
           // functions.ToastFunction(context, getString(R.string.passwordLength6Char));
            userPasswordET.setError(getString(R.string.passwordLength6Char));
        }

        // now will check the username and password from the db



        String hashedUserName = PasswordUtils.hashString(name, "");

// Retrieve the user record from the database using the hashedUserName.







    } // close the checkTexts function


} // close the Login class
