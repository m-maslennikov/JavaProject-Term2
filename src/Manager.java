import java.sql.*;
import java.util.ArrayList;

public class Manager extends User {

    public Manager(String username) {
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

    public Object[][] loadData(){
        ArrayList<Room> roomsList = new ArrayList<>();
        int lastElement;

        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "SELECT * FROM rooms";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet result = pst.executeQuery();

            while (result.next()){
                roomsList.add(new Room());
                lastElement = roomsList.size()-1;
                roomsList.get(lastElement).setNumber(result.getInt("room_number"));
                roomsList.get(lastElement).setOccupied(result.getBoolean("room_occupied"));
                roomsList.get(lastElement).setCleaned(result.getBoolean("room_cleaned"));
                roomsList.get(lastElement).setChecked(result.getBoolean("room_checked"));
                roomsList.get(lastElement).setCleaningType(result.getString("room_cleaning_type"));
                roomsList.get(lastElement).setGuestStatus(result.getString("room_guest_status"));
                roomsList.get(lastElement).setCheckoutDate(result.getString("room_checkout_date"));
                roomsList.get(lastElement).setAdditionalInfo(result.getString("room_additional_info"));
                roomsList.get(lastElement).setNoteForSupervisor(result.getString("room_note_for_supervisor"));
                roomsList.get(lastElement).setHousekeeper(result.getString("room_housekeeper"));
                roomsList.get(lastElement).setSupervisor(result.getString("room_supervisor"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = new Object[roomsList.size()][11];
        for (int i=0; i < roomsList.size(); i++){
            data[i][0] = roomsList.get(i).getNumber();
            data[i][1] = roomsList.get(i).getSupervisor();
            data[i][2] = roomsList.get(i).getHousekeeper();
            data[i][3] = roomsList.get(i).isOccupied();
            data[i][4] = roomsList.get(i).isCleaned();
            data[i][5] = roomsList.get(i).isChecked();
            data[i][6] = roomsList.get(i).getCleaningType();
            data[i][7] = roomsList.get(i).getGuestStatus();
            data[i][8] = roomsList.get(i).getCheckoutDate();
            data[i][9] = roomsList.get(i).getAdditionalInfo();
            data[i][10] = roomsList.get(i).getNoteForSupervisor();
        }
        return data;
    }
}
