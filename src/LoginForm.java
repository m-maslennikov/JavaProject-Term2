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

    private JTextField getLoginUsernameTextField() {
        return loginUsernameTextField;
    }

    private JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPanel getContent() {
        return content;
    }


    //------------------------
    // Constructor
    //------------------------
    public LoginForm(String title, User user) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.setUsername(getLoginUsernameTextField().getText());
                user.setPassword(getPasswordField().getText());

                try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

                    String sql = "SELECT * FROM users WHERE user_username=? AND user_password=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, user.getUsername());
                    pst.setString(2, user.getPassword());
                    ResultSet result = pst.executeQuery();

                    if (result.next()){
                        //TODO: implement more secure validation
                        user.setRole(result.getString("user_role"));
                        handleLogin(user);
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

    private void handleLogin(User user){

        switch (user.getRole()) {
            case "Housekeeper":
                HousekeeperForm housekeeperForm = new HousekeeperForm("Housekeeper", user);
                break;
            case "Supervisor":
                SupervisorForm supervisorForm = new SupervisorForm("Supervisor", user);
                break;
            case "Frontdesk":
                FrontdeskForm frontdeskForm = new FrontdeskForm("Frontdesk", user);
                break;
        }
    }
}
