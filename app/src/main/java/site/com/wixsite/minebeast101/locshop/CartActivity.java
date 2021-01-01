package site.com.wixsite.minebeast101.locshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ArrayList productNameArrayListT;
    ArrayList productNameArrayList;
    ArrayList productPriceArrayList;
    ArrayList productImageArrayList;
    ArrayList sellerNameArrayList;
    ListView listView;
    Bitmap productImageBArray[];
    String productNameSArray[];
    String productPriceSArray[];
    String sellerNameSArray[];
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Your Cart");

        productNameArrayListT = new ArrayList();
        productNameArrayList = new ArrayList();
        productPriceArrayList = new ArrayList();
        productImageArrayList = new ArrayList();
        sellerNameArrayList = new ArrayList();
        listView = findViewById(R.id.listView);


        ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");

        buyerQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        buyerQuery.whereExists("cart");
        buyerQuery.orderByDescending("updatedAt");

        buyerQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ParseObject user = objects.get(0);

                            productNameArrayListT = (ArrayList) user.getList("cart");

                            for (Object object : productNameArrayListT) {
                                String productName = object.toString();

                                ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Products");

                                productQuery.whereEqualTo("productName", productName);

                                productQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (objects.size() > 0 && e == null) {
                                            for (ParseObject object : objects) {
                                                ArrayList<ParseFile> tempImageArrayList = new ArrayList();
                                                tempImageArrayList.add(object.getParseFile("productImage"));

                                                for (ParseFile parseFile : tempImageArrayList) {
                                                    parseFile.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] data, ParseException e) {
                                                            if (e == null && data != null) {
                                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                                productNameArrayList.add(object.getString("productName"));
                                                                productImageArrayList.add(bitmap);
                                                                productPriceArrayList.add(object.getString("productPrice"));
                                                                sellerNameArrayList.add(object.getString("seller"));

                                                                productImageBArray = Arrays.asList(productImageArrayList.toArray()).toArray(new Bitmap[productImageArrayList.toArray().length]);
                                                                productNameSArray = Arrays.asList(productNameArrayList.toArray()).toArray(new String[productNameArrayList.toArray().length]);
                                                                productPriceSArray = Arrays.asList(productPriceArrayList.toArray()).toArray(new String[productPriceArrayList.toArray().length]);
                                                                sellerNameSArray = Arrays.asList(sellerNameArrayList.toArray()).toArray(new String[sellerNameArrayList.toArray().length]);

                                                                adapter = new CustomAdapter(CartActivity.this, sellerNameSArray, productNameSArray, productPriceSArray, productImageBArray);

                                                                listView.setAdapter(adapter);

                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }

                        }
                    });

                }
            }
        });
    }


    class CustomAdapter extends ArrayAdapter<String> {
        Context context;
        String rSellerName[];
        String rProductName[];
        String rProductPrice[];
        Bitmap rProductImage[];

        CustomAdapter(Context c, String sellerName[], String productName[], String productPrice[], Bitmap productImage[]) {
            super(c, R.layout.custom_row, R.id.productNameTextView, productName);
            this.context = c;
            this.rSellerName = sellerName;
            this.rProductImage = productImage;
            this.rProductName = productName;
            this.rProductPrice = productPrice;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.custom_row, parent, false);

            ImageView images = row.findViewById(R.id.image);
            TextView seller = row.findViewById(R.id.sellerTextView);
            TextView productName = row.findViewById(R.id.productNameTextView);
            TextView productPrice = row.findViewById(R.id.productPriceTextView);

            images.setImageBitmap(rProductImage[position]);
            seller.setText(rSellerName[position]);
            productName.setText(rProductName[position]);
            productPrice.setText("₹" + rProductPrice[position]);

            return row;
        }
    }
}