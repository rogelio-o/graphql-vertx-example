package com.rogelioorts.training.graphql.fetchers;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.mappers.TasksMapperGraphQL;
import com.rogelioorts.training.graphql.models.ApiTask;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.Future;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TasksFetcher {

    private final TasksRepository tasksRepository;

    public void allTasks(final DataFetchingEnvironment env, final Future<List<ApiTask>> future) {
        final boolean uncompletedOnly = env.getArgument("uncompletedOnly");

        if(uncompletedOnly) {
            tasksRepository.findAllUncompleted().map(TasksMapperGraphQL::mapToGraphQL)
                    .compose(result -> future.complete(result), future);
        } else {
            tasksRepository.findAll().map(TasksMapperGraphQL::mapToGraphQL)
                    .compose(result -> future.complete(result), future);
        }
    }

    public void complete(final DataFetchingEnvironment env, final Future<Boolean> future) {
        final String id = env.getArgument("id");

        tasksRepository.markAsComplete(id)
                .compose(v -> future.complete(true), future);
    }

}
