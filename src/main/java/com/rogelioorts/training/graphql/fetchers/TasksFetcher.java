package com.rogelioorts.training.graphql.fetchers;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.mappers.TasksMapperGraphQL;
import com.rogelioorts.training.graphql.models.ApiTask;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.Future;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TasksFetcher {

    private final TasksRepository tasksRepository;

    public CompletableFuture<List<ApiTask>> allTasks(final DataFetchingEnvironment env) {
        final boolean uncompletedOnly = env.getArgument("uncompletedOnly");

        if(uncompletedOnly) {
            return transformFuture(tasksRepository.findAllUncompleted().map(TasksMapperGraphQL::mapToGraphQL));
        } else {
            return transformFuture(tasksRepository.findAll().map(TasksMapperGraphQL::mapToGraphQL));
        }
    }

    public CompletableFuture<Boolean> complete(final DataFetchingEnvironment env) {
        final String id = env.getArgument("id");

        return transformFuture(tasksRepository.markAsComplete(id).map(v -> true));
    }

    private <T> CompletableFuture<T> transformFuture(final Future<T> f) {
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();

        f.setHandler(res -> {
            if(res.succeeded()) {
                completableFuture.complete(res.result());
            } else {
                completableFuture.completeExceptionally(res.cause());
            }
        });

        return completableFuture;
    }

}
