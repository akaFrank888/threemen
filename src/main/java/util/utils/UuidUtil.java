package util.utils;

import org.junit.Test;

import java.util.UUID;

/**
 * 产生UUID随机字符串工具类，当作激活码
 */
public final class UuidUtil {
	private UuidUtil(){}
	public static String getUuid(){
		// UUID  是   机器生成的标识符
		// UUID.randomUUID() 可生成一个唯一的字符串（因为和电脑硬件信息和时间的毫秒值相关）
		return UUID.randomUUID().toString().replace("-","");
	}


	@Test
	public void test() {
		System.out.println(getUuid());
	}
}
