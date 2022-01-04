package site.com.wixsite.minebeast101.locshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class CartActivity extends AppCompatActivity {

    ArrayList productNameArrayListT;
    ArrayList<String> productNameArrayList;
    ArrayList<String> productPriceArrayList;
    ArrayList<Bitmap> productImageArrayList;
    ArrayList<String> sellerNameArrayList;
    ListView listView;
    Bitmap[] productImageBArray;
    String[] productNameSArray;
    String[] productPriceSArray;
    String[] sellerNameSArray;
    Button buyButton;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Your Cart");

        productNameArrayListT = new ArrayList();
        productNameArrayList = new ArrayList<>();
        productPriceArrayList = new ArrayList<>();
        productImageArrayList = new ArrayList<>();
        sellerNameArrayList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        buyButton = findViewById(R.id.buyButton);


        ParseQuery<ParseObject> buyerQuery = ParseQuery.getQuery("Buyers");

        buyerQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        buyerQuery.whereExists("cart");
        buyerQuery.orderByDescending("updatedAt");

        buyerQuery.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                runOnUiThread(() -> {

                    ParseObject user = objects.get(0);

                    productNameArrayListT = (ArrayList) user.getList("cart");

                    if (productNameArrayListT != null) {
                        for (Object object : productNameArrayListT) {
                            String productName = object.toString();

                            ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Products");

                            productQuery.whereEqualTo("productName", productName);

                            productQuery.findInBackground((objects1, e1) -> {
                                if (objects1.size() > 0 && e1 == null) {
                                    for (ParseObject object1 : objects1) {
                                        ArrayList<ParseFile> tempImageArrayList = new ArrayList<>();
                                        tempImageArrayList.add(object1.getParseFile("productImage"));

                                        for (ParseFile parseFile : tempImageArrayList) {
                                            parseFile.getDataInBackground((data, e11) -> {
                                                if (e11 == null && data != null) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    productNameArrayList.add(object1.getString("productName"));
                                                    productImageArrayList.add(bitmap);
                                                    productPriceArrayList.add(object1.getString("productPrice"));
                                                    sellerNameArrayList.add(object1.getString("seller"));

                                                    productImageBArray = Arrays.asList(productImageArrayList.toArray()).toArray(new Bitmap[productImageArrayList.toArray().length]);
                                                    productNameSArray = Arrays.asList(productNameArrayList.toArray()).toArray(new String[productNameArrayList.toArray().length]);
                                                    productPriceSArray = Arrays.asList(productPriceArrayList.toArray()).toArray(new String[productPriceArrayList.toArray().length]);
                                                    sellerNameSArray = Arrays.asList(sellerNameArrayList.toArray()).toArray(new String[sellerNameArrayList.toArray().length]);

                                                    adapter = new CustomAdapter(CartActivity.this, sellerNameSArray, productNameSArray, productPriceSArray, productImageBArray);

                                                    listView.setAdapter(adapter);

                                                    adapter.notifyDataSetChanged();

                                                    buyButton.setOnClickListener(v -> {
                                                        double sum = 0;
                                                        for(Object o : productPriceArrayList) {
                                                            double d = Double.parseDouble(o.toString());
                                                            sum += d;
                                                        }
                                                        Intent intent = new Intent(CartActivity.this, BuyerPaymentActivity.class);
                                                        intent.putExtra("price", sum);
                                                        intent.putExtra("products", productNameArrayList);
                                                        intent.putExtra("sellers", sellerNameArrayList);
                                                        startActivity(intent);
                                                    });
                                                }
                                            });
                                        }
                                    }
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
//          TODO: Another thing to fix
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