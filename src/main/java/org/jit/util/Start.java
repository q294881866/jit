package org.jit.util;

import lombok.Data;

/**
 * @author ppf
 */
@Data
public class Start {
    private String jar;
    private String mainClass;
    private String[] args;
}
