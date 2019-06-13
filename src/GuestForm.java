import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;

public class GuestForm {
    private JPanel contentPanel;
    private JLabel usernameLabel;
    private JRadioButton dndRadioButton;
    private JRadioButton pleaseCleanUpRadioButton;
    private JComboBox roomsComboBox;
    private JButton saveButton;
    private JButton resetButton;
    private JRadioButton neutralRadioButton;
    private JButton logoutButton;
    private ButtonGroup guestStatusGroup;
    private User user;
    private Room room;

    public GuestForm(String title, User user) {

        //Initialize Frame
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        this.user = user;
        usernameLabel.setText("Current user: " + user.getUsername());
        dndRadioButton.setActionCommand("DND");
        pleaseCleanUpRadioButton.setActionCommand("Needs service");
        neutralRadioButton.setActionCommand("Neutral");

        //Fill comboBox with rooms numbers and clear selection
        fillRoomsComboBox();
        roomsComboBox.setSelectedIndex(-1);

        //Show Frame
        frame.setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm("Log In");
                frame.dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                room.setGuestStatus(guestStatusGroup.getSelection().getActionCommand());
                user.saveToDB(room);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neutralRadioButton.setSelected(true);
            }
        });

        roomsComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int selectedRoom = Integer.parseInt(roomsComboBox.getSelectedItem().toString());
                room = new Room();
                room.setNumber(selectedRoom);

                //Get room status from the database
                try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

                    String sql = "SELECT room_guest_status FROM rooms WHERE room_number=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, room.getNumber());
                    ResultSet result = pst.executeQuery();

                    while (result.next()){
                        room.setGuestStatus(result.getString("room_guest_status"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                switch (room.getGuestStatus()){
                    case "DND":
                        dndRadioButton.setSelected(true);
                        break;
                    case "Needs service":
                        pleaseCleanUpRadioButton.setSelected(true);
                        break;
                    case "Neutral":
                        neutralRadioButton.setSelected(true);
                        break;
                }
            }
        });
    }

    private void fillRoomsComboBox() {
        ArrayList<Room> rooms = user.loadRooms();
        roomsComboBox.removeAllItems();

        for (Room room : rooms) // <-- this is foreach loop.
            // For each room in the rooms do the following actions:
            // add room's number to comboBox
            roomsComboBox.addItem(String.valueOf(room.getNumber()));
    }
}
