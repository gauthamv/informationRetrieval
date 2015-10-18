
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class WordFrequencies {
	
	public static HashMap<String,Integer> StopWords = new HashMap();
	public static void InitializeStopWords() throws IOException
	{
		BufferedReader bhandle = new BufferedReader(new FileReader("/Users/PremNishanth/Desktop/JavaEclipse/Files/StopWords.txt"));
		String Temp; //Stores the temporary string read from the stop words file
		//Storing the stop words in the hash map
		while((Temp=bhandle.readLine())!=null)
		{
			if(!(StopWords.containsKey(Temp)))
			{
				StopWords.put(Temp, 1);
			}
		}
	}
	
	public static HashMap<String,Integer> computeWordFrequencies(ArrayList<String> Tokens)
	{
		Iterator<String> iter = Tokens.iterator(); //Creating Iterator to go through elements of the list Tokens
		HashMap<String,Integer> Map = new HashMap<String,Integer>();
		String temp = new String(); //Temporary variable to store the list
		while(iter.hasNext())
		{
			temp = iter.next();
			if(!(StopWords.containsKey(temp)))
			{
				if(!(Map.containsKey(temp)))
				{
					Map.put(temp, 1);
				}
				else
				{
					Map.put(temp, Map.get(temp)+1);
				}
			}
		}
		return Map;
	}
    public static void Print(HashMap<String,Integer> map)
	{ 
		MyComparator V = new MyComparator(); //Constructor for MyComparator
		Map<String,Integer> M1 = map;   //Copying Hashmap elements to map
		ArrayList List1 = new ArrayList(M1.entrySet()); //Creating a List of Map entry sets  
		Collections.sort(List1,V);
		if(!(List1.isEmpty()))
		{
		   System.out.println(List1);
		}
		else
		{
			System.out.println("No Valid Tokens are present in the Text File");
		}
	}
    public static HashMap<String,Integer> computeTwoGramFrequencies(ArrayList<String> Tokens)
    {
    	Iterator<String> iter = Tokens.iterator(); //Creating Iterator to go through elements of the list Tokens
    	HashMap<String,Integer> Hmap = new HashMap<String,Integer>(); 
    	if(Tokens.isEmpty())
    	{
    	    return Hmap;	
    	}
    	String Prev_element = iter.next(); //Stores the previous element with respect to the current element 
    	String Current_element; //Stores the current element
    	int count = 0; //Stores the total number of 2 gram words
    	while(iter.hasNext())
    	{
    		Current_element = iter.next(); 
    		count++;
    		if(!(StopWords.containsKey(Prev_element)) && (!(StopWords.containsKey(Current_element))))
    		{
    			if(!(Hmap.containsKey(Prev_element+Current_element)))
    			{
    				Hmap.put(Prev_element+Current_element, 1);
    			}
    			else
    			{
    				Hmap.put(Prev_element+Current_element, Hmap.get(Prev_element+Current_element)+1);
    			}
    		}
    		Prev_element = Current_element;
    	}
    	//System.out.println(count); // Prints the value of count of 2 gram words
    	return Hmap;
    }
    public static void PrintTwograms(HashMap<String,Integer> TwogramPairs)
    {
    	Map<String,Integer> map = TwogramPairs;
    	MyComparator M1 = new MyComparator();
    	ArrayList List1 = new ArrayList(map.entrySet());
    	Collections.sort(List1, M1);
    	if(!(List1.isEmpty()))
    	{
    	   System.out.println(List1);
    	}
    	else
    	{
    	   System.out.println("No Valid 2 gram words are present in the Text File");
    	}
    }
}
