package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Mypanel extends javax.swing.JPanel { 
	public Mypanel() {

	}
	private List<Integer> list1 = new LinkedList<Integer>();
	private List<Integer> list2 = new LinkedList<Integer>();
	private final int XPOINT = 100;
	private final int YPOINT = 400;
	private final int TEP = 5; //像素的位移量
	private int num = 0;
	private final int YY = 200; //产生随机数的范围
	public void paintComponent(Graphics g) {
	    g.clearRect(0, 0, Heart.WIDTH, Heart.HEIGHT);
	    super.paintComponent(g);
	    g.setColor(Color.BLUE);
	    //画X轴
	    g.drawLine(this.XPOINT, Heart.HEIGHT - this.XPOINT, this.XPOINT,
	               this.XPOINT);
	    //画Y轴
	    g.drawLine(this.XPOINT, Heart.HEIGHT - this.XPOINT,
	               Heart.WIDTH - this.XPOINT, Heart.HEIGHT - this.XPOINT);
	    //标题
	    g.drawString("模拟心电图", (Heart.WIDTH - 2 * (this.XPOINT)) / 2 + 50,
	                 this.XPOINT - 50);
	    //画X轴和Y轴的箭头
	    int[] X = {this.XPOINT - 6, this.XPOINT, this.XPOINT + 6};
	    int[] Y = {this.XPOINT + 8, this.XPOINT, this.XPOINT + 8};
	    g.drawPolyline(X, Y, 3);
	    int[] X1 = {Heart.WIDTH - this.XPOINT - 8, Heart.WIDTH - this.XPOINT,
	               Heart.WIDTH - this.XPOINT - 8};
	    int[] Y1 = {Heart.HEIGHT - this.XPOINT - 6, Heart.HEIGHT - this.XPOINT,
	               Heart.HEIGHT - this.XPOINT + 6};
	    g.drawPolyline(X1, Y1, 3);
	    //设置像素点移动
	    this.list1.add(this.num + this.XPOINT);
	    this.num += this.TEP;
	    this.list2.add(this.round());
	    if (this.num > (Heart.WIDTH - 2 * (this.XPOINT))) {
	        this.list2.remove(0);
	    }
	    int[] xx = new int[10000];
	    int[] yy = new int[1000];
	    int j = 0;
	    //把链表中的元素导入数组
	    for (Integer elem : (this.list1)) {
	        xx[j++] = elem.intValue();
	        if(j>(Heart.WIDTH - 2 * (this.XPOINT)))break;
	    }
	    int i = 0;
	    for (Integer elem : this.list2) {
	        yy[i++] = elem.intValue();
	    }
	    g.setColor(Color.RED);
	    //绘制图形
	    g.drawPolyline(xx, yy, list2.size());
	}

	//生成随机数并控制图形在坐标中的位置
	public int round() {
	    return ((int) (Math.random() * this.YY) + Heart.HEIGHT -
	            3 * this.XPOINT - 20);
	}
//	public int round() {
//	    Scanner scanner=new Scanner(System.in);
//	    System.out.println("请输入下一个心率值");
//	    int heart=scanner.nextInt();
//	    return heart;
//	}

	public List<Integer> getList2() {
		return list2;
	}

	public void setList2(List<Integer> list2) {
		this.list2 = list2;
	}
	
}