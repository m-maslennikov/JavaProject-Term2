import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class HousekeeperForm {
    private JPanel contentPanel;
    private JTable roomsTable;
    private JButton saveButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JLabel usernameLabel;
    private DefaultTableModel housekeeperTableModel;
    private User user;

    public HousekeeperForm(String title, User user) {

        //Initialize Frame
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        this.user = user;
        usernameLabel.setText("Current user: " + user.getUsername());

        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTable(loadHousekeeperData());
        checkTable(roomsTable);

        //Show Frame
        frame.setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm("Log In");
                frame.dispose();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(loadHousekeeperData());
                checkTable(roomsTable);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRooms();
                updateTable(loadHousekeeperData());
                checkTable(roomsTable);
            }
        });
    }

    private void saveRooms() {
        ArrayList<Room> roomsList = new ArrayList<>();
        int numberOfRows=roomsTable.getRowCount();
        int lastElement;
        for(int i = 0; i < numberOfRows; i++) {
            roomsList.add(new Room());
            lastElement = roomsList.size()-1;
            roomsList.get(lastElement).setNumber(Integer.parseInt(roomsTable.getValueAt(i, 0).toString()));
            roomsList.get(lastElement).setSupervisor(roomsTable.getValueAt(i, 1).toString());
            roomsList.get(lastElement).setCleaningType(roomsTable.getValueAt(i, 2).toString());
            roomsList.get(lastElement).setGuestStatus(roomsTable.getValueAt(i, 3).toString());
            roomsList.get(lastElement).setAdditionalInfo(roomsTable.getValueAt(i, 4).toString());
            roomsList.get(lastElement).setNoteForSupervisor(roomsTable.getValueAt(i, 5).toString());
            roomsList.get(lastElement).setCleaned(Boolean.parseBoolean(roomsTable.getValueAt(i, 6).toString()));

            user.saveToDB(roomsList.get(lastElement));
        }
    }

    private void updateTable(Object[][] data){
        housekeeperTableModel = new DefaultTableModel(data, Helper.housekeeperTableHeader){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                // make columns editable except column 0,1,2,3,4
                return column > 4;
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        roomsTable.setModel(housekeeperTableModel);
    }

    private Object[][] loadHousekeeperData(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms WHERE room_housekeeper=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getUsername());
            ResultSet result = pst.executeQuery();

            while (result.next()){
                roomsList.add(new Room());
                lastElement = roomsList.size()-1;
                roomsList.get(lastElement).setNumber(result.getInt("room_number"));
                roomsList.get(lastElement).setCleaned(result.getBoolean("room_cleaned"));
                roomsList.get(lastElement).setCleaningType(result.getString("room_cleaning_type"));
                roomsList.get(lastElement).setGuestStatus(result.getString("room_guest_status"));
                roomsList.get(lastElement).setAdditionalInfo(result.getString("room_additional_info"));
                roomsList.get(lastElement).setNoteForSupervisor(result.getString("room_note_for_supervisor"));
                roomsList.get(lastElement).setSupervisor(result.getString("room_supervisor"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = new Object[roomsList.size()][7];
        for (int i=0; i < roomsList.size(); i++){
            data[i][0] = roomsList.get(i).getNumber();
            data[i][1] = roomsList.get(i).getSupervisor();
            data[i][2] = roomsList.get(i).getCleaningType();
            data[i][3] = roomsList.get(i).getGuestStatus();
            data[i][4] = roomsList.get(i).getAdditionalInfo();
            data[i][5] = roomsList.get(i).getNoteForSupervisor();
            data[i][6] = roomsList.get(i).isCleaned();
        }

        return data;
    }

    private void checkTable(JTable table){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rows = model.getRowCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 7; j++) {
                Object ob = model.getValueAt(i, j);
                if (ob == null || ob.toString().isEmpty()) {
                    model.setValueAt("N/A", i, j);
                }
            }
        }
    }

}
