package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.agh.edu.to.aleksandria.model.rental.RentalService;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.rental.dtos.ExtendRentalRequest;

@Controller
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class WebRentalController {

    private final RentalService rentalService;

    @PostMapping("/web/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String createRental(@RequestParam Integer bookId, @RequestParam Integer userId, @RequestParam Integer titleId, @RequestParam(defaultValue = "14") int rentalDays) {

        CreateRentalRequest request = new CreateRentalRequest(userId, bookId, rentalDays);
        rentalService.createRental(request);

        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/web/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String returnBook(@RequestParam Integer rentalId,
                             @RequestParam Integer userId,
                             RedirectAttributes redirectAttributes) {
        var result = rentalService.returnRental(rentalId);

        if (result.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not return book (already returned or invalid ID).");
        }

        return "redirect:/users/details/" + userId;
    }

    @PostMapping("/web/extend")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String extendBook(@RequestParam Integer rentalId,
                             @RequestParam Integer userId,
                             RedirectAttributes redirectAttributes) {
        try {
            ExtendRentalRequest request = new ExtendRentalRequest(rentalId, 14);
            rentalService.extendRental(request);
            redirectAttributes.addFlashAttribute("successMessage", "Rental extended by 14 days.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/users/details/" + userId;
    }
}
