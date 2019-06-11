import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Room {
    private int number;
    private boolean occupied;
    private boolean cleaned;
    private boolean checked;
    private String cleaningType;
    private String guestStatus;
    private String checkoutDate;
    private String additionalInfo;
    private String noteForSupervisor;
    private String housekeeper;
    private String supervisor;

    public void frontdeskSaveToDB(){
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

            pst.setBoolean(1, occupied);
            pst.setBoolean(2, cleaned);
            pst.setBoolean(3, checked);
            pst.setString(4, cleaningType);
            pst.setString(5, guestStatus);

            if (checkoutDate.equals("N/A")){
                pst.setString(6, null);
            } else {
                pst.setString(6, checkoutDate);
            }

            pst.setString(7, additionalInfo);
            pst.setString(8, noteForSupervisor);

            if (housekeeper.equals("N/A")){
                pst.setString(9, null);
            } else {
                pst.setString(9, housekeeper);
            }

            if (supervisor.equals("N/A")){
                pst.setString(10, null);
            } else {
                pst.setString(10, supervisor);
            }

            pst.setInt(11, number);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void housekeeperSaveToDB(){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_cleaned=?, " +
                    "room_note_for_supervisor=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, cleaned);
            pst.setString(2, noteForSupervisor);
            pst.setInt(3, number);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void supervisorSaveToDB(){
        try (Connection conn = DriverManager.getConnection(DatabaseCon.CONN_STRING, DatabaseCon.USERNAME, DatabaseCon.PASSWORD)) {

            String sql = "UPDATE Rooms SET " +
                    "room_cleaned=?, " +
                    "room_checked=?, " +
                    "room_note_for_supervisor=? " +
                    "WHERE room_number=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setBoolean(1, cleaned);
            pst.setBoolean(2, checked);
            pst.setString(3, additionalInfo);
            pst.setInt(4, number);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("An existing room was updated successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isCleaned() {
        return cleaned;
    }

    public void setCleaned(boolean cleaned) {
        this.cleaned = cleaned;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCleaningType() {
        return cleaningType;
    }

    public void setCleaningType(String cleaningType) {
        this.cleaningType = cleaningType;
    }

    public String getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(String guestStatus) {
        this.guestStatus = guestStatus;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getNoteForSupervisor() {
        return noteForSupervisor;
    }

    public void setNoteForSupervisor(String noteForSupervisor) {
        this.noteForSupervisor = noteForSupervisor;
    }

    public String getHousekeeper() {
        return housekeeper;
    }

    public void setHousekeeper(String housekeeper) {
        this.housekeeper = housekeeper;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
