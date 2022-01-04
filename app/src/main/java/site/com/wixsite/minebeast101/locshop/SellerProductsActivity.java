package site.com.wixsite.minebeast101.locshop;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class SellerProductsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> productNameArrayList;
    ArrayList<String> productPriceArrayList;
    ArrayList<Bitmap> productImageArrayList;
    ArrayList<String> sellerNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);

        listView = findViewById(R.id.listView);
        productNameArrayList = new ArrayList<>();
        productPriceArrayList = new ArrayList<>();
        productImageArrayList = new ArrayList<>();
        sellerNameArrayList = new ArrayList<>();

        Toast.makeText(this, "Please wait while we retrieve the products!", Toast.LENGTH_SHORT).show();

        ParseQuery<ParseObject> productsRetrievalQuery = ParseQuery.getQuery("Products");

        productsRetrievalQuery.whereEqualTo("seller", ParseUser.getCurrentUser().getUsername());

        productsRetrievalQuery.orderByAscending("updatedAt");

        productsRetrievalQuery.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                runOnUiThread(() -> {
                    for (ParseObject current : objects) {
                        ParseFile image = current.getParseFile("productImage");

                        if (image != null) {
                            image.getDataInBackground((data, e1) -> {
                                if (e1 == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    productImageArrayList.add(bitmap);
                                    productNameArrayList.add(current.getString("productName"));
                                    productPriceArrayList.add(current.getString("productPrice"));
                                    sellerNameArrayList.add(current.getString("seller"));
                                    Bitmap[] productImageBArray = Arrays.asList(productImageArrayList.toArray()).toArray(new Bitmap[productImageArrayList.toArray().length]);
                                    String[] productNameSArray = Arrays.asList(productNameArrayList.toArray()).toArray(new String[productNameArrayList.toArray().length]);
                                    String[] productPriceSArray = Arrays.asList(productPriceArrayList.toArray()).toArray(new String[productPriceArrayList.toArray().length]);
                                    String[] sellerNameSArray = Arrays.asList(sellerNameArrayList.toArray()).toArray(new String[sellerNameArrayList.toArray().length]);

                                    CustomAdapter adapter = new CustomAdapter(SellerProductsActivity.this, sellerNameSArray, productNameSArray, productPriceSArray, productImageBArray);

                                    listView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    class CustomAdapter extends ArrayAdapter<String> {
        Context context;
        String[] rSellerName;
        String[] rProductName;
        String[] rProductPrice;
        Bitmap[] rProductImage;

        CustomAdapter(Context c, String[] sellerName, String[] productName, String[] productPrice, Bitmap[] productImage) {
            super(c, R.layout.custom_row, R.id.productNameTextView, productName);
            this.context = c;
            this.rSellerName = sellerName;
            this.rProductImage = productImage;
            this.rProductName = productName;
            this.rProductPrice = productPrice;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.custom_row, parent, false);
//          TODO: I swear this is the last issue with my implementation
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