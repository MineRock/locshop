package site.com.wixsite.minebeast101.locshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SellerHomeActivity extends AppCompatActivity {

    TextView welcomeUserTextView;
    Button button;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);

        button.setEnabled(false);
        button1.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setTitle("Seller Homepage");

        Toast.makeText(this, "Please wait - do not add new products yet!", Toast.LENGTH_SHORT).show();

        welcomeUserTextView = findViewById(R.id.textView8);

        welcomeUserTextView.setText("Hello, " + getIntent().getStringExtra("username") + "!");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sellers");

        query.whereEqualTo("username", getIntent().getStringExtra("username"));

        query.whereExists("products");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    button.setEnabled(true);
                    button1.setEnabled(true);
                    Toast.makeText(SellerHomeActivity.this, "You can start adding products now!", Toast.LENGTH_SHORT).show();
                } else if (objects.size() == 0) {

                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Sellers");

                    parseQuery.whereEqualTo("username", getIntent().getStringExtra("username"));

                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() == 1 && e == null) {
                                ParseObject selectedUser = objects.get(0);

                                selectedUser.put("products", new ArrayList<>());

                                selectedUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            button.setEnabled(true);
                                            button1.setEnabled(true);
                                            Toast.makeText(SellerHomeActivity.this, "You can start adding products now!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SellerHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(SellerHomeActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}