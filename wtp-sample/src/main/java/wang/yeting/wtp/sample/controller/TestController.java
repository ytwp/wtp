package wang.yeting.wtp.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.yeting.wtp.sample.test.Test;

/**
 * @author : weipeng
 * @date : 2020-07-24 16:40
 */
@RestController
public class TestController {

    @Autowired
    private Test test;

    @RequestMapping("test")
    public String test() {
        return test.getThreadPoolLog();
    }

    @RequestMapping("testPool")
    public String testPool() {
        return test.testPool();
    }
}
