

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.robotstxt.*;

public class Controller {

	public static TreeMap<String, Integer> SubDomainHash = new TreeMap<String, Integer>();

	/*public static void main(String []args) throws Exception
	{
		Date date = new Date();
    	System.out.println(date.toString());
		//Initializing the stop words Hash
		WordFrequencies.InitializeStopWords();  //Stop words Hash is initialized
		*/
		/*CrawlConfig config = new CrawlConfig();  
		config.setCrawlStorageFolder("C:\\Users\\Gautham\\workspace\\SearchEngine\\src\\Crawler\\CrawledPages");  //Storage Folder
		config.setMaxDepthOfCrawling(50); //Setting the Maximum Depth of crawl to 100
		config.setResumableCrawling(true);
		config.setPolitenessDelay(300);
		//config.setUserAgentString("UCI WebCrawler 25963407 51604754 19121142");
		PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController crawlcontroller = new CrawlController(config, pageFetcher, robotstxtServer);
        crawlcontroller.addSeed("http://www.ics.uci.edu");
        crawlcontroller.start(MyCrawler.class, 5);
        */
		/*
        //File Handles to write to files
        PrintWriter writer2 = new PrintWriter("/Users/PremNishanth/Desktop/JavaEclipse/Files/Frequency.txt");
        PrintWriter writer3 = new PrintWriter("/Users/PremNishanth/Desktop/JavaEclipse/Files/TwoGrams.txt");
        PrintWriter writer4 = new PrintWriter("/Users/PremNishanth/Desktop/JavaEclipse/Files/SubDomains1.txt");

		
        MongoClient mongo = new MongoClient("localhost",27017);
    	//DB db = mongo.getDB("pgsDB");
        DB db = mongo.getDB("newDB");
    	//DBCollection collection = db.getCollection("pgsCollold");
        DBCollection collection = db.getCollection("newColl");   
        //DBCollection indexColl = db.getCollection("indexColl");
        DBCollection indexColl = db.getCollection("indexCollnew");
    	DBCursor iter = collection.find();
    	*/
    	//Finds the Top 500 Frequency words
        /*DBObject unwind = new BasicDBObject("$unwind","$Frequency");
        DBObject group = new BasicDBObject("$group",new BasicDBObject("_id","$Frequency.Word").append("Total",new BasicDBObject("$sum",1)));
        DBObject sort = new BasicDBObject("$sort",new BasicDBObject("Total",-1).append("_id",1));
        //DBObject limit = new BasicDBObject("$limit",100);
        List pipe = new ArrayList();
        pipe.add(unwind);
        pipe.add(group);
        pipe.add(sort);
        //pipe.add(limit);
        
        AggregationOptions agg = AggregationOptions.builder().allowDiskUse(true).build();
        Cursor out = collection.aggregate(pipe, agg);
        */
		/*
    	double count = 0;
    	double tcount= 54120;
    	String DocID;
    	Integer Term_Frequency;
    	iter.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
    	while(iter.hasNext())
    	{
    		try
    		{
    		count++;
    		if(count%100 == 0)
    		{
    			System.out.println("Percentage:  " + (count/tcount)*100);
    		}
    		HashMap<String,ArrayList<Integer>> HashPos = new HashMap<String,ArrayList<Integer>>();
    		DBObject holder;
    		holder = iter.next();	
    		ArrayList<String> Terms = new ArrayList<String>();
    		Terms = (ArrayList<String>) holder.get("List");
    		int Term_Size = Terms.size();
    		for(int i=0;i<Term_Size;i++)
    		{
    			if(Terms.get(i).toString().length() < 1024)
    			{
    				ArrayList<Integer> TempList = new ArrayList<Integer>();
    				if(HashPos.containsKey(Terms.get(i)))
    				{
    					TempList = HashPos.get(Terms.get(i));
    				}
    				TempList.add(i+1);
    				HashPos.put(Terms.get(i), TempList);
    			}
    		}
    		Iterator HashIter = HashPos.entrySet().iterator();
    		while(HashIter.hasNext())
    		{
    			ArrayList<DBObject> Temp = new ArrayList<DBObject>();
    			DocID = holder.get("URL").toString();
    			BasicDBObject inner_object = new BasicDBObject().append("DocID", DocID);
    			Map.Entry<String,ArrayList<Integer>> HashPair = (Map.Entry<String,ArrayList<Integer>>)HashIter.next();
    			ArrayList<Integer> Term_Positions = HashPair.getValue(); //List to hold the term positions in a document
    			Term_Frequency = Term_Positions.size();
    			inner_object.append("Positions", Term_Positions);
        		inner_object.append("TermFrequency", 1+Math.log(Term_Frequency));
        		Temp.add(inner_object);
					// BasicDBObject update_object = new BasicDBObject("$set",
					// new BasicDBObject().append("_id",
					// HashPair.getKey())).append("$push", new
					// BasicDBObject("Details",inner_object));
        		//BasicDBObject update_object = new BasicDBObject("$set", new BasicDBObject().append("_id", HashPair.getKey())).append("$push", new BasicDBObject("Details",new BasicDBObject("$each",Temp).append("$sort", new BasicDBObject("TermFrequency",-1))));
        		BasicDBObject update_object = new BasicDBObject("$set", new BasicDBObject().append("_id", HashPair.getKey())).append("$inc", new BasicDBObject("DocCount",1)).append("$push", new BasicDBObject("Details",new BasicDBObject("$each",Temp).append("$sort", new BasicDBObject("TermFrequency",-1))));
        		indexColl.update(new BasicDBObject("_id",HashPair.getKey()), update_object,true,false);
    		}
    		}
    		catch(Exception ex)
    		{
    			System.out.println("Exception occurred:  "+ex.getMessage());
    			
    		}
    	}
    	
    	DBCursor iter1 = indexColl.find();
    	double Total = iter1.count();
    	while(iter1.hasNext())
    	{
    		DBObject holder;
    		holder = iter1.next();
    		indexColl.update(new BasicDBObject("_id",holder.get("_id")), new BasicDBObject("$set",new BasicDBObject("IDF",Math.log(Total/(Integer)holder.get("DocCount")))),true,false);
    	}
    	
    	*/
    	
    	
    	
    	
    	
