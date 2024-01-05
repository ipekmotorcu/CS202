import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ismi iyi olmamış olabilir. yeni kayıt oluştururken bunu kullanırız diye düşündüm.
 */
public class benimDeneme {

    public static void main(String[] args) throws Exception
    {
        benimDeneme f = new benimDeneme();
    }


    public benimDeneme(){
        JFrame frame = new JFrame("Register Frame");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(300,30,600,400);
        //frame.setSize(new Dimension(600,400));
        frame.setResizable(false);


        Container c = frame.getContentPane();



        JLabel id = new JLabel("TC Kimlik No");
        id.setSize(300, 30);
        id.setLocation(300, 30);
        c.add(id);

        JTextField idTxt = new JTextField(15);
        idTxt.setText("***************");
        idTxt.setSize(300, 30);
        idTxt.setLocation(300, 60);
        c.add(idTxt);

        JLabel pass = new JLabel("Şifreniz");
        pass.setSize(300, 30);
        pass.setLocation(0, 30);
        c.add(pass);


        JTextField passTxt = new JTextField(15);
        passTxt.setText("***************");
        passTxt.setSize(300, 30);
        passTxt.setLocation(0, 60);
        c.add(passTxt);

        JButton tamamBut = new JButton("Tamam");
        c.add(tamamBut);

        tamamBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entered();
                System.out.println(idTxt.getText());
                System.out.println(passTxt.getText());
            }
        });




        //panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS)); Olacak şey değil yahu


        String[] ads = {"1","2","3"};
        c.add(new JComboBox<>(ads)); //wow

        //c.setLayout(new GridLayout(3,2));


        frame.setVisible(true);
    }

    /**
     * işte kayıt kısmını bu yapacak herhalde, ne bileyim.
     */
    public void entered(){
        System.out.println("tıklandı");
    }

}
