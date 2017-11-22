package x3.benjamin.test.async.util;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureBuilder {


    public static <T> CompletableFuture<T> build(ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable ex) {
                completableFuture.completeExceptionally(ex);
            }

            @Override
            public void onSuccess(T result) {
                completableFuture.complete(result);
            }
        });

        return completableFuture;
    }

    public static <T> CompletableFuture<T> doNothingCompletableFuture() {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);
        return completableFuture;
    }

}
