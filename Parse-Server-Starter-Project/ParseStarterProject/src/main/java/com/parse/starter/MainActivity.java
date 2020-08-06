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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public boolean signUpModeActive = true;
    TextView loginText;

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
        }
    }

    public void SignUpClicked(View view) {

        EditText userName = (EditText) findViewById(R.id.txtUserName);
        EditText password = (EditText) findViewById(R.id.txtPassword);

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
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                //Login
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginText = (TextView) findViewById(R.id.txtLoginText);
        loginText.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}