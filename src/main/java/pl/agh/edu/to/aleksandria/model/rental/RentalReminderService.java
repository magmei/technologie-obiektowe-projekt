package pl.agh.edu.to.aleksandria.model.rental;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.notifications.MailService;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalReminderService {

    private final RentalRepository rentalRepository;
    private final MailService mailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendRentalReminder() {
        LocalDate reminderDate = LocalDate.now().plusDays(3);

        List<Rental> rentalsDueSoon = rentalRepository.findByDueAndReturnedOnIsNull(reminderDate);

        for (Rental rental : rentalsDueSoon) {
            mailService.sendOnRentalSoonEndEmail(rental);
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendRentalWarning() {
        LocalDate reminderDate = LocalDate.now().minusDays(1);

        List<Rental> rentalsPastDue = rentalRepository.findByDueAndReturnedOnIsNull(reminderDate);

        for (Rental rental : rentalsPastDue) {
            mailService.sendOnRentalPastDueEmail(rental);
        }
    }

}
