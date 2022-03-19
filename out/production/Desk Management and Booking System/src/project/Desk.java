package project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Desk implements Serializable
{
    private Booking booking;
    private int ID;
    private boolean reserved;

    public static int DESK_POINTER = 0;
    public static final int INITIAL_DESKS = 25;
    public static final String ADD_DESK = "Add Desks";
    public static final String REMOVE_DESK = "Remove Desks";
    private static List<Desk> deskSpace;

    private static int HOUR_TRENDS = 0;
    private static int DAY_TRENDS = 0;
    private static int WEEK_TRENDS = 0;

    public static final int HOUR_VIEW = 1;
    public static final int DAY_VIEW = 2;
    public static final int WEEK_VIEW = 3;

    public static List<Desk> getAllDesks()
    {
        return deskSpace;
    }

    public static void setAllDesks(List<Desk> desks)
    {
        deskSpace = desks;
        for(Desk desk : desks)
        {
            Booking booking = desk.getBooking();
            if(!booking.isEmpty())
            {
                if(booking.getHours() != 0)
                    ++HOUR_TRENDS;
                else if(booking.getDays() != 0)
                    ++DAY_TRENDS;
                else
                    ++WEEK_TRENDS;
            }
        }
    }

    public static int getNumberOfDesks()
    {
        return deskSpace.size();
    }

    public Booking makeBooking(String username, Date startDate, int hours, int days, int weeks)
    {
        this.booking = new Booking(username, startDate, ID + 1, hours, days, weeks);
        if(hours != 0)
            ++HOUR_TRENDS;
        else if(days != 0)
            ++DAY_TRENDS;
        else
            ++WEEK_TRENDS;
        return this.booking;
    }

    public boolean isReserved()
    {
        return reserved;
    }

    public void isReserved(boolean reserved)
    {
        this.reserved = reserved;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public Desk(int ID)
    {
        this.booking = new Booking();
        this.reserved = false;
        this.ID = ID;
        deskSpace.add(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Desk desk))
            return false;

        return this.booking.equals(desk.booking) && this.reserved == desk.reserved && this.ID == desk.ID;
    }

    public boolean isAvailable()
    {
        return booking.isEmpty();
    }

    public Booking getBooking()
    {
        return this.booking;
    }

    public static Desk getDesk(int key)
    {
        return deskSpace.get(key);
    }

    public static void addOrRemoveDesk(int numberOfDesks, String operation) throws Exception
    {
        if(numberOfDesks < 1)
            throw new Exception(" not being able to perform the requested operation on the quantity of desks provided!");

        int availableDesks = deskSpace.size();
        if(operation.equals(ADD_DESK))
        {
            for(int quantity = 1; quantity <= numberOfDesks; ++quantity)
                new Desk(availableDesks + quantity);
        }
        else
        {
            if(numberOfDesks >= availableDesks)
                throw new Exception(" not being able to perform the requested operation on the quantity of desks provided!");

            List<Desk> removableDesks = new ArrayList<>();
            for(int startIndex = availableDesks - numberOfDesks; startIndex < availableDesks; ++startIndex)
            {
                Desk desk = deskSpace.get(startIndex);
                if(desk.isAvailable())
                    removableDesks.add(desk);
            }
            for(Desk desk : removableDesks)
                deskSpace.remove(desk);
        }
        Loader.saveDataFile();
    }

    @Override
    public String toString()
    {
        return "" + getID();
    }

    public static void init()
    {
        deskSpace = new ArrayList<>();
        for(int pos = 0; pos < INITIAL_DESKS; ++pos)
            new Desk(pos + 1);
    }

    public static void cancelBooking(Booking booking)
    {
        for(Desk desk : deskSpace)
        {
            if(!desk.booking.isEmpty() && desk.booking.equals(booking))
            {
                desk.booking = new Booking();
                if(booking.getHours() != 0)
                    --HOUR_TRENDS;
                else if(booking.getDays() != 0)
                    --DAY_TRENDS;
                else
                    --WEEK_TRENDS;
            }
        }
    }

    public static List<Desk> getAllAvailableDesks()
    {
        List<Desk> desks = new ArrayList<>();
        for(Desk desk : deskSpace)
        {
            if(desk.isAvailable())
                desks.add(desk);
        }
        return desks;
    }

    public static Desk getFirstAvailableDesk()
    {
        for(Desk desk : deskSpace)
        {
            if(desk.isAvailable())
                return desk;
        }
        return null;
    }

    public int getID()
    {
        return ID;
    }

    public static Desk getFirstAvailableForUser(String username)
    {
        for(Desk desk : deskSpace)
        {
            Booking booking = desk.booking;
            if(booking.isEmpty())
                break;
            else if(booking.getUser().equals(username))
                return null;
        }
        return getFirstAvailableDesk();
    }

    public static int getHourTrends()
    {
        return HOUR_TRENDS;
    }

    public static int getDayTrends()
    {
        return DAY_TRENDS;
    }

    public static int getWeekTrends()
    {
        return WEEK_TRENDS;
    }

    public static List<Desk> getBookings(int viewType, boolean free)
    {
        List<Desk> bookings;
        if(viewType == HOUR_VIEW)
        {
            bookings = new ArrayList<>();
            for(Desk desk : deskSpace)
            {
                Booking booking = desk.getBooking();
                if(free)
                {
                    if(booking.getConstant() == 0)
                        bookings.add(desk);
                }
                else
                {
                    if(booking.getHours() != 0)
                        bookings.add(desk);
                }
            }
            return bookings;
        }
        else if(viewType == DAY_VIEW)
        {
            bookings = new ArrayList<>();
            for(Desk desk : deskSpace)
            {
                Booking booking = desk.getBooking();
                if(free)
                {
                    if(booking.getConstant() == 0)
                        bookings.add(desk);
                }
                else
                {
                    if(booking.getDays() != 0)
                        bookings.add(desk);
                }
            }
            return bookings;
        }
        else if(viewType == WEEK_VIEW)
        {
            bookings = new ArrayList<>();
            for(Desk desk : deskSpace)
            {
                Booking booking = desk.getBooking();
                if(free)
                {
                    if(booking.getConstant() == 0)
                        bookings.add(desk);
                }
                else
                {
                    if(booking.getWeeks() != 0)
                        bookings.add(desk);
                }
            }
            return bookings;
        }
        return null;
    }

}
