package com.rogelioorts.training.domain.repositories;

import com.rogelioorts.training.domain.models.Task;
import io.vertx.core.Future;

import java.util.List;

public interface TasksRepository {

    Future<List<Task>> findAll();

    Future<List<Task>> findAllUncompleted();

    Future<Void> markAsComplete(String id);

}
