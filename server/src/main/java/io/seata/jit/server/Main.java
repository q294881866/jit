package io.seata.jit.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.seata.jit.ApplicationKeeper;
import io.seata.jit.Starter;
import io.seata.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author ppf
 */
public class Main {

    private static List<Starter> dependServers = new ArrayList<>(4);

    private static final ThreadFactory JIT_TF = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("seata-jit-%d")
            .setUncaughtExceptionHandler((t, a) -> a.printStackTrace()).build();
    private static final ThreadPoolExecutor JIT_POOL = new ThreadPoolExecutor(8, 32,
            120L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), JIT_TF, new NewThreadRunsPolicy());

    static {
        dependServers.add(new SeataServerStarter());
        dependServers.add(new ZookeeperStarter());
    }

    public static void main(String[] args) throws Exception {
//        startDepends(args);
        Server server = new Server();
        server.main(args);
//        new ApplicationKeeper().keep();
    }

    public static void startDepends(String[] args) throws Exception {
        // 启动相关服务
        for (Starter dependServer : dependServers) {
            JIT_POOL.execute(() -> dependServer.start(args));
        }
    }


    public static void sleep(long seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }

    private static final class NewThreadRunsPolicy implements RejectedExecutionHandler {
        private NewThreadRunsPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                Thread t = new Thread(r, "Temporary task executor");
                t.start();
            } catch (Throwable var4) {
                throw new RejectedExecutionException("Failed to start a new thread", var4);
            }
        }
    }
}
