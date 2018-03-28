package com.example.a16042079.studentappassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class overviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Bundle extras = getIntent().getExtras();

        final Boolean adding = (boolean)extras.get("adding");
        final String txtName = (String)extras.get("name");
        final String txtGender = (String)extras.get("gender");
        final String txtDOB = (String)extras.get("dob");
        final String txtAddress = (String)extras.get("address");
        final String txtPostcode = (String)extras.get("postcode");
        final String txtStudentNumber = (String)extras.get("studentNumber");
        final String txtCourseTitle = (String)extras.get("courseTitle");
        final String txtStartDate = (String)extras.get("startDate");
        final String txtBursary = (String)extras.get("bursary");
        final String txtEmail = (String)extras.get("email");

        Button btnEdit = (Button)findViewById(R.id.btnEdit);

        EditText name = (EditText)findViewById(R.id.name); name.setText(txtName); name.setEnabled(false);
        EditText gender = (EditText)findViewById(R.id.gender); gender.setText(txtGender); gender.setEnabled(false);
        EditText dob = (EditText)findViewById(R.id.dob); dob.setText(txtDOB); dob.setEnabled(false);
        EditText address = (EditText)findViewById(R.id.address); address.setText(txtAddress); address.setEnabled(false);
        EditText postcode = (EditText)findViewById(R.id.postcode); postcode.setText(txtPostcode); postcode.setEnabled(false);
        EditText studentNumber = (EditText)findViewById(R.id.studentNumber); studentNumber.setText(txtStudentNumber); studentNumber.setEnabled(false);
        EditText courseTitle = (EditText)findViewById(R.id.courseTitle); courseTitle.setText(txtCourseTitle); courseTitle.setEnabled(false);
        EditText startDate = (EditText)findViewById(R.id.startDate); startDate.setText(txtStartDate); startDate.setEnabled(false);
        EditText bursary = (EditText)findViewById(R.id.bursary); bursary.setText(txtBursary); bursary.setEnabled(false);
        EditText email = (EditText)findViewById(R.id.email); email.setText(txtEmail); email.setEnabled(false);

        setTitle(name.getText() + " - Details");

        btnEdit.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), detailsActivity.class);
            intent.putExtra("name", txtName);
            intent.putExtra("gender", txtGender);
            intent.putExtra("dob", txtDOB);
            intent.putExtra("address", txtAddress);
            intent.putExtra("postcode", txtPostcode);
            intent.putExtra("studentNumber", txtStudentNumber);
            intent.putExtra("courseTitle", txtCourseTitle);
            intent.putExtra("startDate", txtStartDate);
            intent.putExtra("bursary", txtBursary);
            intent.putExtra("email", txtEmail);
            intent.putExtra("adding", adding);
            startActivity(intent);
            //finish();
        }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
