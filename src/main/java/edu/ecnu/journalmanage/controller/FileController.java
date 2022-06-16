package edu.ecnu.journalmanage.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import edu.ecnu.journalmanage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {
    @Autowired
    public ArticleService articleService;

    @RequestMapping("/fileDownload")
    public String download(int articleID, HttpServletResponse response, HttpServletRequest request) throws IOException {
        //通过articleID查询出对应的relative_filePath
        String relative_filePath = articleService.getArticleById(articleID).getFilePath();
        String[] filePath_split = relative_filePath.split("/");

        // 文件存储路径
        // String path = "D:/MyGithub/JournalManage/src/main/resources/static/files/";
        String path = "/root/SmartPaper/src/main/resources/static/files/";
        // 从请求中获取文件名
        String fileName = filePath_split[filePath_split.length-1];
        // 创建输出流对象
        ServletOutputStream outputStream = response.getOutputStream();
        //以字节数组的形式读取文件
        byte[] bytes = FileUtil.readBytes(path + fileName);
        // 设置返回内容格式
        response.setContentType("application/octet-stream");

        // 把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码
        // 中文不要太多，最多支持17个中文，因为header有150个字节限制。
        // 这一步一定要在读取文件之后进行，否则文件名会乱码，找不到文件
        fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");

        // 设置下载弹窗的文件名和格式（文件名要包括名字和文件格式）
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 返回数据到输出流对象中
        outputStream.write(bytes);
        // 关闭流对象
        IoUtil.close(outputStream);

        return null;
    }
}

