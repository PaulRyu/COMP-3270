import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;

import javax.swing.JFileChooser;

public class AutocompletorBenchmark {

    public static Random ourRandom = new Random(1234);
    public static final String CHARSET = "UTF-8";
    public static final Locale LOCALE = Locale.US;
    public static Autocomplete.Autocompletor getInstance(String[] words, double[] weights) {
        return new Autocomplete.BinarySearchAutocomplete(words, weights);
    }
    public static Autocomplete.Autocompletor[] getInstances(String[] words, double[] weights) {
        Autocomplete.Autocompletor[] instances = new Autocomplete.Autocompletor[3];
        long startTime = System.nanoTime();
        instances[0] = new Autocomplete.BruteAutocomplete(words, weights);
        System.out.println("Time to initialize BruteAutoComplete - " + (System.nanoTime() - startTime) / 1E9);
        startTime = System.nanoTime();
        instances[1] = new Autocomplete.BinarySearchAutocomplete(words, weights);
        System.out.println("Time to initialize Autocomplete.BinarySearchAutocomplete - " + (System.nanoTime() - startTime) / 1E9);
        startTime = System.nanoTime();
        instances[2] = new Autocomplete.TrieAutocomplete(words, weights);
        System.out.println("Time to initialize - Autocomplete.TrieAutocomplete - " + (System.nanoTime() - startTime) / 1E9);
        return instances;
    }
    // chooser allows users to select a file by navigating through
    // directories
    private static JFileChooser ourChooser = new JFileChooser(System
            .getProperties().getProperty("user.dir"));

    /**
     * Brings up chooser for user to select a file
     * 
     * @return Scanner for user selected file, null if file not found
     */
    public static Scanner getScanner() {
        int retval = ourChooser.showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File f = ourChooser.getSelectedFile();
            Scanner s;
            try {
                if (f.canRead()) {
                    System.out.println("Opening - " +  f.getCanonicalPath() + ".");
                } else {
                    System.out.println("Could not open selected file.");
                    return null;
                }
                s = new Scanner(f, CHARSET);
                s.useLocale(LOCALE);
                
            } catch (IOException e) {
                return null;
            }
            return s;
        }
        return null;
    }

    public static long countNodes(Node root) {
        long result = 1;
        for (Node child : root.children.values())
            result += countNodes(child);
        return result;
    }

        public static void main(String[] args) {
        
        Scanner in = null;
        do {
            in = getScanner();
            
        } while (in == null);
        
        int N = 0;
        String[] terms = null;
        double[] weights = null;
        try {
            N = Integer.parseInt(in.nextLine());
            terms = new String[N];
            weights = new double[N];

            for (int i = 0; i < N; i++) {
                String line = in.nextLine();
                int tab = line.indexOf('\t');
                weights[i] = Double.parseDouble(line.substring(0, tab).trim());
                terms[i] = line.substring(tab + 1).toLowerCase();
            }
        } catch (Exception e) { //could be any parsing related exception
            System.err.println("File is malformatted");
            System.exit(0);
        }
        //Autocomplete.Autocompletor auto = getInstance(terms, weights);
        System.out.println("Found " + N + " words");
        Autocomplete.Autocompletor[] instances = getInstances(terms, weights);
        HashMap<String, Double[]> results = new HashMap<String, Double[]>();
        String randomWord = "";
        while (randomWord.length() <= 2)
            randomWord = terms[ourRandom.nextInt(terms.length)];
        String randomPrefix1 = randomWord.substring(0, 1);
        String randomPrefix2 = randomWord.substring(0, 2);
        String[] queries = { "", randomWord, randomPrefix1, randomPrefix2, "notarealword" };
        for (int instanceNum = 0; instanceNum < 3; instanceNum++) {
            long startTime = System.nanoTime();
            Autocomplete.Autocompletor auto = instances[instanceNum];
            System.out.println("Benchmarking " + auto.getClass().getName() + "...");
            if (auto instanceof Autocomplete.TrieAutocomplete)
                System.out.println("Created " + countNodes(((Autocomplete.TrieAutocomplete) auto).myRoot) + " nodes");
            int trial;
            for (String query : queries) {
                startTime = System.nanoTime();
                for (trial = 0; trial < 1000; trial++) {
                    auto.topMatch(query);
                    if (System.nanoTime() - startTime > 5E9)
                        break;
                }
                double time = (System.nanoTime() - startTime) / (1E9 * trial);
                if (!results.containsKey(query)) results.put(query, new Double[3]);
                results.get(query)[instanceNum] = time;
                System.out.println(
                        "Time for topMatch(\"" + query + "\") - " + time);
            }
            for (String query : queries) {
                for (int k = 1; k <= 7; k += 3) {
                    startTime = System.nanoTime();
                    for (trial = 0; trial < 1000; trial++) {
                        auto.topMatches(query, k);
                        if (System.nanoTime() - startTime > 5E9)
                            break;
                    }
                    double time = (System.nanoTime() - startTime) / (1E9 * trial);
                    if (!results.containsKey(query+"_"+k)) results.put(query+"_"+k, new Double[3]);
                    results.get(query+"_"+k)[instanceNum] = time;
                    System.out.println("Time for topKMatches(\"" + query + "\", " + k + ")" + " - " + " " + time);
                }
            }
        }
        System.out.println("\n*****************************************************************");
        System.out.println("Printing Summary of Results ...");
        System.out.println("\n*****************************************************************");
        System.out.printf("%-15s, %-15s, %-15s, %-15s", 
                "prefix", "Brute", "Binary", "Trie\n");
        System.out.println("-------------------------------------------------------------------");
        for (int instanceNum = 0; instanceNum < 3; instanceNum++) { 
            for (String query : results.keySet()) {
                System.out.printf("%-15s,%15.5e, %15.5e, %15.5e\n", query, results.get(query)[0], results.get(query)[1], results.get(query)[2]);
            }
        }
    }
}
