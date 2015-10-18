

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class SEngine {
	
  public static ArrayList<String> Query = new ArrayList<String>();
  public static ArrayList<String> QueryCopy = Query;
 
  static Comparator<Map.Entry<String,Double>> newComp= new Comparator<Map.Entry<String,Double>>()
			{
			    public int compare(Map.Entry<String,Double> entry1, Map.Entry<String,Double> entry2)
			    {
			    	return (entry2.getValue()).compareTo(entry1.getValue());
			    }
			};
			
	public static int CheckDuplicates(String url)
	{
	  Pattern pat = Pattern.compile("(.+)(index\\.php|index\\.html|index)$");
	  Matcher match = pat.matcher(url);
	  //Pattern pat2 = Pattern.compile("^https://.+");
	  //Matcher match2 = pat2.matcher(url);
	  if(match.find() && !(url.contains("lopes")))// || match2.find())
	  {
	   return 1;
	  }
	  else
	  {
	   return 0;
	  }
	}		
			
   public static StringBuilder Search() throws UnknownHostException
   {
	   StringBuilder finalOutput = new StringBuilder();
	   finalOutput.append("<html><body>");
	   //Query.add("mondego");
	   //Query.add("retrieval");
	   //SearchEngineIR.OutputBox.setText("HEYA!");
	   //System.out.println(Query);
	   String QueryWord = new String(); //Word used for searching for relevant text while displaying
	   for(String temp1:Query)
	   {
		   if(!WordFrequencies.StopWords.containsKey(temp1))
		   {
			   QueryWord = temp1;
			   break;
		   }
	   }
	   MongoClient mongo = new MongoClient("localhost",27017);
       DB db = mongo.getDB("newDB"); //Db which contains the indices
       DB db_text = mongo.getDB("newDB");
       DBCollection indexColl = db.getCollection("indexCollnew");
       DBCollection textColl = db_text.getCollection("newColl");
       DB db_html = mongo.getDB("pgsDB");
       DBCollection htmlColl = db_html.getCollection("pgsColl");
       HashMap<String,Double> DocHash = new HashMap<String,Double>();
       for(String temp:Query)
       {
    	   DBCursor iter = indexColl.find(new BasicDBObject("_id",temp));
    	   while(iter.hasNext())
    	   {
    		   DBObject TermHolder = iter.next();
    		   Double IDF = (Double) TermHolder.get("IDF");
    		   ArrayList<DBObject> iter_docs = (ArrayList) TermHolder.get("Details");
    		   Iterator temp2 = iter_docs.iterator();
    		   while(temp2.hasNext())
    		   {
    			   DBObject holder = (DBObject) temp2.next();
    			   String TempDocID = holder.get("DocID").toString();
    			   DBCursor title_iter = htmlColl.find(new BasicDBObject("URL",TempDocID)); //The HTML code for Required URL is got
    			   //System.out.println(title_iter);
    			   Double Title_Score = 0.0; // Score if query terms are there in the title
    			   while(title_iter.hasNext()) //Code to find title score, if query term is present in title tag  
    			   {
    				   //System.out.println("in while");
    				   String HTML_container = title_iter.next().get("HTML").toString();
    				   Pattern pat_html = Pattern.compile("<title>(.+)</title>");
    				   Matcher match_html = pat_html.matcher(HTML_container);
    				   String Title = new String();
    		    	   if(match_html.find())
    		    	   {
    		    		   Title = match_html.group(1).toLowerCase();
    		    	   }
    				   for(String tempcopy:QueryCopy)
    				   {  
    					  if(Title.contains(tempcopy))
    					   {
    						  //System.out.println("matches");
    						  Title_Score += 15.0;
    						  //break;
    					  }
    				   }
    			   }
    			   Double TF_IDF = (Double) holder.get("TermFrequency")*IDF;
    			   if(!(DocHash.containsKey(TempDocID)))
    			   {
    				   DocHash.put(TempDocID, TF_IDF +Title_Score);
    			   }
    			   else
    			   {
    				   DocHash.put(TempDocID, DocHash.get(TempDocID) + TF_IDF + Title_Score);
    			   }
    		   }
    	   } 
       }
       ArrayList list = new ArrayList(DocHash.entrySet());
       Collections.sort(list, newComp);
       System.out.println(list);
       
      
       //Code to Display the relevant content
       Pattern pat = Pattern.compile("(.+)=\\d+\\.\\d+$"); //Pattern to remove the value part from the URL
       Iterator list_iter = list.iterator();
       int limit = 5;  //limit is for displaying top k documents alone
       while(list_iter.hasNext() && (limit > 0))
       {
    	   String url_holder = list_iter.next().toString();
    	   Matcher match = pat.matcher(url_holder);
    	   if(match.find())
    	   {
    		   //System.out.println(match.group(1));
    		   if(CheckDuplicates(match.group(1)) == 1)
    			   continue;
    		   System.out.println(match.group(1));
    		  ArrayList<String> Text = (ArrayList<String>) textColl.find(new BasicDBObject("URL",match.group(1))).next().get("List");
    		  HashMap<String,Integer> CheckUrlExist = new HashMap<String,Integer>();
    		  for(String temp_query:QueryCopy)
    		  {  
    		  QueryWord = temp_query;	  
    		  ArrayList<DBObject> Pos_List = (ArrayList<DBObject>) indexColl.find(new BasicDBObject("_id",QueryWord)).next().get("Details");
    		  Iterator Pos_iter = Pos_List.iterator();
    		  while(Pos_iter.hasNext())
    		  {
    			  DBObject Pos_Holder = (DBObject) Pos_iter.next();
    			  if(Pos_Holder.get("DocID").toString().equals(match.group(1)))
    			  {
    				  if(CheckUrlExist.containsKey(match.group(1)))
    				  {
    					  break;
    				  }
    				  else
    				  {
    					  CheckUrlExist.put(match.group(1), 1);
    				  }
    				  ArrayList<Integer> Text_Positions = (ArrayList) Pos_Holder.get("Positions");
    				  finalOutput.append("<a href="+match.group(1)+">"+match.group(1)+"</a>");
    				  finalOutput.append("<br>");
    				  //SearchEngineIR.OutputBox.setText(match.group(1));
    				  //SearchEngineIR.OutputBox.setText("\n");
    				  
    				  if((Text_Positions.get(0) >= 10) && (Text_Positions.get(0) <= Text.size()-10))
    				  {
    					  for(int i=Text_Positions.get(0)-9;i<=Text_Positions.get(0)+9;i++)
    					  {
    	    				  finalOutput.append(Text.get(i));
    	    				  finalOutput.append(" ");
    	    				  //finalOutput.append("\n");

    						  //SearchEngineIR.OutputBox.setText(Text.get(i) + " ");
    						  //SearchEngineIR.OutputBox.setText("\n");
    					  }
    				  }
    				  else if(Text_Positions.get(0) < 10)
    				  {
    					  for(int i=0;i<10;i++)
    					  {
    	    				  finalOutput.append(Text.get(i));
    	    				  finalOutput.append(" ");
    	    				  //finalOutput.append("\n");
    					  }
    				  }
    				  else if(Text_Positions.get(0) > (Text.size()-10))
    				  {
    					  for(int i=Text.size()-10;i<Text.size();i++)
    					  {
    	    				  finalOutput.append(Text.get(i));
    	    				  finalOutput.append(" ");
    	    				 // finalOutput.append("\n");
    					  }
    				  }
    				  finalOutput.append("<br>");
    				  finalOutput.append("<br>");
    				 // finalOutput.append("\n");

    			  }		  
    		  }
    		  } 	  
    		     
    	   }
    	   limit--; //decrements the limit
       }
       finalOutput.append("</body></html>");
       return finalOutput;
   }
    
}



