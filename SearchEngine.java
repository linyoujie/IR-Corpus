
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.List;
import java.util.HashMap;


/**
 * @author Youjie Lin   
 */
public class SearchEngine {
    
    public static void main(String[] args) throws IOException
    {
        int k = 0, j;
        String arg;
        String resultStr = ""; 
        String CorpusDir ="./RawHtml";
        String InvertedIndex="./InvertedIndex.txt";
        String StopList="./StopList.txt";
        String Queries="./query.txt"; 
        String Results="./result.txt";
        String textCorpus = "false";
        String inversedIn = "false";

        while (k < args.length && args[k].startsWith("-")) {
            arg = args[k++];

            if (arg.equals("-Results")) {
                if (k < args.length)
                    Results = args[k++];
                else
            
                    System.out.println("-Results requires a filename");
            }

            else if (arg.equals("-CorpusDir")) {
                if (k < args.length)
                    CorpusDir = args[k++];
                else
                   
                    System.out.println("-CorpusDir requires a directory");
            }
            else if (arg.equals("-InvertedIndex")) {
                if (k < args.length)
                    InvertedIndex = args[k++];
                else
                    System.out.println("-InvertedIndex with no file, system will generate it from CorpusDir");
            }
            else if (arg.equals("-StopList")) {
                if (k < args.length)
                    StopList = args[k++];
                else
                    System.out.println("-StopList requires a filename");
            }

            else if (arg.equals("-Queries")) {
                if (k < args.length)
                    Queries = args[k++];
                else
                    System.out.println("-Queries requires a filename");
            }

            else if (arg.equals("-inversedIn")) {
                if (k < args.length)
                    inversedIn = args[k++];
                else
                    System.out.println("-inversedIn requires a boolean");
            }

        }

        if (k == args.length)
            System.out.println("Usage: SearchEngine [-CorpusDir directory] directory "+
            "[-InvertedIndex afile] filename [-StopList afile] filename [-Queries afile] filename" +
            "[-Results afile] filename [-output afile] filename \n");
        else
            System.out.println("Command Success, your are using following args: ");
            System.out.println("CorpusDir: "+ CorpusDir);
             System.out.println("InvertedIndex: " + InvertedIndex);
             System.out.println("StopList: " +StopList);
             System.out.println("Queries: "+Queries);
             System.out.println("Results: "+Results);
             



        //inverse index file     
        HashMap<String, HashMap<String, List<Integer>>> invertedIndex = new HashMap<String, HashMap<String, List<Integer>>>();  
        //indexing mapping table 
        HashMap<Integer,String> indexHash = new  HashMap<Integer,String>();
        //name index mapping table
        HashMap<String,Integer> docHash = new  HashMap<String, Integer>();

        //loading stop words
        BufferedReader stopbuff;
        File StopListFile = new File(StopList);
        if (!StopListFile.isFile()) {
            System.out.println("not such a file, please enter valid file");
        }
        ArrayList<String> stopwords = new ArrayList<String>();
        try {
            stopbuff = new BufferedReader(
                    new FileReader(StopListFile));
            String temp=null;
            while((temp=stopbuff.readLine())!=null){
                stopwords.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        //loading html files
        File file = new File(CorpusDir);
        File[] filesList = file.listFiles();
        if (!file.isDirectory()) {
            System.out.println(file + ":");
            System.out.println("not such a file, please enter valid Folder");
        }
        //loading all files for loop
        for (int i = 0; i < filesList.length; i++) {
            if (filesList[i].isFile()) {
                
                String html="";
                BufferedReader buff;
                ArrayList<String> files = new ArrayList<String>();
                System.out.println("File:" + filesList[i]);
                files.add(filesList[i].toString());
                indexHash.putIfAbsent(i, filesList[i].toString());
                docHash.putIfAbsent( filesList[i].toString(), i);
                try {
                    buff = new BufferedReader(
                            new FileReader(filesList[i]));
                    
                    
                    String t=null;
                    while((t=buff.readLine())!=null){
                        html=html+t;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                //clean Html files
                String text=clean(html);

                //indexing key and 
                indexing(text, i, invertedIndex, stopwords);
                //System.out.print(invertedIndex);
                files.clear();
                

            }
      

        }
        System.out.println(invertedIndex);
        //if inversedIn is true output InvertedIndex file 
        if (inversedIn.compareTo("true") == 0){
            BufferedWriter output = null;
    
            try {
                File outputName = new File(InvertedIndex);
                output = new BufferedWriter(new FileWriter(outputName));
            
                output.write(invertedIndex.toString());
            //clean container
            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {
            if ( output != null ) {
                output.close();
            }
            }

        }


 

          //reading query file, and process line by line
          BufferedReader querybuff;
          File queryFile = new File(Queries);

          try {
            querybuff = new BufferedReader(
                      new FileReader(queryFile));
              String temp=null;
              //readline 
              while((temp=querybuff.readLine())!=null){
                resultStr = resultStr + temp + ": \n";
                resultStr = resultStr + queryDoc(temp,  invertedIndex, docHash, indexHash) ;
            
              }
  
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }



         //output file             

         BufferedWriter outputresule = null;
 
         try {
             File out = new File(Results);
             outputresule = new BufferedWriter(new FileWriter(out));
             outputresule.write(resultStr.toString());
         //clean container
         } catch ( IOException e ) {
             e.printStackTrace();
         } finally {
         if ( outputresule != null ) {
            outputresule.close();
         }
         }

    }
      
    
       

    public static String clean(String input) {
      String html = input;
      String text = " ";
      java.util.regex.Pattern pattern;
      java.util.regex.Matcher matcher;
      
      try {
          //remove matcher pattern <script>      
          pattern = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          //remove matcher pattern <style>        
          pattern = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          //remove space matcher pattern <>
          pattern = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          
          //remove extra spaces
          text = html.replaceAll("[\\s]+", " ");
          //remove and conver punctuations
          text = text.replaceAll("&nbsp", " ");
          text = text.replaceAll("\\p{Punct}[^']|[-]", " ");
        
        
          text = text.toLowerCase();
          //remove stopword
        
      } catch (Exception e) {
          System.err.println("html2text: " + e.getMessage());
      }

      return text;
  }



    public static void indexing(String inputFile, Integer docCount, HashMap<String, HashMap<String, List<Integer>>> invertedIndex, ArrayList<String> stopwords ) throws IOException
    {
              //position counter
              Integer posCount =0 ;
              Scanner scanfile = new Scanner(inputFile);;
              System.out.println("Indexing File:" + docCount.toString());

                  while(scanfile.hasNextLine()){
                      Scanner scanword = new Scanner(scanfile.nextLine());
                      while (scanword.hasNext()) {
                          String word = scanword.next();
                          //System.out.println(scanword);
                          //indexing word that is not in the stoplist
                          if (!stopwords.contains(word)){
                              HashMap<String, List<Integer>> inside = new HashMap<String, List<Integer>>();   
                              List<Integer> temInt = new ArrayList<Integer>();
      
                              if(invertedIndex.containsKey(word.toString()))
                              {
                                  
      
                                  inside = invertedIndex.get(word.toString());
                                  if (inside.containsKey(docCount.toString())){
                                  
                                      temInt = inside.get(docCount.toString());
                                      temInt.add(posCount);
                                      inside.put(docCount.toString(), temInt);
                                  }
                                  else{
                                      
                                      temInt.add(posCount);
                                      inside.putIfAbsent(docCount.toString(), temInt);
                                      invertedIndex.put(word.toString(),inside);
                                  }
      
                              }
                              else
                              {    
                                  temInt.add(posCount);
                                  inside.putIfAbsent(docCount.toString(), temInt);
                                  invertedIndex.putIfAbsent(word.toString(), inside);}
                              //System.out.println(word);
                              }
                              posCount ++;
                      }
      
              } 
      scanfile.close();
      //System.out.println(invertedIndex);
      
    }

    public static String queryDoc (String queryLine, HashMap<String, HashMap<String, List<Integer>>> invertedIndex, HashMap<String,Integer> docHash, HashMap<Integer, String> indexHash ){
        String result = " ";
        java.util.regex.Pattern pattern;
        java.util.regex.Matcher matcher;


        if (((Pattern.compile("query<", Pattern.CASE_INSENSITIVE)).matcher(queryLine)).find()){
            String res = " ";
            pattern = Pattern.compile("query<", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(queryLine);
            res = matcher.replaceAll("");
            pattern = Pattern.compile(">", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(res);
            res = matcher.replaceAll("") ;
            //int len = (invertedIndex.get(result)).values().size();
            if (invertedIndex.get(res) != null)
                if (invertedIndex.get(res).values() != null)
                {   
                   
                    for(String str :invertedIndex.get(res).keySet())
                    {
                        result = result + indexHash.get(Integer.parseInt(str)) + "\n";
                    };
                }
        
        }
        

        else if (((Pattern.compile("frequency<", Pattern.CASE_INSENSITIVE)).matcher(queryLine)).find()){
            String res = " ";
            pattern = Pattern.compile("frequency<", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(queryLine);
            res = matcher.replaceAll("");
            pattern = Pattern.compile(">", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(res);
            res = matcher.replaceAll("") ;
            //int len = (invertedIndex.get(result)).values().size();
            //System.out.println(len);
            if (invertedIndex.get(res) != null)
            if (invertedIndex.get(res).values() != null)
            {   
               
                for(String str :invertedIndex.get(res).keySet())
                {
                    result = result +"frequency: "+ invertedIndex.get(res).get(str.toString()).size() + ": \n";
                    result = result + indexHash.get(Integer.parseInt(str)) + "\n";
                };
            }
        }
        
        //System.out.print(queryLine);
        
        return result;
    }

}


