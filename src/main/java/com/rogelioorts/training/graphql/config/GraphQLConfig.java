package com.rogelioorts.training.graphql.config;

import com.rogelioorts.training.domain.repositories.TasksRepository;
import com.rogelioorts.training.graphql.fetchers.TasksFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.Vertx;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class GraphQLConfig {

    public GraphQL setupGraphQL(final Vertx vertx, final TasksRepository tasksRepository) {
       final String schema = vertx.fileSystem().readFileBlocking("tasks.graphqls").toString();

        final SchemaParser schemaParser = new SchemaParser();
        final TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        final TasksFetcher tasksFetcher = new TasksFetcher(tasksRepository);
        final RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("allTasks", tasksFetcher::allTasks))
                .type("Mutation", builder -> builder.dataFetcher("complete", tasksFetcher::complete))
                .build();

        final SchemaGenerator schemaGenerator = new SchemaGenerator();
        final GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema).build();
    }

}
