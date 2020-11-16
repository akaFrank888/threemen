package util.utils;

import java.util.Map;

public class SqlUtil {

    // 采用正则表达式检测是否包含有 单引号(')，分号(;) 和 注释符号(--)的语句，来防止SQL注入
    // false --> 没有注入漏洞     true  -->  有注入漏洞
    public static boolean TransactSQLInjection(Map<String, String[]> map) {

        boolean b = false;
        // 遍历map
        for (String key : map.keySet()) {
            // 遍历value中的String[]数组
            for (String s : map.get(key)) {
                String s1 = s.replaceAll(".*([';]+|(--)+).*", "");
                if (!s1.equals(s)) {
                    // 如果做过修改，即存在敏感字符
                    b = true;
                    break;
                }
            }
            break;
        }
        return b;
    }

    public static boolean TransactSQLInjection(String... value) {
        boolean b = false;
        for (String s : value) {
            String s1 = s.replaceAll(".*([';]+|(--)+).*", "");
            if (!s1.equals(s)) {
                // 如果做过修改，即存在敏感字符
                b = true;
                break;
            }
        }
        return b;
    }
}
