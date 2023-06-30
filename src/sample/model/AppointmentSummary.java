package sample.model;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Model class created to hold data from query that populates the summarized appointment report tableview
 */
public class AppointmentSummary {
    private String month;
    private String type;
    private int totalAppointments;

    /**
     * Constructor for AppointmentSummary
     * @param month
     * @param type
     * @param totalAppointments
     */
    public AppointmentSummary(String month, String type, int totalAppointments) {
        this.month = month;
        this.type = type;
        this.totalAppointments = totalAppointments;
    }

    /**
     *
     * @return
     */
    public String getMonth() {
        return month;
    }

    /**
     *
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getTotal() {
        return totalAppointments;
    }

    /**
     *
     * @param total
     */
    public void setTotal(int total) {
        this.totalAppointments = total;
    }
}
