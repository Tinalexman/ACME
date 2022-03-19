package project;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Booking implements Serializable
{
    private final int weeks;
    private final int days;
    private final int hours;
    private final int id;
    private final Date startDate;
    private final String startTime;
    private final String user;
    private final int constant;

    public static final int MAX_BOOKING_HOURS = 336;
    public static final int MAX_BOOKING_DAYS = 14;
    public static final int MAX_BOOKING_WEEKS = 2;

    protected Booking(String user, Date startDate, int id, int hours, int days, int weeks)
    {
        this.user = user;
        this.startDate = startDate;
        this.hours = hours;
        this.days = days;
        this.weeks = weeks;
        this.id = id;
        if(weeks != 0)
            constant = weeks;
        else if(days != 0)
            constant = days;
        else
            constant = hours;
        this.startTime = DateUtility.formatDate(DateUtility.getTotalSeconds(this.weeks, this.days, this.hours));
    }

    protected Booking()
    {
        this.user = "";
        this.startDate = Calendar.getInstance().getTime();
        this.hours = 0;
        this.days = 0;
        this.weeks = 0;
        this.id = 0;
        this.constant = 0;
        this.startTime = DateUtility.formatDate(DateUtility.getTotalSeconds(this.weeks, this.days, this.hours));
    }

    public boolean isEmpty()
    {
        return this.constant == 0;
    }

    public int getId()
    {
        return id;
    }

    public Date getStartDate()
    {
        return this.startDate;
    }

    public int getConstant()
    {
        return this.constant;
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Booking booking))
            return false;
        return this.user.equals(booking.user) && this.days == booking.days
                && this.hours == booking.hours && this.weeks == booking.weeks
                && this.startDate.equals(booking.startDate) && this.id == booking.id;
    }

    public int getHours()
    {
        return this.hours;
    }

    public String getUser()
    {
        return this.user;
    }

    public String getStartTime()
    {
        return this.startTime;
    }

    public int getWeeks()
    {
        return weeks;
    }

    public int getDays()
    {
        return days;
    }

    @Override
    public String toString()
    {
        return "" + getConstant();
    }

}