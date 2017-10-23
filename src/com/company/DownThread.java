package com.company;

import java.text.DecimalFormat;

public class DownThread extends Thread {
    private String chapterHttp[] = null;
    private String bookName;
    private String savePath;
    private int start;
    private int end;
    private int threadNum;
    private String charsetName;

    public DownThread(String[] chapterHttp, String bookName,String charsetName, String savePath, int start, int end, int threadNum) {
        this.chapterHttp = chapterHttp;
        this.bookName = bookName;
        this.savePath = savePath;
        this.start = start;
        this.end = end;
        this.threadNum = threadNum;
        this.charsetName=charsetName;
    }

    @Override
    public void run() {
        super.run();
        Download download = new Download(bookName, savePath, threadNum);
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        for (int i = start; i < end; i++) {
            double handrund = (int) (i - start + 1) * 100.0 / (end - start);
            boolean bool=true;
            int j=0;
            do {
                if (j<10) {
                    bool = download.getfile(chapterHttp[i], charsetName,(i + 1), decimalFormat.format(handrund),true);
                    j++;
                }else {
                    System.out.println("-------------------文件错误：chapter"+(i+1)+".html 目标网页： "+chapterHttp[i]);
                    break;
                }
            }while (!bool);
        }
    }
}
