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

        JFrame frame = new JFrame("Nurse View");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        //JLabel id = new JLabel("TC Kimlik No");
        //JTextField idTxt = new JTextField(15);


//Bu kısıma gerek var mı not almamışız
       /* JLabel pass = new JLabel("Belli bir tarih aralığını görmek istiyorsanız giriniz: (\"10-09-1938\" formatında girmezseniz çalışmam)");
        JTextField startDate = new JTextField(10);
        startDate.setText("başlangıç");
        JTextField endDate = new JTextField(10);
        endDate.setText("bitiş");
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);
*/
        JButton showApps = new JButton("View Room Availability");
        panel.add(showApps);

        JButton viewUpcoming = new JButton("View Upcoming Rooms");
        panel.add(viewUpcoming);

        showApps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //PatientView.showPastApps();
                try {
                    viewRoomAvailability();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        viewUpcoming.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //PatientView.showPastApps();
                try {
                    viewUpcomingRooms();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /*JButton tarihTamam = new JButton("Tamam");
        panel3.add(tarihTamam);
        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            PatientView.checkDateFormat(startDate.getText());
            PatientView.checkDateFormat(endDate.getText());
            System.out.println(startDate.getText());
            System.out.println(endDate.getText()); //falan işte, şu an bilmiyorum
        });*/


        //panel.setLayout(new GridLayout());
        panel2.setLayout(new GridLayout());
        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS)); Olacak şey değil yahu


        frame.add(panel2);
        frame.add(panel);//butonlar bu panelde
        frame.add(panel3);



        frame.setLayout(new GridLayout(3,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800,400));
        frame.setLocation(300,100);
        frame.setVisible(true);
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

    /**
     * Şu anda zamanına bakmadan o hemşireye atanan bütün odaları gösteriyor
     * Ekrana tarihi de yazdırabiliriz ama gerekli mi bilemedim
     *
     */
    public void viewUpcomingRooms() throws SQLException {
        JFrame frame = new JFrame("Upcoming Rooms");
        JLabel label = new JLabel("");


        JTextArea text = new JTextArea();


        Statement stmt = DBConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("    select a.room_id ,r.room_type from assigns_room a natural join room r where nurse_id = "+ this.nurseId);

        String view = "Room No    Room Type\n";
        while(rs.next()){
            view += rs.getString(1)+"                  ";
            view += rs.getString(2)+ "\n";

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
