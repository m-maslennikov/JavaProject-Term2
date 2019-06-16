import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrontdeskForm {
    private Helper helper = new Helper();
    private JPanel contentPanel;
    private JTable roomsTable;
    private JButton saveButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JLabel usernameLabel;
    private User user;

    public FrontdeskForm(String title, User user) {

        //Initialize Frame
        JFrame frame = new JFrame(title);
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        this.user = user;
        usernameLabel.setText("Current user: " + user.getUsername());

        //Load and check data for the table
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
            roomsList.get(lastElement).setOccupied(Boolean.parseBoolean(roomsTable.getValueAt(i, 2).toString()));
            roomsList.get(lastElement).setCheckoutDate(roomsTable.getValueAt(i, 1).toString());

            user.saveToDB(roomsList.get(lastElement));
        }
    }

    private void updateTable(Object[][] data){
        DefaultTableModel frontdeskTableModel = new DefaultTableModel(data, helper.getFrontDeskTableHeader()){
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
        setUpComboBoxColumn(roomsTable.getColumnModel().getColumn(3), helper.getCleaningOptions());
    }

    private void setUpComboBoxColumn(TableColumn col, String[] options) {
        //Set up the editor for the cells.
        JComboBox comboBox = new JComboBox(options);
        col.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to select");
        col.setCellRenderer(renderer);
    }
}
