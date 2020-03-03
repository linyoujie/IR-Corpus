
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
import java.util.regex.Pattern;

/**
 * @author Youjie Lin   
 */
public class ConvertToTEXT {
    
    public static void main(String[] args) throws IOException
    {
        
           

        File file = new File("./RawHtml");
        File[] filesList = file.listFiles();

        for (int i = 0; i < filesList.length-1; i++) {
            if (filesList[i].isFile()) {
                String HtmlStr="";
                BufferedReader buff;
                ArrayList<String> files = new ArrayList<String>();
                System.out.println("File:" + filesList[i]);
                files.add(filesList[i].toString());
               
                try {
                    buff = new BufferedReader(
                            new FileReader(filesList[i]));
                    
                    
                    String t=null;
                    while((t=buff.readLine())!=null){//读一行
                        HtmlStr=HtmlStr+t;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                //HtmlStr="<div>&nbsp;　三、改进措施</div><div>
                String str=clean(HtmlStr);
                //System.out.println(str);

                BufferedWriter output = null;
                try {
                    File outfile = new File("./TextCorpus/"+i+".txt");
                    output = new BufferedWriter(new FileWriter(outfile));
                    output.write(str);
                    //clean container
                    str = "";
                } catch ( IOException e ) {
                    e.printStackTrace();
                } finally {
                if ( output != null ) {
                    
                    output.close();
                }
                }
                
                files.clear();
            }

        }
        
    }
       



    public static String clean(String inputString) {
      String htmlStr = inputString; // 含html标签的字符串
      String textStr = " ";
      java.util.regex.Pattern p_script;
      java.util.regex.Matcher m_script;
      java.util.regex.Pattern p_style;
      java.util.regex.Matcher m_style;
      java.util.regex.Pattern p_html;
      java.util.regex.Matcher m_html;

      try {
          String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
                                                                                                      // }
          String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
                                                                                                  // }
          String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

          p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
          m_script = p_script.matcher(htmlStr);
          htmlStr = m_script.replaceAll(" "); // 过滤script标签

          p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
          m_style = p_style.matcher(htmlStr);
          htmlStr = m_style.replaceAll(" "); // 过滤style标签

          p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
          m_html = p_html.matcher(htmlStr);
          htmlStr = m_html.replaceAll(" "); // 过滤html标签

          textStr = htmlStr.replaceAll("[\\s]+", " ");
          textStr = textStr.replaceAll("\\p{Punct}[^']|[-]", " ");
          textStr = textStr.replaceAll("[\\s][‘’]|[‘’]", "'");
          textStr = textStr.replaceAll("[\\s][“”]|[“”]", "\"");
          //remove stopword
        

            
     
      } catch (Exception e) {
          System.err.println("Html2Text: " + e.getMessage());
      }

      return textStr;
  }

}





