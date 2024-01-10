import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Kullanıcılar yeni hesap açabiliyor olmalı ve görebildikleri belli bilgiler var.
 */
public class PatientView {
     int patientId;
    public PatientView(int patientId) {
        this.patientId = patientId;
        JFrame frame = new JFrame("Hasta Ekranına Hoşgeldiniz");

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

        AtomicReference<String> start = new AtomicReference<>("1000-10-10");
        AtomicReference<String> end = new AtomicReference<>("3000-10-10");

        JButton showApps = new JButton("Randevularımı Göster");
        panel.add(showApps);

        showApps.addActionListener(e -> showAppsPatient(patientId, start.get(), end.get()));


        JButton tarihTamam = new JButton("Tamam");
        panel3.add(tarihTamam);


        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            checkDateFormat(startDate.getText());
            checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());

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
     * girilen tarih, tarih formatında mı diye bakar deyü düşündüm.
     * @param text
     */
    protected static void checkDateFormat(String text) {
        String[] dates = text.split("-");
        if(dates.length != 3){
            JFrame popup = new JFrame("HATA");
            popup.add(new JLabel("Haspam düzgün yaz şu tarihleri"));
            popup.add(new JLabel(" \"11-09-2001\" formatında olacak dedik bak"));

            popup.setLayout(new GridLayout(2,1));
            popup.setMinimumSize(new Dimension(600,70));
            popup.setVisible(true);
        }
        else{
            for(String s : dates){
                try{
                    Integer.parseInt(s);
                }
                catch(NumberFormatException nfe){
                    JFrame popup = new JFrame("yok");
                    popup.add(new JLabel("tarihi bozuk yazıyosun"));
                    popup.setVisible(true);
                }
            }

            System.out.println("iyisin");
        }

    }

    /**
     * şimdilik anlamsız bir "popup" çıkarmaktan ibaret yaptığı.
     */
    protected static void showPastApps() {
        JFrame popup = new JFrame("Geçmiş randevularınız");
        JLabel label = new JLabel();

        String apps = "LOREM IPSUM DOLOR SIT AMET";
        //apps = getApps(); falan gibi bir metot işte




        label.setText(apps);
        popup.add(label);
        popup.setMinimumSize(new Dimension(250,100));
        //popup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); cık, bunu korsam bütün uygulamayı kapatıyor.
        popup.setVisible(true);
    }

    private static void showAppsPatient(int patientId, String start, String end){
        JFrame popup = new JFrame("Your Appointments");
        JTextArea textArea = new JTextArea();

        String apps = "Patient Name       App Date        Starting Hour     Appointment Status     Appointment ID     Doctor Name\n\n";
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select p.patient_name, a.app_date, a.starting_hour, a.app_status,a.app_id, d.doctor_name " +
                    "from patient p, Appointment a, doctor d " +
                    "where p.patient_id = a.patient_id and p.patient_id = "+patientId+" and d.doctor_id = a.doctor_id and " +
                    "a.app_date > \""+start+"\" and a.app_date < \""+end+"\" ;");
            if(!rs.isBeforeFirst()){
                apps = "You have no registered appointments";
            }

            while(rs.next()){
                apps += rs.getString(1) + "            ";
                apps += rs.getString(2) + "     ";
                apps += rs.getString(3) + "             ";
                apps += rs.getString(4) + "                   ";
                apps += rs.getString(5) + "                       ";
                apps += rs.getString(6);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        textArea.setText(apps);
        popup.add(textArea);
        popup.setMinimumSize(new Dimension(800,100));
        popup.setVisible(true);
    }
}
