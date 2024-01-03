import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ismi iyi olmamış olabilir. yeni kayıt oluştururken bunu kullanırız diye düşündüm.
 */
public class PatientSignUp {
    /**
     * biliyorum, biliyorum çirkin
     */
    public PatientSignUp(){
        JFrame frame = new JFrame("Register Frame");

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel tamam = new JPanel();

        JLabel id = new JLabel("TC Kimlik No");
        JTextField idTxt = new JTextField(15);
        idTxt.setText("***************");

        panel.add(id); panel.add(idTxt);

        JLabel pass = new JLabel("Şifreniz");
        JTextField passTxt = new JTextField(15);
        passTxt.setText("***************");
        panel2.add(pass); panel2.add(passTxt);

        JButton tamamBut = new JButton("Tamam");
        tamam.add(tamamBut);

        tamamBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entered();
                System.out.println(idTxt.getText());
                System.out.println(passTxt.getText());
            }
        });




        panel.setLayout(new GridLayout());
        panel2.setLayout(new GridLayout());
        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS)); Olacak şey değil yahu

        frame.add(panel);
        frame.add(panel2);
        frame.add(tamam);



        frame.setLayout(new GridLayout(3,1));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600,400));
        frame.setVisible(true);
    }

    /**
     * işte kayıt kısmını bu yapacak herhalde, ne bileyim.
     */
    public void entered(){
        System.out.println("tıklandı");
    }

}
