package com.festivr.utils;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Constants {
  private static final List<String> porterRobinson =
      Arrays.asList("http://i.imgur.com/XPAL7k3.jpg",
          "http://i.imgur.com/kh1SkcT.jpg", "http://i.imgur.com/m1vWTsM.jpg",
          "http://i.imgur.com/DzhyCsz.jpg", "http://i.imgur.com/iyLLsCB.jpg",
          "http://i.imgur.com/wKSM7ql.jpg", "http://i.imgur.com/4UXvNKy.jpg",
          "http://i.imgur.com/YnIefnM.jpg", "http://i.imgur.com/17n2jLT.jpg",
          "http://i.imgur.com/fachEfU.jpg", "http://i.imgur.com/bGzAzqW.jpg",
          "http://i.imgur.com/MuY9X3i.jpg", "http://i.imgur.com/0YB0fQN.jpg",
          "http://i.imgur.com/6xXiqc6.jpg", "http://i.imgur.com/U7VKUwN.jpg",
          "http://i.imgur.com/dDjx4A0.jpg", "http://i.imgur.com/w25JFtk.jpg",
          "http://i.imgur.com/5Z0GXAT.jpg", "http://i.imgur.com/rsS2lUE.jpg",
          "http://i.imgur.com/fZ1xGjV.jpg", "http://i.imgur.com/S6zGXu0.jpg",
          "http://i.imgur.com/hxKQt8C.jpg", "http://i.imgur.com/TXDjXCy.jpg",
          "http://i.imgur.com/1V3rAlu.jpg", "http://i.imgur.com/7MNYdN4.jpg",
          "http://i.imgur.com/3bZSsjf.jpg", "http://i.imgur.com/ElKDyJd.jpg",
          "http://i.imgur.com/RA3E2SP.jpg", "http://i.imgur.com/bGrCN1U.jpg",
          "http://i.imgur.com/T1Xfiag.jpg", "http://i.imgur.com/sCRO0S5.jpg",
          "http://i.imgur.com/aNA9T6O.jpg");
  private static final List<String> beyonce =
      Arrays.asList("http://i.imgur.com/Yuqk0pQ.jpg",
          "http://i.imgur.com/bN3ESYF.jpg", "http://i.imgur.com/RsJf1rP.jpg",
          "http://i.imgur.com/mqqKFhW.jpg", "http://i.imgur.com/WYes4Ej.jpg",
          "http://i.imgur.com/dLLreH6.jpg", "http://i.imgur.com/cCnt5Pe.jpg",
          "http://i.imgur.com/wHG5lyd.jpg", "http://i.imgur.com/hDkQvlq.jpg",
          "http://i.imgur.com/wQVqQRU.jpg", "http://i.imgur.com/MGk7mcq.jpg",
          "http://i.imgur.com/xdTP4VU.jpg", "http://i.imgur.com/cH0It3w.jpg",
          "http://i.imgur.com/K94wShr.jpg", "http://i.imgur.com/WSw9wI1.jpg",
          "http://i.imgur.com/KhONS8p.jpg", "http://i.imgur.com/Xq0Mrg5.jpg",
          "http://i.imgur.com/NtbpPC9.png", "http://i.imgur.com/32evRVN.jpg",
          "http://i.imgur.com/mx5Cv3z.jpg", "http://i.imgur.com/F5l5elk.jpg",
          "http://i.imgur.com/CwZLtFN.jpg", "http://i.imgur.com/C88fzmB.jpg",
          "http://i.imgur.com/RHpSAha.jpg", "http://i.imgur.com/eYDhEWZ.jpg",
          "http://i.imgur.com/sbDLwGG.jpg", "http://i.imgur.com/1uobvbU.jpg",
          "http://i.imgur.com/43QnKGW.jpg", "http://i.imgur.com/et0SVUL.jpg",
          "http://i.imgur.com/rGRl2Qq.jpg", "http://i.imgur.com/sTw3a2z.jpg",
          "http://i.imgur.com/X7s6TuJ.jpg");
  private static final List<String> theBeatles =
      Arrays.asList("http://i.imgur.com/24j3VLA.jpg",
          "http://i.imgur.com/ih5kt4s.jpg", "http://i.imgur.com/uFWV9Ru.jpg",
          "http://i.imgur.com/HHmcyRI.jpg", "http://i.imgur.com/d4ecW4l.jpg",
          "http://i.imgur.com/d4ecW4l.jpg", "http://i.imgur.com/rDPT45p.jpg",
          "http://i.imgur.com/zElcVhu.jpg", "http://i.imgur.com/8h7c2Eg.jpg",
          "http://i.imgur.com/LQYcEUA.jpg", "http://i.imgur.com/iUvNBS3.jpg",
          "http://i.imgur.com/65qawwh.jpg", "http://i.imgur.com/6d9dhow.jpg",
          "http://i.imgur.com/zwnUO0O.jpg", "http://i.imgur.com/bbbCgzg.jpg",
          "http://i.imgur.com/bfpr6LC.jpg", "http://i.imgur.com/SXHH7t8.jpg",
          "http://i.imgur.com/tQidFmp.jpg", "http://i.imgur.com/fhmiYkR.jpg",
          "http://i.imgur.com/fTb2FOB.jpg", "http://i.imgur.com/dUZVdwB.jpg",
          "http://i.imgur.com/TA3eEdc.jpg", "http://i.imgur.com/hdSgGt7.jpg");
  private static final List<String> theBlackKeys =
      Arrays.asList("http://i.imgur.com/rkSQ5yW.png",
          "http://i.imgur.com/VrNgod9.jpg", "http://i.imgur.com/bPoNdmp.jpg",
          "http://i.imgur.com/MnOaRHF.jpg", "http://i.imgur.com/dqI9hSs.jpg",
          "http://i.imgur.com/ViTRAIB.jpg", "http://i.imgur.com/UxixTu3.jpg",
          "http://i.imgur.com/ar6LBZu.jpg", "http://i.imgur.com/fvPSTbE.jpg",
          "http://i.imgur.com/X9V9V3n.jpg", "http://i.imgur.com/n2SX9ZT.jpg");
  private static final List<String> coachellaDance =
      Arrays.asList("http://i.imgur.com/4eLU0rP.jpg",
          "http://i.imgur.com/aVk94J8.jpg", "http://i.imgur.com/BEZ7z2N.jpg",
          "http://i.imgur.com/WBtIQ9E.jpg", "http://i.imgur.com/93vnVqW.png",
          "http://i.imgur.com/RVKSgcn.jpg", "http://i.imgur.com/MyGpDis.jpg",
          "http://i.imgur.com/AbRc8qN.jpg", "http://i.imgur.com/kKRpscD.jpg",
          "http://i.imgur.com/mlMKZaA.jpg", "http://i.imgur.com/13EqF7w.jpg",
          "http://i.imgur.com/4q3xDK4.jpg", "http://i.imgur.com/mkkpHPb.jpg",
          "http://i.imgur.com/viBCMbr.jpg", "http://i.imgur.com/tqL3TYh.png",
          "http://i.imgur.com/uLu9P8a.jpg", "http://i.imgur.com/RE6GFDN.jpg",
          "http://i.imgur.com/oxyCPew.jpg", "http://i.imgur.com/aeWWQ0r.jpg",
          "http://i.imgur.com/1LG0o3j.jpg", "http://i.imgur.com/koICApZ.jpg",
          "http://i.imgur.com/JQ7g3v6.jpg", "http://i.imgur.com/5yYl1HT.jpg",
          "http://i.imgur.com/dzCSMNt.png", "http://i.imgur.com/emQy1so.jpg");

  private static ArrayList<String> hugeList;

  private static boolean hasPorterRobinsonBeenShuffled;
  private static boolean hasBeyonceBeenShuffled;
  private static boolean hasTheBeatlesBeenShuffled;
  private static boolean hasTheBlackKeysBeenShuffled;
  private static boolean hasCoachellaDanceBeenShuffled;

  public static ArrayList<String> getPorterRobinson() {
    if (!hasPorterRobinsonBeenShuffled) {
      long seed = System.nanoTime();
      Collections.shuffle(porterRobinson, new Random(seed));
      Collections.shuffle(porterRobinson, new Random(seed));
      hasPorterRobinsonBeenShuffled = true;
    }
    ArrayList<String> returner = new ArrayList<>();
    returner.addAll(porterRobinson);
    return returner;
  }

  public static ArrayList<String> getBeyonce() {
    if (!hasBeyonceBeenShuffled) {
      long seed = System.nanoTime();
      Collections.shuffle(beyonce, new Random(seed));
      Collections.shuffle(beyonce, new Random(seed));
      hasBeyonceBeenShuffled = true;
    }
    ArrayList<String> returner = new ArrayList<>();
    returner.addAll(beyonce);
    return returner;
  }

  public static ArrayList<String> getTheBeatles() {
    if (!hasTheBeatlesBeenShuffled) {
      long seed = System.nanoTime();
      Collections.shuffle(theBeatles, new Random(seed));
      Collections.shuffle(theBeatles, new Random(seed));
      hasTheBeatlesBeenShuffled = true;
    }
    ArrayList<String> returner = new ArrayList<>();
    returner.addAll(theBeatles);
    return returner;
  }

  public static ArrayList<String> getTheBlackKeys() {
    if (!hasTheBlackKeysBeenShuffled) {
      long seed = System.nanoTime();
      Collections.shuffle(theBlackKeys, new Random(seed));
      Collections.shuffle(theBlackKeys, new Random(seed));
      hasTheBlackKeysBeenShuffled = true;
    }
    ArrayList<String> returner = new ArrayList<>();
    returner.addAll(theBlackKeys);
    return returner;
  }

  public static ArrayList<String> getCoachellaDance() {
    if (!hasCoachellaDanceBeenShuffled) {
      long seed = System.nanoTime();
      Collections.shuffle(coachellaDance, new Random(seed));
      Collections.shuffle(coachellaDance, new Random(seed));
      hasCoachellaDanceBeenShuffled = true;
    }
    ArrayList<String> returner = new ArrayList<>();
    returner.addAll(coachellaDance);
    return returner;
  }

  public static ArrayList<String> getHugeList() {
    if (hugeList == null) {
      hugeList = new ArrayList<>();
      hugeList.addAll(porterRobinson);
      hugeList.addAll(beyonce);
      hugeList.addAll(theBeatles);
      hugeList.addAll(theBlackKeys);
      hugeList.addAll(coachellaDance);

      long seed = System.nanoTime();
      Collections.shuffle(hugeList, new Random(seed));
      Collections.shuffle(hugeList, new Random(seed));
      Collections.shuffle(hugeList, new Random(seed));
    }
    return hugeList;
  }

  public static <T> T checkForNonNullArg(T generic) {
    return checkForNonNull(generic, "Input argument cannot be null!");
  }

  public static <T> T checkForNonNull(T generic, String exceptionMsg) {
    if (generic == null) {
      throw new NullPointerException(exceptionMsg);
    }
    return generic;
  }

  public static String checkForNonEmptyText(String text) {
    if (TextUtils.isEmpty(text)) {
      throw new IllegalArgumentException("Text should not be empty or null.");
    }
    return text;
  }

  public static String checkForNonEmptyText(String text, String exceptionMsg) {
    if (TextUtils.isEmpty(text)) {
      throw new IllegalArgumentException(exceptionMsg);
    }
    return text;
  }
}
