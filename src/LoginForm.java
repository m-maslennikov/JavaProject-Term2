import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm {
    private JPanel loginPanel;
    private JLabel loginUsernameLabel;
    private JTextField loginUsernameTextField;
    private JButton loginButton;
    private JLabel loginLabel;
    private JPasswordField passwordField;
    private JLabel loginPasswordLabel;
    private JPanel content;
    private User user;


    //------------------------
    // Constructor
    //------------------------
    public LoginForm(String title) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

                    String sql = "SELECT * FROM users WHERE user_username=? AND user_password=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, loginUsernameTextField.getText());
                    pst.setString(2, passwordField.getText());
                    ResultSet result = pst.executeQuery();

                    if (result.next()){
                        switch (result.getString("user_role")) {
                            case "Housekeeper":
                                user = new Housekeeper(result.getString("user_username"));
                                HousekeeperForm housekeeperForm = new HousekeeperForm("Housekeeper", user);
                                break;
                            case "Supervisor":
                                user = new Supervisor(result.getString("user_username"));
                                SupervisorForm supervisorForm = new SupervisorForm("Supervisor", user);
                                break;
                            case "Frontdesk":
                                user = new Frontdesk(result.getString("user_username"));
                                FrontdeskForm frontdeskForm = new FrontdeskForm("Frontdesk", user);
                                break;
                            case "Manager":
                                user = new Manager(result.getString("user_username"));
                                ManagerForm managerForm = new ManagerForm("Manager", user);
                                break;
                        }

                        frame.dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Access Denied", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
