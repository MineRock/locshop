package site.com.wixsite.minebeast101.locshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class SellerHomeActivity extends AppCompatActivity {

    TextView welcomeUserTextView;
    Button button;
    Button button1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.options_menu_seller, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout_item) {
            ParseUser.logOutInBackground(e -> {
                if(e==null) {
                    Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(SellerHomeActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SellerHomeActivity.this, "Could not logout! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);

        button.setEnabled(false);
        button1.setEnabled(false);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(SellerHomeActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        button1.setOnClickListener(v -> {
            Intent intent = new Intent(SellerHomeActivity.this, SellerProductsActivity.class);
            startActivity(intent);
        });

        setTitle("Seller Homepage");

        welcomeUserTextView = findViewById(R.id.textView8);

        welcomeUserTextView.setText("Hello, " + getIntent().getStringExtra("username") + "!");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sellers");

        query.whereEqualTo("username", getIntent().getStringExtra("username"));

        query.whereExists("products");

        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                button.setEnabled(true);
                button1.setEnabled(true);
            } else if (objects.size() == 0) {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Sellers");

                parseQuery.whereEqualTo("username", getIntent().getStringExtra("username"));

                parseQuery.findInBackground((objects1, e12) -> {
                    if (objects1.size() == 1 && e12 == null) {
                        ParseObject selectedUser = objects1.get(0);

                        selectedUser.put("products", new ArrayList<>());

                        selectedUser.saveInBackground(e1 -> {
                            if (e1 == null) {
                                button.setEnabled(true);
                                button1.setEnabled(true);
                            } else {
                                Toast.makeText(SellerHomeActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(SellerHomeActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}