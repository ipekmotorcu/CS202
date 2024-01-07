import javax.swing.*;
import java.awt.*;

public class AdminView {
    int adminId;
    public AdminView(int adminId){
        this.adminId=adminId;

        JFrame frame = new JFrame("Admin View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,400));
        frame.setVisible(true);
    }
}
