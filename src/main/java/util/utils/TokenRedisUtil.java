package util.utils;

import domain.User;
import redis.clients.jedis.Jedis;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *      将用户User存入redis中：
 *
 */
public class TokenRedisUtil {

    // 创建一个集合当作缓存   （redis的硬盘--->map的内存）
    public static Map<String, User> tokenMap = new HashMap<>();


    // 方法一、通过cookie取到user
    public static User getUserByCookie(Cookie[] cookies) {

        return getUserByToken(getTokenByCookie(cookies));
    }

/*    // 方法二、通过cookie取到userInfo
    public static UserInfo getUserInfoByCookie(Cookie[] cookies) {
        return getUserInfoByToken(getTokenByCookie(cookies));
    }*/

    // 方法二、通过cookie删除user的缓存，redis
    public static boolean deleteUser(Cookie[] cookies) {
        // 先得到token（key）
        // 1. 删除缓存
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("adminId")) {
                token = cookie.getValue();
            }
        }
        if (token != null) {
            tokenMap.remove(token);

            // 2. 删除redis
            Jedis jedis = JedisUtil.getJedis();
            jedis.del(token);

            return true;
        } else {
            throw new RuntimeException("token为空，删除操作导致空指针异常");
        }
    }

    // 方法三、判断有无该用户的cookie
    public static boolean isUserCookie(HttpServletRequest request) {
        boolean b = false;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("adminId")) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }






    // 保存token进 缓存 和 redis
    public static void saveToken(String token, User user) {

        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 以String的结构保存进缓存和redis
        // key = token，value = user
        tokenMap.put(token, user);
        jedis.set(token.getBytes(), SerializeUtil.serialize(user));

        // 3. 关闭jedis
        JedisUtil.close(jedis);

    }

    // 通过cookie[]得到token
    public static String getTokenByCookie(Cookie[] cookies) {
        String token = null;
        if (cookies == null) {
            throw new RuntimeException("cookie没拿到");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("adminId")) {
                token = cookie.getValue();
            }
        }
        return token;
    }



    // 用token取出user
    public static User getUserByToken(String token) {

        // 先判断map中有没有
        User u = tokenMap.get(token);
        if (u != null) {
            // 情况一、目标token和user已经在map中
            return u;
        }

        // 情况二、在redis中，但还不在map中
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 从redis中取出
        byte[] bytes = jedis.get(token.getBytes());
        User user = null;
        if (bytes != null) {
            user = (User) SerializeUtil.unSerialize(bytes);
            // 3. 再保存进缓存中
            tokenMap.put(token, user);
        } else {
            throw new RuntimeException("不存在登录的对象，所以空指针");
        }
        // 4. 关闭jedis
        JedisUtil.close(jedis);

        return user;
    }
}
