package site.com.wixsite.minebeast101.locshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productName;
    TextView productPrice;
    TextView sellerName;
    ImageView productImage;
    Button addToCartButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        sellerName = findViewById(R.id.sellerName);
        productImage = findViewById(R.id.productImage);
        addToCartButton = findViewById(R.id.button4);

        ParseQuery<ParseObject> productsRetrievalQuery = ParseQuery.getQuery("Products");

        productsRetrievalQuery.whereEqualTo("productName", getIntent().getStringExtra("productName"));
        productsRetrievalQuery.whereEqualTo("productPrice", getIntent().getStringExtra("productPrice"));
        productsRetrievalQuery.whereEqualTo("seller", getIntent().getStringExtra("sellerName"));

        productsRetrievalQuery.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseFile parseFile = objects.get(0).getParseFile("productImage");


                if (parseFile != null) {
                    parseFile.getDataInBackground((data, e1) -> {
                        if (e1 == null && data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            productImage.setImageBitmap(bitmap);
                            productName.setText("Product: " + getIntent().getStringExtra("productName"));
                            productPrice.setText("₹" + getIntent().getStringExtra("productPrice"));
                            sellerName.setText("Seller: " + getIntent().getStringExtra("sellerName"));
                        }
                    });
                }
            }
        });

        addToCartButton.setOnClickListener(v -> new AlertDialog.Builder(ProductDetailsActivity.this)
                .setTitle("Add this product to cart")
                .setMessage("Are you sure you want to add " + getIntent().getStringExtra("productName") + " to cart?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", (dialog, which) -> {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Buyers");

                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    query.findInBackground((objects, e) -> {
                        if (e == null && objects.size() > 0) {
                            ParseObject user = objects.get(0);
                            ArrayList<Object> userTempCart = (ArrayList<Object>) user.getList("cart");
                            if (userTempCart != null) {
                                userTempCart.add(getIntent().getStringExtra("productName"));
                                user.put("cart", userTempCart);

                            user.saveInBackground(e12 -> {
                                if(e12 ==null) {
                                    Toast.makeText(ProductDetailsActivity.this, "Product added to cart!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProductDetailsActivity.this, BuyerHomeActivity.class);
                                    intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
                                    startActivity(intent);
                                } else {
                                    e12.printStackTrace();
                                }
                            });
                            }
                        }
                    });
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .setIcon(R.drawable.baseline_shopping_cart_black_18dp)
                .show());
    }
}


