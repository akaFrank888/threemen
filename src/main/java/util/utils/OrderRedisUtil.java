package util.utils;

import domain.Order;
import domain.RestaurantInfo;
import domain.ResultInfo;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yxl15
 * @Date: 2020/10/20 09:42
 * @Description:
 *
 *          将订单对象order，购物车存入redis中：
 *              order：key=订单号，value=order对象
 *              购物车：key=commNum+“myCart”，value=ArrayList<RestaurantInfo>
 *
 */
public class OrderRedisUtil {

    // 方法一、保存一个订单 key=commNum，value=order       （超市代购、快递代拿）
    public static boolean saveOrderByRedis(String commNum, Order order) {
        boolean b = false;
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 保存
        jedis.set(commNum.getBytes(), SerializeUtil.serialize(order));
        // 3. 判断
        if (jedis.get(commNum.getBytes()) != null) {
            b = true;
        }

        // 4. 关闭jedis
        jedis.close();

        return b;
    }




    // 方法二、从redis中取order        （超市代购、快递代拿）
    public static Order getOrderByCommNum(String commNum) {
        Order order = null;
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 获取
        byte[] bytes = jedis.get(commNum.getBytes());
        if (bytes != null) {
            // 反序列化
            order = (Order) SerializeUtil.unSerialize(bytes);
        } else {
            throw new RuntimeException("没有成功保存order进入redis，所以空指针");
        }
        // 3. 关闭jedis
        jedis.close();

        return order;
    }

    // 方法三、保存一个订单 key=commNum+"myCart"，value=ArrayList<RestaurantInfo>       （餐厅代购）
    public static boolean saveMyCartByRedis(String commNum, ArrayList<RestaurantInfo> list) {

        boolean b = false;
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 保存

        // SerializeUtil不能直接序列化一个ArrayList，所以只能用redis的list数据结构，而不是String

        for (RestaurantInfo info : list) {

            jedis.lpush((commNum + "myCart").getBytes(), SerializeUtil.serialize(info));
        }
        // 3. 判断
        if (jedis.lrange(commNum + "myCart", 0, -1) != null) {

            b = true;
        }

        // 4. 关闭jedis
        jedis.close();

        return b;
    }


    // 方法四、从redis中取ArrayList<RestaurantInfo>        （餐厅代购）
    public static ArrayList<RestaurantInfo> getMyCartByKey(String commNum) {

        ArrayList<RestaurantInfo> list = new ArrayList<>();
        // 1. 获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        // 2. 获取
        List<byte[]> lrange = jedis.lrange((commNum + "myCart").getBytes(), 0, -1);
        if (lrange == null) {
            throw new RuntimeException("没有成功保存购物车进入redis，所以空指针");
        }
        for (byte[] bs : lrange) {
            list.add((RestaurantInfo) SerializeUtil.unSerialize(bs));
        }

        // 3. 关闭jedis
        jedis.close();

        return list;
    }

    public static boolean clearMyCart(String commNum) {
        Jedis jedis = JedisUtil.getJedis();
        jedis.del(commNum + "myCart");
        return true;
    }
    @Test
    public void test() {
        ArrayList<RestaurantInfo> list = getMyCartByKey("3ad59b14ab9d44c5b226e28f52b2716e");
        System.out.println(list);

    }
}
