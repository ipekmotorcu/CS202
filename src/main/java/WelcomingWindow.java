import javax.swing.*;
import java.awt.*;

public class WelcomingWindow {

    public WelcomingWindow(){
        JFrame frame = new JFrame("Sefa geldiniz!");

        JPanel patient = new JPanel();


        JButton patientButton = new JButton("Hastayım sana");

        patientButton.addActionListener(e -> {
            PatientView patientView = new PatientView();
        });

        JButton doctorButton = new JButton("El çek ilacımdan tabib");

        doctorButton.addActionListener(e -> {
            DoctorView doctorView = new DoctorView();
        });

        patient.add(patientButton);
        patient.add(doctorButton);
        frame.add(patient);


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
}
