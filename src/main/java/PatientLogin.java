import javax.swing.*;
import java.awt.*;

public class PatientLogin extends Login {
    public PatientLogin(){
        super("Patient");

        Container c = super.frame.getContentPane();

        JButton signin = new JButton("Sign In");
        signin.setSize(100,30);
        signin.setLocation(350,160);
        signin.addActionListener(e -> {
            PatientSignUp patientSignUp = new PatientSignUp();
        });


        c.add(signin);
    }
}
