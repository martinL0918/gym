package com.comps413f.gym;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder>{
    private Context context;
    private List<Action> actionList;
    private FirebaseAuth mAuth;
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
    public void onBindViewHolder(final ActionAdapter.ViewHolder holder, int position) {
        StorageReference mStorageRef;
        final Action action = actionList.get(position);
        holder.card_name.setText(action.getActionName());
        holder.card_description.setText(action.getDescription());
        holder.card_times.setText(action.getTimes());
        holder.card_organs.setText(action.getOrgans());
        holder.card_references.setText(action.getReferences());
        holder.card_usage.setText(action.getUsage());
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
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Edit button of " + action.getzActionID() + " pressed");
                /* TODO: Start a New Intent with Extra attribute (zActionID)

                 */
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just hide the whole view from RecyclerLayout
               holder.constraint_to_hide.setVisibility(View.GONE);
               holder.card_view.setVisibility(View.GONE);
               holder.linear_to_hide.setVisibility(View.GONE);
               /* TODO: Delete data in database

               */

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
            card_view = itemView.findViewById(R.id.card_view);
            constraint_to_hide = itemView.findViewById(R.id.constraint_to_hide);
            linear_to_hide = itemView.findViewById(R.id.linear_to_hide);


        }
    }


}

