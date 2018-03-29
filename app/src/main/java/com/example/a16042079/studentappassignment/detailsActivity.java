package com.example.a16042079.studentappassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;

public class detailsActivity extends AppCompatActivity {

    ArrayList<EditText> texts = new ArrayList<>(); // public to be accessible in other methods
    // Needed for async
    Student stu = new Student();
    String responce = "";
    Boolean adding = false;
    String doing = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        adding = (boolean)extras.get("adding");
        doing = adding ? "added" : "updated";

        // VIEW DECLARTIONS, SET TEXTS AND ADD TO ARRAYLIST
        EditText name = (EditText)findViewById(R.id.name); name.setText((String)extras.get("name")); texts.add(name);
        EditText gender = (EditText)findViewById(R.id.gender); gender.setText((String)extras.get("gender")); texts.add(gender);
        EditText dob = (EditText)findViewById(R.id.dob); dob.setText((String)extras.get("dob")); texts.add(dob);
        EditText address = (EditText)findViewById(R.id.address); address.setText((String)extras.get("address")); texts.add(address);
        EditText postcode = (EditText)findViewById(R.id.postcode); postcode.setText((String)extras.get("postcode")); texts.add(postcode);
        EditText studentNumber = (EditText)findViewById(R.id.studentNumber); studentNumber.setText((String)extras.get("studentNumber"));  texts.add(studentNumber);
        if (!adding) studentNumber.setEnabled(false);
        EditText courseTitle = (EditText)findViewById(R.id.courseTitle); courseTitle.setText((String)extras.get("courseTitle")); texts.add(courseTitle);
        EditText startDate = (EditText)findViewById(R.id.startDate); startDate.setText((String)extras.get("startDate")); texts.add(startDate);
        EditText bursary = (EditText)findViewById(R.id.bursary); bursary.setText((String)extras.get("bursary")); texts.add(bursary);
        EditText email = (EditText)findViewById(R.id.email); email.setText((String)extras.get("email")); texts.add(email);
        Button btnSave = (Button)findViewById(R.id.btnSave); btnSave.setEnabled(false);

        // Used to run a function when any edit text view has been modified
        for (EditText t : texts) {
            t.addTextChangedListener(new TextWatcher() {
                @Override public void afterTextChanged(Editable s) {
                    checkFields();
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }

        // Used to change the title and button depending if the user is adding a student, or editing an existing one
        if (adding) {
            setTitle("Add Student Details");
            btnSave.setText("Add Student");
        } else {
            setTitle(name.getText() + " - Edit Details");
            btnSave.setText("Save Changes");
        }

        // Creates a student object from fields text, then sends over to the server parsed in json (with Gson)
        btnSave.setOnClickListener(new View.OnClickListener()
        { @Override
            public void onClick(View v) {
                stu = new Student(
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
                ); sendStu send = new sendStu(); send.execute();
        }
        });
    }

    // Disables save button if any field is blank, could also have other validation inside
    public void checkFields() {
        Boolean empty = false;
        Button btnSave = (Button)findViewById(R.id.btnSave);
        for (int i = 0; i < texts.size() && !empty; i++) if (texts.get(i).getText().toString().isEmpty()) empty = true;
        if (empty) btnSave.setEnabled(false); else btnSave.setEnabled(true);
    }

    public void onBackPressed() {
        // Toast.makeText(detailsActivity.this, "You going back", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    // ----------------------------------------------------------- ASYNC CLASS -----------------------------------------------------------

    private class sendStu extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Gson gson = new Gson();
                // If started with the intent to add a student, used the returned string from that, else update like originally intended
                return adding ? addStudent(gson.toJson(stu)) : updateStudent(gson.toJson(stu));
            } catch (IOException e) {
                e.printStackTrace();
                return "Error!";
            }
        }

        protected void onPostExecute (String responce)
        {
            // If the response is 1 (successful), state if the student id added/updated, else pop up the server response then go back to the main screen
            Toast.makeText(detailsActivity.this, responce.equals(Integer.toString(1)) ? stu.getName() + " " + doing : responce, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        protected String addStudent(String json) throws IOException {
            return sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/add.php", "apikey="+sharedFunctions.apiKey+"&json=" + json);
        }

        protected String updateStudent(String json) throws IOException {
            return sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/update.php", "apikey="+sharedFunctions.apiKey+"&json=" + json);
        }
    }
}
