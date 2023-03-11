import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.frizzer.music.Intervals;
import java.util.List;
import org.junit.jupiter.api.Test;

class IntervalsTest {

  @Test
  void testIntervalConstructionByNull() {
    assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(null));
  }

  @Test
  void testIntervalConstructionWithIncorrectAmountOfElements() {
    String[] args = {"a", "b", "c", "g"};
    assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(args));
  }

  @Test
  void testIntervalConstructionWithTwoElements() {
    List<String[]> list = List.of(new String[]{"M2", "C"}, new String[]{"P5", "B"},
        new String[]{"m2", "Fb"}, new String[]{"m2", "D#"});
    String[] expectedResult = {"D", "F#", "Gbb", "E"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalConstruction(list.get(i)));
    }
  }

  @Test
  void testIntervalConstructionWithThreeElementsAsc() {
    List<String[]> list = List.of(new String[]{"M2", "C", "asc"}, new String[]{"P5", "B", "asc"},
        new String[]{"m2", "Fb", "asc"}, new String[]{"m2", "D#", "asc"});
    String[] expectedResult = {"D", "F#", "Gbb", "E"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalConstruction(list.get(i)));
    }
  }

  @Test
  void testIntervalConstructionWithThreeElementsDsc() {
    List<String[]> list = List.of(new String[]{"m2", "Bb", "dsc"}, new String[]{"M3", "Cb", "dsc"},
        new String[]{"P4", "G#", "dsc"}, new String[]{"m3", "B", "dsc"});
    String[] expectedResult = {"A", "Abb", "D#", "G#"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalConstruction(list.get(i)));
    }
  }

}
