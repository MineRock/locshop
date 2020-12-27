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

public class SellerSignupActivity extends AppCompatActivity {

    EditText usernameEditText;
    String username;
    EditText passwordEditText;
    String password;
    EditText passwordConfirmationEditText;
    String passwordConfirmation;

    public void makeSellerAccount(View view) {
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
                                    ParseObject sellers = new ParseObject("Sellers");
                                    sellers.put("user", ParseUser.getCurrentUser());
                                    sellers.put("username", ParseUser.getCurrentUser().getUsername());
                                    sellers.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null) {
                                                Toast.makeText(SellerSignupActivity.this, "Sign up completed successfully! User " + ParseUser.getCurrentUser().getUsername() + " is now registered as a seller in the LocShop database!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(SellerSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (e.getMessage().equals("Account already exists for this username.")){
                                    Toast.makeText(SellerSignupActivity.this, "Account already exists! Please log in!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SellerSignupActivity.this, LoginActivity.class);
                                } else {
                                    Toast.makeText(SellerSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_seller_signup);

        setTitle("Seller Signup");

        usernameEditText = findViewById(R.id.editTextTextPersonName);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        passwordConfirmationEditText = findViewById(R.id.editTextTextPassword2);
    }
}