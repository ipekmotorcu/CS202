import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicReference;

public class AdminView {
    int adminId;
    String type;
    int dep;

    public AdminView(int adminId){
        this.adminId=adminId;

        JFrame frame = new JFrame("Admin View");


        Panel panel =new Panel();
        frame.add(panel);
        JButton add = new JButton("Add Medical Staff");
        panel.add(add);

        add.addActionListener(e -> {
            addMedicalStaff();

        });











        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
    Connection connection = DBConnection.getConnection();
    public void addMedicalStaff(){




        JFrame frame = new JFrame("Add Medical Staff");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(400,100,400,250);
        //frame.setSize(new Dimension(600,400));
        frame.setResizable(false);
        frame.setLayout(null);
        Container c = frame.getContentPane();

        JLabel name = new JLabel("Name");
        name.setSize(300, 30);
        name.setLocation(150, 10);
        c.add(name);

        JTextField nameTxt = new JTextField(15); nameTxt.setText("");
        nameTxt.setSize(250,30);
        nameTxt.setLocation(270, 10);
        c.add(nameTxt);


        JLabel id = new JLabel("ID Number");
        id.setSize(300, 30);
        id.setLocation(150, 60);
        c.add(id);

        JTextField idTxt = new JTextField(15); idTxt.setText("");
        idTxt.setSize(250,30);
        idTxt.setLocation(270, 60);
        c.add(idTxt);


        JLabel pass = new JLabel("Password");
        pass.setSize(300, 30);
        pass.setLocation(150, 110);
        c.add(pass);

        JTextField passTxt = new JTextField(15); passTxt.setText("");
        passTxt.setSize(250,30);
        passTxt.setLocation(270, 110);
        c.add(passTxt);

        String[] optionsDep = {"Select Department", "Cardiology", "Neurology","Orthopedics"};
        JLabel selectedOptionsLabel = new JLabel("Select Department: ");
        JComboBox<String>  depComboBox= new JComboBox<>(optionsDep);


        depComboBox.addActionListener(e -> {
            String selectedOptions = (String) depComboBox.getSelectedItem();
            selectedOptionsLabel.setText("Select Department: " + selectedOptions);
            this.dep =depComboBox.getSelectedIndex();

        });
        depComboBox.setSize(200,30);
        depComboBox.setLocation(270,160);
        c.add(depComboBox);


        String[] options = {"Select Medical Staff Type", "Doctor", "Nurse"};
        JLabel selectedOptionLabel = new JLabel("Select Medical Staff Type: ");
        JComboBox<String>  optionsComboBox= new JComboBox<>(options);


        optionsComboBox.addActionListener(e -> {
                String selectedOption = (String) optionsComboBox.getSelectedItem();
                selectedOptionLabel.setText("Select Medical Staff Type: " + selectedOption);
               this.type =optionsComboBox.getSelectedItem().toString();

        });
        type=this.type;//çok saçma bir çözüm oldu ama başka türlü dışarı alamadım hata veriyordu

        optionsComboBox.setSize(200,30);
        optionsComboBox.setLocation(270,210);
        c.add(optionsComboBox);


        JButton enter = new JButton("Add");
        enter.setSize(100,30);
        enter.setLocation(270,260);
        c.add(enter);
        enter.addActionListener(e -> {
            try {
                int Id = Integer.parseInt(idTxt.getText());
                String password = passTxt.getText();
                String staffName = nameTxt.getText();
                String department = String.valueOf(this.dep);
                if (type.equals("Doctor")){

                    password= Cryptography.encrypt(password,Cryptography.generateSecretKey());

                    PreparedStatement stmt = connection.prepareStatement("insert into Doctor values ( ?,  ?  ,?,?)");
                    stmt.setString(1, String.valueOf(Id));
                    stmt.setString(2, String.valueOf(password));
                    stmt.setString(3, String.valueOf(staffName));
                    stmt.setString(4, String.valueOf(department));
                    stmt.executeUpdate();
                    JFrame popup = new JFrame("Yep");
                    popup.add(new JLabel("Your Account has been Successfully Created"));
                    popup.setMinimumSize(new Dimension(300,150));
                    popup.setLocation(500,200);

                    popup.setVisible(true);
                } else if (type.equals("Nurse")) {
                    password= Cryptography.encrypt(password,Cryptography.generateSecretKey());

                    PreparedStatement stmt = connection.prepareStatement("insert into Nurse values ( ?,  ?  ,?)");
                    stmt.setString(1, String.valueOf(Id));
                    stmt.setString(2, String.valueOf(password));
                    stmt.setString(3, String.valueOf(staffName));

                    stmt.executeUpdate();

                    JFrame popup = new JFrame("Yep");
                    popup.add(new JLabel("Your Account has been Successfully Created"));
                    popup.setMinimumSize(new Dimension(300,150));
                    popup.setLocation(500,200);

                    popup.setVisible(true);
                }
                else{
                    JFrame popup = new JFrame("Nope");
                    popup.add(new JLabel("Please choose Medical Staff Type"));
                    popup.setMinimumSize(new Dimension(250, 150));
                    popup.setLocation(500, 200);
                    popup.setVisible(true);
                }


            } catch (Exception ex) {
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Something went wrong"+ex));
                popup.setMinimumSize(new Dimension(250, 150));
                popup.setLocation(500, 200);
                popup.setVisible(true);
            }
        });
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);




    }

}
