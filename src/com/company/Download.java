package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static java.io.File.separator;

public class Download {
    private int threadNum;
    private final int line = 5;
    private String bookName;
    private String savePath;
    private String title = "<h1>";
    private String html1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>";
    private String html4 = "</title>\n</head>\n<body>\n<div style=\"text-align: center\">\n";
    private String html2 = "\n</div>\n";
    private String html5 = "\n</body>\n<html>";
    private String ragex = "<div id=\"content\">";
    private String filename = null;
    private String delName=null;

    public Download(String bookName, String savePath, int threadNum) {
        this.bookName = bookName;
        this.savePath = savePath;
        this.threadNum = threadNum;
    }

    public boolean getfile(String chapterHttp, String charsetName, int num, String handrund, boolean openFilter) {

        boolean bool = false;
        String alltitle = null;
        String database = null;
        File file = null;
        boolean creboolean = false;
        BufferedWriter bufferedWriter = null;

        try {
            URL url = new URL(chapterHttp);
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            while (bufferedReader.ready()) {
                database = bufferedReader.readLine();

                //创建每个章节文件
                if (database.contains(title)) {
                    delName=filename = database.substring(database.indexOf("<h1>") + 4, database.indexOf("</h1>"));
                    filename = filename.replaceAll("（\\d）*|（[^更]*更\\d*）", "").trim();
                    //
                    if (openFilter) {
                        if (!filename.matches(".*第[^章]+章.*")) {
                            if (filename.contains("第") | filename.matches(".*弟[^章]+章.*")) {
                                fixTitle();
                            } else if (filename.contains("序") | filename.contains("开头")) {
                            } else {
                                System.out.println(num + "  ----------" + filename + "----------");
                                bool = true;
                                break;
                            }
                        }
                    }
                    //
                    alltitle = "<h3>" + filename + "</h3>";

                    //位置和文件名
                    file = new File(savePath + bookName + separator + "chap" + num + ".html");
                    if (file.exists()) {
                        file.delete();
                    }
                    creboolean = file.createNewFile();
                }
                //写入内容
                else if (database.contains(ragex) && file != null) {
                    database = replaceBook(database);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

                    bufferedWriter.write(html1);
                    bufferedWriter.write(filename);
                    bufferedWriter.write(html4);
                    bufferedWriter.write(alltitle);
                    bufferedWriter.write(html2);
                    bufferedWriter.write(database);
                    bufferedWriter.write(html5);
                    System.out.println(num + " " + filename + "    线程" + threadNum + "完成：" + handrund + "%");
                    bool = true;
                    break;
                }
                bufferedReader.mark(1);
                bufferedReader.reset();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            bufferedReader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    private void fixTitle() {
        String[] arr = filename.split(" ");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].matches("第[^卷]+")) {
                arr[i] = arr[i].concat("章");
            } else if (arr[i].matches("弟[^章卷]+[章卷]")) {
                arr[i] = arr[i].replace("弟", "第");
            }
        }
        String temp = arr[0];
        for (int i = 1; i < arr.length; i++) {
            temp = temp.concat(" " + arr[i]);
        }
        filename = temp;
    }

    //
    private String replaceBook(String str) {
        String database;
        str = str.replaceAll("(<br />)+", "<br /><br />");
        //中文首行缩进
        str = str.replaceAll("\\s{2,}(&nbsp;)*|(&nbsp;)+", "&#12288;&#12288;");
        String[] arr = filename.split(" ");
        String temp1 = str.substring(0, str.indexOf("<br />"));
        String temp2 = temp1;
        for (int i = 0; i < arr.length; i++) {
            temp1 = temp1.replaceAll(arr[i], "");
        }
        database=str=str.replace(temp2, temp1);
        // ^*** ---
        for (int i = 0; i < line; i++) {
            String temp = str.substring(0, str.indexOf("<br /><br />") + 12);
            str = str.replace(temp, "");
            if (temp.contains("－－") | temp.contains("--") | temp.contains("——")) {
                database = str;
                break;
            }
        }
        str=database;
        for (int i = 0; i < line; i++) {

            String temp = str.substring( str.lastIndexOf("<br /><br />"),str.length());
            str = str.replace(temp, "");
            if (temp.contains("－－") | temp.contains("--") | temp.contains("——")|temp.contains("==")) {
                database = str.concat("</div>");
                break;
            }
        }
        //
        database = database.replace("\\", "");

        database = database.replaceAll("【播报】[^<]+|PS.[^<]+", "");
        //排除 readx(); （）[] ---***---
        database = database.replaceAll("readx\\(\\);|（[^）]*）|\\([^)]*\\)|\\[[^\\]]*\\]|[－—\\-]{2,}[^－—\\-]+[－—\\-]{2,}", "");
        //排除 PS Ps ps 没关注**
        database = database.replaceAll("PS[\\s\\S]*|Ps[\\s\\S]*|ps[\\s\\S]*|没关注[\\s\\S]*(?=公众号)[\\s\\S]*|(&#12288;)+兄弟姐妹们[\\s\\S]*|(&#12288;)+今天是大年[\\s\\S]*", "</div>");
        //换行
        database = database.replaceAll("(<br />)+(&#12288;)+<", "<");
        database = database.replaceAll(">(&#12288;)+\\.?(<br />)+", ">");
        return database;
    }
}
