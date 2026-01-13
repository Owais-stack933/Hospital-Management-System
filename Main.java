package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    Hospital hospital = new Hospital();
    TextArea output = new TextArea();

    @Override
    public void start(Stage stage) {

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField extraField = new TextField();

        idField.setPromptText("ID");
        nameField.setPromptText("Name / Ward Name");
        ageField.setPromptText("Age");
        extraField.setPromptText("Disease / Specialization / Capacity");
        
        Button addPatientBtn = new Button("Add Patient");
        Button addDoctorBtn = new Button("Add Doctor");
        Button addWardBtn = new Button("Add Ward");
        Button assignPatientBtn = new Button("Assign Patient to Ward");
        Button assignDoctorBtn = new Button("Assign Doctor to Ward");
        Button reportBtn = new Button("View Report");

        output.setEditable(false);
        output.setPrefHeight(200);

        addPatientBtn.setOnAction(e -> {
            hospital.patients.add(
                    new Patient(
                            Integer.parseInt(idField.getText()),
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            extraField.getText()
                    )
            );
            output.appendText("Patient Added\n");
        });

        addDoctorBtn.setOnAction(e -> {
            hospital.doctors.add(
                    new Doctor(
                            Integer.parseInt(idField.getText()),
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            extraField.getText()
                    )
            );
            output.appendText("Doctor Added\n");
        });

        addWardBtn.setOnAction(e -> {
            hospital.wards.add(
                    new Ward(nameField.getText(),
                            Integer.parseInt(extraField.getText()))
            );
            output.appendText("Ward Added\n");
        });

        assignPatientBtn.setOnAction(e -> {
            Patient p = hospital.findPatient(Integer.parseInt(idField.getText()));
            Ward w = hospital.findWard(nameField.getText());
            if (p != null && w != null) {
                p.assignToWard(w);
                output.appendText("Patient Assigned to Ward\n");
            } else {
                output.appendText("❌ Patient or Ward not found\n");
            }
        });

        assignDoctorBtn.setOnAction(e -> {
            Doctor d = hospital.findDoctor(Integer.parseInt(idField.getText()));
            Ward w = hospital.findWard(nameField.getText());
            if (d != null && w != null) {
                d.assignToWard(w);
                output.appendText("Doctor Assigned to Ward\n");
            } else {
                output.appendText("Doctor or Ward not found\n");
            }
        });

        reportBtn.setOnAction(e -> {
            output.clear();
            output.appendText("========== HOSPITAL REPORT ==========\n\n");

            for (Ward w : hospital.wards) {

                output.appendText("Ward Name: " + w.name + "\n");
                output.appendText("Capacity: " + w.capacity + "\n\n");

                // Doctors Section
                output.appendText("Doctors Assigned:\n");
                if (w.doctors.isEmpty()) {
                    output.appendText("  No doctors assigned\n");
                } else {
                    for (Doctor d : w.doctors) {
                        output.appendText("  ID: " + d.id +
                                ", Name: " + d.name +
                                ", Age: " + d.age +
                                ", Specialization: " + d.specialization + "\n");
                    }
                }

                // Patients Section
                output.appendText("\nPatients Admitted:\n");
                if (w.patients.isEmpty()) {
                    output.appendText("  No patients admitted\n");
                } else {
                    for (Patient p : w.patients) {
                        output.appendText("  ID: " + p.id +
                                ", Name: " + p.name +
                                ", Age: " + p.age +
                                ", Disease: " + p.disease + "\n");
                    }
                }

                output.appendText("\n------------------------------------\n\n");
            }
        });


        VBox layout = new VBox(10,
                idField, nameField, ageField, extraField,
                addPatientBtn, addDoctorBtn, addWardBtn,
                assignPatientBtn, assignDoctorBtn,
                reportBtn, output
        );

        layout.setPadding(new Insets(15));

        stage.setTitle("Hospital Management System");
        stage.setScene(new Scene(layout, 420, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


abstract class Person {
    protected int id;
    protected String name;
    protected int age;

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public abstract String getRole();
}

interface Assignable {
    void assignToWard(Ward ward);
}

class Patient extends Person implements Assignable {
    String disease;
    Ward ward;

    public Patient(int id, String name, int age, String disease) {
        super(id, name, age);
        this.disease = disease;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public void assignToWard(Ward ward) {
        if (ward.addPatient(this)) {
            this.ward = ward;
        }
    }
}

class Doctor extends Person implements Assignable {
    String specialization;

    public Doctor(int id, String name, int age, String specialization) {
        super(id, name, age);
        this.specialization = specialization;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public void assignToWard(Ward ward) {
        ward.addDoctor(this);
    }
}

class Ward {
    String name;
    int capacity;
    ArrayList<Patient> patients = new ArrayList<>();
    ArrayList<Doctor> doctors = new ArrayList<>();

    public Ward(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public boolean addPatient(Patient p) {
        if (patients.size() < capacity) {
            patients.add(p);
            return true;
        }
        return false;
    }

    public void addDoctor(Doctor d) {
        doctors.add(d);
    }
}

class Hospital {
    ArrayList<Patient> patients = new ArrayList<>();
    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<Ward> wards = new ArrayList<>();

    Patient findPatient(int id) {
        for (Patient p : patients)
            if (p.id == id) return p;
        return null;
    }

    Doctor findDoctor(int id) {
        for (Doctor d : doctors)
            if (d.id == id) return d;
        return null;
    }

    Ward findWard(String name) {
        for (Ward w : wards)
            if (w.name.equalsIgnoreCase(name)) return w;
        return null;
    }
}
