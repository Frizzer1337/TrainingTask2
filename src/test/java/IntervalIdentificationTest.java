import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.frizzer.music.Intervals;
import java.util.List;
import org.junit.jupiter.api.Test;

class IntervalIdentificationTest {

  @Test
  void testIntervalIdentificationByNull() {
    assertThrows(IllegalArgumentException.class, () -> Intervals.intervalIdentification(null));
  }

  @Test
  void testIntervalIdentificationWithIncorrectAmountOfElements() {
    String[] args = {"a", "b", "c", "g"};
    assertThrows(IllegalArgumentException.class, () -> Intervals.intervalIdentification(args));
  }

  @Test
  void testIntervalIdentificationWithTwoElements() {
    List<String[]> list = List.of(new String[]{"C", "D"}, new String[]{"B", "F#"},
        new String[]{"Fb", "Gbb"}, new String[]{"G", "F#"});
    String[] expectedResult = {"M2", "P5", "m2", "M7"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalIdentification(list.get(i)));
    }
  }

  @Test
  void testIntervalIdentificationWithThreeElementsAsc() {
    List<String[]> list = List.of(new String[]{"C", "D","asc"}, new String[]{"B", "F#","asc"},
        new String[]{"Fb", "Gbb","asc"}, new String[]{"G", "F#","asc"});
    String[] expectedResult = {"M2", "P5", "m2", "M7"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalIdentification(list.get(i)));
    }
  }

  @Test
  void testIntervalIdentificationWithThreeElementsDsc() {
    List<String[]> list = List.of(new String[]{"Bb", "A", "dsc"}, new String[]{"Cb", "Abb", "dsc"},
        new String[]{"G#", "D#", "dsc"}, new String[]{"E", "B", "dsc"});
    String[] expectedResult = {"m2", "M3", "P4", "P4"};
    for (int i = 0; i < list.size(); i++) {
      assertEquals(expectedResult[i], Intervals.intervalIdentification(list.get(i)));
    }
  }

}
