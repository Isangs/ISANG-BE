package isang.orangeplanet.domain.activity.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchActivityResponse {
    private String taskMessage;
    private LocalDateTime createdAt;
    private String content;
}
