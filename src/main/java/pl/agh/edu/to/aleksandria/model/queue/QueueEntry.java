package pl.agh.edu.to.aleksandria.model.queue;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name="queue")
public class QueueEntry {

    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITLE_ID")
    private Title title;
    
    @Getter
    @Setter
    private LocalDateTime requestDate;

    public QueueEntry(User user, Title title, LocalDateTime requestDate) {
        this.user = user;
        this.title = title;
        this.requestDate = requestDate;
    }
}
