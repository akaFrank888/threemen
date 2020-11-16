package util.utils;

import com.sun.org.apache.xpath.internal.objects.XNull;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @Auther: yxl15
 * @Date: 2020/11/11 17:48
 * @Description:
 */
public class NonceRedisUtil {

    // 设计一个方法   生成一个nonce，并保存进redis
    public static String generateNonce() {
        // 生成一个nonce
        String nonce = UuidUtil.getUuid();
        // 保存进redis中
        Jedis jedis = JedisUtil.getJedis();
        jedis.set(nonce, nonce);

        return nonce;
    }

    // 设计一个方法   检查并删除redis中的一个nonce
    public static boolean checkAndDeleteNonce(String nonce) {
        Jedis jedis = JedisUtil.getJedis();
        String result = jedis.get(nonce);
        if (result != null) {
            jedis.del(nonce);
            return true;
        }
        return false;
    }

    @Test
    public void test() {

    }
}
