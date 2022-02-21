/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.legacy.function.runner.library.logging;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public final class Log {
    public static void error(String message) {
        log.error(message);
    }

    public static void error(Exception error) {
        log.error(getErrorDetail(error));
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void info(Exception error) {
        log.info(getErrorDetail(error));
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void debug(Exception error) {
        log.debug(getErrorDetail(error));
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void warn(Exception error) {
        log.warn(getErrorDetail(error));
    }

    public static boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public static void prompt(String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
            System.out.println(message);
        } else {
            System.out.println(message);
        }
    }

    private static String getErrorDetail(Exception error) {
        final StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        final String exceptionDetails = sw.toString();
        try {
            sw.close();
        } catch (IOException e) {
            // swallow error to avoid deadlock
        }
        return exceptionDetails;
    }
}
