package sample.model;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDateTime;

public class Appointment {
    private int apptId;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private LocalDateTime apptStartTime;
    private LocalDateTime apptEndTime;
    private LocalDateTime apptCreateDate;
    private String apptCreatedBy;
    private LocalDateTime apptLastUpdate;
    private String apptLastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    public Appointment(int apptId, String apptTitle, String apptDescription, String apptLocation, String apptType, LocalDateTime apptStartTime, LocalDateTime apptEndTime, LocalDateTime apptCreateDate, String apptCreatedBy, LocalDateTime apptLastUpdate, String apptLastUpdatedBy, int customerId, int userId, int contactId) {
        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
        this.apptCreateDate = apptCreateDate;
        this.apptCreatedBy = apptCreatedBy;
        this.apptLastUpdate = apptLastUpdate;
        this.apptLastUpdatedBy = apptLastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }



    //Getters and Setters

    /**
     * return apptId
     * @return
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * set apptId
     * @param apptId
     */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**
     *
     * @return apptTitle
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**
     *
     * @param apptTitle
     */
    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    /**
     *
     * @return apptDescription
     */
    public String getApptDescription() {
        return apptDescription;
    }

    /**
     *
     * @param apptDescription
     */
    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    /**
     *
     * @return apptLocation
     */
    public String getApptLocation() {
        return apptLocation;
    }

    /**
     *
     * @param apptLocation
     */
    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    /**
     *
     * @return apptType
     */
    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    /**
     *
     * @return apptStartTime
     */
    public LocalDateTime getApptStartTime() {
        return apptStartTime;
    }

    /**
     *
     * @param apptStartTime
     */
    public void setApptStartTime(LocalDateTime apptStartTime) {
        this.apptStartTime = apptStartTime;
    }

    /**
     *
     * @return apptEndTime
     */
    public LocalDateTime getApptEndTime() {
        return apptEndTime;
    }

    /**
     *
     * @param apptEndTime
     */
    public void setApptEndTime(LocalDateTime apptEndTime) {
        this.apptEndTime = apptEndTime;
    }

    /**
     *
     * @return apptCreatedDate
     */
    public LocalDateTime getApptCreateDate() {
        return apptCreateDate;
    }

    /**
     *
     * @param apptCreateDate
     */
    public void setApptCreateDate(LocalDateTime apptCreateDate) {
        this.apptCreateDate = apptCreateDate;
    }

    /**
     *
     * @return apptCreatedBy
     */
    public String getApptCreatedBy() {
        return apptCreatedBy;
    }

    /**
     *
     * @param apptCreatedBy
     */
    public void setApptCreatedBy(String apptCreatedBy) {
        this.apptCreatedBy = apptCreatedBy;
    }

    /**
     *
     * @return apptLatUpdate
     */
    public LocalDateTime getApptLastUpdate() {
        return apptLastUpdate;
    }

    /**
     *
     * @param apptLastUpdate
     */
    public void setApptLastUpdate(LocalDateTime apptLastUpdate) {
        this.apptLastUpdate = apptLastUpdate;
    }

    /**
     *
     * @return apptLastUpdatedBy
     */
    public String getApptLastUpdatedBy() {
        return apptLastUpdatedBy;
    }

    /**
     *
     * @param apptLastUpdatedBy
     */
    public void setApptLastUpdatedBy(String apptLastUpdatedBy) {
        this.apptLastUpdatedBy = apptLastUpdatedBy;
    }

    /**
     *
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     *
     * @param contactId
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
