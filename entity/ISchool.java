package entity;

import java.util.ArrayList;

import org.apache.http.impl.client.CloseableHttpClient;

public interface ISchool {
	//�����Ҫ��½���ܻ�ȡ�α�Ļ�����½Ȼ�󷵻�һ���Ѿ�������session��HttpClient����
	public CloseableHttpClient login();
	//�������пα����浽�ļ���
	public void downloadTimeTableAndSaveAsFile();
	//����ÿ���α��ļ�������Ϣ���浽���ݿ�
	public void parseFileAndSaveToDatabase();
	//�����ݿ��ж�ȡ���ݲ���װΪClassRoom����
	public ArrayList<ClassRoom> getClassRoomFromDatabase();
	//���ݲ�����ѯ�õ��޿ν���
	public ArrayList<ClassRoom> search(ArrayList<ClassRoom> tts,String campus,String building,String crname,int[] jies,int day,int week);
}
