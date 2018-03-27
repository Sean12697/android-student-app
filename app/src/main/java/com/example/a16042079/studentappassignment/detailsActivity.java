package com.example.a16042079.studentappassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class detailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        final Boolean adding = (boolean)extras.get("adding");

        EditText name = (EditText)findViewById(R.id.name); name.setText((String)extras.get("name"));
        EditText gender = (EditText)findViewById(R.id.gender); gender.setText((String)extras.get("gender"));
        EditText dob = (EditText)findViewById(R.id.dob); dob.setText((String)extras.get("dob"));
        EditText address = (EditText)findViewById(R.id.address); address.setText((String)extras.get("address"));
        EditText postcode = (EditText)findViewById(R.id.postcode); postcode.setText((String)extras.get("postcode"));
        EditText studentNumber = (EditText)findViewById(R.id.studentNumber); studentNumber.setText((String)extras.get("studentNumber")); if (!adding) studentNumber.setEnabled(false);
        EditText courseTitle = (EditText)findViewById(R.id.courseTitle); courseTitle.setText((String)extras.get("courseTitle"));
        EditText startDate = (EditText)findViewById(R.id.startDate); startDate.setText((String)extras.get("startDate"));
        EditText bursary = (EditText)findViewById(R.id.bursary); bursary.setText((String)extras.get("bursary"));
        EditText email = (EditText)findViewById(R.id.email); email.setText((String)extras.get("email"));

        if (adding) setTitle("Add Student Details"); else setTitle(name.getText() + " - Details");

        Button btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener()
        { @Override
            public void onClick(View v) {
                Student stu = new Student(
                        ((EditText)findViewById(R.id.name)).getText().toString(),
                        ((EditText)findViewById(R.id.gender)).getText().toString(),
                        ((EditText)findViewById(R.id.dob)).getText().toString(),
                        ((EditText)findViewById(R.id.address)).getText().toString(),
                        ((EditText)findViewById(R.id.postcode)).getText().toString(),
                        Integer.parseInt(((EditText)findViewById(R.id.studentNumber)).getText().toString()),
                        ((EditText)findViewById(R.id.courseTitle)).getText().toString(),
                        ((EditText)findViewById(R.id.startDate)).getText().toString(),
                        Float.parseFloat(((EditText)findViewById(R.id.studentNumber)).getText().toString()),
                        ((EditText)findViewById(R.id.email)).getText().toString()
                );
                HttpURLConnection urlConnection;
                InputStream in = null;
                Gson gson = new Gson();
            try {
                // If started with the intent to add a student, used the returned string from that, else update like originally intended
                Toast.makeText(detailsActivity.this, adding ? addStudent(gson.toJson(stu)) : updateStudent(gson.toJson(stu)), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        });
    }

    public void onBackPressed() {
        // Toast.makeText(detailsActivity.this, "You going back", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public String addStudent(String json) throws IOException {
        return sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/add.php", "apikey=3ae2b20cca&json=" + json);
    }

    public String updateStudent(String json) throws IOException {
        return sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/update.php", "apikey=3ae2b20cca&json=" + json);
    }
}
