package pl.agh.edu.to.aleksandria.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.user.User;

@Setter
@Getter
@AllArgsConstructor
public class MailGenerator {

    public static String getOnRentalMailSubject(Book book) {
        return "Rental of " + book.getTitle().getTitleName() + " confirmed.";
    }

    public static String getRentalSoonEndMailSubject(Book book, Rental rental) {
        return "Rental of " + book.getTitle().getTitleName() + " is due on " + rental.getDue();
    }

    public static String getRentalPastDueMailSubject(Book book) {
        return "Rental of " + book.getTitle().getTitleName() + " is past due!";
    }

    public static String getOnRentalMailMessage(User user, Book book, Rental rental) {
        return "Hello " + user.getFirstName() + ",\n" +
               "We've received your request and registered you rental of " + book.getTitle().getTitleName() + ".\n" +
               "Your reservation is due on " + rental.getDue() + ".";
    }

    public static String getRentalSoonEndMailMessage(User user, Book book, Rental rental) {
        return "Hello " + user.getFirstName() + ",\n" +
               "Your rental of " + book.getTitle().getTitleName() + " is due on " + rental.getDue() + ".\n" +
               "Please return your book before that. Otherwise, you will be charged extra fee for every day of the delay.\n";
    }

    public static String getRentalPastDueMailMessage(User user, Book book) {
        return "Hello " + user.getFirstName() + ",\n" +
               "Your rental of " + book.getTitle().getTitleName() + " is past due. From this day onward an extra fee " +
               "will be charged.";
    }
}
