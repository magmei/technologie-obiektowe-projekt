package pl.agh.edu.to.aleksandria.model.statistics;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.agh.edu.to.aleksandria.model.statistics.dtos.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticController {
    private final StatisticService statisticService;

    // GET /statistics/book_count
    @GetMapping("/book_count")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<BookCountStatistic> getBookCount() {
        return statisticService.getBookCountStatistics();
    }

    // GET /statistics/genre_count
    @GetMapping("/genre_count")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<GenreCountStatistic> getGenreCount() {
        return statisticService.getGenreCountStatistics();
    }

    // GET /statistics/popular_books
    @GetMapping("/popular_books")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<BookPopularityStatistic> getPopularBooks() {
        return statisticService.getBookPopularityStatistics();
    }

    // GET /statistics/popular_genres
    @GetMapping("/popular_genres")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<GenrePopularityStatistic> getPopularGenres() {
        return statisticService.getGenrePopularityStatistics();
    }

    // GET /statistics/fees_summary
    @GetMapping("/fees_summary")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getFeesSummary() {
        return ResponseEntity.ok(statisticService.getFeesSummaryStatistics());
    }

    // GET /statistics/best_titles
    @GetMapping("/best_titles")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<BookRatingsStatistic> getBestTitles() {
        return statisticService.getBestTitlesByRating();
    }
}
