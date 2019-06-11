import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SupervisorForm {
    private Helper helper = new Helper();
    private JTable roomsTable;
    private JButton saveButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JLabel usernameLabel;
    private JPanel contentPanel;
    private User user;

    public SupervisorForm(String title, User user) {

        //Initialize Frame
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        this.user = user;
        usernameLabel.setText("Current user: " + user.getUsername());

        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateTable(user.loadData());
        helper.checkTable(roomsTable);

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
                updateTable(user.loadData());
                helper.checkTable(roomsTable);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRooms();
                updateTable(user.loadData());
                helper.checkTable(roomsTable);
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
            roomsList.get(lastElement).setHousekeeper(roomsTable.getValueAt(i, 1).toString());
            roomsList.get(lastElement).setCleaningType(roomsTable.getValueAt(i, 2).toString());
            roomsList.get(lastElement).setGuestStatus(roomsTable.getValueAt(i, 3).toString());
            roomsList.get(lastElement).setNoteForSupervisor(roomsTable.getValueAt(i, 4).toString());
            roomsList.get(lastElement).setAdditionalInfo(roomsTable.getValueAt(i, 5).toString());
            roomsList.get(lastElement).setCleaned(Boolean.parseBoolean(roomsTable.getValueAt(i, 6).toString()));
            roomsList.get(lastElement).setChecked(Boolean.parseBoolean(roomsTable.getValueAt(i, 7).toString()));

            user.saveToDB(roomsList.get(lastElement));
        }
    }

    private void updateTable(Object[][] data){
        DefaultTableModel supervisor = new DefaultTableModel(data, helper.getSupervisorTableHeader()){
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
        roomsTable.setModel(supervisor);
    }


}
