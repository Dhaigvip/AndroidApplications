/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    public boolean signUpModeActive = true;
    TextView loginText;
    EditText userName;
    EditText password;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            SignUpClicked(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtLoginText) {
            Button btn = findViewById(R.id.btnSignUp);
            if (signUpModeActive) {
                signUpModeActive = false;
                btn.setText("Login");
                loginText.setText("or, Sign up");
            } else {
                signUpModeActive = true;
                btn.setText("Sign up");
                loginText.setText("or, Login");
            }
        } else if (view.getId() == R.id.imgLogo || view.getId() == R.id.backgroundLayout) {
            //Hides the soft keyboard on click
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void SignUpClicked(View view) {
        if (userName.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "User name and password are required!", Toast.LENGTH_LONG).show();
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(userName.getText().toString());
            user.setPassword(password.getText().toString());

            if (signUpModeActive) {
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Success");
                            ShowUserList();
                        } else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                //Login
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            ShowUserList();
                            Log.i("Login", "Sucees");
                        } else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void ShowUserList() {
        Intent intent = new Intent(getApplicationContext(), UsersListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram");

        userName = findViewById(R.id.txtUserName);
        password = findViewById(R.id.txtPassword);
        ImageView logo = findViewById(R.id.imgLogo);
        RelativeLayout layout = findViewById(R.id.backgroundLayout);
        loginText = findViewById(R.id.txtLoginText);

        //Connect these control with OnClick implemented by this class.
        loginText.setOnClickListener(this);
        logo.setOnClickListener(this);
        layout.setOnClickListener(this);

        //Connect password control with OnKey implemented by this class.
        password.setOnKeyListener(this);
        if (ParseUser.getCurrentUser() != null) {
            ShowUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}