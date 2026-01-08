package pl.agh.edu.to.aleksandria.notifications;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.rental.Rental;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOnRentalEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getOnRentalMailSubject(rental.getBook()),
                MailGenerator.getOnRentalMailMessage(rental.getUser(), rental.getBook(), rental)
        );
    }

    public void sendOnRentalSoonEndEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalSoonEndMailSubject(rental.getBook(), rental),
                MailGenerator.getRentalSoonEndMailMessage(rental.getUser(), rental.getBook(), rental)
        );
    }

    public void sendOnRentalPastDueEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalPastDueMailSubject(rental.getBook()),
                MailGenerator.getRentalPastDueMailMessage(rental.getUser(), rental.getBook())
        );
    }

    public void sendOnRentalReturnedEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalReturnedMailSubject(rental.getBook()),
                MailGenerator.getRentalReturnedMailMessage(rental.getUser(), rental.getBook(), rental)
        );
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("adsuliga@student.agh.edu.pl");
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
