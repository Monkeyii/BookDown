小说下载器

    功能：从网站抓取书籍，保存格式为html，每章一个文件
    特点：分析重建章节，多线程，线程数可自定义（Analysis.java中threadSum），未使用java类库分析xml，自动匹配字符集，剔除无用内容
使用

    保存地址为
    Main.java  savePath
    书籍下载地址为
    Main.java bookHttp
    要求：含有书籍章节信息
    示例：http://www.xxbiquge.com/0_638/
网站要求：

    下载地址里包含：
    <h1>书籍名</h1>
    <dd>章节
    
    每章书包含：
    <h1>章节<h1>
    <div id="content">内容
    
    序言：
    <h1>序<h1>
    <h1>开头<h1>
功能详见：
    
    1.剔除章节（）内容
    2.若章节只含有第××，不是第××章，自动补齐
    3.去除假章节
    4.章节内容修改：
        去除：
        ✓ 章节内容里的章节名
        ✓（***）
        ✓ [***]
        ✓ PS***
        ✓ Ps***
        ✓ ps***
        ✓ -----
          ****
          -----
        ✓ -----
          ****
        ✓ 没关注**公众号*** 
        
        调整：
        段间距
        使用中文空格制表符
    
    
    
