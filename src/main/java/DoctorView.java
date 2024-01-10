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

        JFrame frame = new JFrame("Doktor Ekranına Hoşgeldiniz");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

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


        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel);


        frame.setLayout(new GridLayout(3,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
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
    public static void declareUnavailability(String date, String starting_hour, String finishing_hour, int DoctorId){
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            stmt.executeUpdate("insert into Unavailability values ('"+date+"', '"+starting_hour+"', '"+finishing_hour+"', "+DoctorId+")");}
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}