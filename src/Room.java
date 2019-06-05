public class Room {
    private String id;
    private String number;
    private String status;
    private String guestStatus;
    private String additionalInfo;
    private String infoForSupervisor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(String guestStatus) {
        this.guestStatus = guestStatus;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getInfoForSupervisor() {
        return infoForSupervisor;
    }

    public void setInfoForSupervisor(String infoForSupervisor) {
        this.infoForSupervisor = infoForSupervisor;
    }


}
