package pl.agh.edu.to.aleksandria.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.config.RentalConfig;
import pl.agh.edu.to.aleksandria.model.queue.QueueEntry;
import pl.agh.edu.to.aleksandria.model.rental.Rental;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final RentalConfig rentalConfig;


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

    public void sendOnRentalExtendedEmail(Rental rental, int extendedDays) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalExtendedMailSubject(rental.getBook()),
                MailGenerator.getRentalExtendedMailMessage(rental.getUser(), rental.getBook(), rental, extendedDays)
        );
    }

    public void sendOnRentalPastDueEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalPastDueMailSubject(rental.getBook()),
                MailGenerator.getRentalPastDueMailMessage(rental.getUser(), rental.getBook(), rentalConfig.getLateFee())
        );
    }

    public void sendOnRentalReturnedEmail(Rental rental) {
        this.sendEmail(
                rental.getUser().getEmail(),
                MailGenerator.getRentalReturnedMailSubject(rental.getBook()),
                MailGenerator.getRentalReturnedMailMessage(rental.getUser(), rental.getBook(), rental)
        );
    }

    public void sendOnQueueJoinedEmail(QueueEntry queueEntry) {
        this.sendEmail(
                queueEntry.getUser().getEmail(),
                MailGenerator.getQueueJoinedMailSubject(queueEntry),
                MailGenerator.getQueueJoinedMailMessage(queueEntry.getUser(), queueEntry)
        );
    }

    public void sendOnQueueLeftEmail(QueueEntry queueEntry) {
        this.sendEmail(
                queueEntry.getUser().getEmail(),
                MailGenerator.getQueueLeftMailSubject(queueEntry),
                MailGenerator.getQueueLeftMailMessage(queueEntry.getUser(), queueEntry)
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
