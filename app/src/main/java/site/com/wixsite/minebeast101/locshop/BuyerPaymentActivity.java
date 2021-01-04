package site.com.wixsite.minebeast101.locshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

public class BuyerPaymentActivity extends AppCompatActivity {

    TextView priceTextView;
    Double price;
    String randomID;

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public void completePayment(View view) {
        Toast.makeText(this, "Please wait, commencing order!", Toast.LENGTH_SHORT).show();

        randomID = Double.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("orderNumber", randomID);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size() == 0) {
                    ParseObject orders = new ParseObject("Orders");

                    orders.put("username", ParseUser.getCurrentUser().getUsername());
                    orders.put("price", price.toString());
                    orders.put("products", getIntent().getParcelableArrayListExtra("products"));
                    orders.put("sellers", removeDuplicates(getIntent().getParcelableArrayListExtra("sellers")));
                    orders.put("orderNumber", randomID);

                    orders.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");
                                buyerQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                buyerQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e==null && objects.size() == 1) {
                                            ParseObject user = objects.get(0);

                                            user.put("cart", new ArrayList());

                                            user.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e==null) {
                                                        Toast.makeText(BuyerPaymentActivity.this, "Order successfully completed!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(BuyerPaymentActivity.this, BuyerHomeActivity.class);
                                                        intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else if(objects.size() > 0) {
                    String previousNumber = objects.get(0).getString("orderNumber");
                    randomID = Double.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

                    while (previousNumber.equals(randomID)) {
                        randomID = Double.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                    }

                    ParseObject orders = new ParseObject("Orders");

                    orders.put("username", ParseUser.getCurrentUser().getUsername());
                    orders.put("price", price.toString());
                    orders.put("products", getIntent().getParcelableArrayListExtra("products"));
                    orders.put("sellers", removeDuplicates(getIntent().getParcelableArrayListExtra("sellers")));
                    orders.put("orderNumber", randomID);

                    orders.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(BuyerPaymentActivity.this, "Order successfully completed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BuyerPaymentActivity.this, BuyerHomeActivity.class);
                                intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_payment);

        priceTextView = findViewById(R.id.priceTextView);

        price = getIntent().getDoubleExtra("price", 0);

        priceTextView.setText("Total price to be paid: ₹" + price.toString());
    }
}