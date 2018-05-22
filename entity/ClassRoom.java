package entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import utils.DBUtil;
import utils.Utils;

/**
 * ʵ���࣬һ�������Ӧһ����ҵĿα�
 * */

public class ClassRoom {
	private String id;
	private String campus;
	private String building;
	private String crid;
	private String crname;
	private String setsnum;
	private String faculty;
	private String roomtype;
	private String ttinfourl;
	private int[][][] class_;

	public ClassRoom(String id, String campus, String building, String crid, String crname, String setsnum,
			String faculty, String roomtype, String ttinfourl, int[][][] class_) {
		super();
		this.id = id;
		this.campus = campus;
		this.building = building;
		this.crid = crid;
		this.crname = crname;
		this.setsnum = setsnum;
		this.faculty = faculty;
		this.roomtype = roomtype;
		this.ttinfourl = ttinfourl;
		this.class_ = class_;
	}

	public ClassRoom(String fileName) {
		String[] fields=fileName.split("_");
		this.id=fields[0];
		this.campus=fields[1];
		this.building=fields[2];
		this.crid=fields[3];
		this.crname=fields[4];
		this.setsnum=fields[5];
		this.faculty=fields[6];
		this.roomtype=fields[7];
	}

	public ClassRoom(Element tr) {
		this.id=tr.child(0).html();
		this.campus=tr.child(1).html();
		this.building=tr.child(2).html();
		this.crid=tr.child(3).html();
		this.crname=tr.child(4).html();
		this.setsnum=tr.child(5).html();
		this.faculty=tr.child(6).html();
		this.roomtype=tr.child(7).html();
		String onclick=tr.child(8).child(0).attr("onclick");
		this.ttinfourl=onclick.substring(9,onclick.length()-3);
	}

	public static ClassRoom[] getAll(String path){
		String html=Utils.fileToString(path, "gbk");
		Document doc=Jsoup.parse(html);
		Elements trs=doc.select("#user > tbody > tr");
		ClassRoom[] tts=new ClassRoom[trs.size()];
		for(int i=0;i<tts.length;i++){
			tts[i]=new ClassRoom(trs.get(i));
		}
		return tts;
	}

	public void parseAndSetClass(ArrayList<ArrayList<String>> classes){
		int[][][] class_=new int[11][7][18];
		for(int i=0;i<classes.size();i++){
			for(int j=0;j<classes.get(i).size();j++){
				String scs=classes.get(i).get(j);
				int[] subClass_=new int[18];
				String[] cs=scs.split(",");
				for(int k=0;k<cs.length;k++){
					String c=cs[k];
					if(!c.equals("")){
						//						System.out.println(c);
						String[] scale=c.split("-");
						int start=Integer.parseInt(scale[0]);
						int end=Integer.parseInt(scale[1]);
						for(int l=start-1;l<end;l++){
							subClass_[l]=1;
						}
					}
				}
				class_[i][j]=subClass_;
			}
		}
		this.class_=class_;
	}


	//����һ�����ҵĿα��ļ�������һ��QFNUTimeTable����
	public static ClassRoom parseTT(String dir,String fileName){
		String html=Utils.fileToString(dir+fileName, "gbk");
		Document doc=Jsoup.parse(html);
		Pattern tpattern=Pattern.compile("\\d{1,2}-\\d{1,2}");
		//ƥ��ÿһ�ڿ�
		Pattern cpattern=Pattern.compile("[^| ].*?\\(.*?,.*?,.*?\\)");
		ArrayList<ArrayList<String>> classes=new ArrayList<ArrayList<String>>();
		//����������
		for(int i=3;i<=15;i++){
			Element tr=doc.select("#user > thead > tr:nth-child("+i+")").get(0);
			Elements tds=tr.children();
			ArrayList<String> days=new ArrayList<String>();
			//tΪ1-7��
			if(tds.size()>1){
				//����һ�е����и���
				for(int j=tds.size()-7;j<tds.size();j++){
					Element td=tds.get(j);
					Matcher cmatcher=cpattern.matcher(td.text());
					StringBuilder sb=new StringBuilder();
					//ֻҪ�ҵ��пμ�ѭ��
					while(cmatcher.find()){
						Matcher tmatcher=tpattern.matcher(cmatcher.group());
						while(tmatcher.find()){
							if(!cmatcher.group().contains("�����")){
								sb.append(tmatcher.group()+",");
							}
						}
					}
					days.add(sb.toString());
				}
				classes.add(days);
			}
		}
		ClassRoom tt=new ClassRoom(fileName);
		tt.parseAndSetClass(classes);
		return tt;
	}

