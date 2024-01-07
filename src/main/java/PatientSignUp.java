import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class PatientSignUp {

    public PatientSignUp() {
        JFrame frame = new JFrame("Patient Sign up");
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

        JPasswordField passTxt = new JPasswordField(15); passTxt.setText("");
        passTxt.setSize(250,30);
        passTxt.setLocation(270, 110);
        c.add(passTxt);

        JLabel pass2 = new JLabel("Confirm Password");
        pass2.setSize(300, 30);
        pass2.setLocation(150, 160);
        c.add(pass2);

        JPasswordField passTxt2 = new JPasswordField(15); passTxt.setText("");
        passTxt2.setSize(250,30);
        passTxt2.setLocation(270, 160);
        c.add(passTxt2);

        JButton enter = new JButton("Sign up");
        enter.setSize(100,30);
        enter.setLocation(270,210);
        enter.addActionListener(e-> {
            try{
                int Id= Integer.parseInt(idTxt.getText());
                String password= passTxt.getText();
                String patientName= nameTxt.getText();

                if(passTxt.getText().equals(passTxt2.getText())){ //kayıt ekranlarında genelde böyle olduğu için bunu ekledim
                    if(enter(Id,password,patientName))
                        frame.dispose();
                }
                else{
                    JFrame popup = new JFrame("Nope");
                    popup.add(new JLabel("Error! Confirm Password Not Match"));
                    popup.setMinimumSize(new Dimension(250,150));
                    popup.setLocation(500,200);
                    popup.setVisible(true);
                }

            }

            catch(NumberFormatException nfe){
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Enter a valid ID number"));
                popup.setMinimumSize(new Dimension(250,150));
                popup.setLocation(500,200);
                popup.setVisible(true);
            } catch (Exception ex) {
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Something Went Wrong :/"));
                popup.setMinimumSize(new Dimension(250,150));
                popup.setLocation(500,200);
                popup.setVisible(true);

            }

        });
        c.add(enter);




        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
    Connection connection = DBConnection.getConnection();//bunu böyle yazmamız lazım prepared statement için

    private boolean enter(int Id,String password,String name) throws Exception {
       // PreparedStatement stmt = connection.prepareStatement("select "+userType+"_password  from "+type+" where "+type+"_id = ? ");




        password= Cryptography.encrypt(password,Cryptography.generateSecretKey());

        PreparedStatement stmt = connection.prepareStatement("insert into Patient values ( ?,  ?  ,?)");
        stmt.setString(1, String.valueOf(Id));
        stmt.setString(2, String.valueOf(password));
        stmt.setString(3, String.valueOf(name));
        stmt.executeUpdate();


        JFrame popup = new JFrame("Yep");
        popup.add(new JLabel("Your Account has been Successfully Created"));
        popup.setMinimumSize(new Dimension(300,150));
        popup.setLocation(500,200);

        popup.setVisible(true);

        return true;
    }
}
