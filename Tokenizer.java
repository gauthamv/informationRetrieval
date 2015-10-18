
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tokenizer {
	
	public static ArrayList<String> tokenizeFile(String tokenizer) throws IOException
	{
		String pattern = "[a-zA-Z0-9]+"; // Setting the String Pattern
		Pattern r = Pattern.compile(pattern); //Compiling the Pattern
		ArrayList<String> AList = new ArrayList<String>();
		tokenizer = tokenizer.toLowerCase();
		Matcher match = r.matcher(tokenizer); //Stores the matched expression from tokenizer in match
		while(match.find())
		{
			AList.add(match.group()); //Tokenizing all words except stop words
		}
		return AList;
	}
	public static void Print(ArrayList<String> Tokens) //Function to print the list of tokens
	{
		if(Tokens.size()!=0)
		{
		System.out.println(Tokens);
		}
		else
		{
			System.out.println("No Valid Tokens are present in the Text File");
		}
	}
}
