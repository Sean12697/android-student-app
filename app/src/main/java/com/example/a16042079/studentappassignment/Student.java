package com.example.a16042079.studentappassignment;
import com.example.a16042079.studentappassignment.Person;
import java.io.Serializable;

/**
 * com.example.a16042079.studentapp.Student.java a simple class which encapsulates the properties that a com.example.a16042079.studentapp.Student Object requires (including a constructor and getters/setters)
 * @author  Sean O'Mahoney - 16042079
 * @version 1.0
 * @see Class: Tester
 */
public class Student extends Person implements Serializable {
    private int studentNumber;
    private String courseTitle;
    private String startDate;
    private float bursary;
    private String email;

    public Student(String name, String gender, String dob, String address, String postcode, int studentNumber,
                   String courseTitle, String startDate, float bursary, String email) {
        super(name, gender, dob, address, postcode);
        this.studentNumber = studentNumber;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.bursary = bursary;
        this.email = email;
    }

    public Student(String name, String email) {
        super(name);
        this.email = email;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public float getBursary() {
        return bursary;
    }

    public void setBursary(float bursary) {
        this.bursary = bursary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}