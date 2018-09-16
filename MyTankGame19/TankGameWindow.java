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
	
	public static  int GAME_WIDTH = 800;  //���ڿ��
    public static int GAME_HEIGHT = 600;  //���ڳ���

	public void initFrame(){
		this.setLocation(200,200);        //���ô�����ʾ����Ļ��λ��
		this.setSize(GAME_WIDTH,GAME_HEIGHT);            //���ô���Ĵ�С
		this.setResizable(false);         //���ô����С���ɵ�
		this.setLocation(200, 50);
		this.setTitle("̹�˴�ս");           //���ô������
		this.setBackground(Color.WHITE);  //���ô��屳����ɫ
		this.addWindowListener(new WindowAdapter()    //���ô���ر��¼���Ӧ
        {  
            public void windowClosing(WindowEvent e)  
            {  
                System.exit(0);  
            }  
        });	
		 
		tankGameMgr = new TankGameMgr();
		tankGameMgr.initGame();
	
		this.addKeyListener(new KeyMonitor());  
		
		Thread thread;                           //�����̱߳���
		thread = new Thread(new PaintThread());  //������Ϸ�߳�
		thread.start();                          //������Ϸ�߳�
	}	
	
	private class PaintThread implements Runnable  
    {  
        public void run(){  
            while(true){ 
                repaint();  //�˷����ᵼ�´����ػ����
                try{  
                    Thread.sleep(50);  
                }catch(InterruptedException e){
                	e.printStackTrace();
                }  
            }  
         }  
    }
	private class KeyMonitor extends KeyAdapter {//���̼���  		  
        public void keyReleased(KeyEvent e)        {  
        	//tank.keyReleased(e);  
        }    
        public void keyPressed(KeyEvent e) {  
        	tankGameMgr.keyPressed(e);          	//����̹��ļ��봦����
        }          
    }
	//˫���弼��������Ƶ��  
    Image offScreenImage = null;      //����һ���µ�Image���󣬼��ڶ�����
    Graphics gOffScreen = null;

	//paint���������ػ����Զ�����,�ڴ˺�����ʵ��˫�������  
	public void paint(Graphics g)
    {
		//��һ��ʹ��offScreenImageʱ�����������ڶ����棬�����Ļ�������ȫ�������ڵڶ�������
        if(offScreenImage == null) {  
        	//��ȡ��������λ�õ�ͼƬ
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT); 
            //��ý�ȡͼƬ�Ļ���
            gOffScreen = offScreenImage.getGraphics();  
        }
        
        // ���ø�����ػ淽������ֹ�ٴ���ײ����ػ�
        super.paint(gOffScreen); 
      //�����Ļ        
        Color c = gOffScreen.getColor();  
        gOffScreen.setColor(Color.WHITE);  
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);  
        gOffScreen.setColor(c);
        //����������Ϸ����
        tankGameMgr.draw(gOffScreen);
        //���ڶ�����е�����һ����ȫ�����Ƶ���Ļ��
        g.drawImage(offScreenImage, 0, 0, null);
    }  
}
