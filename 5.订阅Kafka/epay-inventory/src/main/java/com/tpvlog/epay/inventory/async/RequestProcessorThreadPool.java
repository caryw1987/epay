package com.tpvlog.epay.inventory.async;

import com.tpvlog.epay.inventory.request.Request;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * 请求处理线程池,线程池中的每个线程监听一个任务
 *
 * @author kfzx-huwb1
 */
@Component
public class RequestProcessorThreadPool {

    private ExecutorService executor;

    private final int size = 10;

    @PostConstruct
    private void init() {
        executor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(2), Executors.defaultThreadFactory());

        RequestQueue requestQueue = RequestQueue.getInstance();
        for (int i = 0; i < size; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(128);
            requestQueue.addQueue(queue);

            executor.submit(new RequestProcessorTask(queue));
        }
    }
}
