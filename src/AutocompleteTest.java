import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

//import java.util.List;
import java.util.Arrays;

public class AutocompleteTest {
   
   Autocomplete.Autocompletor instance;

   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
      String[] terms = {"ape", "app", "ban", "bat", "bee", "car", "cat"};
      double[] weights = {6, 4, 2, 3, 5, 7, 1};
      instance = new Autocomplete.TrieAutocomplete(terms, weights);
   }
   
   @Test public void testTopMatch1() {
      String query = "";
      String expected = "car";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch2() {
      String query = "a";
      String expected = "ape";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch3() {
      String query = "ap";
      String expected = "ape";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch4() {
      String query = "b";
      String expected = "bee";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch5() {
      String query = "ba";
      String expected = "bat";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch6() {
      String query = "c";
      String expected = "car";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch7() {
      String query = "ca";
      String expected = "car";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch8() {
      String query = "cat";
      String expected = "cat";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch9() {
      String query = "d";
      String expected = "";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatch10() {
      String query = " ";
      String expected = "";
      String actual = instance.topMatch(query);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches1() {
      String query = "";
      /* 
       * Used https://www.geeksforgeeks.org/convert-an-iterable-to-collection-in-java/
       * To see how to create an Iterable with values
      */
      Iterable<String> expected = Arrays.asList("car", "ape", "bee", "app", "bat", "ban", "cat");
      int k = 8;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches2() {
      String query = "";
      Iterable<String> expected = Arrays.asList("car");
      int k = 1;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches3() {
      String query = "";
      Iterable<String> expected = Arrays.asList("car", "ape");
      int k = 2;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches4() {
      String query = "";
      Iterable<String> expected = Arrays.asList("car", "ape", "bee");
      int k = 3;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);      
   }
   
   @Test public void testTopMatches5() {
      String query = "a";
      Iterable<String> expected = Arrays.asList("ape");
      int k = 1;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches6() {
      String query = "ap";
      Iterable<String> expected = Arrays.asList("ape");
      int k = 1;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches7() {
      String query = "b";
      Iterable<String> expected = Arrays.asList("bee", "bat");
      int k = 2;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches8() {
      String query = "ba";
      Iterable<String> expected = Arrays.asList("bee", "bat");
      int k = 2;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
   
   @Test public void testTopMatches9() {
      String query = "d";
      Iterable<String> expected = Arrays.asList();
      int k = 100;
      Iterable<String> actual = instance.topMatches(query, k);
      Assert.assertEquals(expected, actual);
   }
}