         /*
        //Finds the Top 20 2gram words
        DBObject unwind1 = new BasicDBObject("$unwind","$TwoGramFrequency");
        DBObject group1 = new BasicDBObject("$group",new BasicDBObject("_id","$TwoGramFrequency.TwogramWord").append("Total",new BasicDBObject("$sum",1)));
        DBObject sort1 = new BasicDBObject("$sort",new BasicDBObject("Total",-1).append("_id",1));
        DBObject limit1 = new BasicDBObject("$limit",20);
        AggregationOptions agg1 = AggregationOptions.builder().allowDiskUse(true).build();
        List pipe1 = new ArrayList();
        pipe1.add(unwind1);
        pipe1.add(group1);
        pipe1.add(sort1);
        pipe1.add(limit1);
        Cursor out1 = collection.aggregate(pipe1, agg1);
        
        while(out1.hasNext())
        {
        	writer3.println(out1.next().toString());
        }
        
        //Finding list of Subdomains
        DBObject query = new BasicDBObject("URL",Pattern.compile("\\w+\\.ics\\.uci\\.edu/?$"));
        DBObject sortbySubDomain = new BasicDBObject("URL",1);
        DBObject fields = new BasicDBObject("Pages",1).append("URL", 1).append("_id", 0);
        DBCursor iter1 = collection.find(query,fields).sort(sortbySubDomain);
        
        //while(iter1.hasNext())
        //{
        //	writer4.println(iter1.next());
        //}
        
        System.out.println("Max Word Count: " + iter.next().get("WordCount"));
        System.out.println("Number of Unique URLs :    " + collection.count());
        System.out.println("SubDomain Count : " + SubDomainHash.size());
        
        DBObject query1 = new BasicDBObject("URL",Pattern.compile("\\w+\\.ics\\.uci\\.edu/\\w+/?"));
        DBObject fields1 = new BasicDBObject("Pages",1).append("URL", 1).append("_id", 0);
        iter1 = collection.find(query1, fields1);
        
        TreeMap<String,Integer> HH = new TreeMap<String,Integer>();
        String Temp,Subd;
        Pattern p = Pattern.compile("(\\w+)\\.ics\\.uci\\.edu/(.+)?"); //Compiling the Pattern
		Matcher match;
        while(iter1.hasNext())
        {
        	Temp = iter1.next().get("URL").toString();
        	match = p.matcher(Temp);
        	if(match.find())
        	{
        		Subd = match.group(1);
        		if(!HH.containsKey(Subd))
            	{
            		HH.put(Subd, 1);
            	}
        		else
        		{
        			HH.put(Subd,HH.get(Subd)+1);
        		}
        	}
        }
        System.out.println(HH.size());
        
        for(String key : HH.keySet())
        {
        	//System.out.println("http://" + key + ".ics.uci.edu" + ":" + HH.get(key));
        	writer4.println("http://" + key + ".ics.uci.edu" + "            :             " + HH.get(key));
        }
        
        
        //Closing data and file handles
    	mongo.close();
    	writer2.close();
    	writer3.close();
    	writer4.close();
    	System.out.println(date.toString());
    	*/
		
		//Indexing Part
		//SEngine.Search();
	/*}*/
}
