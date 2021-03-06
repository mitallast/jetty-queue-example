package org.mitallast.queue.common;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mitallast.queue.common.logging.LoggingService;
import org.mitallast.queue.crdt.CrdtModule;
import org.mitallast.queue.raft.RaftModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class BaseTest {
    protected final LoggingService logging = new LoggingService(MarkerManager.getMarker("test"));
    protected final Logger logger = logging.logger();

    protected final Random random = new Random();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private ExecutorService executorService = Executors.newFixedThreadPool(concurrency(), new DefaultThreadFactory("base-test"));

    @Before
    public void setUpModule() throws Exception {
        new RaftModule();
        new CrdtModule();
    }

    @After
    public void tearDownExecutorService() throws Exception {
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        assert executorService.isTerminated();
    }

    protected int concurrency() {
        return 8;
    }

    protected int max() {
        return 200000;
    }

    protected final int total() {
        return max() * concurrency();
    }

    protected void executeConcurrent(Task task) throws Exception {
        final List<Future> futures = new ArrayList<>(concurrency());
        for (int i = 0; i < concurrency(); i++) {
            Future future = executorService.submit(() -> {
                try {
                    task.execute();
                } catch (Exception e) {
                    assert false : e;
                }
            });
            futures.add(future);
        }
        for (Future future : futures) {
            future.get();
        }
    }

    protected void executeConcurrent(ThreadTask task) throws Exception {
        final List<Future> futures = new ArrayList<>(concurrency());
        for (int i = 0; i < concurrency(); i++) {
            final int thread = i;
            Future future = executorService.submit(() -> {
                try {
                    task.execute(thread, concurrency());
                } catch (Exception e) {
                    assert false : e;
                }
            });
            futures.add(future);
        }
        for (Future future : futures) {
            future.get();
        }
    }

    protected <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                assert false : e;
                throw e;
            }
        });
    }

    protected Future<Void> submit(Runnable runnable) {
        return executorService.submit(runnable, null);
    }

    protected void async(Task task) {
        executorService.submit(() -> {
            try {
                task.execute();
            } catch (Exception e) {
                assert false : e;
            }
        });
    }

    protected void printQps(String metric, long total, long start, long end) {
        long qps = (long) (total / (double) (end - start) * 1000.);
        logger.info(metric + ": " + total + " at " + (end - start) + "ms");
        logger.info(metric + ": " + qps + " qps");
    }

    protected String randomString() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] chars = new char[16];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = str.charAt(random.nextInt(str.length()));
        }
        return new String(chars);
    }

    protected byte[] randomBytes(int len) {
        byte[] data = new byte[len];
        random.nextBytes(data);
        return data;
    }

    public interface Task {
        void execute() throws Exception;
    }

    public interface ThreadTask {
        void execute(int thread, int concurrency) throws Exception;
    }
}
