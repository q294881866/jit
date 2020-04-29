package io.seata.jit.server;

import io.seata.jit.Starter;
import io.seata.jit.TestCase;

import java.io.IOException;
import java.io.OutputStream;
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
        startDepends();

        startTesters();
    }

    public static void startDepends() throws Exception {
        // 启动相关服务
        for (Starter dependServer : dependServers) {
            dependServer.start();
        }
    }


    public static void startTesters() throws IOException, InterruptedException {
        List<Process> processes = new ArrayList<>(4);
        for (TestCase testCase : testCases) {
            System.out.println(testCase.getTitle());
            System.out.println(testCase.getContent());
            for (TestCase.Service service : testCase.getCommands()) {
                ProcessBuilder pb = new ProcessBuilder(service.getCommand());
                Process start = pb.start();
                processes.add(start);
                sleep(service.getInterval());
            }
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
