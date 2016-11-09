package com.example.sarika.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ToDo> todoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ToDoAdapter tAdapter;
    private ImageButton addButton;
    private SQLiteDatabase db;
    private ToDo todo;
    private int location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        addButton = (ImageButton) findViewById(R.id.addButton);

        tAdapter = new ToDoAdapter(todoList);

        //setting recycler view
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tAdapter);

        //Creating database if not exist otherwise opening the existing database
        createDatabase();

        //setting onClickListener to button for adding a new Task
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, AddTask.class);
                startActivity(in);
                prepareToDoList();
            }
        });

        //action to be performed if item from recycler view is clicked
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToDo task = todoList.get(position);
                Intent in = new Intent(MainActivity.this, DisplayTaskDetails.class);
                TaskViewAdapter.todoList = todoList;
                in.putExtra("Position", position);
                startActivity(in);
                Toast.makeText(getApplicationContext(), task.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                location = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                // set title
                alertDialogBuilder.setTitle("Delete Task");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to delete this Task from todo list?!\n Title : " + todoList.get(location).getTitle() + "\n Detail : " + todoList.get(location).getDetail() + "\n Time : " + todoList.get(location).getTime())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, Delete task

                                ToDo listTitle = todoList.get(location);

                                //retrieving data from database corresponding to title,detail and time of the task to be deleted
                                Cursor c = db.rawQuery("SELECT * FROM todolist WHERE title='" + listTitle.getTitle() + "' and detail='" + listTitle.getDetail() + "' and time='" + listTitle.getTime() + "'", null);

                                if (c != null && c.getCount() > 0) {
                                    if (c.moveToFirst()) {
                                        do {
                                            //delete the record from database
                                            db.execSQL("DELETE FROM todolist WHERE title='" + listTitle.getTitle() + "' and detail='" + listTitle.getDetail() + "' and time='" + listTitle.getTime() + "'");
                                            deleteToast();
                                        } while (c.moveToNext());
                                    }
                                }
                                Intent in = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(in);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        }));

        prepareToDoList();
    }

    //Preparing the todoList
    private void prepareToDoList() {
        todoList.clear();

        //Getting Tasks from the database and adding them to the List
        Cursor c = db.rawQuery("SELECT * FROM todolist", null);
        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    todo = new ToDo(c.getString(0), c.getString(1), c.getString(2));
                    todoList.add(todo);
                } while (c.moveToNext());
            }
        }
        tAdapter.notifyDataSetChanged();
    }


    //method to create database or open database if exists
    protected void createDatabase() {
        db = openOrCreateDatabase("MarksTable", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS todolist(title VARCHAR,detail VARCHAR,time VARCHAR);");
    }

    //method to display toast when an item from to do list of recycler view is clicked
    public void displayToast(String str) {
        Toast.makeText(this, str + " pressed", Toast.LENGTH_SHORT).show();
    }

    //method to display a toast when task is successfully deleted from Database
    protected void deleteToast() {
        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

}
