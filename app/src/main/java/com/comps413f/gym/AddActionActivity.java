package com.comps413f.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class AddActionActivity extends AppCompatActivity {
    private Button inputRepeat;
    private Button confirmButton;
    private ImageButton uploadImage;
    protected String[] weekday = {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"};
    protected  boolean[] checkedItems = {false, false, false, false, false, false, false};
    private FirebaseAuth mAuth;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaction);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        inputRepeat = findViewById(R.id.inputRepeat);
        inputRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlertDialog();
            }
        });
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDatabase();
            }
        });
        uploadImage = findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    protected void displayAlertDialog(){
            // Set up the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(AddActionActivity.this);
            builder.setTitle("Choose Days");
            // Add a checkbox list
                    builder.setMultiChoiceItems(weekday, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // The user checked or unchecked a box
                                checkedItems[which] = isChecked;
                        }
                    });
            // Add OK and Cancel buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // The user clicked OK
                            int i =0;
                            String result = "";
                            for (boolean checked: checkedItems) {
                                if (checked == true){
                                    result += weekday[i] + "\n";
                                }
                                i++;
                            }
                            result = result.substring(0,result.length() - 1);
                            inputRepeat.setText(result);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
            // Create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
        }

        private void uploadDatabase() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            EditText inputName = findViewById(R.id.inputName);
            EditText inputDescription = findViewById(R.id.inputDescription);
            EditText inputTimes = findViewById(R.id.inputTimes);
            EditText inputOrgans = findViewById(R.id.inputOrgans);
            EditText inputUsage = findViewById(R.id.inputUsage);
            EditText inputReference = findViewById(R.id.inputReference);
            String repeat = "";

            int count = 0;
            if (TextUtils.isEmpty(inputName.getText().toString())){
                inputName.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            if (TextUtils.isEmpty(inputDescription.getText().toString())){
                inputDescription.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            if (TextUtils.isEmpty(inputTimes.getText().toString())){
                inputTimes.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            if (TextUtils.isEmpty(inputOrgans.getText().toString())){
                inputOrgans.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            if (TextUtils.isEmpty(inputUsage.getText().toString())){
                inputUsage.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            if (TextUtils.isEmpty(inputReference.getText().toString())){
                inputReference.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());
            HashMap<String, String> toUpload= new HashMap<String, String>();

            toUpload.put("actionName",inputName.getText().toString());
            toUpload.put("description",inputDescription.getText().toString());
            toUpload.put("times",inputTimes.getText().toString());
            toUpload.put("organs",inputOrgans.getText().toString());
            toUpload.put("usage",inputUsage.getText().toString());
            toUpload.put("references",inputReference.getText().toString());

            for (int i = 0 ; i< checkedItems.length; i++) {
                if (checkedItems[i] == true){
                    repeat += weekday[i] + ",";
                }
            }
            toUpload.put("days",repeat.substring(0,repeat.length()-1));
            DatabaseReference newRef = myRef.push();
            newRef.setValue(toUpload);
        }
        private void uploadImage(){
            Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot.getDownloadURL();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                uploadImage.setImageBitmap(bitmap);
                uploadImage.setBackground(null);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}