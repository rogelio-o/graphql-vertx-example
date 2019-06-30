package com.rogelioorts.training.graphql.fetchers;

import com.rogelioorts.training.graphql.models.ApiCounter;
import graphql.ExecutionResult;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TickFetcher {

    private final Vertx vertx;

    public void tick(final DataFetchingEnvironment env, final Promise<Publisher<ApiCounter>> promise) {
        final ReactiveWriteStream<ApiCounter> rws = ReactiveWriteStream.writeStream(vertx);

        final AtomicInteger counter = new AtomicInteger(0);
        vertx.setPeriodic(1000, v -> {
            final ApiCounter result = new ApiCounter();
            result.setCount(counter.getAndIncrement());
            rws.write(result);
        });

        promise.complete(rws);
    }

}
