package sample.model;

public class CustomersByState {
    private String state;
    private int totalCustomers;

    /**
     * Constructor for CustomersByState
     * @param state
     * @param totalCustomers
     */

    public CustomersByState(String state, int totalCustomers){
        this.state = state;
        this.totalCustomers = totalCustomers;
    }

    /**
     * Getter
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Setter
     * @param state
     */

    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter
     * @return totalCustomers
     */

    public int getTotalCustomers() {
        return totalCustomers;
    }

    /**
     * Setter
     * @param totalCustomers
     */

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
