import java.sql.*;
import java.util.ArrayList;

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
                roomsList.get(lastElement).setCheckoutDate(result.getString("room_checkout_date"));
                roomsList.get(lastElement).setHousekeeper(result.getString("room_housekeeper"));
                roomsList.get(lastElement).setSupervisor(result.getString("room_supervisor"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = new Object[roomsList.size()][8];
        for (int i=0; i < roomsList.size(); i++){
            data[i][0] = roomsList.get(i).getNumber();
            data[i][1] = roomsList.get(i).getCheckoutDate();
            data[i][2] = roomsList.get(i).isOccupied();
            data[i][3] = roomsList.get(i).getCleaningType();
            data[i][4] = roomsList.get(i).isCleaned();
            data[i][5] = roomsList.get(i).isChecked();
            data[i][6] = roomsList.get(i).getSupervisor();
            data[i][7] = roomsList.get(i).getHousekeeper();
        }

        return data;
    }


}
