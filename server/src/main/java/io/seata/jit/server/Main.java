package io.seata.jit.server;

import io.seata.jit.Starter;
import io.seata.jit.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ppf
 */
public class Main {

    private static List<Starter> dependServers = new ArrayList<>(4);
    private static List<TestCase> testCases = new ArrayList<>(4);

    static {
        dependServers.add(new SeataServerStarter());
        dependServers.add(new ZookeeperStarter());

        testCases.add(getTccTestCase());
    }

    public static void main(String[] args) throws Exception {
        startDepends(args);

        startTesters(args);
    }

    public static void startDepends(String[] args) throws Exception {
        // 启动相关服务
        for (Starter dependServer : dependServers) {
            new Thread(() -> {
                try {
                    dependServer.start(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        sleep(30);
    }


    public static void startTesters(String[] args) throws IOException, InterruptedException {
        List<Process> processes = new ArrayList<>(4);
        for (TestCase testCase : testCases) {
            System.err.println(testCase.getTitle());
            System.err.println(testCase.getContent());
            System.err.println(System.getProperty("java.class.path"));
            for (TestCase.Service service : testCase.getCommands()) {
                System.err.println(service.getCommand());

                new Thread(() -> {
                    try {
                        Process start = Runtime.getRuntime().exec(service.getCommand());

                        processes.add(start);
                        BufferedReader br = new BufferedReader(new InputStreamReader(start.getInputStream()));

                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();

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
        tcc.addService("java -cp .;tcc/target/lib/*;tcc/target/seata-jit-tcc.jar io.seata.jit.starter.TccProviderStarter", 60);
        tcc.addService("java -cp .;tcc/target/lib/*;tcc/target/seata-jit-tcc.jar io.seata.jit.starter.TccConsumerStarter", 60);

        return tcc;
    }


    public static void sleep(long seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }
}
