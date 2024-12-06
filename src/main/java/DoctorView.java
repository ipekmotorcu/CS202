import com.mysql.cj.protocol.x.ReusableOutputStream;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

public class DoctorView {
    int doctorId;
    public DoctorView(int doctorId){
        this.doctorId =doctorId;

        JFrame frame = new JFrame("Doctor View");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();//view



        //JLabel id = new JLabel("TC Kimlik No");
        //JTextField idTxt = new JTextField(15);

        AtomicReference<String> start = new AtomicReference<>("1000-10-10");
        AtomicReference<String> end = new AtomicReference<>("3000-10-10");

        JLabel pass = new JLabel("If you want to search for a certain date range and department enter: (\"1938-10-09\" is the format you must follow)");
        JTextField startDate = new JTextField(10);
        startDate.setText(start.get());
        JTextField endDate = new JTextField(10);
        endDate.setText(end.get());
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);

        JButton showApps = new JButton("Show Appointments");
        panel.add(showApps);



        showApps.addActionListener(e -> {
            PatientView.checkDateFormat(startDate.getText());
            PatientView.checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());
            showAppsDoctor(doctorId, start.get(), end.get());
        }); //buradaki "get"lerin tam nasıl çalıştığını anladığımı söyleyemem

        JButton tarihTamam = new JButton("Tamam");
        //panel3.add(tarihTamam);
        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            PatientView.checkDateFormat(startDate.getText());
            PatientView.checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());
        });


        panel2.setLayout(new GridLayout());

        JButton show = new JButton("View Room Availability");
        panel4.add(show);
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    viewRoomAvailability();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JButton availability = new JButton("Declare Unavailability");
        panel4.add(availability);
        availability.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                declareUnavailability(doctorId);

            }
        });

        JButton assignRoom = new JButton("Assign Room to an Appointment");
        assignRoom.addActionListener(e ->{
            JFrame popup = new JFrame("Assign Room");
            JTextArea textArea = new JTextArea();
            popup.setLayout(null);
            String[] appIds = new String[20];
            appIds[0] = "No appointments";

            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select app_id from Appointment where doctor_id = "+doctorId+" ;");
                for(int i=0; rs.next(); i++){
                    if(i>=appIds.length)
                        appIds = expandStringArray(appIds);
                    appIds[i] = rs.getString(1);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JComboBox<String> apps = new JComboBox<>(appIds);

            //JComboBox<String> apps = new JComboBox<>(appIds);
            //int selectedAppId =  (Integer)apps.getSelectedItem(); EMİN BUNU DÜŞÜNECEK.


            String[] nurseIds = new String[20];
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select nurse_id, nurse_name from Nurse;"); //BURADA ADINI DA YAZZ İNSAN BU
                for(int i=0; rs.next(); i++){
                    if(i>=nurseIds.length)
                        nurseIds = expandStringArray(nurseIds);
                    nurseIds[i] = rs.getString(1) + " "+rs.getString(2);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JComboBox<String> nurses = new JComboBox<>(nurseIds);


            //int appId = (Integer)apps.getSelectedItem();














            int appId = 502;
            String[] roomIds = new String[20];
            try{
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select r.room_id from Room r, Occupied o, Appointment a where r.room_id = o.room_id and r.room_id and a.app_id = "+appId+" " +
                        "and a.app_date != o.occ_date;"); //DOUBLE CHECK
                for(int i=0; rs.next(); i++){
                    if(i>=roomIds.length)
                        roomIds = expandStringArray(roomIds);
                    roomIds[i] = rs.getString(1);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            //JComboBox<String> rooms = new JComboBox<>(roomIds);
            DefaultComboBoxModel<String> roomComboBoxModel = new DefaultComboBoxModel<>(roomIds);
            JComboBox<String> rooms = new JComboBox<>(roomComboBoxModel);

            popup.add(rooms);









            JButton assign = new JButton("Assign");
            assign.addActionListener(d -> {
                int appID = Integer.parseInt((String) apps.getSelectedItem());
                int nurseID = Integer.parseInt(nurses.getSelectedItem().toString().substring(0,3));
                System.out.println(nurseID);
                int roomID = Integer.parseInt((String)rooms.getSelectedItem());
                String date ="";
                try{
                    Statement stmt1 = DBConnection.getConnection().createStatement();
                    Statement stmt = DBConnection.getConnection().createStatement();
                    ResultSet rs1 = stmt1.executeQuery("select * from assigns_room where app_id = "+appID+" ;");
                    if(!rs1.next()){
                        stmt.executeUpdate("insert into assigns_room values ( "+ doctorId +" , "+ nurseID + " , " + roomID + " , "+ appID +" );");
                        ResultSet rs = stmt.executeQuery("select app_date from appointment where app_id = "+ appID+" ;");
                        while(rs.next())
                            date = rs.getString(1);

                        stmt.executeUpdate("insert into occupied values ( '"+ date+ "' ,  '08:00:00'  ,   '17:00:00'  , " +roomID+ ") ;"); //BUNUN İÇİN APPOINTMENTS'TAN DATE'İ ALMAK LAZIM
                    }
                    else {
                        JFrame error = new JFrame("Warning");
                        error.add(new JLabel("That appointment already has a room assigned!"));
                        error.setMinimumSize(new Dimension(400,200));
                        error.setVisible(true);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            popup.add(assign);

            apps.addActionListener(f-> {
                String[] roomIds2 = new String[10];
                try{
                    //int appId2 = Integer.parseInt((String) apps.getSelectedItem());
                    int appId2 = Integer.parseInt((String) apps.getSelectedItem());
                    Statement stmt = DBConnection.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("select r.room_id from Room r, Occupied o, Appointment a where r.room_id = o.room_id and r.room_id and a.app_id = "+appId2+" " +
                            "and a.app_date != o.occ_date;"); //DOUBLE CHECK
                    for(int i=0; rs.next(); i++){
                        if(i>=roomIds2.length)
                            roomIds2 = expandStringArray(roomIds2);
                        roomIds2[i] = rs.getString(1);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                roomComboBoxModel.removeAllElements();
                for(String s : roomIds2)
                    roomComboBoxModel.addElement(s);
            });


            popup.add(new JLabel("Appointment id"));
            popup.add(apps);
            popup.add(new JLabel("Nurse"));
            popup.add(nurses);
            popup.add(new JLabel("Room Number"));
            popup.add(rooms);
            popup.add(assign);



            popup.setLayout(new GridLayout(4,2));
            popup.setMinimumSize(new Dimension(600,400));
            popup.setVisible(true);
        });


        panel.add(assignRoom);


        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel);
        frame.add(panel4);

        frame.setLayout(new GridLayout(4,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800,400));
        frame.setLocation(300,100);
        frame.setVisible(true);
    }

    private String[] expandStringArray(String[] appIds) {
        int length = appIds.length;
        String[] expanded = new String[length*2];
        for(int i=0; i<length; i++)
            expanded[i] = appIds[i];
        return expanded;
    }

    private static void showAppsDoctor(int doctorId, String start, String end) {
        JFrame popup = new JFrame("Your Appointments");
        JTextArea textArea = new JTextArea();

        String apps ="Appointment Date      Patient Name       Starting Hour          Appointment Status\n\n";

        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select a.app_date, p.patient_name, a.starting_hour, a.app_status\n" +
                    "from doctor d, appointment a, patient p\n" +
                    "where d.doctor_id = a.doctor_id and p.patient_id = a.patient_id and d.doctor_id = "+doctorId+" and " +
                    "a.app_date > \""+start+"\" and a.app_date < \""+end+"\" ;");
            if(!rs.isBeforeFirst()){
                apps = "You have no assigned appointments";
            }
            while(rs.next()){
                apps += rs.getString(1) + "               ";
                apps += rs.getString(2) + "             ";
                apps += rs.getString(3) + "                  ";
                apps += rs.getString(4)+"\n";
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        textArea.setText(apps);
        popup.add(textArea);
        popup.setMinimumSize(new Dimension(700,250));
        popup.setVisible(true);
    }

    /**
     * ÇALIŞMAYOR. ZATEN BÖYLE ÇALIŞMAMASI LAZIM.
     * ÇÜNKÜ HACKERLER...
     * @param date
     * @param starting_hour
     * @param finishing_hour
     * @param DoctorId
     */

    static Connection connection = DBConnection.getConnection();
    public static boolean declareUnavailability(int doctorId){


        JFrame frame1 = new JFrame("Declare Unavailability");
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame1.setBounds(400,100,400,250);
        //frame.setSize(new Dimension(600,400));
        frame1.setResizable(false);
        frame1.setLayout(null);
        Container c = frame1.getContentPane();

        JLabel date1 = new JLabel("date");
        date1.setSize(300, 30);
        date1.setLocation(150, 10);
        c.add(date1);

        JTextField dateTxt = new JTextField(15); dateTxt.setText("");
        dateTxt.setSize(250,30);
        dateTxt.setLocation(270, 10);
        c.add(dateTxt);


        JLabel starting_hour = new JLabel("starting hour");
        starting_hour.setSize(300, 30);
        starting_hour.setLocation(150, 60);
        c.add(starting_hour);

        JTextField starting_hourTxt = new JTextField(15); starting_hourTxt.setText("");
        starting_hourTxt.setSize(250,30);
        starting_hourTxt.setLocation(270, 60);
        c.add(starting_hourTxt);


        JLabel finishing_hour = new JLabel("finishing hour");
        finishing_hour.setSize(300, 30);
        finishing_hour.setLocation(150, 110);
        c.add(finishing_hour);

        JTextField finishing_hourTxt = new JTextField(15); finishing_hourTxt.setText("");
        finishing_hourTxt.setSize(250,30);
        finishing_hourTxt.setLocation(270, 110);
        c.add(finishing_hourTxt);

        JButton enter = new JButton("Add");
        enter.setSize(100,30);
        enter.setLocation(270,210);
        c.add(enter);
        enter.addActionListener(e-> {
            try {
                int Id = doctorId;
                String date = dateTxt.getText();
                String startingHour =  starting_hourTxt.getText();
                String finishingHour = finishing_hourTxt.getText();

                PreparedStatement stmt = null;


                    stmt = connection.prepareStatement("insert into Unavailability values ( ?,  ?  ,?,?)");
                    stmt.setString(1, String.valueOf(date));
                    stmt.setString(2, String.valueOf(startingHour));
                    stmt.setString(3, String.valueOf(finishingHour));
                    stmt.setString(4, String.valueOf(Id));
                    stmt.executeUpdate();
                    frame1.dispose();

                } catch (SQLException e1) {

                JFrame popup = new JFrame("Nope");
                popup.add(new JLabel("You're writing something in an incorrect form: "));
                popup.setMinimumSize(new Dimension(1000, 150));
                popup.setLocation(500, 200);
                popup.setVisible(true);
                }

            });
        frame1.setMinimumSize(new Dimension(700,400));
        frame1.setVisible(true);

        return true;

    }

    public static void viewRoomAvailability() throws SQLException {
        JFrame frame = new JFrame("Room Availabilities");
        JLabel label = new JLabel("date    starting hour     finishing hour");
        //label.setSize(new Dimension(700,300)); BURASI GARİP GÖRÜNÜYOR.
        //label.setFont();

        JTextArea text = new JTextArea("Lorem ipsum\nDolor sit amet");


        Statement stmt = DBConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from Occupied");

        String view = "room no    date                 starting hour            finishing hour\n";
        while(rs.next()){
            view += rs.getString(4)+"              ";
            view += rs.getString(1);
            view += "      "+rs.getString(2);
            view += "                  "+rs.getString(3)+ "\n";
            //view += "    "+rs.getString(4) + "\n";
        }
        text.setText(view);

        frame.add(text);
        //frame.add(label,BorderLayout.NORTH);
        //frame.add(text, BorderLayout.SOUTH);

        //frame.setLayout(new GridLayout(2,1));
        frame.setSize(700,500);
        frame.setVisible(true);



    }


}