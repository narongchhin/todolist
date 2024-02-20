package com.cnr.todolist.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cnr.todolist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import utils.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etTaskDetail;
    private AlertDialog dialog;
    private FloatingActionButton floatingButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public AddNewTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewTaskFragment newInstance(String param1, String param2) {
        AddNewTaskFragment fragment = new AddNewTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Task");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_task, container, false);

        etTaskDetail = view.findViewById(R.id.etTaskDetail);
        etTaskDetail.isFocusableInTouchMode();
        etTaskDetail.setFocusable(true);
        etTaskDetail.requestFocus();

        etTaskDetail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Task task = new Task();
                    task.setId(databaseReference.push().getKey());
                    task.setTaskDetail(etTaskDetail.getText().toString());
                    task.setCompleted("false");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // inside the method of on Data change we are setting
                            // our object class to our database reference.
                            // data base reference will sends data to firebase.
                            databaseReference.child(task.getId()).setValue(task);

                            // after adding this data we are showing toast message.
                            //Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // if the data is not added or it is cancelled then
                            // we are displaying a failure toast message.
                            //Toast.makeText(getContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
                }


                return false;
            }
        });


        return view;
    }


}