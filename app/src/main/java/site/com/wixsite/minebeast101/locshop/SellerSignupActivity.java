package site.com.wixsite.minebeast101.locshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Objects;

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

                        user.signUpInBackground(e -> {
                            if(e==null) {
                                ParseObject sellers = new ParseObject("Sellers");
                                sellers.put("user", ParseUser.getCurrentUser());
                                sellers.put("username", ParseUser.getCurrentUser().getUsername());
                                sellers.saveInBackground(e1 -> {
                                    if(e1 ==null) {
                                        Toast.makeText(SellerSignupActivity.this, "Sign up completed successfully! Please login now.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SellerSignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SellerSignupActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (Objects.equals(e.getMessage(), "Account already exists for this username.")){
                                Toast.makeText(SellerSignupActivity.this, "Account already exists! Please log in!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerSignupActivity.this, LoginActivity.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(SellerSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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