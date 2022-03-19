package project;

import java.util.Calendar;
import java.util.Scanner;

public class Test
{
    public static void testProgram()
    {
        Loader.validateHomeFolder();
        Loader.loadDataFile();


        User normalUser = new User("User", "user.com", "1234", UserType.User);
        System.out.println("A new user has been created");
        Scanner scanner = new Scanner(System.in);

        Booking userBooking = normalUser.getBooking();
        if(userBooking.isEmpty())
        {
            System.out.println("User " + normalUser.getUsername() + " has no booking on ground! " +
                    "Creating a new booking automatically");
        }

        System.out.print("Enter the booking type (Hour), (Day) or (Week): ");
        String input = scanner.nextLine();
        input = input.trim().toLowerCase();
        System.out.print("Enter the duration: ");
        String duration = scanner.nextLine();
        duration = duration.trim();
        int time = Integer.parseInt(duration);

        Desk chosenDesk = Desk.getFirstAvailableForUser(normalUser.getUsername());
        String constant;
        if(input.startsWith("h"))
        {
            constant = "hours";
            if(time > Booking.MAX_BOOKING_HOURS || time < 1)
            {
                time = Booking.MAX_BOOKING_HOURS;
                System.err.println("An invalid input was provided. Target is set to " + Booking.MAX_BOOKING_HOURS + constant);
            }
            Booking booking = chosenDesk.makeBooking(normalUser.getUsername(), Calendar.getInstance().getTime(), time, 0, 0);
            normalUser.setBooking(booking);
        }
        else if(input.startsWith("d"))
        {
            constant = "days";
            if(time > Booking.MAX_BOOKING_DAYS || time < 1)
            {
                time = Booking.MAX_BOOKING_DAYS;
                System.err.println("An invalid input was provided. Target is set to " + Booking.MAX_BOOKING_DAYS + constant);
            }
            Booking booking = chosenDesk.makeBooking(normalUser.getUsername(), Calendar.getInstance().getTime(), 0, time, 0);
            normalUser.setBooking(booking);
        }
        else
        {
            constant = "weeks";
            if(time > Booking.MAX_BOOKING_WEEKS || time < 1)
            {
                time = Booking.MAX_BOOKING_WEEKS;
                System.err.println("An invalid input was provided. Target is set to " + Booking.MAX_BOOKING_WEEKS + constant);
            }
            Booking booking = chosenDesk.makeBooking(normalUser.getUsername(), Calendar.getInstance().getTime(), 0, 0, time);
            normalUser.setBooking(booking);
        }
        System.out.println("A booking has been created for user " + normalUser.getUsername()
                + " at Desk " + chosenDesk.getID() + " for " + chosenDesk.getBooking().getStartTime());

        Desk.cancelBooking(normalUser.getBooking());
        normalUser.setBooking(new Booking());

        System.out.println("The booking for user " + normalUser.getUsername() + " has been canceled!");
    }

}
