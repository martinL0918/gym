package com.comps413f.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.UUID;


public class AddActionActivity extends AppCompatActivity {
    private Button inputRepeat;
    private Button confirmButton;
    private SharedPreferences prefs;
    private ImageButton uploadImage;
    protected String[] weekday = {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"};
    protected  boolean[] checkedItems = {false, false, false, false, false, false, false};
    private FirebaseAuth mAuth;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference mStorageRef;
    private String uniqueid = "";
    private String haveImage = "false";

    static final String EXTRA_DATA = "addRoutine"; // Extra key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString(getString(R.string.pref_color),getString(R.string.pref_color_default));
        System.out.println(theme);

        if (theme.equals("Green")){
            setTheme(R.style.AppThemeGreen);
        }
        else if (theme.equals("Purple")){
            setTheme(R.style.AppThemePurple);
        }
        else{
            System.out.println("Orange");
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.addaction);
        //change the color of textViews

        TextView actionName = (TextView) findViewById(R.id.actionName);
        TextView actionDescription = (TextView) findViewById(R.id.actionDescription);
        TextView actionTimes = (TextView) findViewById(R.id.actionTimes);
        TextView actionOrgans = (TextView) findViewById(R.id.actionOrgans);
        TextView actionUsage = (TextView) findViewById(R.id.actionUsage);
        TextView actionReference = (TextView) findViewById(R.id.actionReference);
        TextView actionRepeat = (TextView) findViewById(R.id.actionRepeat);
        TextView confirmButton = (TextView) findViewById(R.id.confirmButton);

        TextView[] textViewArray = new TextView[]{  actionName, actionDescription,actionTimes,actionOrgans,
                actionUsage, actionReference,actionRepeat,confirmButton
        };

        switch (theme){
            case "Green":
                for(TextView aTextView : textViewArray){
                    aTextView.setBackground(getResources().getDrawable(R.drawable.green_circle_background));
                }
                break;
            case "Purple":
                for(TextView aTextView : textViewArray){
                    aTextView.setBackground(getResources().getDrawable(R.drawable.purple_circle_background));
                }
                break;
            default:
                for(TextView aTextView : textViewArray){
                    aTextView.setBackground(getResources().getDrawable(R.drawable.orange_circle_background));
                }
                break;

        }
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
                uploadDatabase(); //include upload Image to Storage inside

            }
        });
        uploadImage = findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        // get the extra value
        String addData = getIntent().getStringExtra(EXTRA_DATA);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
        }
        else{
            ReturnToLogin();
            Toast.makeText(AddActionActivity.this,"You have not signed in",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item_logout = menu.findItem(R.id.item_logout);
        MenuItem item_about = menu.findItem(R.id.item_about);
        item_logout.setVisible(false);
        item_about.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_setting:
                Intent intent = new Intent(AddActionActivity.this,PreferenceActivity.class);
                startActivity(intent);
                break;
            case R.id.item_about:
                ReturnToAbout();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    public void ReturnToAbout(){
        Intent intent = new Intent();
        intent.setClass(AddActionActivity.this,AboutActivity.class);
        startActivity(intent);
        finish();
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
            final AlertDialog dialog = builder.create();
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

            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid()).push();
            HashMap<String, String> toUpload= new HashMap<String, String>();

            toUpload.put("actionName",inputName.getText().toString());
            toUpload.put("description",inputDescription.getText().toString());
            toUpload.put("times",inputTimes.getText().toString());
            toUpload.put("organs",inputOrgans.getText().toString());
            toUpload.put("usage",inputUsage.getText().toString());
            toUpload.put("references",inputReference.getText().toString());
            uniqueid = myRef.getKey();
            toUpload.put("zActionID",uniqueid);
            toUpload.put("haveImage",haveImage);
            toUpload.put("haveChecked","false");
            System.out.println(inputRepeat.getText().toString());
            for (int i = 0 ; i< checkedItems.length; i++) {
                if (checkedItems[i] == true){
                    repeat += weekday[i] + ",";
                }
            }
            if (repeat == null || repeat.equals("")){
                inputRepeat.setError(getString(R.string.cannotBeEmpty));
                return;
            }
            toUpload.put("days",repeat.substring(0,repeat.length()-1));
            myRef.setValue(toUpload);
            if (haveImage.equals("true")) {
                uploadImageToDatabase();
            }else{
                Intent intent = new Intent(AddActionActivity.this,Routine.class);
                startActivity(intent);
                finish();
            }
        }
        private void uploadImageToDatabase(){
           if (filePath !=null) {
               // Progress Bar
               final ProgressDialog progressDialog = new ProgressDialog(AddActionActivity.this);
               progressDialog.setTitle("Uploading Image...");
               if (!isFinishing()) {
                   progressDialog.show();
               }
               //Firebase storage
               StorageReference uploadRef = mStorageRef.child(mAuth.getCurrentUser().getUid()+"/images/" + uniqueid);
               uploadRef.putFile(filePath)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { ;
                               progressDialog.dismiss();
                               Toast.makeText(AddActionActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception exception) {
                               Toast.makeText(AddActionActivity.this, "Failed Image Uploaded!!", Toast.LENGTH_SHORT).show();
                           }
                       });
           }
           Intent intent = new Intent(AddActionActivity.this,Routine.class);
            startActivity(intent);
        }
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage.setImageBitmap(bitmap);
                uploadImage.setBackground(null);
                haveImage = "true";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void ReturnToLogin(){
        Intent intent = new Intent();
        intent.setClass(AddActionActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


}