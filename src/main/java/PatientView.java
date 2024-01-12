import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Kullanıcılar yeni hesap açabiliyor olmalı ve görebildikleri belli bilgiler var.
 */
public class PatientView {
     int patientId;
    public PatientView(int patientId) {
        this.patientId = patientId;
        JFrame frame = new JFrame("Patient View");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        //JLabel id = new JLabel("TC Kimlik No");
        //JTextField idTxt = new JTextField(15);

        AtomicReference<String> start = new AtomicReference<>("1000-10-10");
        AtomicReference<String> end = new AtomicReference<>("3000-10-10");
        AtomicReference<String> department = new AtomicReference<>("");


        JLabel pass = new JLabel("If you want to search for a certain date range and department enter: (\"1938-10-09\" is the format you must follow)");
        JTextField startDate = new JTextField(10);
        startDate.setText(start.get());
        JTextField endDate = new JTextField(10);
        endDate.setText(end.get());
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);



        JButton showApps = new JButton("Show Appointments");
        panel4.add(showApps);

        String[] optionsDep = {"Select Department", "Cardiology", "Neurology","Orthopedics"};
        JComboBox<String>  depComboBox= new JComboBox<>(optionsDep);

        panel3.add(depComboBox);

        showApps.addActionListener(e -> {
            checkDateFormat(startDate.getText());
            checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());
            department.set((String)depComboBox.getSelectedItem());

            showAppsPatient(patientId, start.get(), end.get(), department.get());
        });



        JButton tarihTamam = new JButton("Apply");
        //panel3.add(tarihTamam);


        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            checkDateFormat(startDate.getText());
            checkDateFormat(endDate.getText());
            start.set(startDate.getText());
            end.set(endDate.getText());
            department.set((String)depComboBox.getSelectedItem());

            System.out.println(startDate.getText());
            System.out.println(endDate.getText()); //falan işte, şu an bilmiyorum
            System.out.println(department.get());
        });



        panel2.setLayout(new GridLayout());

        JButton cancelApp = new JButton("Cancel Appointment");
        cancelApp.addActionListener(e -> {
            appCancelFrame(patientId);
        });
        panel.add(cancelApp);


        JButton makeApp = new JButton("Book Appointment");
        makeApp.addActionListener(e -> {
            newAppointmentFrame();
        });

        panel.add(makeApp);


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

    /**
     * bunu daha yazmadım. beraber şaaparız.
     * beraber şaaptık ama daha bitmedi. yapıcam akşam
     */
    private void newAppointmentFrame() {
        JFrame frame1 = new JFrame("New Appointment");
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame1.setBounds(400,100,400,250);
        //frame.setSize(new Dimension(600,400));
        frame1.setResizable(false);
        frame1.setLayout(null);
        Container c = frame1.getContentPane();

        JLabel date1 = new JLabel("Date");
        date1.setSize(300, 30);
        date1.setLocation(150, 10);
        c.add(date1);

        JTextField dateTxt = new JTextField(15); dateTxt.setText("");
        dateTxt.setSize(250,30);
        dateTxt.setLocation(270, 10);
        c.add(dateTxt);


        JLabel starting_hour = new JLabel("Starting Hour");
        starting_hour.setSize(300, 30);
        starting_hour.setLocation(150, 60);
        c.add(starting_hour);

        JTextField starting_hourTxt = new JTextField(15); starting_hourTxt.setText("");
        starting_hourTxt.setSize(250,30);
        starting_hourTxt.setLocation(270, 60);
        c.add(starting_hourTxt);


        JLabel department = new JLabel("Doctor Department");
        department.setSize(300, 30);
        department.setLocation(150, 110);
        c.add(department);

        JTextField departmentTxt = new JTextField(15); departmentTxt.setText("");
        departmentTxt.setSize(250,30);
        departmentTxt.setLocation(270, 110);
        c.add(departmentTxt);



//shjdlfbnd

//sldmfnaşdksf



        JButton enter = new JButton("Enter");
        enter.setSize(100,30);
        enter.setLocation(270,210);
        c.add(enter);
        enter.addActionListener(e -> {
            //BURAYA TARİHİ FALAN KONTROL EDEN ZIKKIMAT EKLENECEK
            /*
            JLabel doctors = new JLabel("Doctor Name");
            doctors.setSize(300, 30);
            doctors.setLocation(150, 160);
            c.add(doctors);*/



            //departmentTxt'ten departman ismini al
            //sql'le oradaki doktorların isimlerini çekip "docNmaes"e ekle.
            String[] docNames = {"Select a Doctor"};

            JComboBox doctorsCombo = new JComboBox(docNames); //departmentTxt.setText("");
            doctorsCombo.setSize(250,30);
            doctorsCombo.setLocation(270, 160);
            c.add(doctorsCombo);
            System.out.println("lololo");
            frame1.setVisible(true);
        });

        frame1.setMinimumSize(new Dimension(700,400));
        frame1.setVisible(true);
    }

    private void appCancelFrame(int patientId) {
        JFrame popup = new JFrame("Cancel Appointment");
        popup.setBounds(300,100,550,300);
        popup.setLayout(null);

        //String[] appsString = {"2000-10-11","2200-10-11","2300-10-11" };
        String[] appsString = new String[10];

        AtomicInteger appId = new AtomicInteger(-1);

        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select a.app_date, a.starting_hour, d.doctor_name, a.app_status, a.app_id \n" +
                    "from patient p, Appointment a, doctor d \n" +
                    "where p.patient_id = a.patient_id and d.doctor_id = a.doctor_id and " +
                    "p.patient_id = "+patientId+" ;");

            for(int i=0; rs.next();i++){
                String addendum = "";
                addendum += rs.getString(1) + "      ";
                addendum += rs.getString(2) + "       ";
                addendum += rs.getString(3) + "       ";
                addendum += rs.getString(4) + "       ";
                addendum += rs.getString(5);
                appsString[i] = addendum;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        JComboBox<String> apps = new JComboBox<>(appsString);
        apps.setSize(450,40);
        apps.setLocation(50,50);

        /*String forId = (String)apps.getSelectedItem();
        appId = Integer.parseInt(forId.substring(forId.length()-3)); //iptal edilecek randevunun id'si
        System.out.println(appId);
*/

        JButton cancel = new JButton("Cancel Appointment");
        cancel.setSize(150,20);
        cancel.setLocation(75,200);
        //int finalAppId = appId.get();
        cancel.addActionListener(e ->{
            try{
                String forId = (String)apps.getSelectedItem();
                appId.set(Integer.parseInt(forId.substring(forId.length() - 3))); //iptal edilecek randevunun id'si
                System.out.println(appId.get());


                Statement stmt =  DBConnection.getConnection().createStatement();
                stmt.executeUpdate("update Appointment\n " +
                        "set app_status = \"Canceled\"\n " +
                        "where app_id = "+ appId +" ;");

                JFrame ok = new JFrame("Congratulations");
                JLabel message = new JLabel("Your appointment was successfully canceled");
                ok.add(message);
                ok.setBounds(350,250,300,150);
                ok.setVisible(true);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton returner = new JButton("Return");
        returner.setSize(150,20);
        returner.setLocation(300,200);
        returner.addActionListener(e -> popup.dispose());


        popup.add(apps);
        popup.add(cancel);
        popup.add(returner);

        popup.setResizable(false);
        popup.setVisible(true);
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


    private static void showAppsPatient(int patientId, String start, String end, String department){
        JFrame popup = new JFrame("Your Appointments");
        JTextArea textArea = new JTextArea();
        String sqlFiller = "";
        if(!department.equals("")){
            switch(department){
                case"Cardiology":
                    sqlFiller = " d.dep_id=1 and ";
                    break;
                case"Neurology":
                    sqlFiller = " d.dep_id=2 and ";
                    break;
                case "Orthopedics":
                    sqlFiller = " d.dep_id=3 and ";
                    break;
                default:
                    sqlFiller = " ";
            }
        }
        String apps = "Patient Name       App Date        Starting Hour     Appointment Status     Appointment ID     Doctor Name\n\n";
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select p.patient_name, a.app_date, a.starting_hour, a.app_status,a.app_id, d.doctor_name " +
                    "from patient p, Appointment a, doctor d " +
                    "where p.patient_id = a.patient_id and p.patient_id = "+patientId+" and d.doctor_id = a.doctor_id and a.app_status = \"Scheduled\" and " +
                    sqlFiller +
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
                apps += rs.getString(6) + "\n";
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
