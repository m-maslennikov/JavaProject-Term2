import javax.swing.*;

public class TestForm {
    private JPanel contentPanel;
    private JLabel currentUserLabel;

    public TestForm(String title, User u) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        currentUserLabel.setText(u.getRole());
    }
}
