import java.sql.*;
import java.util.ArrayList;

public class User {
    private String username;
    private String role;

    //Default constructor
    public User() {
    }

    //Custom constructor
    public User(String username) {
        setRole("Guest");
        setUsername(username);
    }

    public void saveToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_guest_status=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, room.getGuestStatus());
            pst.setInt(2, room.getNumber());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //This method is overridden in subclasses
    //We don't use it with parent class, so here returns null
    public Object[][] loadData(){
        return null;
    }

    public ArrayList<Room> loadRooms(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT room_number, room_guest_status FROM rooms";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet result = pst.executeQuery();

            while (result.next()){
                roomsList.add(new Room());
                lastElement = roomsList.size()-1;
                roomsList.get(lastElement).setNumber(result.getInt("room_number"));
                roomsList.get(lastElement).setGuestStatus(result.getString("room_guest_status"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return roomsList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
