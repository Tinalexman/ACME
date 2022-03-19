package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable
{
    private final UserType userType;
    private final String username;
    private final String password;
    private final String emailAddress;

    private Booking booking;
    private boolean onLeave;
    private boolean alertCancel;
    private boolean attending;

    private static List<User> allUsers;
    private static final String DEFAULT_ADMIN_EMAIL = "admin@acme.com";
    private static final String DEFAULT_PASSWORD = "1234";
    private static final String DEFAULT_ADMIN_NAME = "Admin Staff";
    private static final String DEFAULT_USER_NAME = "User Staff";
    private static final String DEFAULT_USER_EMAIL = "user@acme.com";

    public static void setAllUsers(List<User> users)
    {
        allUsers = users;
    }

    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    public static void removeUser(String username) throws Exception
    {
        for(User user : allUsers)
        {
            if(user.username.equals(username))
            {
                allUsers.remove(user);
                Loader.saveDataFile();
                return;
            }
        }
        throw new Exception("User " + username + " not existing in the database!");
    }

    public static User getUser(String username) throws Exception
    {
        for(User user : allUsers)
        {
            if(user.username.equals(username))
                return user;
        }
        throw new Exception("User " + username + " not existing in the database!");
    }

    @Override
    public String toString()
    {
        return this.username + " " + this.userType;
    }

    public static void addUser(String username, String emailAddress, String password, UserType userType) throws Exception
    {
        User user = new User(username, emailAddress, password, userType);
        if(!allUsers.contains(user))
            throw new Exception("the user already existing in the database!");
        Loader.saveDataFile();
    }

    public static void init()
    {
        allUsers = new ArrayList<>();
        try
        {
            addUser(DEFAULT_ADMIN_NAME, DEFAULT_ADMIN_EMAIL, DEFAULT_PASSWORD, UserType.Administrator);
            addUser(DEFAULT_USER_NAME, DEFAULT_USER_EMAIL, DEFAULT_PASSWORD, UserType.User);
        }
        catch (Exception exception)
        {
            System.err.println("Error: " + exception.getMessage());
        }
    }

    protected User(String username, String email, String password, UserType userType)
    {
        this.username = username;
        this.userType = userType;
        this.password = password;
        this.emailAddress = email;
        this.booking = new Booking();
        this.onLeave = false;
        this.attending = false;
        this.alertCancel = false;
        allUsers.add(this);
    }

    public void setAlertOnLoad(boolean alert)
    {
        this.alertCancel = false;
    }

    public boolean alertOnLoad()
    {
        return this.alertCancel;
    }

    public boolean isOnLeave()
    {
        return onLeave;
    }

    public void isOnLeave(boolean onLeave)
    {
        this.onLeave = onLeave;
    }

    public static User getCurrentUser(String email, String password)
    {
        for(User user : allUsers)
        {
            if(user.emailAddress.equals(email) && user.password.equals(password))
                return user;
        }
        return null;
    }

    public String getUsername()
    {
        return username;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public Booking getBooking()
    {
        return this.booking;
    }

    public void setBooking(Booking booking)
    {
        this.booking = booking;
        Loader.saveDataFile();
    }

    public boolean isAttending()
    {
        return this.attending;
    }

    public void isAttending(boolean attending)
    {
        this.attending = attending;
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof User user))
            return false;
        return this.username.equals(user.username)
                && this.password.equals(user.password) && this.userType == user.userType
                && this.emailAddress.equals(user.emailAddress);
    }

    public static List<User> getAllUsers()
    {
        return allUsers;
    }

    public static int getNumberOfUsers()
    {
        return allUsers.size();
    }

}
