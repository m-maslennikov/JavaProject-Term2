import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class FrontdeskForm {
    private JPanel contentPanel;
    private JTable roomsTable;
    private JButton saveButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JLabel usernameLabel;
    private DefaultTableModel frontdeskTableModel;
    private User user;

    public FrontdeskForm(String title, User user) {

        //Initialize Frame
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        this.user = user;
        usernameLabel.setText("Current user: " + user.getUsername());

        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTable(loadFrontdeskData());
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
                updateTable(loadFrontdeskData());
                checkTable(roomsTable);

            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRooms();
                updateTable(loadFrontdeskData());
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
            roomsList.get(lastElement).setOccupied(Boolean.parseBoolean(roomsTable.getValueAt(i, 2).toString()));
            roomsList.get(lastElement).setCheckoutDate(roomsTable.getValueAt(i, 1).toString());

            user.saveToDB(roomsList.get(lastElement));
        }
    }

    private void updateTable(Object[][] data){
        frontdeskTableModel = new DefaultTableModel(data, Helper.frontDeskTableHeader){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                // make columns editable except column 1,2,3
                return column == 1 || column == 2 || column == 3;
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        roomsTable.setModel(frontdeskTableModel);
    }

    private Object[][] loadFrontdeskData(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet result = pst.executeQuery();

            while (result.next()){
                roomsList.add(new Room());
                lastElement = roomsList.size()-1;
                roomsList.get(lastElement).setNumber(result.getInt("room_number"));
                roomsList.get(lastElement).setOccupied(result.getBoolean("room_occupied"));
                roomsList.get(lastElement).setCleaned(result.getBoolean("room_cleaned"));
                roomsList.get(lastElement).setChecked(result.getBoolean("room_checked"));
                roomsList.get(lastElement).setCleaningType(result.getString("room_cleaning_type"));
                roomsList.get(lastElement).setCheckoutDate(result.getString("room_checkout_date"));
                roomsList.get(lastElement).setHousekeeper(result.getString("room_housekeeper"));
                roomsList.get(lastElement).setSupervisor(result.getString("room_supervisor"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = new Object[roomsList.size()][8];
        for (int i=0; i < roomsList.size(); i++){
            data[i][0] = roomsList.get(i).getNumber();
            data[i][1] = roomsList.get(i).getCheckoutDate();
            data[i][2] = roomsList.get(i).isOccupied();
            data[i][3] = roomsList.get(i).getCleaningType();
            data[i][4] = roomsList.get(i).isCleaned();
            data[i][5] = roomsList.get(i).isChecked();
            data[i][6] = roomsList.get(i).getSupervisor();
            data[i][7] = roomsList.get(i).getHousekeeper();
        }

        return data;
    }

    private void checkTable(JTable table){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rows = model.getRowCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 8; j++) {
                Object ob = model.getValueAt(i, j);
                if (ob == null || ob.toString().isEmpty()) {
                    model.setValueAt("N/A", i, j);
                }
            }
        }
    }

}
