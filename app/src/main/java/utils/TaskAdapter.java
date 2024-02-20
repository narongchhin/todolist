package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cnr.todolist.AddNewActivity;
import com.cnr.todolist.MainActivity;
import com.cnr.todolist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    ArrayList<Task> taskList;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_layout,parent,false);


        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskCheckbox.setText(task.getTaskDetail());
        holder.taskCheckbox.setTag(position);

//        holder.tvTaskDetail.setText(task.getTaskDetail());
//        holder.tvTaskDetail.setPaintFlags(holder.tvTaskDetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(task.getCompleted().equals("true")){
            holder.taskCheckbox.setChecked(true);
            holder.taskCheckbox.setPaintFlags(holder.taskCheckbox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }else{
            holder.taskCheckbox.setChecked(false);
            if((holder.taskCheckbox.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) == Paint.STRIKE_THRU_TEXT_FLAG){
                holder.taskCheckbox.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }
        }

        holder.taskCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference onChecked = FirebaseDatabase.getInstance().getReference("Task");
                if(holder.taskCheckbox.isChecked()){
                    onChecked.child(task.getId()).child("completed").setValue("true");
                }else{
                    onChecked.child(task.getId()).child("completed").setValue("false");
                }
            }
        });


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Are you sure want to delete this task?");
                builder.setPositiveButton(R.string.DeleteTask, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(task.getId());
                        Toast.makeText(context.getApplicationContext(), "Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.CancelDeleteTask, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

               // taskDb.child(task.getId()).removeValue();
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddNewActivity.class);
                i.putExtra("taskID",task.getId());
                i.putExtra("taskDetail",task.getTaskDetail());
                i.putExtra("updateMode",true);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    private void deleteData(String taskId){
        DatabaseReference taskDb = FirebaseDatabase.getInstance().getReference("Task");
        taskDb.child(taskId).removeValue();
        Query query = taskDb.child(taskId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckbox;
        ImageView ivDelete;
        ImageView ivEdit;

        TextView tvTaskDetail;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckbox = itemView.findViewById(R.id.todoCheckBox);
            ivDelete = itemView.findViewById(R.id.ivDeleteTask);
            ivEdit =itemView.findViewById(R.id.ivEditTask);
            tvTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
        }
    }
}
