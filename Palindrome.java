
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Palindrome {

	public static boolean CheckPalindrome(String querry)
    {
    	int i=0,j=querry.length()-1;
    	int flag = 1; //To keep track of palindrome checking
    	while(i<j)
    	{
    		if(querry.charAt(i) != querry.charAt(j))
    		{
    			flag = 0;
    			break;
    		}
    		i++;
    		j--;
    	}
    	if(flag == 0)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    public static HashMap<String,Integer> computePalindromeFrequencies(ArrayList<String> Tokens)
    {
    	int Tokensize = Tokens.size();
    	String Temp = ""; //String which is passed to palindrome check function
    	HashMap<String,Integer> Hmap = new HashMap<String,Integer>();
    	for(int i=0;i<Tokensize;i++)
    	{
    		Temp = "";
    		for(int j=i;j<Tokensize;j++)
    		{
    			Temp = Temp + Tokens.get(j);
    			if(CheckPalindrome(Temp))
    			{
    				if(!(Hmap.containsKey(Temp)))
    				{
    					Hmap.put(Temp, 1);
    				}
    				else
    				{
    					Hmap.put(Temp, Hmap.get(Temp)+1);
    				}
    			}
    		}
    	}
    	return Hmap;
    }
    public static void PrintPalindrome(HashMap<String,Integer> Palindromes)
    {
    	MyComparator M1 = new MyComparator();
    	Map<String,Integer> map = Palindromes;
    	ArrayList List1 = new ArrayList(map.entrySet());
    	Collections.sort(List1, M1);
    	if(List1.isEmpty())
    	{
    	  	System.out.println("No Palindromes Present");
    	}
    	else
    	{
    		System.out.println(List1);
    	}
    }
}
