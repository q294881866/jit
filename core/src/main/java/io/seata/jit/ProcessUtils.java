package io.seata.jit;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ppf
 */
public class ProcessUtils {

    public static void main(String[] args) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process start = processBuilder.start();
        OutputStream outputStream = start.getOutputStream();
        start.destroy();
    }
}
