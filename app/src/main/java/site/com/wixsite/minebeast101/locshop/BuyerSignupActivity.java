package site.com.wixsite.minebeast101.locshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class BuyerSignupActivity extends AppCompatActivity {

    EditText usernameEditText;
    String username;
    EditText passwordEditText;
    String password;
    EditText passwordConfirmationEditText;
    String passwordConfirmation;

    public void makeBuyerAccount(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        passwordConfirmation = passwordConfirmationEditText.getText().toString();
        if(!username.isEmpty()) {
            if(!password.isEmpty()) {
                if(!passwordConfirmation.isEmpty()) {
                    if(password.equals(passwordConfirmation)) {
                        ParseUser user = new ParseUser();

                        user.setUsername(username);
                        user.setPassword(password);

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null) {
                                    ParseObject buyers = new ParseObject("Buyers");
                                    buyers.put("user", ParseUser.getCurrentUser());
                                    buyers.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null) {
                                                Toast.makeText(BuyerSignupActivity.this, "Sign up completed successfully! User " + ParseUser.getCurrentUser().getUsername() + " is now registered as a buyer in the LocShop database!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else if (e.getMessage().equals("Account already exists for this username.")){
                                    Toast.makeText(BuyerSignupActivity.this, "Account already exists! Please log in!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(BuyerSignupActivity.this, LoginActivity.class);

                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "Password confirmation should be the same as the password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Password confirmation cannot be blank!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Password cannot be blank!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Username cannot be blank!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_signup);

        setTitle("Buyer Signup");

        usernameEditText = findViewById(R.id.editTextTextPersonName);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        passwordConfirmationEditText = findViewById(R.id.editTextTextPassword2);
    }
}