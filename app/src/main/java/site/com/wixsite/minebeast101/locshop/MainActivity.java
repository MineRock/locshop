package site.com.wixsite.minebeast101.locshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public void nextActivity(Class nextActivity) {

        Intent intent = new Intent(this, nextActivity);

        startActivity(intent);
    }

    public void buyerSignup(View view) {
        nextActivity(BuyerSignupActivity.class);
    }

    public void sellerSignup(View view) {
        nextActivity(SellerSignupActivity.class);
    }

    public void login(View view) {
        nextActivity(LoginActivity.class);
    }

    String userrole;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().getUsername() != null) {
                // do stuff with the user
                username = ParseUser.getCurrentUser().getUsername();

                ParseQuery<ParseObject> sellerQuery = ParseQuery.getQuery("Sellers");
                sellerQuery.whereEqualTo("username", username);

                sellerQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            userrole = "Seller";
                            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "User login detected! You are now signed in as a seller, " + username + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");
                buyerQuery.whereEqualTo("username", username);

                buyerQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            userrole = "Buyer";
//                        Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class); CHANGE TO BUYERHOMEACTIVITY
//                        startActivity(intent);
                            Toast.makeText(MainActivity.this, "User login detected! You are now signed in as a buyer, " + username + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}