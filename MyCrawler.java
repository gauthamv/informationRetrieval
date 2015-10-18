
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.UnknownHostException;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler {
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
            + "|rm|smil|wmv|swf|wma|zip|rar|gz|data))$");
	
@Override
public boolean shouldVisit(Page page,WebURL url) {
String href = url.getURL().toLowerCase();

 	
return !FILTERS.matcher(href).matches() && href.matches("http(s)?://(www\\.)?(\\w+\\.)?ics\\.uci\\.edu(/)?(.+)?") && !href.startsWith("http://archive.ics.uci.edu/") && !href.startsWith("http://calender.ics.uci.edu/") && !href.startsWith("https://calender.ics.uci.edu/") && !href.startsWith("https://duttgroup.ics.uci.edu/") && !href.startsWith("http://wics.ics.uci.edu/"); 
}

@Override
public void visit(Page page) {

String url = page.getWebURL().getURL();
System.out.println("URL: " + url);

if (page.getParseData() instanceof HtmlParseData) {
HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
String text = htmlParseData.getText();
ArrayList<String> Alist = new ArrayList<String>();
try {
	Alist = Tokenizer.tokenizeFile(text);
} catch (IOException e) {
	e.printStackTrace();
}

//Computing the word Frequencies
HashMap<String,Integer>Map = new HashMap<String,Integer>();
Map = WordFrequencies.computeWordFrequencies(Alist);
StringBuilder Hmap = new StringBuilder();  //Used to store hash map vaues in json format
Hmap.append("["); 
for(String temp:Map.keySet())
{
	Hmap.append("{").append("'").append("Word").append("'").append(":").append("'").append(temp).append("'").append(",").append("'").append("Count").append("'").append(":").append("'").append(Map.get(temp)).append("'").append("}").append(",");
}
Hmap.deleteCharAt(Hmap.length()-1);
Hmap.append("]");

//Computing the 2grams
HashMap<String,Integer>Map2grams = new HashMap<String,Integer>();
Map2grams = WordFrequencies.computeTwoGramFrequencies(Alist);
StringBuilder Hmap2grams = new StringBuilder();  //Used to store hash map vaues in json format
Hmap2grams.append("["); 
for(String temp:Map2grams.keySet())
{
	Hmap2grams.append("{").append("'").append("TwogramWord").append("'").append(":").append("'").append(temp).append("'").append(",").append("'").append("Count").append("'").append(":").append("'").append(Map2grams.get(temp)).append("'").append("}").append(",");
}
Hmap2grams.deleteCharAt(Hmap2grams.length()-1);
Hmap2grams.append("]");


String htmldata = htmlParseData.getHtml();
Set<WebURL> links = htmlParseData.getOutgoingUrls();

//Finding the sub-domains
String pattern = "https?://(www\\.)?(\\w+)\\.ics\\.uci\\.edu(/)?(.+)?";
Pattern pat = Pattern.compile(pattern);
Matcher match = pat.matcher(url);
int subDomain = 0; //USed to indicate if url is subdomain or not
if(match.matches() && (!match.group(2).equals("www")))
{
	subDomain = 1; //Indicates that this url is a sub domain 
}

//Creating the MongoDb object
try {
	MongoClient mongo = new MongoClient("localhost",27017);
	DB db = mongo.getDB("SearchEngineDB");
	DBCollection collection = db.getCollection("myColl");
	String json = "{"+"'URL'"+":"+"'"+url+"'"+","+"'List'"+":"+"'"+Alist+"'"+","+"'WordCount'"+":"+"'"+Alist.size()+"'"+","+"'SubDomain'"+":"+"'"+subDomain+"'"+","+"'Pages'"+":"+"'"+links.size()+"'"+","+"'Frequency'"+":"+Hmap+","+"'TwoGramFrequency'"+":"+Hmap2grams+"}";
	BasicDBObject dbObject = (BasicDBObject) JSON.parse(json);
	collection.insert(dbObject);
	DBCollection collection2 = db.getCollection("myColl2");	
	BasicDBObject dbObject2 = new BasicDBObject("URL", url).append("HTML", htmldata).append("Text",text);
	collection2.insert(dbObject2);
	mongo.close();
} catch (MongoException e) {
	e.printStackTrace();
} catch (UnknownHostException e) {
	e.printStackTrace();
} 

}
}

}
