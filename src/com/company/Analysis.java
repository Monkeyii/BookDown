package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static java.io.File.separator;

public class Analysis {
    private String bookName = null;
    private String savepath = null;
    private String bookHttp = null;
    private String newChapter = null;
    //
    private static int threadSum = 4;


    public Analysis(String savePath, String bookHttp) {
        this.savepath = savePath;
        this.bookHttp = bookHttp;

    }

    public void analysis(String charsetName) {

        URL url = null;
        //获取网站地址
        String http = bookHttp.substring(bookHttp.indexOf("http"), bookHttp.indexOf("com") + 3);
        //临时数据
        String database = null;
        //
        int[] everythreadNum = null;

        try {
            url = new URL(bookHttp);
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            while (bufferedReader.ready()) {
                database = bufferedReader.readLine();
                if (!database.trim().equals(null)) {
                    //获取书籍名字
                    if (database.contains("<h1>")) {
                        bookName = database.substring(database.indexOf("<h1>") + 4, database.indexOf("</h1>"));
                        creatDir(bookName);
                    }
                    //获取书籍章节链接
                    else if (database.contains("<dd>")) {
                        String[] arrChapterHttp = splitString(database, http);
                        if (arrChapterHttp != null && newChapter.equals(arrChapterHttp[arrChapterHttp.length - 1])) {
                            everythreadNum = everyThread(arrChapterHttp.length, threadSum);
                            //开线程
                            if (everythreadNum != null) {
                                for (int i = 0; i < threadSum; i++) {
                                    DownThread downThread = new DownThread(arrChapterHttp, bookName, charsetName, savepath, everythreadNum[i], everythreadNum[i + 1], i + 1);
                                    downThread.start();
                                }
                                break;
                            }
                        } else if (arrChapterHttp != null && !newChapter.equals(arrChapterHttp[arrChapterHttp.length - 1])) {
                            System.out.println("章节分析错误！" + "\n分析最新章节：" + newChapter + "\n网站最新章节：" + arrChapterHttp[arrChapterHttp.length - 1]);
                        }
                    }
                }
                bufferedReader.mark(1);
                bufferedReader.reset();
            }
            bufferedReader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建文件夹
    private boolean creatDir(String bookName) {
        File dir = new File(savepath + bookName);
        if (!dir.exists()) {
            boolean bool = dir.mkdirs();
            return bool;
        } else {
            return true;
        }
    }

    //匹配地址
    private String[] splitString(String database, String http) {
        ArrayList<String> list = new ArrayList<String>();
        String ragex = "href=\"[/_\\w]+\\.html\"[^\"]*";
        list.add("第\\d*章");
        list.add("[!,:;?¨·ˇˉ―‖’”…∶、。〃々〉》」』】〕〗！＂＇）——，．：；？］｀｜～￠{·‘“〈《「『【〔〖（．［￡￥]*");
        list.add("</dd>");
        list.add("</a>");
        list.add("<dd>");
        list.add("[\\u4e00-\\u9fa5]");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            database = database.replaceAll(iterator.next(), "");
        }
        database = database.replaceAll("><", ">\n<");
        String newTemp = database.substring(database.lastIndexOf("href=\"") + 6, database.lastIndexOf(".html") + 5);
        newChapter = http.concat(newTemp);
        String[] splitStr = database.split(" ");
        int trueNum = 0;
        for (int i = 0; i < splitStr.length; i++) {
            if (splitStr[i].matches(ragex)) {
                trueNum++;
            }
        }
        String[] arrChapterHttp = new String[trueNum];
        int trueNum1 = 0;
        for (int i = 0; i < splitStr.length; i++) {
            if (splitStr[i].matches(ragex)) {

                String temp = splitStr[i].substring(splitStr[i].indexOf("\"") + 1, splitStr[i].lastIndexOf("\""));
                String chapterHttp = http.concat(temp);
                arrChapterHttp[trueNum1] = chapterHttp;
                //
                trueNum1++;
            }
        }
        return arrChapterHttp;
    }

    private int[] everyThread(int httpLength, int threadSum) {
        int[] ii = new int[threadSum + 1];

        if (threadSum <= 5) {
            ii[0] = 0;
            for (int i = 1; i < threadSum + 1; i++) {
                ii[i] = (int) ((httpLength * i) / threadSum);
            }
            return ii;
        }
        return null;
    }

    public String getcharsetName() {
        String charsetName = null;
        URL url = null;
        try {
            url = new URL(bookHttp);
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (bufferedReader.ready()) {
                String database = bufferedReader.readLine();
                if (!database.trim().equals(null)) {

                    //匹配网页字符集
                    if (database.contains("charset")) {
                        charsetName = database.substring(database.indexOf("charset=") + 8, database.lastIndexOf("\""));
                        System.out.println("=============匹配字符集:" + charsetName + "============");
                        break;
                    }

                }
                bufferedReader.mark(1);
                bufferedReader.reset();
            }
            bufferedReader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return charsetName;
    }
}








