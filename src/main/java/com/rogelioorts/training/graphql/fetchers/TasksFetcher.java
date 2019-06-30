package com.rogelioorts.training.graphql.fetchers;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.mappers.TasksMapperGraphQL;
import com.rogelioorts.training.graphql.models.ApiTask;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.Promise;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TasksFetcher {

    private final TasksRepository tasksRepository;

    public void allTasks(final DataFetchingEnvironment env, final Promise<List<ApiTask>> promise) {
        final boolean uncompletedOnly = env.getArgument("uncompletedOnly");

        if(uncompletedOnly) {
            tasksRepository.findAllUncompleted().map(TasksMapperGraphQL::mapToGraphQL)
                    .setHandler(promise::handle);
        } else {
            tasksRepository.findAll().map(TasksMapperGraphQL::mapToGraphQL)
                    .setHandler(promise::handle);
        }
    }

    public void complete(final DataFetchingEnvironment env, final Promise<Boolean> promise) {
        final String id = env.getArgument("id");

        tasksRepository.markAsComplete(id)
                .map(true)
                .setHandler(promise::handle);
    }

}
