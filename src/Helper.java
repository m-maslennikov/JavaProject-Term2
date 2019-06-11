import java.sql.*;
import java.util.ArrayList;

public class Helper {
    public static final String[] cleaningOptions = {"Stayover", "Due Out", "None"};
    public static final String[] guestStatuses = {"DND", "Need Service", "Neutral"};
    public static final String[] frontDeskTableHeader = {"Room Number",  "Checkout Date", "Occupied", "Cleaning Type", "Cleaned", "Checked", "Supervisor", "Housekeeper"};
    public static final String[] housekeeperTableHeader = {"Room Number", "Supervisor", "Cleaning Type", "Guest Status", "Additional Info", "Note for Supervisor", "Cleaned"};
    public static final String[] supervisorTableHeader = {"Room Number", "Housekeeper", "Cleaning Type", "Guest Status", "Note for Supervisor", "Additional Info", "Cleaned", "Checked"};
    public static final String[] managerTableHeader = {"Room Number", "Supervisor", "Housekeeper", "Occupied", "Cleaned", "Checked", "Cleaning Type", "Guest Status", "Checkout Date", "Additional Info", "Note for Supervisor"};

    public static String[] getUsers(String userType){
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

}
