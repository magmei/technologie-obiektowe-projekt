package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.agh.edu.to.aleksandria.model.statistics.StatisticService;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class WebStatisticsController {

    private final StatisticService statisticService;

    @GetMapping
    public String showStatisticsPage() {
        return "stats";
    }

    @GetMapping("/fragments/rated")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String getRatedFragment(Model model) {
        model.addAttribute("bestTitles", statisticService.getBestTitlesByRating());
        return "stats-content :: rated";
    }

    @GetMapping("/fragments/availability")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String getAvailabilityFragment(Model model) {
        model.addAttribute("bookCounts", statisticService.getBookCountStatistics());
        return "stats-content :: availability";
    }

    @GetMapping("/fragments/financials")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String getFinancialsFragment(Model model) {
        model.addAttribute("feesSummary", statisticService.getFeesSummaryStatistics());
        return "stats-content :: financials";
    }

    @GetMapping("/fragments/popular-books")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String getPopularBooksFragment(Model model) {
        model.addAttribute("popularBooks", statisticService.getBookPopularityStatistics());
        return "stats-content :: popular-books";
    }

    @GetMapping("/fragments/popular-genres")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String getPopularGenresFragment(Model model) {
        model.addAttribute("popularGenres", statisticService.getGenrePopularityStatistics());
        return "stats-content :: popular-genres";
    }
}