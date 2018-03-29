package com.example.a16042079.studentappassignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Student> students = new ArrayList<>();
    LinkedHashMap<String, String> allStudents = new LinkedHashMap<>(); // LINKED DUE TO NOT BEING IN ORDER WITH A HASHMAP
    // FOR ASYNC
    JSONArray jsonArray = new JSONArray();
    ListView studentList;
    Context context;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        students = new ArrayList<>();
        allStudents = new LinkedHashMap<>();
        context = this;

        studentList = (ListView)findViewById(R.id.students);
        Button btnAdd = (Button)findViewById(R.id.add);

        // START ASYNC
        getAllStu getAllStu = new getAllStu();
        getAllStu.execute();

        //  TO VIEW DETAILS OF A STUDENT
        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "you pressed " + allStudents.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), overviewActivity.class);
                intent.putExtra("name", students.get(i).getName());
                intent.putExtra("gender", students.get(i).getGender());
                intent.putExtra("dob", students.get(i).getDob());
                intent.putExtra("address", students.get(i).getAddress());
                intent.putExtra("postcode", students.get(i).getPostcode());
                intent.putExtra("studentNumber", Integer.toString(students.get(i).getStudentNumber()));
                intent.putExtra("courseTitle", students.get(i).getCourseTitle());
                intent.putExtra("startDate", students.get(i).getStartDate());
                intent.putExtra("bursary", Float.toString(students.get(i).getBursary()));
                intent.putExtra("email", students.get(i).getEmail());
                intent.putExtra("adding", false);
                startActivity(intent);
            }
        });

        // TO ADD A STUDENT
        btnAdd.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), detailsActivity.class);
                intent.putExtra("adding", true);
                startActivity(intent);
            }
        });

        // TO DELETE A STUDENT
        studentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long l) {

                final String name = students.get(i).getName();

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete " + name + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() { // If yes is clicked
                            public void onClick(DialogInterface d, int btnNum) {
                                try {
                                    sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/delete.php", "apikey="+sharedFunctions.apiKey+"&studentnumber=" + students.get(i).getStudentNumber());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, name + " Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return false;
            }
        });
    }

    // ----------------------------------------------------------- ASYNC CLASS -----------------------------------------------------------

    private class getAllStu extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(Void... voids) {

            // NETWORK CALL
            HttpURLConnection urlConnection;
            InputStream in = null;
            try {
                URL url = new URL("http://radikaldesign.co.uk/sandbox/studentapi/getallstudents.php?apikey="+sharedFunctions.apiKey);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } String response = sharedFunctions.convertStreamToString(in);

            // Parse responce
            try {
                jsonArray = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        protected void onPostExecute (JSONArray jsonArray)
        {
            // ADDING STUDENTS TO GLOBAL VARIBLES
            try {

                for (int i=0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getJSONObject(i).get("name").toString();
                    String gender  = jsonArray.getJSONObject(i).get("gender").toString();
                    String dob = jsonArray.getJSONObject(i).get("dob").toString();
                    String address = jsonArray.getJSONObject(i).get("address").toString();
                    String postcode = jsonArray.getJSONObject(i).get("postcode").toString();
                    int studentNumber = Integer.parseInt(jsonArray.getJSONObject(i).get("studentNumber").toString());
                    String courseTitle = jsonArray.getJSONObject(i).get("courseTitle").toString();
                    String startDate = jsonArray.getJSONObject(i).get("startDate").toString();
                    float bursary = Float.parseFloat(jsonArray.getJSONObject(i).get("bursary").toString());
                    String email = jsonArray.getJSONObject(i).get("email").toString();
                    students.add(new Student(name, gender, dob, address, postcode, studentNumber, courseTitle, startDate, bursary, email));
                    allStudents.put(name, email);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ADDING TO VIEWS
            List<HashMap<String, String>> listItems = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(context, listItems, android.R.layout.simple_list_item_2,
                    new String[]{"Name", "Email"},
                    new int[]{android.R.id.text1, android.R.id.text2});

            for (Map.Entry<String, String> entry : allStudents.entrySet())
            {
                HashMap<String, String> resultsMap = new HashMap<>();
                Map.Entry pair = entry;
                resultsMap.put("Name", pair.getKey().toString());
                resultsMap.put("Email", pair.getValue().toString());
                listItems.add(resultsMap);
            } studentList.setAdapter(adapter);
        }
    }
}