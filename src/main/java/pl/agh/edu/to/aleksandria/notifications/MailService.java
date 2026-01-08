package pl.agh.edu.to.aleksandria.notifications;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.user.User;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOnRentalEmail(User user, Book book, Rental rental) {
        this.sendEmail(
                user.getEmail(),
                MailGenerator.getOnRentalMailSubject(book),
                MailGenerator.getOnRentalMailMessage(user, book, rental)
        );
    }

    public void sendOnRentalSoonEndEmail(User user, Book book, Rental rental) {
        this.sendEmail(
                user.getEmail(),
                MailGenerator.getRentalSoonEndMailSubject(book, rental),
                MailGenerator.getRentalSoonEndMailMessage(user, book, rental)
        );
    }

    public void sendOnRentalPastDueEmail(User user, Book book, Rental rental) {
        this.sendEmail(
                user.getEmail(),
                MailGenerator.getRentalPastDueMailSubject(book),
                MailGenerator.getRentalPastDueMailMessage(user, book)
        );
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
