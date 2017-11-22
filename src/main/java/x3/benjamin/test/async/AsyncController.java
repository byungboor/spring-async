package x3.benjamin.test.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import x3.benjamin.test.async.service.AsyncService;

/**
 * Created by benjamin on 2017. 11. 13..
 */
@Slf4j
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    /**
     * curl -HGET http://127.0.0.1:8080/async?input=test
     *
     * @param input
     * @return
     */
    @GetMapping(value = "/async")
    public DeferredResult<String> get(@RequestParam String input) {

        log.info("Controller Thread Started. threadName={}", Thread.currentThread().getName());

        DeferredResult<String> deferredResult = new DeferredResult<>();
        asyncService.process(deferredResult, input);

        log.info("Controller Thread Finished. threadName={}", Thread.currentThread().getName());
        return deferredResult;
    }
}
