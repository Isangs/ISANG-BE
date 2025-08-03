package isang.orangeplanet.domain.task.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task")
@RequiredArgsConstructor
@Tag(name = "Task", description = "할일 관련 API")
public class TaskController {
  private final TaskService taskService;


}
