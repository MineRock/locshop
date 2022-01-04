package site.com.wixsite.minebeast101.locshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class BuyerOrdersActivity extends AppCompatActivity {

    public String foo(double value) {
        DecimalFormat formatter;

        formatter = new DecimalFormat("0");

        Log.d("value", String.valueOf(value));
        Log.d("formatted", formatter.format(value));
        return formatter.format(value);
    }

    String username;
    ArrayList<String> productNameArrayList;
    ArrayList<String> productPriceArrayList;
    ArrayList<String> orderNumberArrayList;
    ArrayList<String> dateArrayList;
    ListView listView;
    String[] orderNumberSArray;
    String[] productNameSArray;
    String[] productPriceSArray;
    String[] dateSArray;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_orders);
        productNameArrayList = new ArrayList<>();
        productPriceArrayList = new ArrayList<>();
        orderNumberArrayList = new ArrayList<>();
        dateArrayList = new ArrayList<>();
        listView = findViewById(R.id.listView2);


        username = ParseUser.getCurrentUser().getUsername();

        ParseQuery<ParseObject> userOrderQuery = ParseQuery.getQuery("Orders");

        userOrderQuery.whereEqualTo("username", username);

        userOrderQuery.orderByDescending("updatedAt");

        userOrderQuery.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                for (ParseObject object : objects) {
                    if (object != null) {
                        if (object.getList("products").size() == 1) {
                            productNameArrayList.add((String) object.getList("products").get(0));
                        } else {
                            productNameArrayList.add(object.getList("products").get(0) + ", " + object.getList("products").get(1) + " and others");
                        }
                        productPriceArrayList.add(object.getString("price"));
                        Date orderBuyDate = object.getUpdatedAt();
                        String pattern = "E, dd MMM yyyy HH:mm:ss z";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                        String orderBuyDateString = simpleDateFormat.format(orderBuyDate);
                        dateArrayList.add(orderBuyDateString);
                        double orderNumber = Double.parseDouble(object.getString("orderNumber"));
                        orderNumberArrayList.add(foo(orderNumber));

                        orderNumberSArray = Arrays.asList(orderNumberArrayList.toArray()).toArray(new String[orderNumberArrayList.toArray().length]);
                        productNameSArray = Arrays.asList(productNameArrayList.toArray()).toArray(new String[productNameArrayList.toArray().length]);
                        productPriceSArray = Arrays.asList(productPriceArrayList.toArray()).toArray(new String[productPriceArrayList.toArray().length]);
                        dateSArray = Arrays.asList(dateArrayList.toArray()).toArray(new String[dateArrayList.toArray().length]);

                        adapter = new CustomAdapter(BuyerOrdersActivity.this, orderNumberSArray, productNameSArray, productPriceSArray, dateSArray);

                        listView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        for (Object o : productNameArrayList) {
                            System.out.println(o.toString());
                        }

                        for (Object o : dateArrayList) {
                            System.out.println(o.toString());
                        }
                    }
                }
            }
        });
    }

    class CustomAdapter extends ArrayAdapter<String> {
        Context context;
        String[] rOrderNumber;
        String[] rDate;
        String[] rProductName;
        String[] rProductPrice;

        CustomAdapter(Context c, String[] orderNumber, String[] productName, String[] productPrice, String[] date) {
            super(c, R.layout.custom_row_orders, R.id.productNameTextView, productName);
            this.context = c;
            this.rOrderNumber = orderNumber;
            this.rProductName = productName;
            this.rProductPrice = productPrice;
            this.rDate = date;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            TODO: I have no idea what this is either but exactly the same error so fix it later
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.custom_row_orders, parent, false);

            TextView orderNumber = row.findViewById(R.id.orderNumberTextView);
            TextView productName = row.findViewById(R.id.productNameTextView);
            TextView productPrice = row.findViewById(R.id.productPriceTextView);
            TextView date = row.findViewById(R.id.dateTextView);

            orderNumber.setText(rOrderNumber[position]);
            productName.setText(rProductName[position]);
            date.setText(rDate[position]);
            productPrice.setText("₹" + rProductPrice[position]);

            return row;
        }
    }
}