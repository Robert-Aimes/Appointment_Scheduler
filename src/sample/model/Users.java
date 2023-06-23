package sample.model;

public class Users {
    public int userId;
    public String userName;
    public String userPassword;

    public Users(int userId, String userName, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
    }



    //Getters and Setters

    /**
     *
     * @return userID
     */
    public int getUserID() {
        return userId;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(int userID) {
        this.userId = userID;
    }

    /**
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
