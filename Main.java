package abc;

import java.util.ArrayList;
import java.util.Scanner;


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

class Doctor extends Person implements Assignable {
 private String specialization;

 public Doctor(int id, String name, int age, String specialization) {
     super(id, name, age);
     this.specialization = specialization;
 }

 public int getId() {
     return id;
 }

 @Override
 public String getRole() {
     return "Doctor";
 }

 @Override
 public void assignToWard(Ward ward) {
     ward.addDoctor(this);
 }

 public void display() {
     System.out.println(id + " | " + name + " | " + age + " | " + specialization);
 }
}


class Ward {
 private String name;
 private int capacity;
 private ArrayList<Patient> patients = new ArrayList<>();
 private ArrayList<Doctor> doctors = new ArrayList<>();

 public Ward(String name, int capacity) {
     this.name = name;
     this.capacity = capacity;
 }

 public String getName() {
     return name;
 }

 public boolean addPatient(Patient p) {
     if (patients.size() < capacity) {
         patients.add(p);
         return true;
     }
     System.out.println("Ward is Full!");
     return false;
 }

 public void addDoctor(Doctor d) {
     doctors.add(d);
     System.out.println("Doctor assigned to ward: " + name);
 }

 public void report() {
     System.out.println("Ward: " + name +
             " | Patients: " + patients.size() +
             "/" + capacity +
             " | Doctors: " + doctors.size());
 }
 
}


class Patient extends Person implements Assignable {
    private String disease;
    private Ward ward;
    private Doctor doctor;

    public Patient(int id, String name, int age, String disease) {
        super(id, name, age);
        this.disease = disease;
    }

    public int getId() {
        return id;
    }

    public void assignDoctor(Doctor doctor) {
        this.setDoctor(doctor);
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public void assignToWard(Ward ward) {
        if (ward.addPatient(this)) {
            this.setWard(ward);
        }
    }

    public void display() {
        System.out.println(id + " | " + name + " | " + age + " | " + disease);
    }

	public Ward getWard() {
		return ward;
	}

	public void setWard(Ward ward) {
		this.ward = ward;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}


class Hospital {
    ArrayList<Patient> patients = new ArrayList<>();
    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<Ward> wards = new ArrayList<>();

    public void addPatient(Patient p) {
        patients.add(p);
    }

    public void addDoctor(Doctor d) {
        doctors.add(d);
    }

    public void addWard(Ward w) {
        wards.add(w);
    }

    public Patient findPatient(int id) {
        for (Patient p : patients)
            if (p.getId() == id)
                return p;
        return null;
    }

    public Doctor findDoctor(int id) {
        for (Doctor d : doctors)
            if (d.getId() == id)
                return d;
        return null;
    }

    public Ward findWard(String name) {
        for (Ward w : wards)
            if (w.getName().equalsIgnoreCase(name))
                return w;
        return null;
    }

    public void report() {
        System.out.println("\n🏥 Hospital Report");
        for (Ward w : wards)
            w.report();
    }
}


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Hospital hospital = new Hospital();

        while (true) {
            System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Add Ward");
            System.out.println("4. Assign Patient to Ward");
            System.out.println("5. Assign Doctor to Ward");
            System.out.println("6. View Hospital Report");
            System.out.println("0. Exit");
            System.out.print("Choose Option: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Patient ID: ");
                    int pid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Name: ");
                    String pname = sc.nextLine();
                    System.out.print("Age: ");
                    int page = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Disease: ");
                    String disease = sc.nextLine();

                    hospital.addPatient(new Patient(pid, pname, page, disease));
                    System.out.println("Patient Added");
                    break;

                case 2:
                    System.out.print("Doctor ID: ");
                    int did = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Name: ");
                    String dname = sc.nextLine();
                    System.out.print("Age: ");
                    int dage = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Specialization: ");
                    String spec = sc.nextLine();

                    hospital.addDoctor(new Doctor(did, dname, dage, spec));
                    System.out.println("Doctor Added");
                    break;

                case 3:
                    sc.nextLine();
                    System.out.print("Ward Name: ");
                    String wname = sc.nextLine();
                    System.out.print("Capacity: ");
                    int cap = sc.nextInt();

                    hospital.addWard(new Ward(wname, cap));
                    System.out.println("Ward Added");
                    break;

                case 4:
                    System.out.print("Patient ID: ");
                    pid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Ward Name: ");
                    wname = sc.nextLine();

                    Patient p = hospital.findPatient(pid);
                    Ward w = hospital.findWard(wname);

                    if (p != null && w != null)
                        p.assignToWard(w);
                    else
                        System.out.println("Invalid Patient or Ward");
                    break;

                case 5:
                    System.out.print("Doctor ID: ");
                    did = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Ward Name: ");
                    wname = sc.nextLine();

                    Doctor d = hospital.findDoctor(did);
                    w = hospital.findWard(wname);

                    if (d != null && w != null)
                        d.assignToWard(w);
                    else
                        System.out.println("Invalid Doctor or Ward");
                    break;

                case 6:
                    hospital.report();
                    break;

                case 0:
                    System.out.println("Exiting System...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}
