package x3.benjamin.test.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by benjamin on 2017. 11. 13..
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Async
    public void process(DeferredResult<String> deferredResult, String str) {

        log.info("Business Process Started. threadName={}", Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
            str = str.toUpperCase();
            deferredResult.setResult(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        log.info("Business Process Finished. threadName={}", Thread.currentThread().getName());
    }
}
