package com.rogelioorts.training.graphql.mappers;

import com.rogelioorts.training.domain.models.Task;
import com.rogelioorts.training.graphql.models.ApiTask;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class TasksMapperGraphQL {

    private TasksMapperGraphQL() {
    }

    public static ApiTask mapToGraphQL(final Task domainModel) {
        final ApiTask graphQLModel = new ApiTask();
        graphQLModel.setId(domainModel.getId());
        graphQLModel.setDescription(domainModel.getDescription());
        graphQLModel.setCompleted(domainModel.isCompleted());

        return graphQLModel;
    }

    public static List<ApiTask> mapToGraphQL(final List<Task> domainModels) {
        return domainModels.stream().map(TasksMapperGraphQL::mapToGraphQL).collect(toList());
    }

}
