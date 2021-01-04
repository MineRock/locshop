package site.com.wixsite.minebeast101.locshop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BuyerOrdersActivity extends AppCompatActivity {

    public String foo(double value) //Got here 6.743240136E7 or something..
    {
        DecimalFormat formatter;

        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0.00"); //Here you can also deal with rounding if you wish..
        else
            formatter = new DecimalFormat("0");


        Log.d("value", String.valueOf(value));
        Log.d("formatted", String.valueOf(formatter.format(value)));
        return formatter.format(value);
    }

    String username;
    ArrayList productNameArrayList;
    ArrayList productPriceArrayList;
    ArrayList orderNumberArrayList;
    ArrayList dateArrayList;
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
        productNameArrayList = new ArrayList();
        productPriceArrayList = new ArrayList();
        orderNumberArrayList = new ArrayList();
        dateArrayList = new ArrayList();
        listView = findViewById(R.id.listView2);


        username = ParseUser.getCurrentUser().getUsername();

        ParseQuery<ParseObject> userOrderQuery = ParseQuery.getQuery("Orders");

        userOrderQuery.whereEqualTo("username", username);

        userOrderQuery.orderByDescending("updatedAt");

        userOrderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0) {
                    for(ParseObject object : objects) {
                        productNameArrayList.add(object.getList("products").get(0) + ", " + object.getList("products").get(1) + " and others");
                        productPriceArrayList.add(object.getString("price"));
                        Date orderBuyDate = object.getUpdatedAt();
                        String pattern = "E, dd MMM yyyy HH:mm:ss z";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String orderBuyDateString = simpleDateFormat.format(orderBuyDate).toString();
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
        String rOrderNumber[];
        String rDate[];
        String rProductName[];
        String rProductPrice[];

        CustomAdapter(Context c, String orderNumber[], String productName[], String productPrice[], String date[]) {
            super(c, R.layout.custom_row_orders, R.id.productNameTextView, productName);
            this.context = c;
            this.rOrderNumber = orderNumber;
            this.rProductName = productName;
            this.rProductPrice = productPrice;
            this.rDate = date;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.custom_row_orders, parent, false);

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