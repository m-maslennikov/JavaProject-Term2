import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Frontdesk extends User {

    public Frontdesk(String username) {
        super();
        setRole("Frontdesk");
        setUsername(username);
    }

    public void saveToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_occupied=?, " +
                    "room_checkout_date=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, room.isOccupied());

            if (room.getCheckoutDate().equals("N/A") || room.getCheckoutDate().equals("")){
                pst.setString(2, null);
            } else {
                pst.setString(2, room.getCheckoutDate());
            }

            pst.setInt(3, room.getNumber());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
