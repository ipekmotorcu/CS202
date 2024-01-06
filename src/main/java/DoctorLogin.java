import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DoctorLogin {
    public DoctorLogin(){
        JFrame frame = new JFrame("Doctor Login");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(400,100,400,250);
        //frame.setSize(new Dimension(600,400));
        frame.setResizable(false);
        frame.setLayout(null);
        Container c = frame.getContentPane();

        //JPanel panel = new JPanel();
        //JPanel panel2 = new JPanel();
        //JPanel tamam = new JPanel();

        JLabel id = new JLabel("ID Number");
        id.setSize(300, 30);
        id.setLocation(150, 60);
        c.add(id);

        JTextField idTxt = new JTextField(15); idTxt.setText("");
        idTxt.setSize(250,30);
        idTxt.setLocation(250, 60);
        c.add(idTxt);


        JLabel pass = new JLabel("Password");
        pass.setSize(300, 30);
        pass.setLocation(150, 95);
        c.add(pass);

        JTextField passTxt = new JTextField(15); passTxt.setText("");
        passTxt.setSize(250,30);
        passTxt.setLocation(250, 95);
        c.add(passTxt);


        JButton enter = new JButton("Enter");
        enter.setSize(100,30);
        enter.setLocation(200,130);
        enter.addActionListener(e-> {
            try{
                int doctorId= Integer.parseInt(idTxt.getText());
                String password= passTxt.getText();
                enter(doctorId,password);
            }

            catch(NumberFormatException nfe){
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Enter a valid ID number"));
                popup.setMinimumSize(new Dimension(250,150));
                popup.setLocation(500,200);
                popup.setVisible(true);
            }

        });
        c.add(enter);

        JButton forgot = new JButton("Forgot Pass");
        forgot.setSize(100,30);
        forgot.setLocation(350,130);
        forgot.addActionListener(e-> {
            JFrame forgotFrame = new JFrame("Sorry lad");
            forgotFrame.add(new JLabel("Refer to your Admin"));
            forgotFrame.setMinimumSize(new Dimension(250,150));
            forgotFrame.setLocation(500,200);
            forgotFrame.setVisible(true);
        });
        c.add(forgot);


        //frame.setLayout(new GridLayout(2,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
    Connection connection = DBConnection.getConnection();//bunu böyle yazmamız lazım prepared statement için
    /**
     * Bunun şifre ve kimlik numarasını kontrol falan etmesi lazım aslında.
     *
     * kimlik numarasını ve şifreyi kontrol ediyor geçerli değilse uyarıyor
     * Prepared statement kullandım burada çünkü hoca mailde öyle yazmış
     *
     * Hashleme kısmına bakmadım daha onun için databasedeki şifreleri de değiştirmemiz gerekecek!!
     */
    private void enter(int doctorId,String password) {



        try {
            PreparedStatement stmt = connection.prepareStatement("select doc_password  from Doctor where doctor_id = ? ");

            stmt.setString(1, String.valueOf(doctorId));
            ResultSet rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Incorrect ID or Password"));
                popup.setMinimumSize(new Dimension(250,150));
                popup.setLocation(500,200);
                popup.setVisible(true);
            }
            while(rs.next()){
               // System.out.println(rs.getString(1)+password);
                if (rs.getString(1).equals(password)){
                    DoctorView doctorView = new DoctorView(doctorId);
                }
                else{
                    JFrame popup = new JFrame("Nope");
                    popup.add(new JLabel("Enter a valid ID number and password"));
                    popup.setMinimumSize(new Dimension(250,150));
                    popup.setLocation(500,200);
                    popup.setVisible(true);
                }
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
