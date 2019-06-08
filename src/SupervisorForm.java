import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupervisorForm {
    private JPanel contentPanel;
    private JPanel supervisorPanel;
    private JLabel supervisorUsernameLabel;
    private JTable supervisorRoomsTable;
    private JLabel supervisorSelectedRoomLabel;
    private JTextArea supervisorAdditionalInfoTextArea;
    private JTextArea supervisorNoteForSupervisorTextArea;
    private JButton supervisorSaveButton;
    private JButton supervisorRefreshButton;
    private JButton supervisorLogoutButton;
    private JCheckBox checkedCheckBox;
    private JLabel supervisorGuestStatusLabel;
    private JLabel supervisorCleaningTypeLabel;

    public SupervisorForm(String title, User user) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        supervisorUsernameLabel.setText("Logged in as: " + user.getUsername());

        supervisorLogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm("Log In", user);
                frame.dispose();
            }
        });
    }
}
