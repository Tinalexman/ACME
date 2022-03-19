package project;

public class DateUtility
{
    public static final int SECONDS_IN_WEEK = 604800;
    public static final int SECONDS_IN_DAY = 86400;
    public static final int SECONDS_IN_HOUR = 3600;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MILLIS_IN_SECONDS = 1000;

    public static int getTotalSeconds(int weeks, int days, int hours)
    {
        int totalSeconds = 0;
        totalSeconds += (hours * SECONDS_IN_HOUR);
        totalSeconds += (days * SECONDS_IN_DAY);
        totalSeconds += (weeks * SECONDS_IN_WEEK);
        return totalSeconds;
    }

    public static long getSecondsLeft(long currentMilliSeconds, Booking booking)
    {
        int weeks = booking.getWeeks();
        int days = booking.getDays();
        int hours = booking.getHours();
        long pastMilliSeconds = booking.getStartDate().getTime();
        long totalSeconds = getTotalSeconds(weeks, days, hours);
        long diff = (totalSeconds * MILLIS_IN_SECONDS) - (currentMilliSeconds - pastMilliSeconds);
        return (long) (diff * 0.001);
    }

    public static long getTimeDifference(long currentMilliSeconds, long pastMilliSeconds)
    {
        return (long) ((currentMilliSeconds - pastMilliSeconds) * 0.001);
    }

    public static String formatDate(long seconds)
    {
        int weeks = (int) (seconds / SECONDS_IN_WEEK);
        seconds %= SECONDS_IN_WEEK;
        int days = (int) (seconds / SECONDS_IN_DAY);
        seconds %= SECONDS_IN_DAY;
        int hours = (int) (seconds / SECONDS_IN_HOUR);
        seconds %= SECONDS_IN_HOUR;
        String answer = "";
        if(weeks != 0)
            answer += (weeks + (weeks == 1 ? " week " : " weeks "));
        if(days != 0)
            answer += (days + (days == 1 ? " day " : " days "));
        if(hours != 0)
            answer += (hours + (hours == 1 ? " hour " : " hours "));
        int minutes = (int) (seconds / SECONDS_IN_MINUTE);
        if(minutes != 0)
            answer += (minutes + (minutes == 1 ? " minute " : " minutes "));
        seconds %= SECONDS_IN_MINUTE;
        answer += seconds + " seconds ";
        return answer;
    }
}
