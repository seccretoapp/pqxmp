package com.seccreto.infrastructure.transport.multiplexer;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DefaultMatcher {
    private static final int MAX_HTTP_READ = 4096;

    // Matcher that matches any connection
    public static MatcherInterface any() {
        return input -> true;
    }

    // Prefix matcher for connection
    public static MatcherInterface prefixMatcher(String... strs) {
        return input -> {
            try (BufferedReaderOptimized reader = new BufferedReaderOptimized(input)) {
                String firstLine = reader.readLine();
                if (firstLine != null) {
                    for (String str : strs) {
                        if (firstLine.startsWith(str)) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (IOException e) {
                System.err.println("Error reading from input stream: " + e.getMessage());
                return false;
            }
        };
    }

    // HTTP1 fast matcher
    public static MatcherInterface http1Fast(String... extMethods) {
        String[] defaultMethods = {
                "OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "CONNECT"
        };
        return prefixMatcher(mergeArrays(defaultMethods, extMethods));
    }

    // TLS matcher
    public static MatcherInterface tls(int... versions) throws NoSuchAlgorithmException {
        if (versions.length == 0) {
            versions = new int[]{SSLContext.getDefault().getProtocol().equals("TLSv1.3") ? 0 : 1}; // Adjust as needed
        }
        return input -> {
            // Placeholder for TLS logic
            return true; // Placeholder
        };
    }

    // HTTP1 matcher
    public static MatcherInterface http1() {
        return input -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(input)))) {
                reader.mark(MAX_HTTP_READ);
                String firstLine = reader.readLine();
                if (firstLine == null) return false;

                String[] parts = parseRequestLine(firstLine);
                return parts != null && parts.length > 2 && parts[2].startsWith("HTTP/1.");
            } catch (IOException e) {
                System.err.println("Error reading HTTP/1 request: " + e.getMessage());
                return false;
            }
        };
    }

    // Parses the request line
    private static String[] parseRequestLine(String line) {
        return line.split(" ");
    }

    // HTTP2 matcher
    public static MatcherInterface http2() {
        return input -> hasHTTP2Preface(input);
    }

    private static boolean hasHTTP2Preface(InputStream input) {
        byte[] preface = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[preface.length];
        try {
            int bytesRead = input.read(buffer);
            return bytesRead >= preface.length && Arrays.equals(buffer, preface);
        } catch (IOException e) {
            System.err.println("Error reading HTTP/2 preface: " + e.getMessage());
            return false;
        }
    }

    // Utility to merge two string arrays
    private static String[] mergeArrays(String[] array1, String[] array2) {
        String[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}