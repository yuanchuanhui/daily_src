package spz;

import java.util.Random;

public class InputData {
	public static void main(String[] args) {
		Random random=new Random();
		for(int i=0;i<1000;i++){
			long randlong=random.nextLong();
			System.out.println(randlong);
			DBUtil.excute("insert into kegongchang(first) values(?)", new String[]{String.valueOf(randlong)});
		}
	}
}
