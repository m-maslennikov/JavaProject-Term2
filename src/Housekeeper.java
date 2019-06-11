import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Housekeeper extends User {

    public Housekeeper(String username) {
        super();
        setRole("Housekeeper");
        setUsername(username);
    }

    public void saveToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_cleaned=?, " +
                    "room_guest_status=?, " +
                    "room_note_for_supervisor=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, room.isCleaned());

            if (room.isCleaned()){
                pst.setString(2, "Neutral");
            } else {
                pst.setString(2, room.getGuestStatus());
            }

            pst.setString(3, room.getNoteForSupervisor());
            pst.setInt(4, room.getNumber());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
