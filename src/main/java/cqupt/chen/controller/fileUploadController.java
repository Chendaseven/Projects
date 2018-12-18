package cqupt.chen.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import cqupt.chen.pojo.fileBean;
import cqupt.chen.service.fileListService;

@Controller
public class fileUploadController {
	@Autowired
	private fileListService filelistservice;
	
	@RequestMapping(value = "list")
	public String uploadlist(Model model) {
		List<fileBean> filelist = filelistservice.fileList();
		model.addAttribute("list", filelist);
		
		return "list";
	}
	
	@RequestMapping(value = "searchFile")
	public String findFileByName(Model model,String sename) {
		List<fileBean> fileList= filelistservice.findByName(sename);
		model.addAttribute("list", fileList);
		return "list";
	}
	
	@RequestMapping(value = "searchimage")
	public String findFileByImage(Model model) {
		List<fileBean> fileList= filelistservice.findByimage("image");
		model.addAttribute("list", fileList);
		return "list";
	}
	
	@RequestMapping(value = "UploadServlet")
	public String uploadFile(HttpServletRequest request,MultipartFile[] attachment,Model model) throws IllegalStateException, IOException {
		//�����ļ��ϴ�ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String uploadDate = sdf.format(new Date());
		List<fileBean> filebeanList = new ArrayList<>();
		
		if(attachment != null && attachment.length>0) {
			for(MultipartFile file : attachment) {
				if(file != null & !file.isEmpty()) {
					
					//����ļ�ԭʼ��
					//�ر�ע�⣺�ļ��ϴ�ʱ��MultipartFile file ��file��Ӧ����ǰ��jspҳ��<input type="file" name="file"/> ���name����ͬ������ȡ����file
					String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
					//��ȡ�ļ�����
					String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
					File fileLocal = new File("H:/temp"+File.separator+file.getOriginalFilename());
					
					Long fileSize = file.getSize();
					//�����ļ���С
					/**
					 *  e.g.   1024b=1KB  1024*1024b=1MB
					 */
					String sizeStr = "";
					if(fileSize>=1024 && fileSize<1024*1024){
						sizeStr = (fileSize/1024)+"KB";
					}else if(fileSize>1024*1024 && fileSize<=1024*1024*1024){
						sizeStr = (fileSize/(1024*1024))+"MB";
					}else if(fileSize >= 1024*1024*1024){
						sizeStr = (fileSize/(1024*1024*1024))+"GB";
					}else{
						sizeStr = fileSize+"B";
					}
					//String fileType = file.getContentType();
					String info = request.getParameter("info1");
					//�ֱ𱣴�ÿһ���ϴ����ļ�
					fileBean filebean = new fileBean();
					filebean.setName(fileName);
					filebean.setSize(sizeStr);
					filebean.setType(fileType);
					filebean.setAddtime(uploadDate);
					filebean.setFile_path(fileLocal.getPath());
					filebean.setInfo(info);
					//��ÿһ��fileBean������List��
					filebeanList.add(filebean);
					//��ÿһ���ļ�������ָ��Ŀ¼
					file.transferTo(fileLocal);
					//��ÿһ���ļ�����Ϣ���뵽���ݿ��У�����Mybatis��ʹ��һ�����ͬʱ���������ļ���Ϣ
					filelistservice.uploadFile(filebean);
				}
			}
		}
		model.addAttribute("fileList", filebeanList);
		return "success";
	}
	
	@RequestMapping(value = "DownServlet")
	public void downFile(fileBean filebean,Integer id,HttpServletRequest request,HttpServletResponse response) throws IOException {
		fileBean filebeanById = filelistservice.findByID(id);
		//��ȡ�ļ�����
		String fileName = filebeanById.getName();
		//��ȡ�ļ���+����
		fileName = fileName +"."+ filebeanById.getType();
		//��ȡ�ļ�����·��
		String path = "H:\\temp" + "\\" + fileName;
		InputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		fileName = URLEncoder.encode(fileName, "utf-8");
		//����һ����Ӧͷ: Content-Disposition  ��������������صķ����򿪸���Դ
		String content = "attachment;filename="+fileName;
		response.setHeader("Content-Disposition", content);
		//���ļ����ݷ��͸������
		OutputStream out = response.getOutputStream();
		 int len = 0;
		 byte[] buf = new byte[1024];
         while((len = in.read(buf)) != -1){
             out.write(buf, 0, len);
             out.flush();
         }  
         out.close();
         in.close();
	}
	
	
}
