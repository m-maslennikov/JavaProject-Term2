import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Manager extends User {

    public Manager(String username) {
        super();
        setRole("Manager");
        setUsername(username);
    }

    public void saveToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_occupied=?, " +
                    "room_cleaned=?, " +
                    "room_checked=?, " +
                    "room_cleaning_type=?, " +
                    "room_guest_status=?, " +
                    "room_checkout_date=?, " +
                    "room_additional_info=?, " +
                    "room_note_for_supervisor=?, " +
                    "room_housekeeper=?, " +
                    "room_supervisor=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, room.isOccupied());
            pst.setBoolean(2, room.isCleaned());
            pst.setBoolean(3, room.isChecked());
            pst.setString(4, room.getCleaningType());
            pst.setString(5, room.getGuestStatus());

            if (room.getCheckoutDate().equals("N/A") || room.getCheckoutDate().equals("")){
                pst.setString(6, null);
            } else {
                pst.setString(6, room.getCheckoutDate());
            }

            pst.setString(7, room.getAdditionalInfo());
            pst.setString(8, room.getNoteForSupervisor());

            if (room.getHousekeeper().equals("N/A") || room.getHousekeeper().equals("")){
                pst.setString(9, null);
            } else {
                pst.setString(9, room.getHousekeeper());
            }

            if (room.getSupervisor().equals("N/A") || room.getSupervisor().equals("")){
                pst.setString(10, null);
            } else {
                pst.setString(10, room.getSupervisor());
            }

            pst.setInt(11, room.getNumber());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
