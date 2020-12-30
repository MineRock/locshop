package site.com.wixsite.minebeast101.locshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        byte[] bytes = getIntent().getByteArrayExtra("bitmapbytes");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        productImage.setImageBitmap(bmp);


        productName.setText("Product: " + getIntent().getStringExtra("productName"));
        productPrice.setText("₹" + getIntent().getStringExtra("productPrice"));
        sellerName.setText("Seller: " + getIntent().getStringExtra("sellerName"));

    }
}