	//���ļ��еĿα���Ϣ���������ŵ����ݿ���
	public static void saveToDatabase(){
		File dir=new File("C:\\Users\\473574509\\Documents\\timetable\\");
		if(dir.isDirectory()){
			String[] filenames=dir.list();
			for (String filename : filenames) {
				ClassRoom tt=ClassRoom.parseTT("C:\\Users\\473574509\\Documents\\timetable\\", filename);
				for(int r=0;r<11;r++){
					System.out.print((r+1)+"\t");
					for(int c=0;c<7;c++){
						System.out.print(Utils.intarrayToString(tt.getClass_()[r][c])+"\t");
					}
					System.out.println();
				}
				DBUtil.excute("insert into timetable(id,campus,building,crid,crname,setsnum,faculty,roomtype,classes) values(?,?,?,?,?,?,?,?,?)", new String[]{
						tt.getId(),tt.getCampus(),tt.getBuilding(),tt.getCrid(),tt.getCrname(),tt.getSetsnum(),tt.getFaculty(),tt.getRoomtype(),new Gson().toJson(tt.getClass_())
				});
			}
		}
	}

	//�����ݿ��ж�ȡ�־û���CLassRoom��
	public static ArrayList<ClassRoom> readFromDatabase(){
		List<Map<String, String>> ttMaps=DBUtil.query("select * from timetable", null, new String[]{"id","campus","building","crid","crname","setsnum","faculty","roomtype","classes"});
		ArrayList<ClassRoom> tts=new ArrayList<>();
		for (Map<String, String> map : ttMaps) {
			tts.add(new ClassRoom(map.get("id"), map.get("campus"), map.get("building"), map.get("crid"), map.get("crname"), map.get("setsnum"), map.get("faculty"), map.get("roottype"), null, new Gson().fromJson(map.get("classes"),int[][][].class)));
		}
		return tts;
	}

	//查询符合条件的ClassRoom集合
	public static ArrayList<ClassRoom> search(ArrayList<ClassRoom> tts,String campus,String building,String crname,int[] jies,int day,int week){
		long start=System.currentTimeMillis();
		ArrayList<ClassRoom> result=(ArrayList<ClassRoom>) tts.clone();
		for (ClassRoom tt : tts) {
			if(tt.getCampus()==null||tt.getCampus().equals(campus)){
				if(tt.getBuilding()==null||tt.getBuilding().equals(building)){
					if(crname==null||tt.getCrname().equals(crname)){						
						for (int i=0;i<jies.length;i++) {
							if(tt.getClass_()==null){
								System.out.println(tt.getId()+"的classes为空");
								tts.remove(tt);
								break;
							}
							if(tt.getClass_()!=null&&tt.getClass_()[jies[i]-1][day-1][week-1]!=0){
								result.remove(tt);
								break;
							}
						}
					}
				}
				else{
					result.remove(tt);
				}
			}
			else{
				result.remove(tt);
			}
		}
		System.out.println("本次查询共"+(System.currentTimeMillis()-start)+"ms");
		return result;
	}


	public int[][][] getClass_() {
		return class_;
	}

	//��������"1-18"���ַ������飬����Ϊ����111111111111111111���ַ���
	public void setClass_(boolean[][][] class_) {
		this.class_ = null;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getCrid() {
		return crid;
	}
	public void setCrid(String crid) {
		this.crid = crid;
	}
	public String getCrname() {
		return crname;
	}
	public void setCrname(String crname) {
		this.crname = crname;
	}
	public String getSetsnum() {
		return setsnum;
	}
	public void setSetsnum(String setsnum) {
		this.setsnum = setsnum;
	}
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	public String getRoomtype() {
		return roomtype;
	}
	public void setRoomtype(String roomtype) {
		this.roomtype = roomtype;
	}
	public String getTtinfourl() {
		return ttinfourl;
	}
	public void setTtinfourl(String ttinfourl) {
		this.ttinfourl = ttinfourl;
	}

	@Override
	public String toString() {
		return "ClassRoom [id=" + id + ", campus=" + campus + ", building=" + building + ", crid=" + crid
				+ ", crname=" + crname + ", setsnum=" + setsnum + ", faculty=" + faculty + ", roomtype=" + roomtype
				+ ", ttinfourl=" + ttinfourl + "]";
	}

}
