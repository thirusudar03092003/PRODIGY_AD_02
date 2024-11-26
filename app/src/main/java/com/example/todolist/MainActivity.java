package com.example.todolist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTask;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize database and get tasks
        db = new DataBaseHelper(this);
        taskList = db.getAllTasks();

        // Initialize views and setup RecyclerView
        initializeViews();
        setupRecyclerView();

        // Setup EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        editTextTask = findViewById(R.id.editTextTask);
        recyclerView = findViewById(R.id.recyclerViewTasks);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> addTask());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(adapter);
    }

    private void addTask() {
        String title = editTextTask.getText().toString().trim();
        if (!title.isEmpty()) {
            Task task = new Task(0, title, "", false);
            long id = db.addTask(task);
            task.setId((int) id);
            taskList.add(task);
            adapter.notifyItemInserted(taskList.size() - 1);
            editTextTask.setText("");
        }
    }
}