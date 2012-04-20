package org.opencloudsync.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Mimics Apache commons codec DigestUtils but adds some functionality.
 * Date: 5/04/12
 * Time: 06:46
 */
public class DigestUtils {

    /**
     * Returns an SHA-1 digest.
     *
     * @return An SHA-1 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }

    /**
     * Calls {@link org.apache.commons.codec.binary.StringUtils#getBytesUtf8(String)}
     *
     * @param data
     *            the String to encode
     * @return encoded bytes
     */
    private static byte[] getBytesUtf8(final String data) {
        return StringUtils.getBytesUtf8(data);
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest
     */
    public static byte[] sha(byte[] data) {
        return getShaDigest().digest(data);
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest
     */
    public static byte[] sha(String data) {
        return sha(getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest as a hex string
     */
    public static String shaHex(byte[] data) {
        return Hex.encodeHexString(sha(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     *
     * @param data
     *            Data to digest
     * @return SHA-1 digest as a hex string
     */
    public static String shaHex(String data) {
        return Hex.encodeHexString(sha(data));
    }

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            the name of the algorithm requested. See <a
     *            href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix A in the Java
     *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
     *            names.
     * @return An MD5 digest instance.
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Updates a message digest.
     * @param messageDigest the message digest to update
     * @param valueToDigest the value to 'add' to the digest
     * @return the updated digest
     */
    public static MessageDigest updateDigest(final MessageDigest messageDigest, String valueToDigest){
        messageDigest.update(getBytesUtf8(valueToDigest));
        return messageDigest;
    }

    /**
     * Updates a message digest.
     * @param messageDigest the message digest to update
     * @param valueToDigest the value to 'add' to the digest
     * @return the updated digest
     */
    public static MessageDigest updateDigest(final MessageDigest messageDigest, byte[] valueToDigest){
        messageDigest.update(valueToDigest);
        return messageDigest;
    }

}
