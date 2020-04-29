package org.jit.util;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashMap;

/**
 * @author ppf
 */
@ToString
@Data
public class Result {
    private boolean success;
    private String message;
    private LinkedHashMap<String, Boolean> diagram = new LinkedHashMap<>(8);


    public LinkedHashMap addNode(String name, boolean status) {
        diagram.put(name, status);
        return diagram;
    }
}
