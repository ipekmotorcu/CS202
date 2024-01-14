import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
            frame.dispose(); //bu satır yetiyor önceki pencereyi kapatmak için :p

        });





        JButton patientS = new JButton("Patient Statistics");
        panel.add(patientS);

        patientS.addActionListener(e -> showPatientStatistics());

        JButton roomS = new JButton("Room Statistics");
        panel.add(roomS);
        roomS.addActionListener(e -> {
            JFrame popup = new JFrame("Rooms booked to Appointment ratio for each department");
            String roomStat="";
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select dep.dep_name, (count(s.app_id)/count(a.app_id))\n" +
                        "from Appointment a natural left join assigns_room s, Department dep, Doctor d\n" +
                        "where (a.doctor_id = d.doctor_id and dep.dep_id = d.dep_id)\n" +
                        "group by dep.dep_id;");
                while(rs.next()){
                    roomStat += rs.getString(1) + ":  " + rs.getString(2) + "\n";
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            popup.add(new JTextArea(roomStat));
            popup.setMinimumSize(new Dimension(300, 300));
            popup.setLocation(500, 200);

            popup.setVisible(true);

        });

        JButton nurseS = new JButton("Nurse Statistics");
        panel.add(nurseS);

        nurseS.addActionListener(e -> {
            JFrame popup = new JFrame("# of assigned unique nurses to rooms booked ratio for each department");
            String nurseStat="";
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select dep.dep_name, count(distinct s.nurse_id)/count(s.room_id) \n" +
                        "from assigns_room s, Doctor d, Department dep\n" +
                        "where s.doctor_id = d.doctor_id and dep.dep_id = d.dep_id\n" +
                        "group by dep.dep_id;");
                while(rs.next()){
                    nurseStat += rs.getString(1) + ":  " + rs.getString(2) + "\n";
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            popup.add(new JTextArea(nurseStat));
            popup.setMinimumSize(new Dimension(300, 300));
            popup.setLocation(500, 200);

            popup.setVisible(true);

        });
        JButton doctorS = new JButton("Doctor Statistics");
        panel.add(doctorS);

        doctorS.addActionListener(e -> {
            JFrame popup = new JFrame("doctor which books the most rooms for each department");
            String doctorStat="";
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select max(count), T.doc_name, T.dep_name\n" +
                        "from(select d.doctor_name as doc_name, d.dep_id as dep_id, dep.dep_name as dep_name, count(*) as count\n" +
                        " from doctor d, assigns_room a, department dep\n" +
                        " where d.doctor_id = a.doctor_id and d.dep_id = dep.dep_id\n" +
                        " group by d.doctor_id) as T\n" +
                        "group by T.dep_id,T.doc_name, T.dep_name;");
                while(rs.next()){
                    doctorStat += rs.getString(3) + ":  " + rs.getString(2) + "\n";
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            popup.add(new JTextArea(doctorStat));
            popup.setMinimumSize(new Dimension(300, 300));
            popup.setLocation(500, 200);

            popup.setVisible(true);

        });

        JButton mostS = new JButton("Most Statistics");
        //panel.add(mostS);

        mostS.addActionListener(e -> {
            JFrame popup = new JFrame("The most booked room for each department");
            String mostStat="";
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select s.room_id, dep.dep_name\n" +
                        "from assigns_room s, Doctor d, Department dep\n" +
                        "where s.doctor_id = d.doctor_id and d.dep_id = dep.dep_id and (\n" +
                        "(select count(*) from assigns_room s3 where s3.room_id=s.room_id)>= all(\n" +
                        "select count(r.room_id)\n" +
                        "from assigns_room s2, Room r\n" +
                        "where s2.room_id = r.room_id and s2.doctor_id = d.doctor_id\n" +
                        "group by r.room_id))\n" +
                        "group by dep.dep_id");
                while(rs.next()){
                    mostStat += rs.getString(1) + ":  " + rs.getString(2) + "\n";
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            popup.add(new JTextArea(mostStat));
            popup.setMinimumSize(new Dimension(300, 300));
            popup.setLocation(500, 200);

            popup.setVisible(true);

        });


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800,400));
        frame.setLocation(300,100);
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
                popup.add(new JLabel("Something went wrong: "+ex));
                popup.setMinimumSize(new Dimension(250, 150));
                popup.setLocation(500, 200);
                popup.setVisible(true);
            }
        });
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);




    }
    public void showPatientStatistics(){
        JFrame frame = new JFrame("Patient Statistics");
       // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(400,100,400,250);

        frame.setResizable(false);
        frame.setLayout(null);
        Container c = frame.getContentPane();

        JLabel startingDate = new JLabel("Starting Date");
        startingDate.setSize(300, 30);
        startingDate.setLocation(150, 10);
        c.add(startingDate);

        JTextField startingDateTxt = new JTextField(15); startingDateTxt.setText("");
        startingDateTxt.setSize(250,30);
        startingDateTxt.setLocation(270, 10);
        c.add(startingDateTxt);


        JLabel finishingDate = new JLabel("Finishing Date");
        finishingDate.setSize(300, 30);
        finishingDate.setLocation(150, 60);
        c.add(finishingDate);

        JTextField finishingDateTxt = new JTextField(15); finishingDateTxt.setText("");
        finishingDateTxt.setSize(250,30);
        finishingDateTxt.setLocation(270, 60);
        c.add(finishingDateTxt);


        JButton show = new JButton("Show");
        show.setSize(100,30);
        show.setLocation(270,260);
        c.add(show);
        show.addActionListener(e -> {
            try {
                String starting = startingDateTxt.getText();
                String finishing = finishingDateTxt.getText();


                PreparedStatement stmt = connection.prepareStatement("select dep.dep_name, count(a.patient_id)\n" +
                        "from Appointment a, Department dep, Doctor d\n" +
                        "where a.doctor_id = d.doctor_id AND dep.dep_id = d.dep_id AND ? < app_date AND\n" +
                        "app_date < ? \n" +
                        "group by dep.dep_id");
                stmt.setString(1, String.valueOf(starting));
                stmt.setString(2, String.valueOf(finishing));

                ResultSet rs=stmt.executeQuery();
                String patientStat="";
                while(rs.next()){
                   patientStat+= rs.getString(1) +": "+ rs.getString(2)+"\n";
                }
                JFrame popup = new JFrame("The number of patients over a time period and the departments where they received medical care");
                popup.add(new JTextArea(patientStat));
                popup.setMinimumSize(new Dimension(600, 300));
                popup.setLocation(500, 200);

                popup.setVisible(true);
            } catch (SQLException ex) {
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Please enter the date in this format: 2023-03-03"));
                popup.setMinimumSize(new Dimension(500, 150));
                popup.setLocation(500, 200);
                popup.setVisible(true);
            } catch (Exception ex) {
                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("Please enter the date in this format: 2023-03-03"));
                popup.setMinimumSize(new Dimension(500, 150));
                popup.setLocation(500, 200);
                popup.setVisible(true);
            }});

            frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }

}
