package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @CrossOrigin
    @RequestMapping("/greeting")
    public
    @ResponseBody
    Greeting greeting(@RequestParam(required = false, defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping("/greeting-javaconfig")
    public
    @ResponseBody
    Greeting greetingWithJavaconfig(@RequestParam(required = false, defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @CrossOrigin
    @RequestMapping("/time")
    public SseEmitter time() throws IOException {
        final SseEmitter sseEmitter = new SseEmitter();
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // send time every second
        final ScheduledFuture<?> timer = scheduler.scheduleAtFixedRate(() -> sendDate(sseEmitter), 0, 1, TimeUnit.SECONDS);
        // cancel after 10 seconds
        scheduler.schedule(() -> timer.cancel(true), 10, TimeUnit.SECONDS);

        return sseEmitter;
    }

    private void sendDate(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(new Date().toString());
        } catch (IOException e) {
            logger.warn("Unable to send time", e);
        }
    }
}

