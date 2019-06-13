import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User supervisor = new Supervisor("Test Supervisor");
    private User housekeeper = new Housekeeper("Test Housekeeper");
    private User frontdesk = new Frontdesk("Test Frontdesk");
    private User manager = new Manager("Test Manager");
    private String errorMessage = "Incorrect value";

    private Room createTestRoomObject(){
        Room room = new Room();
        room.setNumber(9999);
        room.setOccupied(false);
        room.setCleaned(false);
        room.setChecked(false);
        room.setCleaningType("");
        room.setGuestStatus("");
        room.setCheckoutDate("");
        room.setAdditionalInfo("");
        room.setNoteForSupervisor("");
        room.setHousekeeper("");
        room.setSupervisor("");
        return room;
    }

    private void addTestRoomToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "INSERT INTO Rooms (" +
                    "room_occupied, " +
                    "room_cleaned, " +
                    "room_checked, " +
                    "room_cleaning_type, " +
                    "room_guest_status, " +
                    "room_checkout_date, " +
                    "room_additional_info, " +
                    "room_note_for_supervisor, " +
                    "room_housekeeper," +
                    "room_supervisor," +
                    "room_number" +
                    ") VALUES (?,?,?,?,?,?,?,?,?,?,?)";

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
                System.out.println("Room was added successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Room getTestRoomFromDB(int room_number){
        Room room = new Room();
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms WHERE room_number=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, room_number);
            ResultSet result = pst.executeQuery();

            while (result.next()){
                room.setNumber(result.getInt("room_number"));
                room.setOccupied(result.getBoolean("room_occupied"));
                room.setCleaned(result.getBoolean("room_cleaned"));
                room.setChecked(result.getBoolean("room_checked"));
                room.setCleaningType(result.getString("room_cleaning_type"));
                room.setGuestStatus(result.getString("room_guest_status"));
                room.setCheckoutDate(result.getString("room_checkout_date"));
                room.setAdditionalInfo(result.getString("room_additional_info"));
                room.setNoteForSupervisor(result.getString("room_note_for_supervisor"));
                room.setHousekeeper(result.getString("room_housekeeper"));
                room.setSupervisor(result.getString("room_supervisor"));
            }
            return room;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return room;
    }

    private void deleteTestRoomFromDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "DELETE FROM rooms WHERE room_number=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, room.getNumber());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Room was deleted successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    void usersShouldHaveProperRoles(){

        // assert statements
        assertEquals("Supervisor", supervisor.getRole(), "User role should be Supervisor");
        assertEquals("Housekeeper", housekeeper.getRole(), "User role should be Housekeeper");
        assertEquals("Frontdesk", frontdesk.getRole(), "User role should be Frontdesk");
        assertEquals("Manager", manager.getRole(), "User role should be Manager");
    }

    @Test
    void managerShouldBeAbleToUpdateRoomInfo() {

        //Create test room object
        Room room = createTestRoomObject();

        //Add test room to the database
        addTestRoomToDB(room);

        //Change room info
        room.setOccupied(true);
        room.setCleaned(true);
        room.setChecked(true);
        room.setCleaningType("Stayover");
        room.setGuestStatus("Neutral");
        room.setCheckoutDate("2019-06-25");
        room.setAdditionalInfo("Some additional info from manager");
        room.setNoteForSupervisor("Some note for supervisor from manager");
        room.setHousekeeper("Housekeeper 1");
        room.setSupervisor("Supervisor 2");

        //Save room info to the database as a manager
        manager.saveToDB(room);

        //Check if room info saved to the database
        room = getTestRoomFromDB(9999);
        assertEquals(9999, room.getNumber());
        assertTrue(room.isOccupied());
        assertTrue(room.isCleaned());
        assertTrue(room.isChecked());
        assertEquals("Stayover", room.getCleaningType(), errorMessage);
        assertEquals("Neutral", room.getGuestStatus(), errorMessage);
        assertEquals("2019-06-25", room.getCheckoutDate(), errorMessage);
        assertEquals("Some additional info from manager", room.getAdditionalInfo(), errorMessage);
        assertEquals("Some note for supervisor from manager", room.getNoteForSupervisor(), errorMessage);
        assertEquals("Housekeeper 1", room.getHousekeeper(), errorMessage);
        assertEquals("Supervisor 2", room.getSupervisor(), errorMessage);

        //Delete test room from the database
        deleteTestRoomFromDB(room);

    }

    @Test
    void housekeeperShouldBeAbleToUpdateRoomInfo() {

        //Create test room object
        Room room = createTestRoomObject();

        //Add test room to the database
        addTestRoomToDB(room);

        //Change room info
        room.setOccupied(true);
        room.setCleaned(true);
        room.setChecked(true);
        room.setCleaningType("Stayover");
        room.setGuestStatus("Neutral");
        room.setCheckoutDate("2019-06-25");
        room.setAdditionalInfo("Some additional info from manager");
        room.setNoteForSupervisor("Some note for supervisor from housekeeper");
        room.setHousekeeper("Housekeeper 1");
        room.setSupervisor("Supervisor 2");

        //Save room info to the database as a manager
        housekeeper.saveToDB(room);

        //Check if room info saved to the database
        room = getTestRoomFromDB(9999);
        assertEquals(9999, room.getNumber());
        assertFalse(room.isOccupied());
        assertTrue(room.isCleaned());
        assertFalse(room.isChecked());
        assertEquals("", room.getCleaningType(), errorMessage);
        assertEquals("Neutral", room.getGuestStatus(), errorMessage);
        assertEquals(null, room.getCheckoutDate(), errorMessage);
        assertEquals("", room.getAdditionalInfo(), errorMessage);
        assertEquals("Some note for supervisor from housekeeper", room.getNoteForSupervisor(), errorMessage);
        assertEquals(null, room.getHousekeeper(), errorMessage);
        assertEquals(null, room.getSupervisor(), errorMessage);

        //Delete test room from the database
        deleteTestRoomFromDB(room);
    }
}