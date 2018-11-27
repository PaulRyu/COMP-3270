/*
 @author Paul Chong
  *
  * Test cases for Autocomplete.java
  * Submitted: 26 November 2018
  * Course: COMP 3270
  * Institution: Auburn University

 */
import org.junit.Assert;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class AutocompleteTest {

    private Autocomplete.Autocompletor instance;

    /** Fixture initialization (common initialization
     *  for all tests). **/

    /*
     * Set Up
     */
    @Before public void setUp() {
        String[] terms = {"ape", "app", "ban", "bat", "bee", "car", "cat"};
        double[] weights = {6, 4, 2, 3, 5, 7, 1};
        instance = new Autocomplete.TrieAutocomplete(terms, weights);
    }

    /*
     * TopMatch Tests
     */
    @Test public void testTopMatch_000() {
        String query = " ";
        String expected = "";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_001() {
        String query = "";
        String expected = "car";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_002() {
        String query = "a";
        String expected = "ape";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_003() {
        String query = "b";
        String expected = "bee";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_004() {
        String query = "c";
        String expected = "car";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_005() {
        String query = "d";
        String expected = "";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_006() {
        String query = "ap";
        String expected = "ape";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_007() {
        String query = "ba";
        String expected = "bat";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_008() {
        String query = "ca";
        String expected = "car";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatch_009() {
        String query = "cat";
        String expected = "cat";
        String actual = instance.topMatch(query);
        Assert.assertEquals(expected, actual);
    }

    /*
     * TopMatches tests
     */
    @Test public void testTopMatches_010() {
        int k = 8;
        String query = "";
        Iterable<String> expected = Arrays.asList("car", "ape", "bee", "app", "bat", "ban", "cat");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_011() {
        int k = 1;
        String query = "";
        Iterable<String> expected = Collections.singletonList("car");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_012() {
        int k = 2;
        String query = "";
        Iterable<String> expected = Arrays.asList("car", "ape");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_013() {
        int k = 3;
        String query = "";
        Iterable<String> expected = Arrays.asList("car", "ape", "bee");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_014() {
        int k = 1;
        String query = "a";
        Iterable<String> expected = Collections.singletonList("ape");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_015() {
        int k = 1;
        String query = "ap";
        Iterable<String> expected = Collections.singletonList("ape");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_016() {
        int k = 2;
        String query = "b";
        Iterable<String> expected = Arrays.asList("bee", "bat");
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    @Test public void testTopMatches_017() {
        int k = 100;
        String query = "d";
        Iterable<String> expected = Collections.emptyList();
        Iterable<String> actual = instance.topMatches(query, k);
        Assert.assertEquals(expected, actual);
    }

    // Complete: 18 Test Cases.
    //           18 Passed.
    //           0  Failed.
}