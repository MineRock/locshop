
package site.com.wixsite.minebeast101.locshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap bitmap;
    EditText productNameEditText;
    EditText productPriceEditText;
    Button addImageButton;

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, 1);
    }

    public void addImage(View view) {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }

    public void addProduct(View view) {
        Toast.makeText(this, "Please wait! This is a heavy process and depends on the size of your image! If it takes longer than 2 minutes, then close the app and retry! Thank you for selling your product on LocShop!", Toast.LENGTH_SHORT).show();
        if(!productNameEditText.getText().toString().isEmpty() && !productPriceEditText.getText().toString().isEmpty() && bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            ParseFile parseFile = new ParseFile(productNameEditText.getText().toString() + ".png", byteArray);

            ParseObject products = new ParseObject("Products");

            products.put("productName", productNameEditText.getText().toString());
            products.put("productPrice", productPriceEditText.getText().toString());
            products.put("seller", ParseUser.getCurrentUser().getUsername());
            products.put("productImage", parseFile);

            products.saveInBackground(e -> {
                if (e == null) {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Sellers");

                    parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    parseQuery.findInBackground((objects, e12) -> {
                        if (e12 == null && objects.size() > 0) {
                            ArrayList<Object> products1 = (ArrayList<Object>) objects.get(0).getList("products");

                            if (products1 != null) {
                                products1.add(productNameEditText.getText().toString());


                                ParseObject parseObject = objects.get(0);

                                parseObject.put("products", products1);

                                parseObject.saveInBackground(e1 -> {
                                    if (e1 == null) {
                                        Intent intent = new Intent(AddProductActivity.this, SellerHomeActivity.class);
                                        intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
                                        startActivity(intent);
                                        Toast.makeText(AddProductActivity.this, "Product added to store!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddProductActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                                        e1.printStackTrace();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setTitle("Add a product to LocShop");

        imageView = findViewById(R.id.imageView2);
        productNameEditText = findViewById(R.id.editTextTextPersonName2);
        productPriceEditText = findViewById(R.id.editTextTextPersonName3);
        addImageButton = findViewById(R.id.button1);

    }
}