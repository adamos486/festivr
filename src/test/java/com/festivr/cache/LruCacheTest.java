package com.festivr.cache;

import android.graphics.Bitmap;
import com.festivr.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class LruCacheTest {
  private static final Bitmap A = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap B = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap C = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap D = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap E = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap F = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap G = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap H = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap I = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap J = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap K = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
  private static final Bitmap L = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);

  protected LruCache subject;

  @Before public void setup() {
    subject = new LruCache(40000000, 13);
  }

  @Test public void whenConstructingLruCache_withMaxMem_andMaxPool_bothShouldBeSet()
      throws Exception {
    assertTrue(subject.getMaxMemSize() == 40000000);
    assertTrue(subject.getMaxPoolSize() == 13);
  }

  @Test public void whenCurrentMemSizeIsGreaterThanMax_evict() throws Exception {
    populateCache();
    //Verify control
    assertTrue(subject.getCurrentMemSize() == 40000000);

    //Test case
    subject.put("some_unique_url", A);

    //verify evictions and memMax respected.
    assertTrue(subject.getCurrentMemSize() == 40000000);
    assertFalse(subject.contains("mock_url_1"));
    assertTrue(subject.contains("mock_url_2"));
    assertTrue(subject.contains("some_unique_url"));
  }

  @Test public void whenCurrentPoolSizeIsGreaterThanMax_evict() throws Exception {
    populateCache();
    //Add a few more memory free Bitmaps.
    subject.put("mock_url_11", mock(Bitmap.class));
    subject.put("mock_url_12", mock(Bitmap.class));

    //Verify controls
    assertTrue(subject.getCurrentPoolSize() == 12);

    // /Limit
    subject.put("mock_url_13", mock(Bitmap.class));
    assertTrue(subject.getCurrentPoolSize() == 13);

    //One over limit --> Test case
    subject.put("mock_url_14", mock(Bitmap.class));

    //Verify evictions
    assertTrue(subject.getCurrentPoolSize() == 13);
    assertFalse(subject.contains("mock_url_1"));
    assertTrue(subject.contains("mock_url_2"));
    assertTrue(subject.contains("mock_url_14"));
  }

  @Test public void whenRemoveIsCalled_currentMem_andCurrentPool_shouldBeUpdated() throws Exception {
    populateCache();
    //verify controls
    assertTrue(subject.getCurrentMemSize() == 40000000);
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.remove("mock_url_1");

    //Verify eviction
    assertTrue(subject.getCurrentMemSize() < 40000000);
    assertTrue(subject.getCurrentPoolSize() == 9);
  }

  @Test public void whenChangingMemoryMode_shouldEvict() throws Exception {
    populateCache();
    //verify controls
    assertTrue(subject.contains("mock_url_1"));

    //test case
    subject.setMemoryMode(LruCache.LOW_MEMORY);

    //verify evictions
    assertFalse(subject.contains("mock_url_1"));
    assertFalse(subject.contains("mock_url_2"));
    assertFalse(subject.contains("mock_url_3"));
    assertFalse(subject.contains("mock_url_4"));
    assertFalse(subject.contains("mock_url_5"));

    //verify head is where we thought.
    assertTrue(subject.contains("mock_url_6"));
  }

  @Test public void whenChangingMemoryMode_shouldEvict_thenShouldRespectNewMax() throws Exception {
    populateCache();
    //Control verification.
    assertTrue(subject.contains("mock_url_1"));

    subject.setMemoryMode(LruCache.LOW_MEMORY);

    //Verify eviction.
    assertFalse(subject.contains("mock_url_1"));

    //Verify new max eviction and addition.
    subject.put("changing_url_1", J);
    assertFalse(subject.contains("mock_url_6"));
    assertTrue(subject.contains("changing_url_1"));

    //Another max test and addition.
    subject.put("changing_url_2", K);
    assertFalse(subject.contains("mock_url_7"));
    assertTrue(subject.contains("changing_url_2"));

    //Another max test and addition.
    subject.put("changing_url_3", L);
    assertFalse(subject.contains("mock_url_8"));
    assertTrue(subject.contains("changing_url_3"));
  }

  @Test public void whenSettingLargerMaxMemSize_shouldEvictNothing_andRespect() throws Exception {
    populateCache();
    //Control verifications.
    assertTrue(subject.getCurrentMemSize() == 40000000);
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.setMaxMemSize(60000000);

    //Verify no evictions.
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Add something new.
    subject.put("new_url_1", J);

    //Verify no evictions.
    assertTrue(subject.getCurrentPoolSize() == 11);
  }

  @Test public void whenSettingSmallerMaxMemSize_shouldEvict_thenRespect() throws Exception {
    populateCache();
    //Control verifications.
    assertTrue(subject.getCurrentMemSize() == 40000000);
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.setMaxMemSize(20000000);

    //Verify evictions
    assertFalse(subject.contains("mock_url_1"));
    assertTrue(subject.getCurrentPoolSize() == 5);

    //Add something new
    subject.put("new_url_1", J);

    //Verify evictions
    assertFalse(subject.contains("mock_url_6"));
    assertTrue(subject.contains("new_url_1"));
    assertTrue(subject.getCurrentPoolSize() == 5);
  }

  @Test public void whenSettingLargerPoolSize_shouldNotEvict_andRespect() throws Exception {
    populateCache();
    //Control verifications.
    assertTrue(subject.getCurrentMemSize() == 40000000);
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.setMaxPoolSize(15);

    //Verify no evictions.
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Add something new.
    subject.put("new_url_1", mock(Bitmap.class));

    //Verify no evictions.
    assertTrue(subject.getCurrentPoolSize() == 11);
  }

  @Test public void whenSettingSmallerPoolSize_shouldEvict_thenRespect() throws Exception {
    populateCache();
    //Control verifications.
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.setMaxPoolSize(5);

    //Verify evictions
    assertFalse(subject.contains("mock_url_1"));
    assertTrue(subject.getCurrentPoolSize() == 5);

    //Add something new
    subject.put("new_url_1", J);

    //Verify evictions
    assertFalse(subject.contains("mock_url_6"));
    assertTrue(subject.contains("new_url_1"));
    assertTrue(subject.getCurrentPoolSize() == 5);
  }

  @Test public void whenClearCache_shouldEvictEverything() throws Exception {
    populateCache();
    //Control verifications.
    assertTrue(subject.getCurrentPoolSize() == 10);

    //Test case
    subject.clearCache();

    //Verify evictions.
    assertTrue(subject.getCurrentPoolSize() == 0);
    assertTrue(subject.getCacheSize() == 0);
    assertTrue(subject.getCurrentMemSize() == 0);
  }

  @Test public void whenAnInsertIsLargerThanMaxCacheSize_shouldEvictImmediately() throws Exception {
    //Re-establishing a new cache.
    subject = new LruCache(15000000, 13);
    subject.put("mock_url_1", A);

    int size = subject.getCurrentMemSize();

    Bitmap XXL =
        Bitmap.createBitmap(20000000, 20000000, Bitmap.Config.ARGB_8888);
    //Test case
    subject.put("xxl", XXL);

    //Verify that cache doesn't contain
    assertTrue(subject.getCurrentMemSize() == size);
    assertFalse(subject.contains("xxl"));
  }

  private void populateCache() {
    subject.put("mock_url_1", A);
    subject.put("mock_url_2", B);
    subject.put("mock_url_3", C);
    subject.put("mock_url_4", D);
    subject.put("mock_url_5", E);
    subject.put("mock_url_6", F);
    subject.put("mock_url_7", G);
    subject.put("mock_url_8", H);
    subject.put("mock_url_9", I);
    subject.put("mock_url_10", J);
  }
}
