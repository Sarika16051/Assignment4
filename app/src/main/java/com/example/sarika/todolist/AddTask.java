package com.example.sarika.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddTask extends ActionBarActivity {

    private EditText titleText;
    private EditText detailText;
    private Button addToDo;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddTask);
        setSupportActionBar(toolbar);

        //For enabliing Hierarchical Navigation
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        titleText = (EditText) findViewById(R.id.titleText);
        detailText = (EditText) findViewById(R.id.detailText);
        addToDo = (Button) findViewById(R.id.addToDo);

        //Creating database if not exist otherwise opening the existing database
        createDatabase();

        //When submit button is clicked, a new Task is added to the database
        addToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString();
                String detail = detailText.getText().toString();

                java.util.Date utilDate = new java.util.Date();
                java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());

                String time = "" + sqlDate;

                if (title.equalsIgnoreCase("") || detail.equalsIgnoreCase("") || time.equalsIgnoreCase("")) {
                    error();
                } else {
                    //Inserting values to database
                    String query = "INSERT INTO todolist VALUES('" + title + "','" + detail + "','" + time + "');";
                    db.execSQL(query);
                    displaySuccess();
                    Intent in = new Intent(AddTask.this, MainActivity.class);
                    startActivity(in);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //method to toast an error message
    public void error() {
        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
    }

    //method to toast successful addition of data to database
    public void displaySuccess() {
        Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
    }


    //method to create database or open database if exists
    protected void createDatabase() {
        db = openOrCreateDatabase("MarksTable", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS todolist(title VARCHAR,detail VARCHAR,time VARCHAR);");
    }

}
