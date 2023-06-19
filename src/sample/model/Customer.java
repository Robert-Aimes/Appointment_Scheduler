package sample.model;

import java.time.LocalDateTime;

public class Customer {
    private int custId;
    private String custName;
    private String custAddress;
    private String custPostalCode;
    private String custPhone;
    private LocalDateTime custCreateDate;
    private String createdBy;
    private LocalDateTime custLastUpdate;
    private String lastUpdatedBy;
    private int custDivisionId;
    private String custDivisionName;

    public Customer(int custId, String custName, String custAddress, String custPostalCode, String custPhone, LocalDateTime custCreateDate, String createdBy, LocalDateTime custLastUpdate, String lastUpdatedBy, int custDivisionId, String custDivisionName) {
        this.custId = custId;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPostalCode = custPostalCode;
        this.custPhone = custPhone;
        this.custCreateDate = custCreateDate;
        this.createdBy = createdBy;
        this.custLastUpdate = custLastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.custDivisionId = custDivisionId;
        this.custDivisionName = custDivisionName;
    }

    /**
     *
     * @return custId
     */
    public int getCustId() {
        return custId;
    }

    /**
     *
     * @param custId
     */
    public void setCustId(int custId) {
        this.custId = custId;
    }

    /**
     *
     * @return custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     *
     * @param custName
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     *
     * @return custAddress
     */
    public String getCustAddress() {
        return custAddress;
    }

    /**
     *
     * @param custAddress
     */
    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    /**
     *
     * @return custPostalCode
     */
    public String getCustPostalCode() {
        return custPostalCode;
    }

    /**
     *
     * @param custPostalCode
     */
    public void setCustPostalCode(String custPostalCode) {
        this.custPostalCode = custPostalCode;
    }

    /**
     *
     * @return custPhone
     */
    public String getCustPhone() {
        return custPhone;
    }

    /**
     *
     * @param custPhone
     */
    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    /**
     *
     * @return custCreatedDate
     */
    public LocalDateTime getCustCreateDate() {
        return custCreateDate;
    }

    /**
     *
     * @param custCreateDate
     */
    public void setCustCreateDate(LocalDateTime custCreateDate) {
        this.custCreateDate = custCreateDate;
    }

    /**
     *
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return custLastUpdate
     */
    public LocalDateTime getCustLastUpdate() {
        return custLastUpdate;
    }

    /**
     *
     * @param custLastUpdate
     */
    public void setCustLastUpdate(LocalDateTime custLastUpdate) {
        this.custLastUpdate = custLastUpdate;
    }

    /**
     *
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     *
     * @param lastUpdatedBy
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *
     * @return custDivisionId
     */
    public int getCustDivisionId() {
        return custDivisionId;
    }

    /**
     *
     * @param custDivisionId
     */
    public void setCustDivisionId(int custDivisionId) {
        this.custDivisionId = custDivisionId;
    }

    /**
     *
     * @return custDivisionName
     */
    public String getCustDivisionName() {
        return custDivisionName;
    }

    /**
     *
     * @param custDivisionName
     */
    public void setCustDivisionName(String custDivisionName) {
        this.custDivisionName = custDivisionName;
    }
}
