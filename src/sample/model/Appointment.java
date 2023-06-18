package sample.model;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.time.LocalDateTime;

public class Appointment {
    private IntegerProperty appointmentId;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty type;
    private ObjectProperty<LocalDateTime> start;
    private ObjectProperty<LocalDateTime> end;
    private ObjectProperty<LocalDateTime> createDate;
    private StringProperty createdBy;
    private ObjectProperty<LocalDateTime> lastUpdate;
    private StringProperty lastUpdatedBy;
    private IntegerProperty customerId;
    private IntegerProperty userId;
    private IntegerProperty contactId;


    public IntegerProperty getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(IntegerProperty appointmentId) {
        this.appointmentId = appointmentId;
    }

    public StringProperty getTitle() {
        return title;
    }

    public void setTitle(StringProperty title) {
        this.title = title;
    }

    public StringProperty getDescription() {
        return description;
    }

    public void setDescription(StringProperty description) {
        this.description = description;
    }

    public StringProperty getLocation() {
        return location;
    }

    public void setLocation(StringProperty location) {
        this.location = location;
    }

    public StringProperty getType() {
        return type;
    }

    public void setType(StringProperty type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start.get();
    }

    public ObjectProperty<LocalDateTime> startProperty() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start.set(start);
    }

    public LocalDateTime getEnd() {
        return end.get();
    }

    public ObjectProperty<LocalDateTime> endProperty() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end.set(end);
    }

    public LocalDateTime getCreateDate() {
        return createDate.get();
    }

    public ObjectProperty<LocalDateTime> createDateProperty() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate.set(createDate);
    }

    public StringProperty getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringProperty createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate.get();
    }

    public ObjectProperty<LocalDateTime> lastUpdateProperty() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public StringProperty getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(StringProperty lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public IntegerProperty getCustomerId() {
        return customerId;
    }

    public void setCustomerId(IntegerProperty customerId) {
        this.customerId = customerId;
    }

    public IntegerProperty getUserId() {
        return userId;
    }

    public void setUserId(IntegerProperty userId) {
        this.userId = userId;
    }

    public IntegerProperty getContactId() {
        return contactId;
    }

    public void setContactId(IntegerProperty contactId) {
        this.contactId = contactId;
    }


}
