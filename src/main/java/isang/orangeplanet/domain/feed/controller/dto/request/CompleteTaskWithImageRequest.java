package isang.orangeplanet.domain.feed.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskWithImageRequest {
    @NotNull
    private String imageUrl;
}
