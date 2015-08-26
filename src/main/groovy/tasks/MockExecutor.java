package tasks;

import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import groovy.util.logging.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.*;

/**
 * @author luis
 *         Date 17/08/15 20:09
 *         Project: asyncdemo
 */
public class MockExecutor {

    static Logger log = LoggerFactory.getLogger(MockExecutor.class);

    private static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    private static RemovalListener<String, Object> removalListener = new RemovalListener<String, Object>() {
        @Override
        public void onRemoval(RemovalNotification<String, Object> notification) {
            Object value = notification.getValue();
            String key = notification.getKey();
            log.info("Removing key = " + key + " for value = " + value);
        }
    };
    private static Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .softValues()
            .removalListener(removalListener)
            .build();

    private MockExecutor() {
    }

    private static MockExecutor INSTANCE;

    public static MockExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockExecutor();
        }
        return INSTANCE;
    }

    public <T> ListenableFuture<T> submit(Callable<T> p, final String id) {
        ListenableFuture<T> future = service.submit(p);
        Map provisional = new HashMap();
        provisional.put("id", id);
        provisional.put("status", "RUNNING");
        cache.put(id, provisional);
        Futures.addCallback(future, new FutureCallback<T>() {
            public void onSuccess(T asyncResult) {
                cache.put(id, asyncResult);
            }

            public void onFailure(Throwable thrown) {
                cache.put(id, thrown);
            }
        });

        return future;
    }

    public <T> T lookUpFor(String key) {
        return (T) cache.getIfPresent(key);
    }
}
