import javax.swing.*;
import java.awt.*;

public class WelcomingWindow {

    public WelcomingWindow(){
        JFrame frame = new JFrame("Sefa geldiniz!");

        JPanel patient = new JPanel();


        JButton patientButton = new JButton("Hastayım sana");

        patientButton.addActionListener(e -> {
            PatientLogin patientLogin = new PatientLogin();
        });

        JButton doctorButton = new JButton("El çek ilacımdan tabib");

        doctorButton.addActionListener(e -> {
            DoctorLogin doctorLogin = new DoctorLogin();
        });

        JButton nurseButton = new JButton("El ver bacım");

        nurseButton.addActionListener(e -> {
            NurseLogin nurseLogin = new NurseLogin();
        });
//Admin
        JButton adminButton = new JButton("Admin");

        adminButton.addActionListener(e -> {
            AdminLogin adminLogin = new AdminLogin();
        });

        patient.add(adminButton);
        patient.add(patientButton);
        patient.add(doctorButton);
        patient.add(nurseButton);
        frame.add(patient);


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
}
