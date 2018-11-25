import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 *
 * @author Austin Lu
 * @author Jeff Forbes
 */
public class TrieAutocomplete implements Autocomplete.Autocompletor {

    /**
     * Root of entire trie
     */
    protected Node myRoot;
    private int flag = 0;

    /**
     * Constructor method for TrieAutocomplete. Should initialize the trie
     * rooted at myRoot, as well as add all nodes necessary to represent the
     * words in terms.
     *
     * @param terms
     *            - The words we will autocomplete from
     * @param weights
     *            - Their weights, such that terms[i] has weight weights[i].
     * @throws NullPointerException
     *             if either argument is null
     * @throws IllegalArgumentException
     *             if terms and weights are different weight
     */
    public TrieAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null)
            throw new NullPointerException("One or more arguments null");
        if (terms.length != weights.length)
            throw new IllegalArgumentException("terms and weights are not the same length");
        HashSet<String> words = new HashSet<String>();
        // Represent the root as a dummy/placeholder node
        myRoot = new Node('-', null, 0);

        for (int i = 0; i < terms.length; i++) {
            if (words.contains(terms[i]))
                throw new IllegalArgumentException("Duplicate input terms " + terms[i]);
            words.add(terms[i]);
            add(terms[i], weights[i]);
        }
        if (words.size() != terms.length)
            throw new IllegalArgumentException("Duplicate input terms");
        if (terms.length == 0)
            flag = 1;
    }

    /**
     * Add the word with given weight to the trie. If word already exists in the
     * trie, no new nodes should be created, but the weight of word should be
     * updated.
     *
     * In adding a word, this method should do the following: Create any
     * necessary intermediate nodes if they do not exist. Update the
     * subtreeMaxWeight of all nodes in the path from root to the node
     * representing word. Set the value of myWord, myWeight, isWord, and
     * mySubtreeMaxWeight of the node corresponding to the added word to the
     * correct values
     */
    private void add(String word, double weight) {
        // TODO: Implement add
        // Find the node (Creating the new Nodes where necessary) for word
        if (word == null)
            throw new NullPointerException("Word is null " + word);
        if (weight < 0)
            throw new IllegalArgumentException("Negative weight "+ weight);
        Node current = myRoot;
        for (char ch:word.toCharArray()){
            if (current.mySubtreeMaxWeight < weight)
                current.mySubtreeMaxWeight = weight;
            if (!current.children.containsKey(ch))
                current.children.put(ch, new Node(ch, current, weight));
            current = current.getChild(ch);
        }
        current.setWeight(weight);
        current.isWord = true;
        current.setWord(word);
//		System.out.println(word + ": " + weight);
    }

    /**
     * Required by the Autocompletor interface. Returns an array containing the
     * k words in the trie with the largest weight which match the given prefix,
     * in descending weight order. If less than k words exist matching the given
     * prefix (including if no words exist), then the array instead contains all
     * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
     * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
     * 2) should return {"air"}
     *
     * @param prefix
     *            - A prefix which all returned words must start with
     * @param k
     *            - The (maximum) number of words to be returned
     * @return An Iterable of the k words with the largest weights among all
     *         words starting with prefix, in descending weight order. If less
     *         than k such words exist, return all those words. If no such words
     *         exist, return an empty Iterable
     */
    public Iterable<String> topMatches(String prefix, int k) {
        // TODO: Implement topKMatches
        if (prefix == null)
            throw new NullPointerException("Prefix is null");
        if (k < 0)
            throw new IllegalArgumentException("Illegal value of k:"+k);
        if (k == 0 || flag == 1)
            return new LinkedList<String>();

        // maintain pq of size k
        Node current = myRoot;
        PriorityQueue<Node> pq = new PriorityQueue<Node>(k, new Node.ReverseSubtreeMaxWeightComparator());
        LinkedList<String> L = new LinkedList<String>();
        for (char ch : prefix.toCharArray()){
            if (current.children.containsKey(ch))
                current = current.getChild(ch);
            else return L;
        }
        pq.add(current);
        while (pq.size() > 0 && L.size() <= k){
//			System.out.println(pq.size());
            current = pq.poll();
            if (current.isWord){
                L.add(current.myWord);
                if (L.size() >= k)
                    break;
            }
            for(Node child : current.children.values())
                pq.add(child);
        }
//		int numResults = Math.min(k, pq1.size());
//		for (int i = 0; i < numResults; i++)
//			L.addLast(pq1.poll().getWord());
//		return L;
        if (L.size() <= k)
            return L;
        else return L.subList(0, k);
    }

    /**
     * Given a prefix, returns the largest-weight word in the trie starting with
     * that prefix.
     *
     * @param prefix
     *            - the prefix the returned word should start with
     * @return The word from with the largest weight starting with prefix, or an
     *         empty string if none exists
     *
     */
    public String topMatch(String prefix) {
        if (prefix == null)
            throw new NullPointerException("null error");
        Node currentNode = myRoot;
        for (char i : prefix.toCharArray()){
            if (currentNode.children.containsKey(i))
                currentNode = currentNode.getChild(i);
            else
                return "";
        }

        double highestNode = currentNode.mySubtreeMaxWeight;

        if (currentNode.myWeight == highestNode)
            return currentNode.myWord;

        while(currentNode.myWeight != highestNode){
            for (Node child : currentNode.children.values())
                if(child.mySubtreeMaxWeight == highestNode){
                    currentNode = child;
                    break;
                }
        } return currentNode.myWord;
    }

    /**
     * Return the weight of a given term. If term is not in the dictionary,
     * return 0.0
     */
    public double weightOf(String prefix) {
        // TODO complete weightOf
        Node current = myRoot;
        for (char ch:prefix.toCharArray()){
            if (current.children.containsKey(ch))
                current = current.getChild(ch);
            else
                return 0.0;
        }
        if (current.isWord)
            return current.myWeight;
        else
            return 0.0;
    }

    /**
     * Optional: Returns the highest weighted matches within k edit distance of
     * the word. If the word is in the dictionary, then return an empty list.
     *
     * @param word
     *            The word to spell-check
     * @param dist
     *            Maximum edit distance to search
     * @param k
     *            Number of results to return
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        return null;
    }
}