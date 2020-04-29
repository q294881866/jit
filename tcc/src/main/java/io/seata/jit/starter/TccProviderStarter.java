package io.seata.jit.starter;

import io.seata.jit.ApplicationKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ppf
 */
public class TccProviderStarter {
    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"spring/seata-tcc.xml", "spring/seata-dubbo-provider.xml"});
        new ApplicationKeeper().keep();
    }
}
