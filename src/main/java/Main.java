import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.PathIterator;
import java.sql.SQLException;
import java.util.EventListener;

public class Main {
    public static void main(String[] args) throws SQLException {
        //PatientSignUp signUp = new PatientSignUp();

        //PatientView patientView = new PatientView();

        //WelcomingWindow welcomingWindow = new WelcomingWindow();

        //NurseView nurseView = new NurseView();

        //NurseView.viewRoomAvailability();

        DoctorView.declareUnavailability("2024-05-15", "11:00:00", "13:00:00", 911);

    }

    public static void denemeler(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        JLabel doctor = new JLabel("doktor");
        JLabel nurse = new JLabel("hemş");

        panel.add(doctor);
        JTextField textField = new JTextField("Enter Doctor ID");
        panel.add(textField);
        panel.add(nurse);
        JTextField textField2 = new JTextField("Enter Nurse ID");

        JButton button = new JButton("tıklasanaa");
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setText("uuu, nays");
            }
        });

        panel.add(textField2);



        frame.add(panel);




        frame.setMinimumSize(new Dimension(400,400));
        panel.setLayout(new GridLayout(3,2));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }


}
