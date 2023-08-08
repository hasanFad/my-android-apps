package com.shoesock.personalassistant1.activities.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.password_functions.PasswordUtils;
import com.shoesock.personalassistant1.models.UserModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class RegisterNewUser extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword, editTextPhone;
    private Button btnRegister;
    Context context;
    Functions functions ;
    SharedPreferencesAssistant preferencesAssistant;
    private RealTimeDataBase realTimeDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new_user);
        context = this;

        setIds();

    }

    private void setIds() {
        editTextUserName = findViewById(R.id.editTextUsername_register);
        editTextPassword = findViewById(R.id.editTextPassword_register);
        editTextPhone = findViewById(R.id.editTextPhoneNumber_register);
        btnRegister = findViewById(R.id.buttonRegister_register);
        functions = new Functions(RegisterNewUser.this);
        realTimeDataBase = new RealTimeDataBase();
        preferencesAssistant = new SharedPreferencesAssistant(context);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheNewUserData();
            }
        });
    }

    private void checkTheNewUserData() {
        String userName = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            editTextUserName.setError(getString(R.string.insertYourName));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.insertYourPassword));
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.passwordLength6Char));
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError(getString(R.string.insertPhoneNumber));
            return;
        }

        // Add your code to save the new user to the database
        // For example, you can use Firebase Realtime Database or any other database system
        // After saving the user, you can redirect to the login page or any other activity as needed

        String saltPassword = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashString(password, saltPassword);
        String hashedUserName = PasswordUtils.hashString(userName, "");

        UserModel newUser = new UserModel(hashedUserName, saltPassword, userName, hashedPassword, phone);

        realTimeDataBase.registerNewUser(newUser, new RealTimeDataBase.OnListener() {
            @Override
            public void onSuccess() {
                preferencesAssistant.saveSharedPreferences("loginPreferences","userName", userName);

                Intent intent = new Intent(RegisterNewUser.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {

                editTextUserName.setBackgroundResource(R.drawable.red_edit_text_border);
                editTextPassword.setBackgroundResource(R.drawable.red_edit_text_border);
                editTextPhone.setBackgroundResource(R.drawable.red_edit_text_border);
                functions.ToastFunction(context, getString(R.string.registerProblem));
                functions.ToastFunction(context, errorMessage);

            }
        });




    } // close the checkTheNewUserData function


} // close the  RegisterNewUser class
