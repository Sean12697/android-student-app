package com.example.a16042079.studentappassignment;
import java.io.Serializable;

/**
 * Person.java a simple class which encapsulates the properties that a Person Object requires (including a constructor and getters/setters)
 * @author  Sean O'Mahoney - 16042079
 * @version 1.0
 * @see Class: Tester
 */
public class Person {
    private String name;
    private String gender;
    private String dob;
    private String address;
    private String postcode;

    public Person(String name, String gender, String dob, String address, String postcode) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.postcode = postcode;
    }

    public Person(String name) {
        this.name = name;
    }

    public Person() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    // Function I wrote to return type (Person, com.example.a16042079.studentapp.Student, any class that inherits it)
    public String type() {
        StringBuffer type = new StringBuffer("");
        String ref = toString();
        boolean end = false;
        for (int i = 0; i < ref.length() && !end; i++) {
            char current = ref.charAt(i);
            if (current == '@') {
                end = true;
            } else { type.append(current); }
        } return type.toString();
    }
}