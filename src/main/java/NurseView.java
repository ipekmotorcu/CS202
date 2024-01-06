import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NurseView {

    int nurseId;

    public NurseView(int nurseId){
        this.nurseId = nurseId;


        JFrame frame = new JFrame("Hemşire Ekranına Hoşgeldiniz");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();



        JLabel pass = new JLabel("Belli bir tarih aralığını görmek istiyorsanız giriniz: (\"10-09-1938\" formatında girmezseniz çalışmam)");
        JTextField startDate = new JTextField(10);
        startDate.setText("başlangıç");
        JTextField endDate = new JTextField(10);
        endDate.setText("bitiş");
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);

        JButton showRoomAvailabilities = new JButton("Show Room Availabilities");
        panel.add(showRoomAvailabilities);

        JButton showApps = new JButton("Show Upcoming Room Assignments");
        panel.add(showApps);

        showRoomAvailabilities.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    viewRoomAvailability();
            }
        });

        showApps.addActionListener(e -> {
            viewUpcomingRooms(nurseId);
        });

        JButton tarihTamam = new JButton("Tamam");
        panel3.add(tarihTamam);
        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            PatientView.checkDateFormat(startDate.getText());
            PatientView.checkDateFormat(endDate.getText());
            System.out.println(startDate.getText());
            System.out.println(endDate.getText()); //falan işte, şu an bilmiyorum
        });


        //panel.setLayout(new GridLayout());
        panel2.setLayout(new GridLayout());
        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS)); Olacak şey değil yahu


        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel);


        frame.setLayout(new GridLayout(3,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }

    /**
     * Bunda enjeksiyona karşı korunacak bir durum yok.
     */
    public static void viewRoomAvailability()  {
        JFrame frame = new JFrame("Room Availabilities");

        JTextArea text = new JTextArea("Lorem ipsum\nDolor sit amet");
        Statement stmt;
        ResultSet rs;
        try {
            stmt = DBConnection.getConnection().createStatement();
            rs = stmt.executeQuery("select * from for_nurse");

        String view = "date                 starting hour            finishing hour\n";
        while(rs.next()){
            view += rs.getString(1);
            view += "    "+rs.getString(2);
            view += "                    "+rs.getString(3)+ "\n";
            //view += "    "+rs.getString(4) + "\n";
        }
        text.setText(view);

        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        frame.add(text);
        //frame.add(label,BorderLayout.NORTH);
        //frame.add(text, BorderLayout.SOUTH);

        //frame.setLayout(new GridLayout(2,1));
        frame.setSize(700,500);
        frame.setVisible(true);
    }

    /**
     * uses the nurseId from the login page and shows the upcoming rooms for that nurse.
     * uses the app_status field in Appointment entity to check if the appointment is upcoming or if it passed.
     * @param nurseId the nurse id given by the caller.
     */
    public static void viewUpcomingRooms(int nurseId){
        JFrame frame = new JFrame("Your Upcoming Rooms");
        JTextArea txt = new JTextArea("You don't have any rooms lad. One should assign you something!");

        Statement stmt;
        ResultSet rs;
        try{
            stmt = DBConnection.getConnection().createStatement();
            rs = stmt.executeQuery("select ass.room_id from Appointment as app, assigns_room as ass where ass.nurse_id = "+ nurseId+ " and app.app_id = ass.app_id and app.app_status = 'Scheduled'");

            String rooms = "For the nurse having the id " + nurseId + " the following are the upcoming rooms:\n\n";
            while(rs.next()){
                rooms += "Room id: "+rs.getString(1)+"\n";
            }
            txt.setText(rooms);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }

        frame.add(txt);
        frame.setSize(700,500);
        frame.setVisible(true);
    }


}
