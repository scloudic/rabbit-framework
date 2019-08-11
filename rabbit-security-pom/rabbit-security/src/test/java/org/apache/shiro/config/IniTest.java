package org.apache.shiro.config;

import java.util.Scanner;

/**
 * @author: justin.liang
 * @date: 16/5/9 上午12:15
 */
public class IniTest {
    public static void main(String[] args) {
        String str1 = "/login=anon\n" +
                "/login/logout=logout\n" +
                "/login/**=anon\n" +
                "/unauthorized=anon\n" +
                "/**=authc";
        String str = "/login=anon\n/login/logout=logout\n/login/**=anon\n/unauthorized=anon\n/**=authc";
        Scanner scanner = new Scanner(str);
        while (scanner.hasNextLine()) {
            String rawLine = scanner.nextLine();
            System.out.println(rawLine);
        }
    }
}
