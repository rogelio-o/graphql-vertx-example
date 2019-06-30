package com.rogelioorts.training.graphql;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.config.GraphQLConfig;
import com.rogelioorts.training.infra.repositories.TasksRepositoryInMemory;
import graphql.GraphQL;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.GraphQLSocketHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class GraphQLVerticle extends AbstractVerticle {

    @Override
    public void start() {
        final TasksRepository tasksRepository = new TasksRepositoryInMemory();
        final GraphQLConfig graphQLConfig = new GraphQLConfig();

        final GraphQL graphQL = graphQLConfig.setupGraphQL(vertx, tasksRepository);
        final GraphQLHandler graphQLHandler = GraphQLHandler.create(graphQL, new GraphQLHandlerOptions()
                .setGraphiQLOptions(new GraphiQLOptions()
                        .setEnabled(true)));

        final SockJSHandlerOptions sockOptions = new SockJSHandlerOptions()
                .setHeartbeatInterval(2000);
        final SockJSHandler sockJSHandler = SockJSHandler.create(vertx, sockOptions);
        sockJSHandler.socketHandler(GraphQLSocketHandler.create(graphQL));


        final Router router = Router.router(vertx);
        router.route("/").handler(StaticHandler.create("views"));
        router.route("/graphql").handler(graphQLHandler);
        router.route("/websocket/*").handler(sockJSHandler);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    public static void main(final String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GraphQLVerticle.class.getName());
    }

}
