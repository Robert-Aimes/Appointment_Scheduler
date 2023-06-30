package sample.model;

public class CustomersByState {
    private String state;
    private int totalCustomers;

    public CustomersByState(String state, int totalCustomers){
        this.state = state;
        this.totalCustomers = totalCustomers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
