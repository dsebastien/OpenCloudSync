package org.opencloudsync;

/**
 * Date: 4/04/12
 * Time: 18:07
 */
public interface DigestHolder {
    byte[] getDigest();

    String getDigestAsHexString();
}
