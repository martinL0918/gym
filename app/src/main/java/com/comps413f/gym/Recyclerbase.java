package com.comps413f.gym;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class Recyclerbase extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerbase);
        List<Action> actionList = new ArrayList<>();
        actionList.add(new Action("Sit up","description","organs","times","usage","references","days"));
        actionList.add(new Action("Push up","description","organs","times","usage","references","days"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Recyclerbase.this));
        ActionAdapter adapter = new ActionAdapter(this,actionList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
