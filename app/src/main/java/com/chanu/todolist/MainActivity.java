package com.chanu.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import java.util.Calendar;
import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText id, sub, task, desc, deu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        myDb = new DatabaseHelper(this);

        //Drop Table
        //myDb.dropTable();

        id = findViewById(R.id.id);
        sub = findViewById(R.id.sub);
        task = findViewById(R.id.task);
        desc = findViewById(R.id.desc);
        deu = findViewById(R.id.due);

        //For Select Due Date
        deu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();

                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Set the selected date in the EditText or TextView
                                //SQLite Format  YYYY-MM-DD
                                deu.setText( String.valueOf(year)+ "-" + String.valueOf(month + 1) + "-" + String.valueOf( dayOfMonth));
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
    }

    public void addTask(View view){
        String tSub = sub.getText().toString();
        String tTask = task.getText().toString();
        if(!tSub.isEmpty() && !tTask.isEmpty()){
            boolean isInsert = myDb.addTask(
                    tSub,
                    tTask,
                    desc.getText().toString(),
                    deu.getText().toString()
            );

            if(isInsert){
                Toast.makeText(this, "Task Added Successfully.", Toast.LENGTH_LONG).show();
                sub.setText(null);
                task.setText(null);
                desc.setText(null);
                deu.setText(null);
            }else {
                Toast.makeText(this, "Task NOT Added!", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Please provide data!", Toast.LENGTH_LONG).show();
        }
    }


    public void viewTask(View view){
        Cursor results = myDb.getAllTask();
        if(results.getCount()==0){
            showMessage("Error:", "No task is available");
            return;
        }else {
            StringBuffer buffer = new StringBuffer();
            while (results.moveToNext()) {
                buffer.append("ID : " + results.getString(0) + "\n");
                buffer.append("Subject : " + results.getString(1) + "\n");
                buffer.append("Task : " + results.getString(2) + "\n");
                buffer.append("Description : " + results.getString(3) + "\n");
                buffer.append("Deu Date : " + results.getString(4) + "\n");
                String isComplete = "NO";
                if (Integer.valueOf(results.getString(5)) == 1) {
                    isComplete = "Yes";
                }
                buffer.append("Complete : " + isComplete + "\n\n");
            }
            showMessage("List of Task", buffer.toString());
            return;
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // dismisses the dialog
            }
        });
        builder.show();
    }

    public void updateTask(View view){
        String tID = id.getText().toString();
        String tSub = sub.getText().toString();
        String tTask = task.getText().toString();
        if(!tSub.isEmpty() && !tTask.isEmpty() && !tID.isEmpty()){
            boolean isUpdate = myDb.updateTask(
                    tID,
                    tSub,
                    tTask,
                    desc.getText().toString(),
                    deu.getText().toString()
            );

            if(isUpdate){
                Toast.makeText(this, "ID \""+tID+"\" is Updated Successfully.", Toast.LENGTH_LONG).show();
                id.setText(null);
                sub.setText(null);
                task.setText(null);
                desc.setText(null);
                deu.setText(null);
            }else {
                Toast.makeText(this, "ID \""+tID+"\" is NOT Updated!", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Please provide data!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTask(View view){
        String tID = id.getText().toString();
        if(!tID.isEmpty()){
            Integer deleteCount = myDb.deleteTask(tID);

            if(deleteCount!=0){
                Toast.makeText(this, "ID \""+tID+"\" is Deleted Successfully.", Toast.LENGTH_LONG).show();
                id.setText(null);
            }else {
                Toast.makeText(this, "ID \""+tID+"\" is NOT Deleted!", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Please provide data!", Toast.LENGTH_LONG).show();
        }
    }

    public void completeTask(View view){
        String tID = id.getText().toString();
        if(!tID.isEmpty()){
            boolean isComplete = myDb.makeAsComplete(tID);

            if(isComplete){
                Toast.makeText(this, "ID \""+tID+"\" is Make as Complete Successfully.", Toast.LENGTH_LONG).show();
                id.setText(null);
            }else {
                Toast.makeText(this, "ID \""+tID+"\" is NOT Deleted!", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Please provide data!", Toast.LENGTH_LONG).show();
        }
    }

}