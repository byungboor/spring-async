package x3.benjamin.test.async.sample;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by benjamin on 2017. 9. 4..
 */
@Slf4j
public class CompletableFutureTest {

    public static void main(String[] args) throws Exception {
        CompletableFutureTest cfTest = new CompletableFutureTest();

//        cfTest.chainMethods();
//        cfTest.allOfMethods();
//        cfTest.allOfAcceptMethods();
        cfTest.handleMethods();

    }

    public void chainMethods() {
        System.out.println(">>> Main Method Started.");
        ExecutorService es = Executors.newFixedThreadPool(10);

        String address = "address";
        CompletableFuture
                .supplyAsync(() -> this.getUpperName(address), es)
                .thenAcceptAsync(name -> this.print(name), es);

        String threadName = Thread.currentThread().getName();
        System.out.println("<<< End Main Method. ThreadName = " + threadName);
    }

    public void allOfAcceptMethods() throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> list = IntStream.range(0, 10).boxed()
                .map(i -> {
                    final String temp = "PortNumber:" + i;
                    return CompletableFuture.supplyAsync(() -> this.getUpperName(temp), es);
                })
                .collect(Collectors.toList());

        CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]))
                .thenAcceptAsync(aVoid -> {
                    String combined = list.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.joining(" "));
                    String threadName = Thread.currentThread().getName();
                    System.out.println("thread name : " + threadName + ". combined:" + combined);
                }).thenAcceptAsync(aVoid -> {
            System.out.println("final work");
        });

        String threadName = Thread.currentThread().getName();
        System.out.println("thread name : " + threadName + ". End Call1");
    }

    public void allOfMethods() {

        // Thread 숫자를 조절해서 보여줄것.
        ExecutorService es = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> list = IntStream.range(0, 10).boxed()
                .map(i -> {
                    final String temp = "PortNumber:" + i;
                    return CompletableFuture.supplyAsync(() -> this.getUpperName(temp), es);
                })
                .collect(Collectors.toList());

        CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));

        String threadName = Thread.currentThread().getName();
        System.out.println(":::::: thread name : " + threadName + ". ");

        String combined = list.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining(", "));

        threadName = Thread.currentThread().getName();
        System.out.println(":::::: thread name : " + threadName + ". End Call2");
        System.out.println("Result : " + combined);
    }

    public void handleMethods() {

        ExecutorService es = Executors.newFixedThreadPool(10);

        String address = "address";
        CompletableFuture
                .supplyAsync(() -> this.getUpperName(address), es)
                .thenApplyAsync(name -> {
                    //에외 발생 가능 부분
                    if (name.length() < 10)
                        throw new RuntimeException("Too short name to process");
                    return name;
                }, es)
                .thenAcceptAsync(name -> this.print(name), es)
                .exceptionally(throwable -> this.handleException(throwable));
    }

    public String getUpperName(String name) {
        System.out.println("--> getUpperName Method Started...");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        String threadName = Thread.currentThread().getName();
        System.out.println("<-- End getUpperName Method. Thread name : " + threadName + " : " + name);
        return name.toUpperCase();
    }

    public void print(String name) {
        System.out.println("~~> print Method Started...");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        String threadName = Thread.currentThread().getName();
        System.out.println("<~~ End print Method. Thread name : " + threadName + " : " + name);
    }

    public Void handleException(Throwable throwable) {
        log.error("!!! Error : {}", throwable);
        return null;
    }
}
