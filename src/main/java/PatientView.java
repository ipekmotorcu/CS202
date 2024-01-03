import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Kullanıcılar yeni hesap açabiliyor olmalı ve görebildikleri belli bilgiler var.
 */
public class PatientView {

    public PatientView(){
        JFrame frame = new JFrame("Hasta Ekranına Hoşgeldiniz");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        //JLabel id = new JLabel("TC Kimlik No");
        //JTextField idTxt = new JTextField(15);



        JLabel pass = new JLabel("Belli bir tarih aralığını görmek istiyorsanız giriniz: (\"10-09-1938\" formatında girmezseniz çalışmam)");
        JTextField startDate = new JTextField(10);
        startDate.setText("başlangıç");
        JTextField endDate = new JTextField(10);
        endDate.setText("bitiş");
        panel2.add(pass);

        panel3.add(startDate); panel3.add(endDate);

        JButton showApps = new JButton("Randevularımı Göster");
        panel.add(showApps);

        showApps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPastApps();
            }
        });

        JButton tarihTamam = new JButton("Tamam");
        panel3.add(tarihTamam);
        tarihTamam.addActionListener(e -> { //oha java'da lambda notasyonu var lan
            checkDateFormat(startDate.getText());
            checkDateFormat(endDate.getText());
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
}
