package com.uninew.net.JT905.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密、解密
 * @author Administrator
 *
 */
public class RSAAlgorithm {   
    public static final String KEY_ALGORITHM = "RSA";   
    /** 
     * 解密<br> 
     * @param data 
     * @param key 私钥
     * @return 
     * @throws Exception 
     */ 
    public static byte[] decrypt(byte[] data, Key key) throws Exception {   
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
        // 对数据解密   
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
        cipher.init(Cipher.DECRYPT_MODE, key);   
        return cipher.doFinal(data);   
    }   

    /** *//** 
     * 加密<br> 
     * @param data 
     * @param key 公钥
     * @return 
     * @throws Exception 
     */ 
    public static byte[] encrypt(byte[] data, Key key) throws Exception {   
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
        // 对数据加密   
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
        cipher.init(Cipher.ENCRYPT_MODE, key);   
        return cipher.doFinal(data);   
    }   

    /**
     * 取得私钥 
     * @param keyPair
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(KeyPair keyPair) throws Exception {   
    	RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   
        return privateKey;   
    }   

    /**
     * 取得公钥 
     * @param keyPair
     * @return
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(KeyPair keyPair) throws Exception {   
    	RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey;   
    }   

    /** *//** 
     * 获取密钥对
     * @return 
     * @throws Exception 
     */ 
    public static KeyPair getKeyPair() throws Exception {   
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);   
        //生成N的字节数组为128个(128*8=1024)????
        keyPairGen.initialize(1020);
        KeyPair keyPair = keyPairGen.generateKeyPair();   
        // 公钥   
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
//        byte[] publicKeyData = publicKey.getEncoded();

        
        // 私钥   
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   
//        byte[] privateKeyData = privateKey.getEncoded();
        return keyPair;   
    }  
    
	/**
	 * 使用N、d值还原公钥
	 * @param modulus N
	 * @param privateExponent D
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus,
		bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	/**
	 * 使用N、e值还原公钥
	 * @param modulus N
	 * @param publicExponent e
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
		bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	/**
	 * 使用N、e值还原公钥
	 * @param modulus N
	 * @param publicExponent e
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKeyByBytes(byte[] modulus, byte[] publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
		bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	/**
	 * 通过公钥byte[]将公钥还原，适用于RSA算法
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 通过私钥byte[]将公钥还原，适用于RSA算法
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	/**
	 * 获取公钥的N
	 * @param key
	 * @return
	 */
	public static BigInteger getPublicKeyN(PublicKey key) {
		RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
		return rsaPublicKey.getModulus();
	}
	
	/**
	 * 获取公钥的e
	 * @param key
	 * @return
	 */
	public static BigInteger getPublicKeyE(PublicKey key) {
		RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
		return rsaPublicKey.getPublicExponent();
	}
	
	/**
	 * 获取私钥的N
	 * @param key
	 * @return
	 */
	public static BigInteger getPrivateKeyN(PrivateKey key) {
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
		return rsaPrivateKey.getModulus();
	}
	
	/**
	 * 获取私钥的d
	 * @param key
	 * @return
	 */
	public static BigInteger getPrivateKeyD(PrivateKey key) {
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
		return rsaPrivateKey.getPrivateExponent();
	}
}