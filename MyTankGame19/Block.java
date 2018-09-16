import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
/*
 * ���������ϰ���
 * */
public class Block {
	private final byte SIZE=30;		//��С
	public int x;					//ʯ�����Ͻ�����
	public int y;
	private byte number=1;			//ʯ������ķ������
	private boolean isVertical=true;//ʯ���Ƿ���������	
	private Color color=Color.BLUE;	//ʯ����ɫ	

	
	//�Զ��幹�캯��,�������ʯ��
	public Block(){
		Random rand= new Random();				
		isVertical = rand.nextBoolean();//��������Ƿ���������
		if(isVertical){//��������
			number = (byte)(rand.nextInt(10)+3);//�������������3��13�в���
			x = rand.nextInt(TankGameWindow.GAME_WIDTH-50)+10;
			y = rand.nextInt(TankGameWindow.GAME_HEIGHT-40-number*SIZE)+30;//�������λ��			
		}else{	
			number = (byte)(rand.nextInt(15)+3);//��������
			x = rand.nextInt(TankGameWindow.GAME_WIDTH-20-number*SIZE)+10;
			y = rand.nextInt(TankGameWindow.GAME_HEIGHT-70)+30;
		}
	}
	//���캯�����أ�ָ����С����block
	public Block(int ax,int ay,byte aNumber,boolean aIsV){
		x = ax;
		y = ay;
		number = aNumber;
		isVertical = aIsV;
	}
	//֧����ײ̽�⣬���ش�����ľ���
	public Rectangle getRect(){
		Rectangle rect=null;
		if(isVertical)
			rect = new Rectangle(x,y,SIZE,SIZE*number);
		else
			rect = new Rectangle(x,y,SIZE*number,SIZE);
		return rect;//�½������ϰ�
	}

	//�˺���Ϊÿһ֡������õĺ���
	public void draw(Graphics g){
		int tmpx,tmpy;
		tmpx = x;
		tmpy = y;
		Color c = g.getColor();     //���滭����ԭ������ɫ	
		g.setColor(color);			//���û����ϵ���ɫΪ̹����ɫ	
		for(int i=0;i<number;i++){
			g.drawRect(tmpx, tmpy, SIZE, SIZE);
			g.drawRect(tmpx+5, tmpy+5,SIZE-10,SIZE-10);
			g.drawLine(tmpx, tmpy, tmpx+SIZE, tmpy+SIZE);
			g.drawLine(tmpx, tmpy+SIZE, tmpx+SIZE, tmpy);
			if(isVertical)
				tmpy += SIZE;//ÿ���ϰ�������� �����仯
			else
				tmpx += SIZE;			
		}		
		g.setColor(c);        		//�ָ������ϵĻ���ԭ������ɫ		
	}	
}
