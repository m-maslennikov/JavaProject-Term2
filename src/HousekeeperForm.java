import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HousekeeperForm {
    private JPanel contentPanel;
    private JPanel housekeeperPanel;
    private JLabel housekeeperUsernameLabel;
    private JTable housekeeperRoomsTable;
    private JLabel housekeeperSelectedRoomLabel;
    private JTextArea housekeeperAdditionalInfoTextArea;
    private JTextArea housekeeperNoteForSupervisorTextArea;
    private JButton housekeeperSaveButton;
    private JButton housekeeperRefreshButton;
    private JButton housekeeperLogoutButton;
    private JCheckBox cleanedCheckBox;
    private JLabel housekeeperGuestStatusLabel;
    private JLabel housekeeperCleaningTypeLabel;

    public HousekeeperForm(String title, User user) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        housekeeperUsernameLabel.setText("Logged in as: " + user.getUsername());

        housekeeperLogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm("Log In", user);
                frame.dispose();
            }
        });
    }
}
