package site.com.wixsite.minebeast101.locshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productName;
    TextView productPrice;
    TextView sellerName;
    ImageView productImage;
    Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        sellerName = findViewById(R.id.sellerName);
        productImage = findViewById(R.id.productImage);

        ParseQuery<ParseObject> productsRetrievalQuery = ParseQuery.getQuery("Products");

        productsRetrievalQuery.whereEqualTo("productName", getIntent().getStringExtra("productName"));
        productsRetrievalQuery.whereEqualTo("productPrice", getIntent().getStringExtra("productPrice"));
        productsRetrievalQuery.whereEqualTo("seller", getIntent().getStringExtra("sellerName"));

        productsRetrievalQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseFile parseFile = objects.get(0).getParseFile("productImage");

                    parseFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                productImage.setImageBitmap(bitmap);
                                productName.setText("Product: " + getIntent().getStringExtra("productName"));
                                productPrice.setText("₹" + getIntent().getStringExtra("productPrice"));
                                sellerName.setText("Seller: " + getIntent().getStringExtra("sellerName"));
                            }
                        }
                    });
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProductDetailsActivity.this)
                        .setTitle("Add this product to cart")
                        .setMessage("Are you sure you want to add " + getIntent().getStringExtra("productName") + " to cart?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Buyers");

                                query.whereEqualTo("username", getIntent().getStringExtra("username"));

                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null && objects.size() > 0) {
                                            ParseObject user = objects.get(0);
                                            ArrayList userTempCart = (ArrayList) user.getList("cart");
                                            userTempCart.add(getIntent().getStringExtra("productName"));
                                            user.put("cart", userTempCart);
                                            user.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e==null) {
                                                        Toast.makeText(ProductDetailsActivity.this, "Product added to cart!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ProductDetailsActivity.this, BuyerHomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.baseline_shopping_cart_black_18dp)
                        .show();
            }
        });
    }
}


