
package site.com.wixsite.minebeast101.locshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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
        if(!productNameEditText.getText().toString().isEmpty() && !productPriceEditText.getText().toString().isEmpty() && bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            ParseFile parseFile = new ParseFile(productNameEditText.getText().toString() + ".png", byteArray);

            ParseObject products = new ParseObject("Products");

            products.put("productName", productNameEditText.getText().toString());
            products.put("productPrice", productPriceEditText.getText().toString());
            products.put("seller", ParseUser.getCurrentUser().getUsername());
            products.put("productImage", parseFile);

            products.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Sellers");

                        parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null && objects.size() > 0) {
                                    ArrayList products = (ArrayList) objects.get(0).getList("products");

                                    products.add(productNameEditText.getText().toString());

                                    ParseObject parseObject = objects.get(0);

                                    parseObject.put("products", products);

                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Intent intent = new Intent(AddProductActivity.this, SellerHomeActivity.class);
                                                intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
                                                startActivity(intent);
                                                Toast.makeText(AddProductActivity.this, "Product added to store!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else if (e != null) {
                                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        addImageButton = findViewById(R.id.button2);

    }
}