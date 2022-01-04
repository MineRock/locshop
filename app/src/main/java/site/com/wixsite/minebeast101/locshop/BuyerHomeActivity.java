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

public class BuyerHomeActivity extends AppCompatActivity {

    TextView welcomeUserTextView;
    Button button;
    Button button1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.options_menu_buyer, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout_item) {
            ParseUser.logOutInBackground(e -> {
                if(e==null) {
                    Intent intent = new Intent(BuyerHomeActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(BuyerHomeActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BuyerHomeActivity.this, "Could not logout! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if(item.getItemId() == R.id.cart_item) {
            Intent intent = new Intent(BuyerHomeActivity.this, CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        setTitle("Buyer Homepage");

        welcomeUserTextView = findViewById(R.id.textView13);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        welcomeUserTextView.setText("Hello, " + getIntent().getStringExtra("username") + "!");

        button.setEnabled(false);
        button1.setEnabled(false);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(BuyerHomeActivity.this, BuyProductActivity.class);
            startActivity(intent);
        });

        button1.setOnClickListener(v -> {
            Intent intent = new Intent(BuyerHomeActivity.this, BuyerOrdersActivity.class);
            startActivity(intent);
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Buyers");

        query.whereEqualTo("username", getIntent().getStringExtra("username"));

        query.whereExists("cart");

        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                button.setEnabled(true);
                button1.setEnabled(true);
            } else if (objects.size() == 0) {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Buyers");

                parseQuery.whereEqualTo("username", getIntent().getStringExtra("username"));

                parseQuery.whereDoesNotExist("cart");

                parseQuery.findInBackground((objects1, e1) -> {
                    if (objects1.size() == 1 && e1 == null) {
                        ParseObject selectedUser = objects1.get(0);

                        selectedUser.put("cart", new ArrayList<>());

                        selectedUser.saveInBackground(e11 -> {
                            if (e11 == null) {
                                button.setEnabled(true);
                                button1.setEnabled(true);
                            } else {
                                Toast.makeText(BuyerHomeActivity.this, e11.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        button.setEnabled(true);
                        button1.setEnabled(true);
                    }
                });
            } else {
                Toast.makeText(BuyerHomeActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}