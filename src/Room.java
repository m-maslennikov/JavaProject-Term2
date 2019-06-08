public class Room {
    private String number;
    private String guestStatus;
    private String housekeepingStatus;
    private String fdeskStatus;
    private String supervisorStatus;
    private String cleaningType;
    private String additionalInfo;
    private String infoForSupervisor;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(String guestStatus) {
        this.guestStatus = guestStatus;
    }

    public String getHousekeepingStatus() {
        return housekeepingStatus;
    }

    public void setHousekeepingStatus(String housekeepingStatus) {
        this.housekeepingStatus = housekeepingStatus;
    }

    public String getFdeskStatus() {
        return fdeskStatus;
    }

    public void setFdeskStatus(String fdeskStatus) {
        this.fdeskStatus = fdeskStatus;
    }

    public String getSupervisorStatus() {
        return supervisorStatus;
    }

    public void setSupervisorStatus(String supervisorStatus) {
        this.supervisorStatus = supervisorStatus;
    }

    public String getCleaningType() {
        return cleaningType;
    }

    public void setCleaningType(String cleaningType) {
        this.cleaningType = cleaningType;
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
