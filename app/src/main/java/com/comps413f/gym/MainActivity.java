package com.comps413f.gym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private Button inputRepeat;
    protected String[] weekday = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    protected  boolean[] checkedItems = {false, false, false, false, false, false, false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaction);
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        DatabaseReference newRef = database.getReference("Peter");
        myRef.child("name").setValue("martin");
        myRef.child("message").setValue("hello world!");
        myRef.child("id").setValue("12345678");
        myRef.child("user").child("username").setValue("Martin");
        newRef.child("user").setValue("apple");*/
        inputRepeat = findViewById(R.id.inputRepeat);
        inputRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlertDialog();
            }
        });

    }
    protected void displayAlertDialog(){
            // Set up the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

}