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

                        user.signUpInBackground(e -> {
                            if(e==null) {
                                ParseObject buyers = new ParseObject("Buyers");
                                buyers.put("user", ParseUser.getCurrentUser());
                                buyers.put("username", ParseUser.getCurrentUser().getUsername());
                                buyers.saveInBackground(e1 -> {
                                    if(e1 ==null) {
                                        Toast.makeText(BuyerSignupActivity.this, "Sign up completed successfully! Please login now.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(BuyerSignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else if (Objects.equals(e.getMessage(), "Account already exists for this username.")){
                                Toast.makeText(BuyerSignupActivity.this, "Account already exists! Please log in!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BuyerSignupActivity.this, LoginActivity.class);

                                startActivity(intent);
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