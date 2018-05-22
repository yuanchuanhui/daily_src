package test;

import java.util.ArrayList;

import entity.ClassRoom;
import entity.ISchool;
import entity.QfnuSchool;
import utils.Utils;

public class test {
	public static void main(String[] args) throws Exception {
		//		Calendar calendar=Calendar.getInstance();
		//		calendar.setTimeInMillis(calendar.getTimeInMillis());
		//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		//		System.out.println(sdf.format(calendar.getTime()));
		//		Device device=Utils.readFromTomcat();
		//		System.out.println(device.getTime());
		//		System.out.println(Utils.parseTime(1526281600422L));
		//		Calendar calendar=Calendar.getInstance();
		//		calendar.set(Calendar.HOUR_OF_DAY, 0);
		//		calendar.set(Calendar.MINUTE, 0);
		//		System.out.println(Utils.parseTime(calendar.getTimeInMillis()));
		//		Device device=new Device();
		//		device.setTime("1526281600422");
		//		device.setChangeTime(true);
		//		Utils.outputToDatabase("1", new Gson().toJson(device));
		ISchool qfnu=new QfnuSchool();
		ArrayList<ClassRoom> tts=qfnu.getClassRoomFromDatabase();
		ArrayList<ClassRoom> result=ClassRoom.search(
				tts, "曲阜", 
				"综合教学楼",
				null,
				Utils.strToIntArray("2,3,4"),
				4,
				11);
		for (ClassRoom tt : result) {
			System.out.println(tt.getCrname());
		}
	}
}
