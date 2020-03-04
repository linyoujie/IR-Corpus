
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
        int i = 0, j;
        String arg;

        String CorpusDir;
        String InvertedIndex;
        String StopList;
        String Queries; 
        String Results;

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];


    // use this type of check for arguments that require arguments
            if (arg.equals("-Results")) {
                if (i < args.length)
                    Results = args[i++];
                else
                    System.err.println("-Results requires a filename");
            }

            else if (arg.equals("-CorpusDir")) {
                if (i < args.length)
                    CorpusDir = args[i++];
                else
                    System.err.println("-CorpusDir requires a directory");
            }
            else if (arg.equals("-InvertedIndex")) {
                if (i < args.length)
                    InvertedIndex = args[i++];
                else
                    System.err.println("-InvertedIndex requires a filename");
            }
            else if (arg.equals("-StopList")) {
                if (i < args.length)
                    StopList = args[i++];
                else
                    System.err.println("-StopList requires a filename");
            }

            else if (arg.equals("-Queries")) {
                if (i < args.length)
                    Queries = args[i++];
                else
                    System.err.println("-Queries requires a filename");
            }

            else if (arg.equals("-Frequency")) {
                if (i < args.length)
                Frequency = args[i++];
                else
                    System.err.println("-Frequency requires a filename");
            }
    // use this type of check for a series of flag arguments
            else {
                for (j = 1; j < arg.length(); j++) {
                    flag = arg.charAt(j);
                    switch (flag) {
                    case 'x':
                        if (vflag) System.out.println("Option x");
                        break;
                    case 'n':
                        if (vflag) System.out.println("Option n");
                        break;
                    default:
                        System.err.println("ParseCmdLine: illegal option " + flag);
                        break;
                    }
                }
            }
        }
        if (i == args.length)
            System.err.println("Usage: ParseCmdLine [-verbose] [-xn] [-output afile] filename");
        else
            System.out.println("Success!");











        HashMap<String, HashMap<String, List<Integer>>> invertedIndex = new HashMap<String, HashMap<String, List<Integer>>>();   
        HashMap<Integer,String> indexHash = new  HashMap<Integer,String>();
        //loading stop words
        BufferedReader stopbuff;
        File StopListFile = new File("./StopList.txt");
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
        File file = new File("./RawHtml");
        File[] filesList = file.listFiles();
        //loading all files for loop
        for (int i = 0; i < filesList.length; i++) {
            if (filesList[i].isFile()) {
                
                String html="";
                BufferedReader buff;
                ArrayList<String> files = new ArrayList<String>();
                System.out.println("File:" + filesList[i]);
                files.add(filesList[i].toString());
                indexHash.putIfAbsent(i, filesList[i].toString());
                try {
                    buff = new BufferedReader(
                            new FileReader(filesList[i]));
                    
                    
                    String t=null;
                    while((t=buff.readLine())!=null){//读一行
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
        //output InvertedIndex file 
        BufferedWriter output = null;
        try {
            File outputName = new File("./InvertedIndex.txt");
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
       

    public static String clean(String input) {
      String html = input;
      String text = " ";
      java.util.regex.Pattern pattern;
      java.util.regex.Matcher matcher;


      try {
          //remove matcher pattern<script>      
          pattern = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          //remove matcher pattern<style>        
          pattern = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          //remove space matcher pattern<>
          pattern = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
          matcher = pattern.matcher(html);
          html = matcher.replaceAll(" "); 
          
          //remove extra spaces
          text = html.replaceAll("[\\s]+", " ");
          //remove and conver punctuations
          text = text.replaceAll("&nbsp", " ");
          text = text.replaceAll("\\p{Punct}[^']|[-]", " ");
          text = text.replaceAll("[\\s][‘’]|[‘’]", " '");
          text = text.replaceAll("[\\s][“”]|[“”]", " \"").toLowerCase();
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
}


