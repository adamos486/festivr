package com.festivr.url;

import android.net.Uri;
import android.text.TextUtils;
import com.festivr.utils.Constants;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import timber.log.Timber;

public class UrlKeyCombo {
  //Allowed characters for Uri encoding.
  private static final String ALLOWED_CHARS = "@#&=*+-_.,:!?()/~'%";
  private final String urlAsString;
  //Define UTF-8 Charset
  String UTF8_CHARSET_NAME = "UTF-8";
  Charset CHARSET = Charset.forName(UTF8_CHARSET_NAME);
  private URL url = null;
  private URL safelyEncodedUrl;
  private String safelyEncodedString;
  private byte[] byteArrayKey;
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

  public byte[] getBytesOfCacheKey() {
    if (byteArrayKey == null) {
      byteArrayKey = getCacheKey().getBytes(CHARSET);
    }
    return byteArrayKey;
  }

  @Override public boolean equals(Object o) {
    if (o instanceof UrlKeyCombo) {
      UrlKeyCombo that = (UrlKeyCombo) o;
      return this.getMD5HashKey().equals(that.getMD5HashKey());
    }
    return false;
  }

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

  @Override public int hashCode() {
    int hashCode = getCacheKey().hashCode();
    //Use of prime number is recommended.
    hashCode = 31 * hashCode;
    return hashCode;
  }
}
