package com.festivr.url;

import android.net.Uri;
import android.text.TextUtils;
import com.festivr.utils.Constants;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import timber.log.Timber;

/**
 * Url storage and cache key wrapped into one.
 * This allows for already unique url's to be used as keys, or a generated 32-byte md5 key.
 */
public class UrlKeyCombo {
  //Allowed characters for Uri encoding.
  private static final String ALLOWED_CHARS = "@#&=*+-_.,:!?()/~'%";
  private final String urlAsString;
  private URL url = null;
  private URL safelyEncodedUrl;
  private String safelyEncodedString;
  private String md5Key;

  public UrlKeyCombo(URL url) {
    this.url = Constants.checkForNonNull(url,
        "URL object should never be null in UrlKeyCombo construction!");
    this.urlAsString = url.toString();
  }

  public UrlKeyCombo(String url) {
    try {
      this.url = new URL(url);
    } catch (MalformedURLException e) {
      Timber.e("MalformedURLException when trying to construct a UrlKeyCombo. \n url:" + url, e);
    }
    this.urlAsString = Constants.checkForNonEmptyText(url,
        "Url String should never be empty or null in UrlKeyCombo construction!");
  }

  /**
   * Should it be needed, transforms the safe string url into a safe URL object.
   *
   * @return The product of this transformation as a {@link URL} object.
   */
  public URL getSafelyEncodedUrl() {
    if (safelyEncodedUrl == null) {
      try {
        safelyEncodedUrl = new URL(getSafelyEncodedUrlString());
      } catch (MalformedURLException e) {
        Timber.e("Encountered a URL that can't be encoded.", e);
      }
    }
    return safelyEncodedUrl;
  }

  /**
   * Provides a safely encoded url for the OkHttp request builder.
   *
   * @return The product or stored safe url.
   */
  public String getSafelyEncodedUrlString() {
    if (TextUtils.isEmpty(safelyEncodedString)) {
      String nonEncoded = urlAsString;
      if (TextUtils.isEmpty(nonEncoded)) {
        nonEncoded = url.toString();
      }
      safelyEncodedString = Uri.encode(nonEncoded, ALLOWED_CHARS);
    }
    return safelyEncodedString;
  }

  public String getCacheKey() {
    return urlAsString != null ? urlAsString : url.toString();
  }

  @Override public String toString() {
    return getCacheKey();
  }

  /**
   * Equals method uses md5 key to check for equality.
   *
   * @param o The other UrlKeyCombo being compared
   * @return Whether or not these two objects have matching md5 cache keys.
   */
  @Override public boolean equals(Object o) {
    if (o instanceof UrlKeyCombo) {
      UrlKeyCombo that = (UrlKeyCombo) o;
      return this.getMD5HashKey().equals(that.getMD5HashKey());
    }
    return false;
  }

  /**
   * Generates or provides the md5 key, which is a 32-byte object.
   *
   * @return The {@link String} md5 cache key which will be unique and retrievable for this object
   * alone.
   */
  public String getMD5HashKey() {
    if (md5Key == null) {
      try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(urlAsString.getBytes(), 0, urlAsString.length());
        md5Key = new BigInteger(1, m.digest()).toString(16);
      } catch (NoSuchAlgorithmException e) {
        Timber.e("No Such Algorithm when trying to generate md5 key!!!");
      }
    }
    return md5Key;
  }

  /**
   * Generates a better hashCode, using the url string and multiplying by 31 a prime number larger
   * than 17.
   */
  @Override public int hashCode() {
    int hashCode = getCacheKey().hashCode();
    //Use of prime number is recommended.
    hashCode = 31 * hashCode;
    return hashCode;
  }
}
