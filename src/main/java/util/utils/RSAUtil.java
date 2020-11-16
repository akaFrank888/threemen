package util.utils;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class RSAUtil {
    private static HashMap<Integer, String> keyMap = new HashMap<>();  //用于封装随机产生的公钥与私钥

    /**
     * 随机生成密钥对
     */
    public static void getKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // Plan A：将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥

        // Plan B：将公钥和私钥保存到redis
        keysSaveToRedis(privateKeyString, publicKeyString);

    }
    /**
     * RSA公钥加密
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
         return new String(cipher.doFinal(inputByte));
    }

    // Plan B： 将一对公钥和私钥存入redis
    public static void keysSaveToRedis(String privateKey, String publicKey) {
        Jedis jedis = JedisUtil.getJedis();
        jedis.set("privateKey", privateKey);
        jedis.set("publicKey", publicKey);
    }

    /**
     * 获得公钥
     */
    public static String getPublicKey() {
        return keyMap.get(0);
    }

    public static String getPublicKeyTest() {
        Jedis jedis = JedisUtil.getJedis();
        return jedis.get("publicKey");
    }

    /**
     * 获得私钥
     */
    public static String getPrivateKey() {
        return keyMap.get(1);
    }

    public static String getPrivateKeyTest() {
        Jedis jedis = JedisUtil.getJedis();
        return jedis.get("privateKey");
    }


    // 测试
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        getKeyPair();
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message,keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }

    // 模拟前端 用公钥加密，得到sign
    @Test
    public void test() throws Exception {
        StringBuilder sb = new StringBuilder("123123123");
        sb.append("&");
        sb.append("123123123");
        sb.append("&");
        sb.append("d637cfecc1ab49148f6786539fbf6269");

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIFncKk3fVrJhgILqObEUAgcS7XjJfaGJZ1zxBXtTcdPXLVfx/Ao+DD+V8fb+kBIaNGIPnEqE6i23h7Vd66CsIIMM9q6ylWGzsQjst39fu+TEx80Nq0o5ZGwgJW4DYgnizlKmzB+2oBSy4zX36laGOPCGgTjfiblWVSVWydvZOswIDAQAB";
        String encrypt = RSAUtil.encrypt(sb.toString(), publicKey);

        System.out.println(encrypt);

    }

    @Test
    public void test1() throws NoSuchAlgorithmException {


        System.out.println("这是privateKey："+getPrivateKey());
        System.out.println("这是publicKey：" + getPublicKey());
    }
}

