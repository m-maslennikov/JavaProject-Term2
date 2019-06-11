import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Helper {
    private final String[] cleaningOptions = {"Stayover", "Due Out", "None"};
    private final String[] guestStatuses = {"DND", "Need Service", "Neutral"};
    private final String[] frontDeskTableHeader = {"Room Number",  "Checkout Date", "Occupied", "Cleaning Type", "Cleaned", "Checked", "Supervisor", "Housekeeper"};
    private final String[] housekeeperTableHeader = {"Room Number", "Supervisor", "Cleaning Type", "Guest Status", "Additional Info", "Note for Supervisor", "Cleaned"};
    private final String[] supervisorTableHeader = {"Room Number", "Housekeeper", "Cleaning Type", "Guest Status", "Note for Supervisor", "Additional Info", "Cleaned", "Checked"};
    private final String[] managerTableHeader = {"Room Number", "Supervisor", "Housekeeper", "Occupied", "Cleaned", "Checked", "Cleaning Type", "Guest Status", "Checkout Date", "Additional Info", "Note for Supervisor"};

    public String[] getUsers(String userType){
        ArrayList<String> usersList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT user_username FROM users WHERE user_role=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userType);
            ResultSet result = pst.executeQuery();

            while (result.next()){
                usersList.add(result.getString("user_username"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usersList.toArray(new String[0]);
    }

    public void checkTable(JTable table){
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                Object ob = table.getValueAt(i, j);
                if (ob == null || ob.toString().isEmpty()) {
                    table.setValueAt("N/A", i, j);
                }
            }
        }
    }

    public String[] getCleaningOptions() {
        return cleaningOptions;
    }

    public String[] getGuestStatuses() {
        return guestStatuses;
    }

    public String[] getFrontDeskTableHeader() {
        return frontDeskTableHeader;
    }

    public String[] getHousekeeperTableHeader() {
        return housekeeperTableHeader;
    }

    public String[] getSupervisorTableHeader() {
        return supervisorTableHeader;
    }

    public String[] getManagerTableHeader() {
        return managerTableHeader;
    }
}
