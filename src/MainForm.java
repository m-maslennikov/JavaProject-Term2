import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class MainForm {
    private JPanel contentPanel;

    //------------------------
    //Login Panel Components
    //------------------------
    private JPanel loginPanel;
    private JLabel loginLabel;
    private JLabel loginUsernameLabel;
    private JTextField loginUsernameTextField;
    private JPasswordField passwordField;
    private JLabel loginPasswordLabel;
    private JButton loginButton;

    //------------------------
    //Housekeeper Panel Components
    //------------------------
    private JPanel housekeeperPanel;
    private JTable housekeeperRoomsTable;
    private JTextArea housekeeperAdditionalInfoTextArea;
    private JTextArea housekeeperNoteForSupervisorTextArea;
    private JButton housekeeperSaveButton;
    private JButton housekeeperRefreshButton;
    private JButton housekeeperLogoutButton;
    private JCheckBox cleanedCheckBox;
    private JLabel housekeeperGuestStatusLabel;
    private JLabel housekeeperSelectedRoomLabel;
    private JLabel housekeeperUsernameLabel;
    private JLabel housekeeperCleaningTypeLabel;

    //------------------------
    //Supervisor Panel Components
    //------------------------
    private JPanel supervisorPanel;
    private JLabel supervisorUsernameLabel;
    private JLabel supervisorSelectedRoomLabel;
    private JTextArea supervisorAdditionalInfoTextArea;
    private JTextArea supervisorNoteForSupervisorTextArea;
    private JButton supervisorSaveButton;
    private JButton supervisorRefreshButton;
    private JButton supervisorLogoutButton;
    private JLabel supervisorGuestStatusLabel;
    private JCheckBox checkedCheckBox;
    private JTable supervisorRoomsTable;
    private JLabel supervisorCleaningTypeLabel;


    //------------------------
    //Manager Panel Components
    //------------------------


    //------------------------
    //Frontdesk Panel Components
    //------------------------

    public JPanel getContentPanel() {
        return contentPanel;
    }

    //------------------------
    // Constructor
    //------------------------
    public MainForm() {
        //Set login form heading size
        loginLabel.setFont(loginLabel.getFont().deriveFont(32.0f));


        housekeeperLogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //logout();
            }
        });

        supervisorLogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //logout();
            }
        });
    }

    //------------------------
    // Panels helper functions
    //------------------------
    private void hideAllPanels(){
        housekeeperPanel.setVisible(false);
        loginPanel.setVisible(false);
    }

    private void showPanel(JPanel panel){
        hideAllPanels();
        panel.setVisible(true);
    }



}
