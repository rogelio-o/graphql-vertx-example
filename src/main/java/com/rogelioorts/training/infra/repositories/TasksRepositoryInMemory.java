package com.rogelioorts.training.infra.repositories;

import com.rogelioorts.training.domain.models.Task;
import com.rogelioorts.training.domain.repositories.TasksRepository;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TasksRepositoryInMemory implements TasksRepository {

    private static final Map<String, Task> TASKS = Stream.<Task>builder()
            .add(Task.builder()
                    .id(UUID.randomUUID().toString())
                    .description("Learn GraphQL")
                    .build())
            .add(Task.builder()
                    .id(UUID.randomUUID().toString())
                    .description("Build awesome GraphQL server")
                    .build())
            .add(Task.builder()
                    .id(UUID.randomUUID().toString())
                    .description("Profit")
                    .build())
            .build()
            .collect(toMap(Task::getId, Function.identity()));


    @Override
    public Future<List<Task>> findAll() {
        return Future.succeededFuture(TASKS.values().stream().collect(toList()));
    }

    @Override
    public Future<List<Task>> findAllUncompleted() {
        return Future.succeededFuture(TASKS.values().stream()
                .filter(t -> !t.isCompleted())
                .collect(toList()));
    }

    @Override
    public Future<Void> markAsComplete(final String id) {
        Optional.ofNullable(TASKS.get(id))
                .ifPresent(t -> t.setCompleted(true));

        return Future.succeededFuture();
    }

}
