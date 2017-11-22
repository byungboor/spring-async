package x3.benjamin.test.async.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import x3.benjamin.test.async.util.CompletableFutureBuilder;

import java.util.List;

/**
 * Created by benjamin on 2017. 11. 17..
 */
@Slf4j
public class AsyncRestTemplateTest {

    private static final ParameterizedTypeReference<List<String>> TYPE_REFERENCE;

    static {
        TYPE_REFERENCE = new ParameterizedTypeReference<List<String>>() {
        };
    }

    public static void main(String[] args) throws Exception {
        AsyncRestTemplateTest test = new AsyncRestTemplateTest();
//        test.getWords();
//        test.asyncGetWords();
//        test.asyncCombinedProcess();
        test.asyncCombinedProcessByCF();

        Thread.sleep(10000L);
    }

    private void getWords() {
        System.out.println("...... Begin Method");
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        try {
            ResponseEntity<List<String>> responseEntity = asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.GET, null, TYPE_REFERENCE).get();
            System.out.println("StatusCode : " + responseEntity.getStatusCode());
            System.out.println("body : " + responseEntity.getBody());
        } catch (Exception e) {
            System.out.println("Error Occur : " + e.getMessage());
        }
        System.out.println("...... End Method");
    }

    public void asyncGetWords() {

        System.out.println("...... Begin Method");

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<List<String>>> listenableFuture = asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.GET, null, TYPE_REFERENCE);
        listenableFuture.addCallback(
                new ListenableFutureCallback<ResponseEntity<List<String>>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("Error Occur : " + throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(ResponseEntity<List<String>> listResponseEntity) {
                        System.out.println("StatusCode : " + listResponseEntity.getStatusCode());
                        System.out.println("body : " + listResponseEntity.getBody());
                    }
                });

        System.out.println("...... End Method");
    }

    public void asyncCombinedProcess() {
        System.out.println("...... Begin Method");

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<List<String>>> listenableFuture = asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.GET, null, TYPE_REFERENCE);
        listenableFuture.addCallback(
                new ListenableFutureCallback<ResponseEntity<List<String>>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("Error Occur : " + throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(ResponseEntity<List<String>> listResponseEntity) {
                        System.out.println("StatusCode : " + listResponseEntity.getStatusCode());
                        System.out.println("body : " + listResponseEntity.getBody());

                        List<String> words = listResponseEntity.getBody();
                        HttpEntity httpEntity = new HttpEntity<>(words);
                        asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.POST, httpEntity, TYPE_REFERENCE)
                                .addCallback(rt -> {
                                    System.out.println("----StatusCode : " + rt.getStatusCode());
                                    System.out.println("----body : " + rt.getBody());
                                }, th -> {
                                    log.error("error ", th);
                                });
                    }
                });

        System.out.println("...... End Method");
    }


    public void asyncCombinedProcessByCF() {
        System.out.println("...... Begin Method");

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        CompletableFutureBuilder.build(asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.GET, null, TYPE_REFERENCE))
                .thenComposeAsync(listResponseEntity -> {
                    System.out.println("StatusCode : " + listResponseEntity.getStatusCode());
                    System.out.println("body : " + listResponseEntity.getBody());

                    List<String> words = listResponseEntity.getBody();
                    HttpEntity httpEntity = new HttpEntity<>(words);

                    return CompletableFutureBuilder.build(asyncRestTemplate.exchange("http://127.0.0.1:8080/words", HttpMethod.POST, httpEntity, TYPE_REFERENCE));
                })
                .thenAcceptAsync(upperResponseEntity -> {
                    System.out.println("----StatusCode : " + upperResponseEntity.getStatusCode());
                    System.out.println("----body : " + upperResponseEntity.getBody());
                })
                .exceptionally(th -> {
                    System.out.println("Error Occur : " + th.getMessage());
                    return null;
                });

        System.out.println("...... End Method");
    }


}
