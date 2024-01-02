import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        JLabel doctor = new JLabel("doktor");
        JLabel nurse = new JLabel("hemş");

        panel.add(doctor);
        JTextField textField = new JTextField("Enter Doctor ID");
        panel.add(textField);
        panel.add(nurse);
        JTextField textField2 = new JTextField("Enter Nurse ID");
        JTextField textField3 = new JTextField("deneme3");
        JTextField textField4 = new JTextField("deneme4");

        panel.add(textField2);
        panel.add(textField3);
        panel.add(textField4);




        frame.add(panel);




        frame.setMinimumSize(new Dimension(100,100));
        panel.setLayout(new GridLayout(3,2));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
