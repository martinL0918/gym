package com.comps413f.gym;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder>{
    private Context context;
    private List<Action> actionList;
    private FirebaseAuth mAuth;
    private boolean finished = false;
    ActionAdapter(Context context, List<Action> actionList){
        this.context = context;
        this.actionList = actionList;
    }
    @Override
    public ActionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.actioncardview, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StorageReference mStorageRef;
        final Action action = actionList.get(position);
        holder.card_name.setText(action.getActionName());
        holder.card_description.setText(action.getDescription());
        holder.card_times.setText(action.getTimes());
        holder.card_organs.setText(action.getOrgans());
        holder.card_references.setText(Html.fromHtml(action.getReferences()));
        holder.card_usage.setText(action.getUsage());
        holder.card_references.setMovementMethod(LinkMovementMethod.getInstance());
        holder.card_references.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String ref = action.getReferences();
                if(!ref.contains("http://")){
                    ref = "http://".concat(ref);
                }
                Uri myBlogUri = Uri.parse(ref);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,myBlogUri);
                context.startActivity(webIntent);
            }
        });
        System.out.println(mAuth.getCurrentUser().getUid()+"/images/"+action.getzActionID());
        //如果有image先retrieve
        if (action.getHaveImage().equals("true")) {
            //Retreive Image from Firebase Storage
            //The location of firebase storage according to our own defined structure for this mini project
            StorageReference downloadRef = FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid() + "/images/" + action.getzActionID());
            final long ONE_MEGABYTE = 1024 * 1024;
            downloadRef.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            DisplayMetrics dm = new DisplayMetrics();
                            ((Recyclerbase) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                            holder.actionPicture.setMinimumHeight(dm.heightPixels);
                            holder.actionPicture.setMinimumWidth(dm.widthPixels);
                            holder.actionPicture.setImageBitmap(bm);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Error in loading image - Use original image instead");
                }
            });
        }
        if (action.getHaveChecked() == "true"){
            System.out.println("yes, checked");
            holder.finishButton.setBackgroundResource(R.drawable.checked);
            holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.editButton.setVisibility(View.INVISIBLE);
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Edit button of " + action.getzActionID() + " pressed");
                Intent intent = new Intent(context, EditActionActivity.class);
                intent.putExtra("zActionID",action.getzActionID());
                context.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just hide the whole view from RecyclerLayout
                holder.constraint_to_hide.setVisibility(View.GONE);
                holder.card_view.setVisibility(View.GONE);
                holder.linear_to_hide.setVisibility(View.GONE);
                String aKey = action.getzActionID();
                String userId = mAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference(userId+"/"+aKey).removeValue();
            }
        });

        //peter edit finishBtn
        holder.finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userId = mAuth.getCurrentUser().getUid();
                String aKey = action.getzActionID();
                if(!finished) {
                    holder.finishButton.setBackgroundResource(R.drawable.checked);
                    holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));
                    holder.editButton.setVisibility(View.INVISIBLE);
                    holder.deleteButton.setVisibility(View.INVISIBLE);
                    FirebaseDatabase.getInstance().getReference(userId+"/"+aKey+"/haveChecked").setValue("true");
                }
                else {
                    holder.finishButton.setBackgroundResource(R.drawable.notchecked);
                    holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    holder.editButton.setVisibility(View.VISIBLE);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference(userId+"/"+aKey+"/haveChecked").setValue("false");
                }
                finished = !finished;

            }
        });
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView card_name;
        TextView card_description;
        TextView card_times;
        TextView card_organs;
        TextView card_references;
        TextView card_usage;
        TextView cardDays;
        ImageView actionPicture;
        ImageButton editButton;
        ImageButton deleteButton;
        ImageButton finishButton;
        CardView card_view;
        ConstraintLayout constraint_to_hide;
        LinearLayout linear_to_hide;
        ViewHolder(View itemView) {
            super(itemView);
            actionPicture = (ImageView) itemView.findViewById(R.id.actionPicture);
            card_name = (TextView) itemView.findViewById(R.id.card_name);
            card_description = (TextView) itemView.findViewById(R.id.card_description);
            card_times = (TextView) itemView.findViewById(R.id.card_times);
            card_organs = (TextView) itemView.findViewById(R.id.card_organs);
            card_references = (TextView) itemView.findViewById(R.id.card_references);
            card_usage = (TextView) itemView.findViewById(R.id.card_usage);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            finishButton = itemView.findViewById(R.id.finishButton);
            card_view = itemView.findViewById(R.id.card_view);
            constraint_to_hide = itemView.findViewById(R.id.constraint_to_hide);
            linear_to_hide = itemView.findViewById(R.id.linear_to_hide);
        }
    }

}

