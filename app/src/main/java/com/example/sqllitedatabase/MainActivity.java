package com.example.sqllitedatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText rollno, name, marks;
    Button add, delete, modify, view, viewall, show;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollno = findViewById(R.id.rollno);
        marks = findViewById(R.id.marks);
        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        modify = findViewById(R.id.modify);
        view = findViewById(R.id.view);
        viewall = findViewById(R.id.viewall);
        show = findViewById(R.id.show);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        modify.setOnClickListener(this);
        view.setOnClickListener(this);
        viewall.setOnClickListener(this);
        show.setOnClickListener(this);
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                if (rollno.getText().toString().trim().length() == 0 ||
                        name.getText().toString().trim().length() == 0 ||
                        marks.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    showMessage("Error", "Invalid Input");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + rollno.getText() + "','" + name.getText() +
                        "','" + marks.getText() + "');");
                showMessage("Success", "Record added");
                clearText();
                Toast.makeText(this, "Added" + add.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete:
                if (rollno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE rollno='" + rollno.getText() + "'");
                    showMessage("Success", "Record Deleted");
                } else {
                    showMessage("Error", "Invalid Rollno");
                }
                Toast.makeText(this, "Deleted" + delete.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.modify:
                if (modify.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                c = db.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + name.getText() + "',marks='" + marks.getText() +
                            "' WHERE rollno='" + rollno.getText() + "'");
                    showMessage("Success", "Record Modified");
                } else {
                    showMessage("Error", "Invalid Rollno");
                }
                Toast.makeText(this, "Modified" + modify.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.view:

                if (rollno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                c = db.rawQuery("SELECT * FROM student WHERE rollno='" + rollno.getText() + "'", null);
                if (c.moveToFirst()) {
                    name.setText(c.getString(1));
                    marks.setText(c.getString(2));
                } else {
                    showMessage("Error", "Invalid Rollno");
                    clearText();
                }
                Toast.makeText(this, "View is finished" + view.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.viewall:
            {
                c = db.rawQuery("SELECT * FROM student", null);
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    buffer.append("Rollno: " + c.getString(0) + "\n");
                    buffer.append("Name: " + c.getString(1) + "\n");
                    buffer.append("Marks: " + c.getString(2) + "\n\n");
                }
                showMessage("Student Details", buffer.toString());
        }
        Toast.makeText(this, "View all is finished" + viewall.getText(), Toast.LENGTH_SHORT).show();
        break;

        case R.id.show:
            showMessage("Student Record Application", "Developed By Pooja Pawar");
        }
        Toast.makeText(this, "shown" + show.getText(), Toast.LENGTH_SHORT).show();
    {
        showMessage("Developed By - ", "Ms.Pooja Pawar");

    }

}
    private void clearText(){
        rollno.setText("");
        name.setText("");
        marks.setText("");
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
