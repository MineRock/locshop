package site.com.wixsite.minebeast101.locshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {

    public void nextActivity(Class nextActivity) {
        Intent intent = new Intent(MainActivity.this, nextActivity);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}