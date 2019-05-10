package com.rogelioorts.training.graphql;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.config.GraphQLConfig;
import com.rogelioorts.training.infra.repositories.TasksRepositoryInMemory;
import graphql.GraphQL;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.GraphiQLOptions;

public class GraphQLVerticle extends AbstractVerticle {

    @Override
    public void start() {
        final TasksRepository tasksRepository = new TasksRepositoryInMemory();
        final GraphQLConfig graphQLConfig = new GraphQLConfig();

        final GraphQL graphQL = graphQLConfig.setupGraphQL(vertx, tasksRepository);
        final GraphQLHandler graphQLHandler = GraphQLHandler.create(graphQL, new GraphQLHandlerOptions()
                .setGraphiQLOptions(new GraphiQLOptions()
                        .setEnabled(true)));

        final Router router = Router.router(vertx);
        router.route("/graphql").handler(graphQLHandler);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    public static void main(final String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GraphQLVerticle.class.getName());
    }

}
