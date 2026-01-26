package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.agh.edu.to.aleksandria.model.book.BookService;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class WebBookController {

    private final BookService bookService;

    @PostMapping("/web/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String createBook(@RequestParam Integer titleId) {
        bookService.createBook(titleId);
        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String deleteBook(@RequestParam Integer id, @RequestParam(required = false) Integer titleId) {
        bookService.deleteBook(id);
        if (titleId != null) {
            return "redirect:/titles/view/" + titleId;
        }
        return "redirect:/";
    }
}
