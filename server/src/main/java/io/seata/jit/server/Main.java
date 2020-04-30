package io.seata.jit.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.seata.jit.Starter;
import io.seata.jit.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ppf
 */
public class Main {

    private static List<Starter>  dependServers = new ArrayList<>(4);
    private static List<TestCase> testCases     = new ArrayList<>(4);

    private static final ThreadFactory      JIT_TF   = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("seata-jit-%d")
            .setUncaughtExceptionHandler((t, a) -> a.printStackTrace()).build();
    private static final ThreadPoolExecutor JIT_POOL = new ThreadPoolExecutor(3, Runtime.getRuntime().availableProcessors(),
            120L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), JIT_TF, new NewThreadRunsPolicy());

    static {
        dependServers.add(new SeataServerStarter());
        dependServers.add(new ZookeeperStarter());

        testCases.add(getTccTestCase());
    }

    public static void main(String[] args) throws Exception {
        startDepends(args);

        startTesters(args);

        System.exit(0);
    }

    public static void startDepends(String[] args) throws Exception {
        // 启动相关服务
        for (Starter dependServer : dependServers) {
            JIT_POOL.execute(() -> dependServer.start(args));
        }
        sleep(30);
    }

    public static void startTesters(String[] args) throws IOException, InterruptedException {
        List<Process> processes = new ArrayList<>(4);
        for (TestCase testCase : testCases) {
            System.err.println(testCase.getTitle());
            System.err.println(testCase.getContent());
            for (TestCase.Service service : testCase.getCommands()) {
                JIT_POOL.execute(() -> {
                    try {
                        Process start = Runtime.getRuntime().exec(service.getCommand());
                        processes.add(start);
                        BufferedReader br = new BufferedReader(new InputStreamReader(start.getInputStream()));
                        for (String line = service.getCommand(); start.isAlive() && line != null; line = br.readLine()) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                sleep(service.getInterval());
            }
            sleep(testCase.getExecTime());
            for (Process process : processes) {
                process.destroy();
            }
            processes.clear();
        }

    }

    public static TestCase getTccTestCase() {
        TestCase tcc = new TestCase("Tcc", "spring dubbo");
        tcc.addService("java -cp .:tcc/target/lib/*:tcc/target/seata-jit-tcc.jar io.seata.jit.starter.TccProviderStarter", 60);
        tcc.addService("java -cp .:tcc/target/lib/*:tcc/target/seata-jit-tcc.jar io.seata.jit.starter.TccConsumerStarter", 60);

        return tcc;
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
