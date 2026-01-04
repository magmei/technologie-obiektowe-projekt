package pl.agh.edu.to.aleksandria.model.queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddToQueueRequest {
    private int userId;
    private int titleId;
}
