package io.seata.jit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ppf
 */
@Data
public class TestCase {
    /**
     * 执行测试依赖的服务按顺序启动
     */
    private List<Service> commands = new ArrayList<>(8);

    /**
     * 测试名称
     */
    private String title;

    /**
     * 测试内容
     */
    private String content;

    /**
     * 执行时间
     */
    private long execTime = -1;



    public TestCase(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 添加一个服务
     * <pre>
     *     服务需要的参数，或者服务相关进来包装在服务里面
     * </pre>
     *
     * @param command  启动服务的命令
     * @param interval 服务需要多久启动
     * @return
     */
    public List<Service> addService(String command, long interval) {
        commands.add(new Service(command, interval));
        return commands;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Service {
        /**
         * 执行服务的命令
         */
        private String command;
        /**
         * 服务间隔时间
         */
        private long interval = 3000;
    }

}
