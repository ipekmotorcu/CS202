import javax.swing.*;
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
        JPanel panel5 = new JPanel();
        JPanel panel4 = new JPanel();//view



        //JLabel id = new JLabel("TC Kimlik No");
        //JTextField idTxt = new JTextField(15);



        JLabel pass = new JLabel("Belli bir tarih aralığını görmek istiyorsanız giriniz: (\"1938-10-09\" formatında girmezseniz çalışmam)");
        JTextField startDate = new JTextField(10);
        startDate.setText("başlangıç");
        JTextField endDate = new JTextField(10);
        endDate.setText("bitiş");
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);

        JButton showApps = new JButton("Randevularımı Göster");
        panel.add(showApps);

        AtomicReference<String> start = new AtomicReference<>("1000-10-10");
        AtomicReference<String> end = new AtomicReference<>("3000-10-10");

        showApps.addActionListener(e -> showAppsDoctor(doctorId, start.get(), end.get())); //buradaki "get"lerin tam nasıl çalıştığını anladığımı söyleyemem

        JButton tarihTamam = new JButton("Tamam");
        panel3.add(tarihTamam);
        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            PatientView.checkDateFormat(startDate.getText());
            PatientView.checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());
        });


        //panel.setLayout(new GridLayout());
        panel2.setLayout(new GridLayout());
        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS)); Olacak şey değil yahu

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



        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel);

        frame.add(panel4);


        frame.setLayout(new GridLayout(3,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800,400));
        frame.setLocation(300,100);
        frame.setVisible(true);
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
                popup.add(new JLabel("Something went wrong: "+e1));
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