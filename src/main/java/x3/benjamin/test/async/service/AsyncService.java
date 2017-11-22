package x3.benjamin.test.async.service;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by benjamin on 2017. 11. 13..
 */
public interface AsyncService {

    void process(DeferredResult<String> deferredResult, String str);
}
