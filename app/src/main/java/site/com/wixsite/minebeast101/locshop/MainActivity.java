package site.com.wixsite.minebeast101.locshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

                sellerQuery.findInBackground((objects, e) -> {
                    if (e == null && objects.size() > 0) {
                        userrole = "Seller";
                        Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                });
                ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");
                buyerQuery.whereEqualTo("username", username);

                buyerQuery.findInBackground((objects, e) -> {
                    if (e == null && objects.size() > 0) {
                        userrole = "Buyer";
                        Intent intent = new Intent(MainActivity.this, BuyerHomeActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                });
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}