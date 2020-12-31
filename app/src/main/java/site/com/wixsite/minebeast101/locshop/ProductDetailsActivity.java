package site.com.wixsite.minebeast101.locshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.*;

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
    }
}


