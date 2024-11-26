package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/** @noinspection ALL*/
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks;
    private final Context context;
    private final DataBaseHelper db;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.db = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textViewTitle.setText(task.getTitle());
        holder.checkBoxTask.setChecked(task.isCompleted());

        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            db.updateTask(task);
        });

        holder.buttonEdit.setOnClickListener(v -> showEditDialog(task));
        holder.buttonDelete.setOnClickListener(v -> deleteTask(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void showEditDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null);
        EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextTitle.setText(task.getTitle());

        builder.setView(view)
                .setTitle("Edit Task")
                .setPositiveButton("Save", (dialog, which) -> {
                    task.setTitle(editTextTitle.getText().toString());
                    db.updateTask(task);
                    notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask(int position) {
        Task task = tasks.get(position);
        db.deleteTask(task.getId());
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        CheckBox checkBoxTask;
        ImageButton buttonEdit;
        ImageButton buttonDelete;

        TaskViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}