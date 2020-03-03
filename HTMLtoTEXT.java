
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Youjie Lin   
 */
public class HTMLtoTEXT {
    public static void main(String[] args)
    {
        BufferedReader buff;
           
        String HtmlStr="";
        try {
            buff = new BufferedReader(
                     new FileReader("./CorpusDir/• Mass shootings in the U.S. by state 1982-2020 _ Statista.html"));
               
               
            String t=null;
            while((t=buff.readLine())!=null){//读一行
                HtmlStr=HtmlStr+t;
              }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
           
        //HtmlStr="<div>&nbsp;　三、改进措施</div><div>
        String str=clean(HtmlStr);
        System.out.println(str);
        getFiles("./CorpusDir");
        
    }
       


    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
    
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                  System.out.println("文件：" + tempList[i]);
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
                  System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
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

          textStr = htmlStr.replaceAll(" +", " ");
          textStr = textStr.replaceAll("\\p{Punct}", " ");

      } catch (Exception e) {
          System.err.println("Html2Text: " + e.getMessage());
      }

      return textStr;// 返回文本字符串
  }

}





