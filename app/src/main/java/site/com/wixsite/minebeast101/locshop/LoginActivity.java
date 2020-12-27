package site.com.wixsite.minebeast101.locshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    String username;
    EditText passwordEditText;
    String password;
    Switch userSwitch;
    boolean isSeller = false;
    Button logoutButton;
    String selectedRole;
    String trueRole;

    public void login(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (!username.isEmpty() && !password.isEmpty()) {
            if (!isSeller) {
                selectedRole = "Buyer";
            } else {
                selectedRole = "Seller";
            }

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            ParseQuery<ParseObject> sellerQuery = ParseQuery.getQuery("Sellers");
            sellerQuery.whereEqualTo("username", username);

            sellerQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        trueRole = "Seller";
                        if (selectedRole.equals(trueRole)) {
                            ParseUser.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null) {
                                        logoutButton.setAlpha(1);
                                        Toast.makeText(LoginActivity.this, "Login successful! You are now signed in as " + username + ", an authorized " + selectedRole + ".", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry! You are not a " + selectedRole + " but a " + trueRole + ". Please change your login option accordingly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");
            buyerQuery.whereEqualTo("username", username);

            buyerQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        trueRole = "Buyer";
                        if (selectedRole.equals(trueRole)) {
                            ParseUser.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null) {
                                        logoutButton.setAlpha(1);
                                        Toast.makeText(LoginActivity.this, "Login successful! You are now signed in as " + username + ", an authorized " + selectedRole + ".", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry! You are not a " + selectedRole + " but a " + trueRole + ". Please change your login option accordingly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        } else {
            Toast.makeText(this, "Username/Password cannot be blank!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        logoutButton = findViewById(R.id.button2);
        logoutButton.setAlpha(0);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            logoutButton.setAlpha(0);
                            Toast.makeText(LoginActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        usernameEditText = findViewById(R.id.editTextTextPersonName);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        userSwitch = findViewById(R.id.switch1);

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        isSeller = userSwitch.getShowText();

        userSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSeller = isChecked;
            }
        });
    }
}