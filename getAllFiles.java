import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.List;
/**
 * @author Youjie Lin   
 */
public class getAllFiles {
    public static void main(String[] args) throws IOException
    {

        HashMap<String, ArrayList<ArrayList<Integer>> > invertedIndex = new HashMap<String, ArrayList<ArrayList<Integer>>>();
        BufferedReader stopbuff;
        File StopListFile = new File("./StopList.txt");
        ArrayList<String> stopwords = new ArrayList<String>();
        try {
            stopbuff = new BufferedReader(
                    new FileReader(StopListFile));
            String temp=null;
            while((temp=stopbuff.readLine())!=null){//读一行
                stopwords.add(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(stopwords);
        //ArrayList<String> stopwords = new ArrayList<String>();


        
        
        File file = new File("./textCorpus");
        File[] filesList = file.listFiles();

        for (int i = 0; i < filesList.length-1; i++) {
            if (filesList[i].isFile()) {
                //position counter
                int posCount = 0;
                String HtmlStr="";
                Scanner scanfile;
                ArrayList<String> files = new ArrayList<String>();
                System.out.println("File:" + filesList[i]);
                //files.add(filesList[i].toString());
               
                try {
                    scanfile = new Scanner(
                            new FileReader(filesList[i]));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    
  
                    while(scanfile.hasNextLine()){//读一行
                        Scanner scanword = new Scanner(scanfile.nextLine());
                        while (scanword.hasNext()) {
                            String word = scanword.next();
                            //System.out.println(scanword);
                        if (!stopwords.contains(scanword)){
                            invertedIndex.put(scanword, ArrayList<ArrayList<Integer>(filesList[i], posCount ));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                
              



    }



}
