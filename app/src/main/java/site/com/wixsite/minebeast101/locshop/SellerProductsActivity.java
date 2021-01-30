package site.com.wixsite.minebeast101.locshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellerProductsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList productNameArrayList;
    ArrayList productPriceArrayList;
    ArrayList productImageArrayList;
    ArrayList sellerNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);

        listView = findViewById(R.id.listView);
        productNameArrayList = new ArrayList();
        productPriceArrayList = new ArrayList();
        productImageArrayList = new ArrayList();
        sellerNameArrayList = new ArrayList();

        Toast.makeText(this, "Please wait while we retrieve the products!", Toast.LENGTH_SHORT).show();

        ParseQuery<ParseObject> productsRetrievalQuery = ParseQuery.getQuery("Products");

        productsRetrievalQuery.whereEqualTo("seller", ParseUser.getCurrentUser().getUsername());

        productsRetrievalQuery.orderByAscending("updatedAt");

        productsRetrievalQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    runOnUiThread(new Runnable(){
                        public void run() {
                            for (ParseObject current : objects) {
                                ParseFile image = current.getParseFile("productImage");

                                image.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            productImageArrayList.add(bitmap);
                                            productNameArrayList.add(current.getString("productName"));
                                            productPriceArrayList.add(current.getString("productPrice"));
                                            sellerNameArrayList.add(current.getString("seller"));
                                            Bitmap productImageBArray[] = Arrays.asList(productImageArrayList.toArray()).toArray(new Bitmap[productImageArrayList.toArray().length]);
                                            String productNameSArray[] = Arrays.asList(productNameArrayList.toArray()).toArray(new String[productNameArrayList.toArray().length]);
                                            String productPriceSArray[] = Arrays.asList(productPriceArrayList.toArray()).toArray(new String[productPriceArrayList.toArray().length]);
                                            String sellerNameSArray[] = Arrays.asList(sellerNameArrayList.toArray()).toArray(new String[sellerNameArrayList.toArray().length]);

                                            CustomAdapter adapter = new CustomAdapter(SellerProductsActivity.this, sellerNameSArray, productNameSArray, productPriceSArray, productImageBArray);

                                            listView.setAdapter(adapter);

                                            adapter.notifyDataSetChanged();

                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent(SellerProductsActivity.this, ProductDetailsActivity.class);
                                                    intent.putExtra("productName", productNameSArray[position]);
                                                    intent.putExtra("productPrice", productPriceSArray[position]);
                                                    intent.putExtra("sellerName", sellerNameSArray[position]);
                                                    startActivity(intent);
                                                }
                                            });
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