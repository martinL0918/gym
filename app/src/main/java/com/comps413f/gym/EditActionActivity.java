package com.comps413f.gym;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class EditActionActivity extends AppCompatActivity {
    private Button inputRepeat;
    private Button confirmButton;
    private SharedPreferences prefs;
    protected String[] weekday = {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"};
    private ImageButton uploadImage;
    protected  boolean[] checkedItems = {false, false, false, false, false, false, false};
    private FirebaseAuth mAuth;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference mStorageRef;
    private String zActnioID = "";
    private String haveImage = "false";
    private AlertDialog dialog;

    static final String EXTRA_DATA = "zActionID"; // Extra key

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
        setContentView(R.layout.editaction);


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
                System.out.println("Update database");
                updateDatabase();
            }
        });
        uploadImage = findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        //change the colors of the views
/*
        String[] aString = [actionName, actionDescription,actionTimes,actionOrgans
                            ,actionUsage, actionReference,actionRepeat,confirmButton];
*/
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

        final EditText inputName = findViewById(R.id.inputName);
        final EditText inputDescription = findViewById(R.id.inputDescription);
        final EditText inputTimes = findViewById(R.id.inputTimes);
        final EditText inputOrgans = findViewById(R.id.inputOrgans);
        final EditText inputUsage = findViewById(R.id.inputUsage);
        final EditText inputReference = findViewById(R.id.inputReference);
        final Button inputRepeat = (Button)findViewById(R.id.inputRepeat);

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


        // get the extra value
        String addData = getIntent().getStringExtra(EXTRA_DATA);
        System.out.println("Extra id: "+ addData);

        //retrieve data from firebase by zActionId

        String userId = mAuth.getCurrentUser().getUid();
        String path = userId+"/"+addData;
        System.out.println("The path: "+path);
        DatabaseReference mGetReference = FirebaseDatabase.getInstance().getReference(path);
        mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    System.out.println("dataSnapshot:"+dataSnapshot);
                    System.out.println("dataSnapshot getValue():"+dataSnapshot.getValue());
                    Action temp = dataSnapshot.getValue(Action.class);

                    inputName.setText(temp.getActionName());
                    inputDescription.setText(temp.getDescription());
                    inputTimes.setText(temp.getTimes());
                    inputOrgans.setText(temp.getOrgans());
                    inputUsage.setText(temp.getUsage());
                    inputReference.setText(temp.getReferences());
                    inputRepeat.setText(temp.getDays());
                    zActnioID = temp.getzActionID();
                    haveImage = temp.getHaveImage();
                    if (haveImage.equals("true")) {
                        //Retreive Image from Firebase Storage
                        //The location of firebase storage according to our own defined structure for this mini project
                        StorageReference downloadRef = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid() + "/images/" + zActnioID);
                        final long ONE_MEGABYTE = 1024 * 1024;
                        downloadRef.getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        DisplayMetrics dm = new DisplayMetrics();
                                        (EditActionActivity.this).getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        uploadImage.setMinimumHeight(dm.heightPixels);
                                        uploadImage.setMinimumWidth(dm.widthPixels);
                                        uploadImage.setBackground(null);
                                        uploadImage.setImageBitmap(bm);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                System.out.println("Error in loading image - Use original image instead");
                            }
                        });
                    }
                    List<String> dayLists = Arrays.asList(temp.getDays().split(","));

                    if(dayLists.contains("Day 1")){
                        checkedItems[0] = true;
                    }
                    if(dayLists.contains("Day 2")){
                        checkedItems[1] = true;
                    }
                    if(dayLists.contains("Day 3")){
                        checkedItems[2] = true;
                    }
                    if(dayLists.contains("Day 4")){
                        checkedItems[3] = true;
                    }
                    if(dayLists.contains("Day 5")){
                        checkedItems[4] = true;
                    }
                    if(dayLists.contains("Day 6")){
                        checkedItems[5] = true;
                    }
                    if(dayLists.contains("Day 7")){
                        checkedItems[6] = true;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
        }
        else{
            ReturnToLogin();
            Toast.makeText(EditActionActivity.this,"You have not signed in",Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(EditActionActivity.this,PreferenceActivity.class);
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
        intent.setClass(EditActionActivity.this,AboutActivity.class);
        startActivity(intent);
        finish();
    }

    protected void displayAlertDialog(){
        // Set up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActionActivity.this);
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

    private void updateDatabase() {
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

        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid()+"/"+getIntent().getStringExtra(EXTRA_DATA));
        System.out.println(mAuth.getCurrentUser().getUid()+"/"+getIntent().getStringExtra(EXTRA_DATA));
        HashMap<String, String> toUpload= new HashMap<String, String>();
        toUpload.put("actionName",inputName.getText().toString());
        toUpload.put("description",inputDescription.getText().toString());
        toUpload.put("times",inputTimes.getText().toString());
        toUpload.put("organs",inputOrgans.getText().toString());
        toUpload.put("usage",inputUsage.getText().toString());
        toUpload.put("references",inputReference.getText().toString());
        toUpload.put("zActionID",zActnioID);
        toUpload.put("haveImage",haveImage);
        toUpload.put("haceChecked","false");
        for (int i = 0 ; i< checkedItems.length; i++) {
            if (checkedItems[i] == true){
                repeat += weekday[i] + ",";
            }
        }
        if ( repeat.equals("")){
            inputRepeat.setError(getString(R.string.cannotBeEmpty));
            return;
        }
        toUpload.put("days",repeat.substring(0,repeat.length()-1));
        myRef.setValue(toUpload);

        if (haveImage.equals("true")) {
            uploadImageToDatabase();
        }else{
            Intent intent = new Intent(EditActionActivity.this,Routine.class);
            startActivity(intent);
        }

    }
    private void uploadImageToDatabase(){
        if (filePath !=null) {
            // Progress Bar
            final ProgressDialog progressDialog = new ProgressDialog(EditActionActivity.this);
            progressDialog.setTitle("Uploading Image...");
            if (!isFinishing()) {
                progressDialog.show();
            }
            //Firebase storage
            StorageReference uploadRef = mStorageRef.child(mAuth.getCurrentUser().getUid()+"/images/" + zActnioID);
            uploadRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { ;
                            progressDialog.dismiss();
                            Toast.makeText(EditActionActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EditActionActivity.this, "Failed Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        Intent intent = new Intent(EditActionActivity.this,Routine.class);
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
        intent.setClass(EditActionActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }



}