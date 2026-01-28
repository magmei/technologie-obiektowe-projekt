package pl.agh.edu.to.aleksandria.model.statistics;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.book.BookRepository;
import pl.agh.edu.to.aleksandria.model.genre.Genre;
import pl.agh.edu.to.aleksandria.model.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.rental.RentalRepository;
import pl.agh.edu.to.aleksandria.model.review.Review;
import pl.agh.edu.to.aleksandria.model.review.ReviewRepository;
import pl.agh.edu.to.aleksandria.model.statistics.dtos.*;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticService {

    private BookRepository bookRepository;
    private TitleRepository titleRepository;
    private RentalRepository rentalRepository;
    private GenreRepository genreRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    public List<BookCountStatistic> getBookCountStatistics() {
        List<BookCountStatistic> result = new ArrayList<>();
        List<Title> titles = titleRepository.findAll();

        for (Title title : titles) {
            BookCountStatistic stat = new BookCountStatistic(
                    title.getId(),
                    title.getTitleName(),
                    title.getAuthor(),
                    bookRepository.findByTitle(title).size(),
                    bookRepository.findByAvailableTrueAndTitle_TitleName(title.getTitleName()).size()
            );

            result.add(stat);
        }

        result.sort(Comparator.comparingInt(BookCountStatistic::getAllBookCount).reversed());

        return result;
    }

    public List<GenreCountStatistic> getGenreCountStatistics() {
        List<GenreCountStatistic> result = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();

        for (Genre genre : genres) {

            int titleCount = genre.getTitles().size(), bookCount = 0;

            for (Title title : genre.getTitles()) {
                bookCount += bookRepository.findByTitle(title).size();
            }

            GenreCountStatistic stat = new GenreCountStatistic(
                    genre.getId(),
                    genre.getName(),
                    titleCount,
                    bookCount
            );

            result.add(stat);
        }

        result.sort(Comparator.comparingInt(GenreCountStatistic::getTitleCount).reversed());

        return result;
    }

    public List<BookPopularityStatistic> getBookPopularityStatistics() {
        List<BookPopularityStatistic> result = new ArrayList<>();
        List<Title> titles = titleRepository.findAll();

        for (Title title : titles) {
            BookPopularityStatistic stat = new BookPopularityStatistic(
                    title.getId(),
                    title.getTitleName(),
                    title.getAuthor(),
                    rentalRepository.findByBook_Title_Id(title.getId()).size(),
                    rentalRepository.findByBook_Title_IdAndReturnedOnIsNull(title.getId()).size()
            );

            result.add(stat);
        }

        result.sort(Comparator.comparingInt(BookPopularityStatistic::getRentedCount).reversed());

        return result;
    }

    public List<GenrePopularityStatistic> getGenrePopularityStatistics() {
        List<GenrePopularityStatistic> result = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();

        for (Genre genre : genres) {
            int rentedCount = 0, rentingRightNowCount = 0;

            for (Title title : genre.getTitles()) {
                rentedCount += rentalRepository.findByBook_Title_Id(title.getId()).size();
                rentingRightNowCount += rentalRepository.findByBook_Title_IdAndReturnedOnIsNull(title.getId()).size();
            }

            GenrePopularityStatistic stat = new GenrePopularityStatistic(
                    genre.getId(),
                    genre.getName(),
                    rentedCount,
                    rentingRightNowCount
            );

            result.add(stat);
        }

        result.sort(Comparator.comparingInt(GenrePopularityStatistic::getRentedCount).reversed());

        return result;
    }

    public FeesSummaryStatistic getFeesSummaryStatistics() {
        List<Rental> rentals = rentalRepository.findAll();
        FeesSummaryStatistic stat = new FeesSummaryStatistic(0.0, 0.0, List.of());
        double totalPaid = 0.0, totalToBePaid = 0.0;

        for (Rental rental : rentals) {
            totalPaid += rental.getReturnedOn() == null ? 0.0 : rental.getFee();
            totalToBePaid += rental.getReturnedOn() == null ? rental.getFee() : 0.0;
        }

        stat.setTotalPaid(totalPaid);
        stat.setTotalToBePaid(totalToBePaid);

        List<FeesStatisticByUser> statsByUser = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            totalPaid = 0.0;
            totalToBePaid = 0.0;

            List<Rental> userRentals = rentalRepository.findByUser_Id(user.getId());
            for (Rental rental : userRentals) {
                totalPaid += rental.getReturnedOn() == null ? 0.0 : rental.getFee();
                totalToBePaid += rental.getReturnedOn() == null ? rental.getFee() : 0.0;
            }

            FeesStatisticByUser statByUser = new FeesStatisticByUser(
                    user.getId(),
                    user.getFirstName() + " " + user.getLastName(),
                    totalPaid,
                    totalToBePaid
            );

            statsByUser.add(statByUser);
        }

        statsByUser.sort(Comparator.comparingInt(FeesStatisticByUser::getUserId).reversed());

        stat.setFeesByUser(statsByUser);

        return stat;
    }

    public List<BookRatingsStatistic> getBestTitlesByRating() {
        List<BookRatingsStatistic> result = new ArrayList<>();

        for (Title title : titleRepository.findAll()) {

            List<Review> titleReviews = reviewRepository.findByTitleId(title.getId());
            double sum = 0.0;
            for (Review review : titleReviews) {
                sum += review.getRating();
            }
            double avg = sum / titleReviews.size();

            BookRatingsStatistic stat = new BookRatingsStatistic(
                    title.getId(),
                    title.getTitleName(),
                    title.getAuthor(),
                    avg,
                    titleReviews.size()
            );

            result.add(stat);
        }

        result.sort(Comparator.comparingDouble(BookRatingsStatistic::getAverageRating).reversed());

        return result;
    }
}
