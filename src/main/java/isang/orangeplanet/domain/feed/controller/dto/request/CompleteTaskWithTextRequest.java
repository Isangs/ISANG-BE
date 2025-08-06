package isang.orangeplanet.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskWithTextRequest {
    @NotNull
    private String content;
}
