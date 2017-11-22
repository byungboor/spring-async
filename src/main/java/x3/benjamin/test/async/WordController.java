package x3.benjamin.test.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjamin on 2017. 11. 16..
 */
@Slf4j
@RestController
public class WordController {

    @GetMapping(value = "/words")
    public List<String> get() {

        try {
            Thread.sleep(1000);
        } catch (Exception ignorable) {

        }

        return Arrays.asList("a", "b", "c", "d", "e");
    }

    @PostMapping(value = "/words")
    public List<String> post(@RequestBody List<String> word) {
        try {
            Thread.sleep(1000);
        } catch (Exception ignorable) {

        }

        return word.stream().map(String::toUpperCase).collect(Collectors.toList());
    }
}
