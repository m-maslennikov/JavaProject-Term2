import javax.swing.*;
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
    private FrontdeskTableModel housekeeperTableModel;
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
                LoginForm loginForm = new LoginForm("Log In", user);
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
            //roomsList.get(lastElement).setHousekeeper(roomsTable.getValueAt(i, 2).toString());
            //roomsList.get(lastElement).setOccupied(Boolean.parseBoolean(roomsTable.getValueAt(i, 3).toString()));
            //roomsList.get(lastElement).setChecked(Boolean.parseBoolean(roomsTable.getValueAt(i, 5).toString()));
            //roomsList.get(lastElement).setCheckoutDate(roomsTable.getValueAt(i, 8).toString());

            roomsList.get(lastElement).housekeeperSaveToDB();
        }
    }

    private void updateTable(Object[][] data){
        housekeeperTableModel = new FrontdeskTableModel(data, GlobalVariables.housekeeperTableHeader);
        roomsTable.setModel(housekeeperTableModel);
        //setUpComboBoxColumn(roomsTable.getColumnModel().getColumn(6), GlobalVariables.cleaningOptions);
        //setUpComboBoxColumn(roomsTable.getColumnModel().getColumn(7), GlobalVariables.guestStatuses);
    }

    private Object[][] loadHousekeeperData(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms WHERE room_housekeeper=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, user.getId());
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
                //roomsList.get(lastElement).setOccupied(result.getBoolean("room_occupied"));
                //roomsList.get(lastElement).setChecked(result.getBoolean("room_checked"));
                //roomsList.get(lastElement).setCheckoutDate(result.getString("room_checkout_date"));
                //roomsList.get(lastElement).setHousekeeper(result.getString("room_housekeeper"));
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
            //data[i][2] = roomsList.get(i).getHousekeeper();
            //data[i][3] = roomsList.get(i).isOccupied();
            //data[i][5] = roomsList.get(i).isChecked();
            //data[i][8] = roomsList.get(i).getCheckoutDate();

        }

        return data;
    }

    private void checkTable(JTable table){
        FrontdeskTableModel model = (FrontdeskTableModel) table.getModel();
        int rows = model.getRowCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= 6; j++) {
                Object ob = model.getValueAt(i, j);
                if (ob == null || ob.toString().isEmpty()) {
                    model.setValueAt("N/A", i, j);
                }
            }
        }
    }

}
