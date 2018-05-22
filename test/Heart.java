package test;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

public class Heart extends JFrame { 
	protected static final int WIDTH = 800; 
	protected static final int HEIGHT = 500; 
	private java.awt.Dimension dimension = java.awt.Toolkit.getDefaultToolkit().
			getScreenSize();
	Mypanel panel = new Mypanel();
	public Heart(String title) { 
		super(title); 
		this.setSize(WIDTH, HEIGHT); 
		this.setLocation((dimension.width - WIDTH) / 2, 
				(dimension.height - HEIGHT) / 2); 
		this.add(panel); 
	}
	//Timer事件
	public javax.swing.Timer timer = new javax.swing.Timer(20,
			new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			panel.repaint(); //重画面板
			System.out.println(panel.getList2().size());
		}
	});
	public static void main(String[] args) {
		Heart hart = new Heart("模拟心电图");
		hart.setVisible(true);
		hart.timer.start();
		hart.setDefaultCloseOperation(hart.EXIT_ON_CLOSE);
	}
}