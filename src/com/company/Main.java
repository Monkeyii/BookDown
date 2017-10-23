package com.company;


public class Main {


    public static void main(String[] args) {
        String charsetName = null;

        //书籍网站下载地址 http://www.xxbiquge.com
        String savePath = "/home/ma/Book/";
        String bookHttp = "http://www.xxbiquge.com/75_75918/";
        Analysis analysis = new Analysis(savePath, bookHttp);
        charsetName = analysis.getcharsetName();
        if (charsetName != null) {
            analysis.analysis(charsetName);
        }
    }
}
