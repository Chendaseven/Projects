package cqupt.chen.service;

import java.util.List;

import cqupt.chen.pojo.fileBean;

public interface fileListService {
	public List<fileBean> fileList();
	//service�ж������name�����ļ�
	public List<fileBean> findByName(String name);
	//����ͼƬ���Ͳ����ļ�
	public List<fileBean> findByimage(String type);
	//�ļ��ϴ�
	public void uploadFile(fileBean filebean);
	//����id��ѯ�ļ�����
	public fileBean findByID(Integer id);
}
