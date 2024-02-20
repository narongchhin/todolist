package com.cnr.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import utils.Task;
import utils.TaskAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView reyclerView;
    ArrayList<Task> list;

    DatabaseReference databaseReference;
    FloatingActionButton fab;

    TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        fab = findViewById(R.id.MainFab);
        reyclerView = findViewById(R.id.taskRecycleViewNew);
        databaseReference = FirebaseDatabase.getInstance().getReference("Task");
        list = new ArrayList<>();
        reyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this,list);
        reyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Task task = dataSnapshot.getValue(Task.class);
                    list.add(task);
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddNewActivity.class);
                startActivity(i);
            }
        });


    }
}