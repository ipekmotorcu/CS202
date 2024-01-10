import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;


public class Main {
    public static void main(String[] args) throws Exception {
        //PatientSignUp signUp = new PatientSignUp();

        PatientView patientView = new PatientView(301);

        //WelcomingWindow welcomingWindow = new WelcomingWindow();

        //NurseView nurseView = new NurseView(201);

        //NurseView.viewRoomAvailability();

        //DoctorView.declareUnavailability("2024-05-15", "11:00:00", "13:00:00", 911);
        //System.out.println(Cryptography.encrypt("pass123",Cryptography.generateSecretKey()));

        //PatientLogin  lg = new PatientLogin();
        //DoctorView d = new DoctorView(102);
        //AdminView admin = new AdminView(401);


        Statement stmt =  DBConnection.getConnection().createStatement();
        stmt.executeUpdate("update Appointment\n " +
                "set app_status = \"Scheduled\"\n " +
                "where app_id = "+ 501 +" ;");
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
