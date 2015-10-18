

import java.io.*;
import java.util.*;
import java.util.regex.*;
public class TextProcessor {
   
	public static void main(String []args) throws IOException
	{
		ArrayList<String> Tokens = new ArrayList<String>(); //Used to Store the List of Tokens
		Tokens = Tokenizer.tokenizeFile(args[0]); //Used to tokenize the given text file
		Tokenizer.Print(Tokens);			//Used to print the list of tokens
		HashMap<String,Integer> Pairs = new HashMap<String,Integer>();
		Pairs = WordFrequencies.computeWordFrequencies(Tokens);
		WordFrequencies.Print(Pairs);
		HashMap<String,Integer> TwogramPairs = new HashMap<String,Integer>(); //USed to Compute TwoGram Word Frequencies
		TwogramPairs = WordFrequencies.computeTwoGramFrequencies(Tokens);
		WordFrequencies.PrintTwograms(TwogramPairs); //Used to print the TwoGramWord Frequencies in Descending Order
		HashMap<String,Integer> Palindromes = new HashMap<String,Integer>(); //USed to Compute Palindromes
		//Palindromes = Palindrome.computePalindromeFrequencies(Tokens);
		//Palindrome.PrintPalindrome(Palindromes);
	}
}

class MyComparator implements Comparator<Map.Entry<String, Integer>>{
	public MyComparator()
	{
	}
	public int compare(Map.Entry<String,Integer> one,Map.Entry<String,Integer> two)
	{
	   return two.getValue().compareTo(one.getValue());
	}	
}

