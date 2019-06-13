import java.sql.*;
import java.util.ArrayList;

public class Supervisor extends User{

    public Supervisor(String username) {
        setRole("Supervisor");
        setUsername(username);
    }

    public void saveToDB(Room room){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_cleaned=?, " +
                    "room_checked=?, " +
                    "room_additional_info=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, room.isCleaned());
            pst.setBoolean(2, room.isChecked());
            pst.setString(3, room.getAdditionalInfo());
            pst.setInt(4, room.getNumber());
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Object[][] loadData(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms WHERE room_supervisor=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, this.getUsername());
            ResultSet result = pst.executeQuery();

            while (result.next()){
                roomsList.add(new Room());
                lastElement = roomsList.size()-1;
                roomsList.get(lastElement).setNumber(result.getInt("room_number"));
                roomsList.get(lastElement).setCleaned(result.getBoolean("room_cleaned"));
                roomsList.get(lastElement).setCleaningType(result.getString("room_cleaning_type"));
                roomsList.get(lastElement).setGuestStatus(result.getString("room_guest_status"));
                roomsList.get(lastElement).setAdditionalInfo(result.getString("room_additional_info"));
                roomsList.get(lastElement).setNoteForSupervisor(result.getString("room_note_for_supervisor"));
                roomsList.get(lastElement).setChecked(result.getBoolean("room_checked"));
                roomsList.get(lastElement).setHousekeeper(result.getString("room_housekeeper"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = new Object[roomsList.size()][8];
        for (int i=0; i < roomsList.size(); i++){
            data[i][0] = roomsList.get(i).getNumber();
            data[i][1] = roomsList.get(i).getHousekeeper();
            data[i][2] = roomsList.get(i).getCleaningType();
            data[i][3] = roomsList.get(i).getGuestStatus();
            data[i][4] = roomsList.get(i).getNoteForSupervisor();
            data[i][5] = roomsList.get(i).getAdditionalInfo();
            data[i][6] = roomsList.get(i).isCleaned();
            data[i][7] = roomsList.get(i).isChecked();
        }
        return data;
    }
}
