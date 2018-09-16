import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class TankGameWindow extends JFrame   {
	public TankGameMgr tankGameMgr;
	
	public static  int GAME_WIDTH = 800;  //窗口宽度
    public static int GAME_HEIGHT = 600;  //窗口长度

	public void initFrame(){
		this.setLocation(200,200);        //设置窗体显示在屏幕的位置
		this.setSize(GAME_WIDTH,GAME_HEIGHT);            //设置窗体的大小
		this.setResizable(false);         //设置窗体大小不可调
		this.setLocation(200, 50);
		this.setTitle("坦克大战");           //设置窗体标题
		this.setBackground(Color.WHITE);  //设置窗体背景颜色
		this.addWindowListener(new WindowAdapter()    //设置窗体关闭事件响应
        {  
            public void windowClosing(WindowEvent e)  
            {  
                System.exit(0);  
            }  
        });	
		 
		tankGameMgr = new TankGameMgr();
		tankGameMgr.initGame();
	
		this.addKeyListener(new KeyMonitor());  
		
		Thread thread;                           //声明线程变量
		thread = new Thread(new PaintThread());  //创建游戏线程
		thread.start();                          //启动游戏线程
	}	
	
	private class PaintThread implements Runnable  
    {  
        public void run(){  
            while(true){ 
                repaint();  //此方法会导致窗口重绘操作
                try{  
                    Thread.sleep(50);  
                }catch(InterruptedException e){
                	e.printStackTrace();
                }  
            }  
         }  
    }
	private class KeyMonitor extends KeyAdapter {//键盘监听  		  
        public void keyReleased(KeyEvent e)        {  
        	//tank.keyReleased(e);  
        }    
        public void keyPressed(KeyEvent e) {  
        	tankGameMgr.keyPressed(e);          	//调用坦类的键码处理函数
        }          
    }
	//双缓冲技术，消除频闪  
    Image offScreenImage = null;      //声明一个新的Image对象，即第二缓存
    Graphics gOffScreen = null;

	//paint方法窗口重绘中自动调用,在此函数中实现双缓冲机制  
	public void paint(Graphics g)
    {
		//第一次使用offScreenImage时创建它，即第二缓存，后续的绘制内容全部绘制在第二缓存中
        if(offScreenImage == null) {  
        	//截取窗体所在位置的图片
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT); 
            //获得截取图片的画布
            gOffScreen = offScreenImage.getGraphics();  
        }
        
        // 调用父类的重绘方法，防止再从最底层来重绘
        super.paint(gOffScreen); 
      //清除屏幕        
        Color c = gOffScreen.getColor();  
        gOffScreen.setColor(Color.WHITE);  
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);  
        gOffScreen.setColor(c);
        //绘制所有游戏对象
        tankGameMgr.draw(gOffScreen);
        //将第二绘存中的内容一次性全部绘制到屏幕上
        g.drawImage(offScreenImage, 0, 0, null);
    }  
}
