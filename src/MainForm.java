import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.*;

public class MainForm {
    private JPanel contentPanel;
    private JPanel loginPanel;
    private JTextField jTFUsername;
    private JButton jBTLogin;
    private JPanel North;
    private JPanel East;
    private JTextArea noteForSupervisorTA;
    private JButton applyButton;
    private JButton resetButton;
    private JTextArea additionalInfoTA;
    private JRadioButton cleanRadioButton;
    private JRadioButton dirtyRadioButton;
    private JPanel West;
    private JPanel South;
    private JButton logoutButton;
    private JPanel Center;
    private JList hkRoomList;
    private JPanel housekeeperPanel;
    private JLabel selectedRoom;

    public MainForm() {
        jBTLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:
                //if user is housekeeper > show housekeeper form
                //if user is manager > show manager form
                //if user is front desk > show front desk form
                //if user is supervisor > show supervisor form

                try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

                    // code to execute SQL queries goes here...
                    System.out.println("we are connected");
                    String sql = "SELECT * FROM users WHERE user_username=?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, jTFUsername.getText());
                    ResultSet result = statement.executeQuery();

                    while (result.next()){
                        String role = result.getString("user_role");
                        System.out.println(role);

                        if (role.equals("admin")){
                            loginPanel.setVisible(false);
                            housekeeperPanel.setVisible(true);
                            //JOptionPane.showMessageDialog(null,"You are logged in as Admin");

                        }

                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }//comment
        });

        housekeeperPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                //JOptionPane.showMessageDialog(null,"test message");
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.setVisible(true);
                housekeeperPanel.setVisible(false);
                jTFUsername.setText("");
            }
        });
        hkRoomList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedRoom.setText(hkRoomList.getSelectedValue().toString());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
