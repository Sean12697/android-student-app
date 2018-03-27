package com.example.a16042079.studentappassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<Student> students = new ArrayList<>();
    HashMap<String, String> allStudents = new HashMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView studentList = (ListView)findViewById(R.id.students);

        HttpURLConnection urlConnection;
        InputStream in = null;
        try {
            URL url = new URL("http://radikaldesign.co.uk/sandbox/studentapi/getallstudents.php?apikey=3ae2b20cca");
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } String response = sharedFunctions.convertStreamToString(in);
        //System.out.println(response);


        try {
            JSONArray jsonArray = new JSONArray(response);

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

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, android.R.layout.simple_list_item_2,
                new String[]{"Name", "Email"},
                new int[]{android.R.id.text1, android.R.id.text2});

        Iterator it = allStudents.entrySet().iterator();
        while (it.hasNext())
        {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("Name", pair.getKey().toString());
            resultsMap.put("Email", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        studentList.setAdapter(adapter);

        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "you pressed " + allStudents.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), detailsActivity.class);
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
                startActivity(intent);
            }
        });

        studentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long l) {

                final String name = students.get(i).getName();

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete " + name + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int btnNum) {
                                try {
                                    deleteStudent(students.get(i).getStudentNumber());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, name + " Deleted", Toast.LENGTH_SHORT).show();
                                onCreate(savedInstanceState);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                return false;
            }
        });
    }

    public String deleteStudent(int id) throws IOException {
        return sharedFunctions.serverCallTest("http://radikaldesign.co.uk/sandbox/studentapi/delete.php", "apikey=3ae2b20cca&studentnumber=" + id);
    }

    public void onBackPressed() {
        // Toast.makeText(detailsActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
    }
}
