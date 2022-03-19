package project;

import java.io.*;
import java.util.List;

public class Loader
{
    private static File databaseFile;
    private static final String HOME_DIRECTORY = System.getProperty("user.home");
    private static final String ACME_HOME = HOME_DIRECTORY + "\\ACME";
    private static String ERROR_MESSAGE = "";

    public static boolean loadDataFile()
    {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(databaseFile)))
        {
            List<User> users = (List<User>) inputStream.readObject();
            User.setAllUsers(users);
            List<Desk> desks = (List<Desk>) inputStream.readObject();
            Desk.setAllDesks(desks);
            return true;
        }
        catch (IOException | ClassNotFoundException exception)
        {
            ERROR_MESSAGE = exception.getMessage();
        }
        return false;
    }

    private static boolean loadDefaults()
    {
        User.init();
        Desk.init();
        return saveDataFile();
    }

    public static boolean validateHomeFolder()
    {
        try
        {
            File directory = new File(ACME_HOME);
            if(!directory.exists())
            {
                directory.mkdir();
                databaseFile = new File(ACME_HOME + "\\database.acme");
                if(!databaseFile.exists())
                    databaseFile.createNewFile();
                return loadDefaults();
            }
            else
            {
                databaseFile = new File(ACME_HOME + "\\database.acme");
                if(!databaseFile.exists())
                {
                    databaseFile.createNewFile();
                    return loadDefaults();
                }
                return true;
            }
        }
        catch(IOException exception)
        {
            ERROR_MESSAGE = exception.getMessage();
        }
        return false;
    }

    public static String getErrorMessage()
    {
        return ERROR_MESSAGE;
    }

    public static boolean saveDataFile()
    {
        try
        {
            new PrintWriter(databaseFile).close();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(databaseFile));
            outputStream.writeObject(User.getAllUsers());
            outputStream.writeObject(Desk.getAllDesks());
            outputStream.flush();
            outputStream.close();
            return true;
        }
        catch (IOException exception)
        {
            ERROR_MESSAGE = exception.getMessage();
        }
        return false;
    }

    public static boolean saveReceipt(String username, String duration, int id)
    {
        try
        {
            File file = new File(HOME_DIRECTORY + "\\" + username + ".txt");
            PrintWriter writer;
            if(!file.exists())
                file.createNewFile();
            writer = new PrintWriter(file);
            writer.println("Staff Name: " + username);
            writer.println("Desk Id: " + id);
            writer.println("Booked For: " + duration);
            writer.close();
            return true;
        }
        catch (IOException ex)
        {
            ERROR_MESSAGE = ex.getMessage();
        }
        return false;
    }
}
