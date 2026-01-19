package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookService;
import pl.agh.edu.to.aleksandria.model.queue.QueueEntry;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.review.Review;
import pl.agh.edu.to.aleksandria.model.review.ReviewService;
import pl.agh.edu.to.aleksandria.model.review.dtos.CreateReviewRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleService;
import pl.agh.edu.to.aleksandria.model.title.dtos.CreateTitleRequest;
import pl.agh.edu.to.aleksandria.model.title.dtos.UpdateTitleRequest;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/titles")
@RequiredArgsConstructor
public class WebTitleController {

    private final TitleService titleService;
    private final ReviewService reviewService;
    private final BookService bookService;
    private final QueueService queueService;
    private final UserService userService;

    @GetMapping("/view/{id}")
    public String viewTitle(@PathVariable Integer id, Model model) {
        Title title = titleService.getTitleById(id).orElseThrow(() -> new IllegalArgumentException("Invalid title Id:" + id));
        model.addAttribute("title", title);

        List<Review> reviews = reviewService.getAllReviewsByTitleId(id);
        model.addAttribute("reviews", reviews);

        Map<Integer, User> authors = new HashMap<>();
        for (Review review : reviews) {
            int userId = review.getUserId();
            if (!authors.containsKey(userId)) {
                userService.getUserById(userId).ifPresent(user -> authors.put(userId, user));
            }
        }
        model.addAttribute("authors", authors);

        List<Book> books = bookService.getBooksByTitleId(id);
        model.addAttribute("books", books);

        List<QueueEntry> queue = queueService.getQueueEntriesForTitle(id);
        model.addAttribute("queue", queue);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            model.addAttribute("currentUser", (User) auth.getPrincipal());
        }

        return "title-details";
    }

    @PostMapping("/review/add")
    @PreAuthorize("hasRole('READER')")
    public String addReview(@RequestParam Integer titleId, @RequestParam Integer rating, @RequestParam String comment) {

        CreateReviewRequest request = new CreateReviewRequest();
        request.setTitleId(titleId);
        request.setRating(rating);
        request.setReviewText(comment);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        request.setUserId(currentUser.getId());

        reviewService.createReview(request);

        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/web/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String createTitle(@RequestParam String titleName, @RequestParam String author, @RequestParam(required = false) Integer genreId) {

        CreateTitleRequest request = new CreateTitleRequest(titleName, author, Collections.singletonList(genreId));

        titleService.createTitle(request);

        return "redirect:/";
    }

    @PostMapping("/web/update")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String updateTitle(@RequestParam Integer id,
                              @RequestParam String titleName,
                              @RequestParam String author,
                              @RequestParam(required = false) Integer genreToAdd,
                              @RequestParam(required = false) Integer genreToDelete) {

        List<Integer> toAdd = (genreToAdd != null) ? Collections.singletonList(genreToAdd) : Collections.emptyList();
        List<Integer> toDelete = (genreToDelete != null) ? Collections.singletonList(genreToDelete) : Collections.emptyList();
        UpdateTitleRequest request = new UpdateTitleRequest(id, titleName, author, toAdd, toDelete);

        titleService.updateTitle(request);

        return "redirect:/titles/view/" + id;
    }


}
