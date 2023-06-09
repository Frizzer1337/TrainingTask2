package com.frizzer.music;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Intervals {

  private static final Map<String, Map<String, Integer>> interval = new HashMap<>();
  private static Map<String, Integer> semitonesMap = new HashMap<>();

  private static final Map<String, Integer> originalSemitonesMap = new HashMap<>();
  private static final Map<String, Integer> reverseSemitonesMap = new HashMap<>();

  private static final String[] noteOrder = {"A", "B", "C", "D", "E", "F", "G"};
  public static final String SEMITONE = "semitones";
  public static final String DEGREE = "degrees";

  private static final AtomicBoolean isReversed = new AtomicBoolean(false);

  static {
    interval.put("m2", Map.of(SEMITONE, 1, DEGREE, 2));
    interval.put("M2", Map.of(SEMITONE, 2, DEGREE, 2));
    interval.put("m3", Map.of(SEMITONE, 3, DEGREE, 3));
    interval.put("M3", Map.of(SEMITONE, 4, DEGREE, 3));
    interval.put("P4", Map.of(SEMITONE, 5, DEGREE, 4));
    interval.put("P5", Map.of(SEMITONE, 7, DEGREE, 5));
    interval.put("m6", Map.of(SEMITONE, 8, DEGREE, 6));
    interval.put("M6", Map.of(SEMITONE, 9, DEGREE, 6));
    interval.put("m7", Map.of(SEMITONE, 10, DEGREE, 7));
    interval.put("M7", Map.of(SEMITONE, 11, DEGREE, 7));
    interval.put("P8", Map.of(SEMITONE, 12, DEGREE, 8));

    semitonesMap.put("b", 1);
    semitonesMap.put("bb", 2);
    semitonesMap.put("", 0);
    semitonesMap.put("#", -1);
    semitonesMap.put("##", -2);

    originalSemitonesMap.put("b", 1);
    originalSemitonesMap.put("bb", 2);
    originalSemitonesMap.put("", 0);
    originalSemitonesMap.put("#", -1);
    originalSemitonesMap.put("##", -2);

    reverseSemitonesMap.put("b", -1);
    reverseSemitonesMap.put("bb", -2);
    reverseSemitonesMap.put("", 0);
    reverseSemitonesMap.put("#", 1);
    reverseSemitonesMap.put("##", 2);
  }

  public static String intervalConstruction(String[] args) {

    final int INTERVAL = 0;
    final int START_NOTE = 1;
    final int NOTE_ORDER = 2;
    final int FULL_CIRCLE = 7;

    //bring collections to default conditions
    if(isReversed.compareAndSet(true,false)){
      Collections.reverse(Arrays.asList(noteOrder));
      semitonesMap = originalSemitonesMap;
    }

    //preconditions according to task
    preconditions(args);

    //args[START_NOTE] is string that contains two parts(second is optional) first is note,
    //second is semitone in below code lines we divide our string into this two parts
    String startNoteValue = args[START_NOTE].substring(0, 1);
    String semitoneValue = findSemitoneValue(args[START_NOTE]);

    int startDegree = Arrays.binarySearch(noteOrder, startNoteValue);
    int startSemitone = semitonesMap.get(semitoneValue);

    //using our interval we can count how many degrees and semitones to move
    int degreeToMove = interval.get(args[INTERVAL]).get(DEGREE) - 1;
    int semitoneToMove = interval.get(args[INTERVAL]).get(SEMITONE);

    //there we have some modifiers for algorithm if order is reversed
    if (args.length == 3 && args[NOTE_ORDER].equals("dsc")) {
      isReversed.compareAndSet(false,true);
      Collections.reverse(Arrays.asList(noteOrder));
      semitonesMap = reverseSemitonesMap;
      startDegree = 6 - startDegree;
      startSemitone = -startSemitone;
    }

    //to found result degree we need to move from the start degree taking into account
    //that degrees works like cycle with period of 7 elements
    int endDegreeIndex = startDegree + degreeToMove;
    if (endDegreeIndex >= FULL_CIRCLE) {
      endDegreeIndex -= FULL_CIRCLE;
    }
    String endDegree = noteOrder[endDegreeIndex];

    //to found result semitone we need to perform same steps as in result degree part, but taking
    //into account that usual distance between semitones is 2, but 1 is possible
    int endSemitoneIndex =
        countSemitone(startDegree, endDegreeIndex, startSemitone, semitoneToMove);
    String endSemitone = semitonesMap.entrySet()
        .stream()
        .filter(entry -> entry.getValue() == endSemitoneIndex)
        .map(Entry::getKey).collect(Collectors.joining());

    return endDegree + endSemitone;
  }

  public static String intervalIdentification(String[] args) {

    final int START_NOTE = 0;
    final int END_NOTE = 1;
    final int NOTE_ORDER = 2;
    final int FULL_CIRCLE = 7;

    preconditions(args);

    //if order is reversed we can just call function reversing args
    if (args.length == 3 && args[NOTE_ORDER].equals("dsc")) {
      return intervalIdentification(new String[]{args[END_NOTE],args[START_NOTE]});
    }

    //args[START_NOTE] and args[END_NOTE] are strings that contains two parts(second is optional) first is note,
    //second is semitone in below code lines we divide our string into this two parts
    String startNoteValue = args[START_NOTE].substring(0, 1);
    String startSemitoneValue = findSemitoneValue(args[START_NOTE]);

    int startDegree = Arrays.binarySearch(noteOrder, startNoteValue);
    int startSemitone = semitonesMap.get(startSemitoneValue);

    String endNoteValue = args[END_NOTE].substring(0, 1);
    String endSemitoneValue = findSemitoneValue(args[END_NOTE]);

    int endDegree = Arrays.binarySearch(noteOrder, endNoteValue);
    int endSemitone = semitonesMap.get(endSemitoneValue);

    //With knowledge about start and end both of degree and semitone we can calculate how many
    //degrees and semitones are between them
    if(endDegree <= startDegree){
      startDegree-=FULL_CIRCLE;
    }
    int degreeToMove = endDegree - startDegree + 1;
    int semitoneToMove = countSemitone(startDegree,endDegree,startSemitone,endSemitone);

    //With knowledge about amount of degrees and semitones between start and end
    //we can find the interval
    Map<String,Integer> resultMap = Map.of(SEMITONE,semitoneToMove,DEGREE,degreeToMove);
    String resultInterval = interval.entrySet()
        .stream()
        .filter(entry -> entry.getValue().equals(resultMap))
        .map(Entry::getKey).collect(Collectors.joining());
    if(resultInterval.isEmpty()){
      throw new RuntimeException("Cannot find the interval");
    }

    return resultInterval;
  }

  //this function check if input matches preconditions
  public static void preconditions(String[] args) {
    if (args == null) {
      throw new IllegalArgumentException("Illegal number of elements in input array");
    }
    if (args.length != 3 && args.length != 2) {
      throw new IllegalArgumentException("Illegal number of elements in input array");
    }
  }

  //this function helps to find semitone from input
  public static String findSemitoneValue(String semitone) {
    String semitoneValue = "";
    if (semitone.length() > 2) {
      semitoneValue = semitone.substring(1, 3);
    } else if (semitone.length() > 1) {
      semitoneValue = semitone.substring(1, 2);
    }

    return semitoneValue;
  }

  //this function counts amount of semitones between start and result degree
  public static int countSemitone(
      int startDegree, int endDegree, int startSemitone, int semitoneToMove) {
    int semitoneAmount = 0;
    int fullCircle = 7;
    List<Integer> gapWithOneSemitone = List.of(1, 4, 9, 11);

    if (startDegree >= endDegree) {
      endDegree += fullCircle;
    }

    for (int i = startDegree; i < endDegree; i++) {
      if (gapWithOneSemitone.contains(i)) {
        semitoneAmount++;
      } else {
        semitoneAmount += 2;
      }
    }

    return semitoneAmount - semitoneToMove + startSemitone;
  }


  private Intervals() {
  }

}
