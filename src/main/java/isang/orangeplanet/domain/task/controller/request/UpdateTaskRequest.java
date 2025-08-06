package isang.orangeplanet.domain.task.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "할일 생성 요청 객체")
public record UpdateTaskRequest(
    @NotNull
    Boolean isAddRecord,

    @NotNull
    Boolean isPublic
) { }
