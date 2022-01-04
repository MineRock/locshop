package site.com.wixsite.minebeast101.locshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    String username;
    EditText passwordEditText;
    String password;
    Switch userSwitch;
//    TODO: Change to material Switch in UI overhaul
    boolean isSeller = false;
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

            sellerQuery.findInBackground((objects, e) -> {
                if (e == null && objects.size() > 0) {
                    trueRole = "Seller";
                    if (selectedRole.equals(trueRole)) {
                        ParseUser.logInInBackground(username, password, (user, e1) -> {
                            if (e1 == null) {
                                Intent intent = new Intent(LoginActivity.this, SellerHomeActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "You are a " + trueRole + ", not a " + selectedRole + ".", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");
            buyerQuery.whereEqualTo("username", username);

            buyerQuery.findInBackground((objects, e) -> {
                if (e == null && objects.size() > 0) {
                    trueRole = "Buyer";
                    if (selectedRole.equals(trueRole)) {
                        ParseUser.logInInBackground(username, password, (user, e12) -> {
                            if (e12 == null) {
                                Intent intent = new Intent(LoginActivity.this, BuyerHomeActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, e12.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Sorry! You are not a " + selectedRole + " but a " + trueRole + ". Please change your login option accordingly.", Toast.LENGTH_SHORT).show();
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

        usernameEditText = findViewById(R.id.editTextTextPersonName);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        userSwitch = findViewById(R.id.switch1);

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        isSeller = userSwitch.getShowText();

        userSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> isSeller = isChecked);
    }
}