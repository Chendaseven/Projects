package cqupt.chen.mapper;

import java.util.List;

import cqupt.chen.pojo.fileBean;

public interface fileListMapper {
	public List<fileBean> findAllFile();
	//�����ļ��������ļ�
	public List<fileBean> findFileByName(String fileName);
	//����ͼƬ�����ļ�
	public List<fileBean> findFileByImage(String type);
	//�ϴ��ļ�
	public void uploadFile(fileBean filebean);
}
