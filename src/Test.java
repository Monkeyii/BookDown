import com.company.Download;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Test {

    public static void main(String[] args) {

//        String database="href=\"/7573/18008.html\">";
//        String ragex = "href=\"[/_\\d]+\\.html[^\"]*";
//        System.out.println("第一章 哈喽（第1更）".trim());
//        String str="<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;PS：重要通知，武神读者必看。<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;目前为止，有百分之九十的读者，都是在";
//          String str="&nbsp;&nbsp;&nbsp;";
//          System.out.println(str.replaceAll("(&nbsp;)+|\\s{2,4}","11"));
        Download download = new Download("Test", "/home/ma/Book/", 1);
        download.getfile("http://www.xxbiquge.com/75_75918/3934151.html","utf-8",0,"",true);
//        String title = "-----";
//        String temp=title.substring(0,title.indexOf("<br /><br />")+12);
//        title=title.replace(temp,"");
    /*    String[] arr=title.split(" ");
        for (int i=0;i<arr.length;i++){

            System.out.println(arr[i]);
        */
    }


}



