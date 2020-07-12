package orederondoor.com.projectorder.Shopkeeper.RecyclerView;

public class Users {

    private String userName;
    private String userType;
    private String userContact;
    private String userPassword;
    private String phoneNo;

    public Users(){
    }
    public Users(String userName, String userPassword)
    {
        this.userName=userName;
        this.userPassword=userPassword;

    }
    public Users(String userName, String userType, String userContact, String userPassword) {
        this.userName = userName;
        this.userType = userType;
        this.userContact = userContact;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
/*
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }*/

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
