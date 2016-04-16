package com.festivr.url;

import android.net.Uri;
import android.text.TextUtils;
import com.festivr.utils.Constants;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class UrlKeyCombo {
  //Allowed characters for Uri encoding.
  private static final String ALLOWED_CHARS = "@#&=*+-_.,:!?()/~'%";
  private final URL url;
  private final String urlAsString;

  //Define UTF-8 Charset
  String UTF8_CHARSET_NAME = "UTF-8";
  Charset CHARSET = Charset.forName(UTF8_CHARSET_NAME);
  private URL safelyEncodedUrl;
  private String safelyEncodedString;
  private byte[] byteArrayKey;

  public UrlKeyCombo(URL url) {
    this.url = Constants.checkForNonNull(url,
        "URL object should never be null in UrlKeyCombo construction!");
    this.urlAsString = null;
  }

  public UrlKeyCombo(String url) {
    this.url = null;
    this.urlAsString = Constants.checkForNonEmptyText(url,
        "Url String should never be empty or null in UrlKeyCombo construction!");
  }

  public URL transformToURL() throws MalformedURLException {
    return getSafelyEncodedUrl();
  }

  public URL getSafelyEncodedUrl() throws MalformedURLException {
    if (safelyEncodedUrl == null) {
      safelyEncodedUrl = new URL(getSafelyEncodedUrlString());
    }
    return safelyEncodedUrl;
  }

  private String getSafelyEncodedUrlString() {
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

  private byte[] getBytesOfCacheKey() {
    if (byteArrayKey == null) {
      byteArrayKey = getCacheKey().getBytes(CHARSET);
    }
    return byteArrayKey;
  }

  @Override public boolean equals(Object o) {
    if (o instanceof UrlKeyCombo) {
      UrlKeyCombo that = (UrlKeyCombo) o;
      return this.getCacheKey().equals(that.getCacheKey());
    }
    return false;
  }

  @Override public int hashCode() {
    int hashCode = getCacheKey().hashCode();
    //Use of prime number is recommended.
    hashCode = 31 * hashCode;
    return hashCode;
  }
}
