package com.cnr.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utils.Task;

public class AddNewActivity extends AppCompatActivity {

    AutoCompleteTextView txtFilter;
    String taskID,taskDetail;
    Boolean updateMode,isTaskDeplicate = false;

    Button btnUpdate;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    String[] countries={"India","Australia","West indies","indonesia","Indiana",
            "South Africa","England","Bangladesh","Srilanka","singapore"};

    List<String> listKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_addnew);
        setSupportActionBar(myToolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");
        listKey = new ArrayList<>();
        btnUpdate = findViewById(R.id.btnUpdateTask);
        txtFilter = findViewById(R.id.txtFilterSearch);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            taskID = bundle.getString("taskID");
            taskDetail = bundle.getString("taskDetail");
            updateMode = bundle.getBoolean("updateMode");
            //Toast.makeText(getApplicationContext(),taskID + " " + updateMode.toString(),Toast.LENGTH_SHORT).show();
            if(updateMode){
                btnUpdate.setVisibility(View.VISIBLE);
                txtFilter.setText(taskDetail);
                txtFilter.setHint("");
                getSupportActionBar().setTitle("Update Task");
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!txtFilter.getText().toString().isEmpty()){
                            Task task = new Task();
                            task.setId(taskID);
                            task.setTaskDetail(txtFilter.getText().toString());
                            task.setCompleted("false");
                            databaseReference.child(task.getTaskDetail()).setValue(task);
                            Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }

        }
        else{
            try{
                getSupportActionBar().setTitle("Add New Task");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,countries);


            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        Task tasks = data.getValue(Task.class);
                        if(tasks != null){
                            listKey.add(tasks.getTaskDetail());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listKey);
            txtFilter.setThreshold(2);
            txtFilter.setAdapter(adapter);

            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            txtFilter.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            if(!txtFilter.getText().toString().isEmpty()){
                                Task task = new Task();
                                task.setId(databaseReference.push().getKey());
                                task.setTaskDetail(txtFilter.getText().toString());
                                task.setCompleted("false");
                                databaseReference.child(task.getId()).setValue(task);
                                finish();
                            }
                        return true;
                    }
                    return false;
                }
            });
        }

    }
}