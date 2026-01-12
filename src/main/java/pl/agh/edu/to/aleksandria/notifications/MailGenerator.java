package pl.agh.edu.to.aleksandria.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.queue.QueueEntry;
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

    public static String getRentalReturnedMailSubject(Book book) {
        return "Return of " + book.getTitle().getTitleName() + " confirmed.";
    }

    public static String getQueueJoinedMailSubject(QueueEntry queueEntry) {
        return "You've been added to the waiting list for " + queueEntry.getTitle().getTitleName() + ".";
    }

    public static String getQueueLeftMailSubject(QueueEntry queueEntry) {
        return "You've left waiting list for " + queueEntry.getTitle().getTitleName() + ".";
    }

    public static String getOnRentalMailMessage(User user, Book book, Rental rental) {
        return "Hello " + user.getFirstName() + ",\n" +
               "We've received your request and registered you rental of " + book.getTitle().getTitleName() + ".\n" +
               "Your reservation is due on " + rental.getDue() + ".";
    }

    public static String getRentalSoonEndMailMessage(User user, Book book, Rental rental) {
        return "Hello " + user.getFirstName() + ",\n" +
               "Your rental of " + book.getTitle().getTitleName() + " is due on " + rental.getDue() + ".\n" +
               "Please return your book before that. Otherwise, you will be charged extra fee of 2.5 PLN for every day of the delay.\n";
    }

    public static String getRentalPastDueMailMessage(User user, Book book) {
        return "Hello " + user.getFirstName() + ",\n" +
               "Your rental of " + book.getTitle().getTitleName() + " is past due. From this day onward an extra fee " +
               "of 2.5 PLN per day will be charged.";
    }

    public static String getRentalReturnedMailMessage(User user, Book book, Rental rental) {
        String response = "Hello " + user.getFirstName() + ",\n" +
                          "We've registered your return of " + book.getTitle().getTitleName() + " on " + rental.getReturnedOn() + ".\n";

        response += rental.getFee() > 0 ? "We'd like to also inform you that because of the delay an additional fee of " +
                                          rental.getFee() + " PLN was charged.\n" : "";

        return response;
    }

    public static String getQueueJoinedMailMessage(User user, QueueEntry queueEntry) {
        return "Hello " + user.getFirstName() + ",\n" +
               "As we've no " + queueEntry.getTitle().getTitleName() + " left, you've been added to the waiting " +
               "list for this title. We'll inform you as soon as any volume will become available.";
    }

    public static String getQueueLeftMailMessage(User user, QueueEntry queueEntry) {
        return "Hello " + user.getFirstName() + ",\n" +
               "Title you've been waiting for has become available. You are removed from the waiting list, please " +
               "come to the library and pick up your volume.";
    }
}
