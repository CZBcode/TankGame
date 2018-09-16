import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Tank {
	public final byte  SIZE = 20;         	//̹�˵Ĵ�С
	private final byte  FIRECOOLTIME=10;  	//̹�˵��ڵ���ȴʱ��
	private final byte ANIMATIONTRACK=3;   	//�Ĵ�����֡��
	private final byte ANIMATIONDIE=10;    	//��ը����֡��
	public int x;                         	//̹�����ĵ�x����
	public int  y;							//̹�����ĵ�y����
	private Color  color = Color.WHITE;		//̹����ɫ
	private Direction  dir=Direction.UP;	//̹�˷���
	public byte  velocity = 2;				//̹���ٶ�	
	private boolean  isAlive=true;			//̹���Ƿ񻹻���
	private boolean  isVisible=true;		//̹���Ƿ�ɼ�
	private byte  dieState = 0;				//̹�˱�ը����״̬
	private byte  trackState = 0;			//̹���Ĵ�����״̬
	private byte fireTime = 0; 				//��¼���ϴη����ڵ�������֡��
	

	public Tank(int ax,int ay){
		x = ax;
		y = ay;
	}
	public void setColor(Color clr){
		color = clr;
	}
	public void setDirection(Direction adir){
		dir = adir;
	}
	public boolean getVisible(){
		return isVisible;
	}
	public boolean getAlive(){
		return isAlive;
	}
	public void setAlive(boolean alive){
		isAlive = alive; 
	}
	
	public boolean isHitByShell(Shell shell){
		boolean result = false;
		//�õ������ӵ��ľ���
		Rectangle rect1 = new Rectangle(shell.x-shell.SIZE/2, shell.y-shell.SIZE/2, shell.SIZE,shell.SIZE);
		//�õ�����̹�˵ľ���
		Rectangle rect2 = new Rectangle(x-SIZE/2,y-SIZE/2,SIZE,SIZE);
		if(rect1.intersects(rect2))
			result = true;
		return result;
	}
	//֧����ײ̽�⣬���ش���̹�˵ľ���
	public Rectangle getRect(){
		return new Rectangle(x-SIZE/2,y-SIZE/2,SIZE,SIZE);
	}
	//���ݵ�ǰ̹�˷��������һ����̹������
	private int[] nextPosition(){
		
		int[] result = new int[2];
		result[0]=x;				//�洢̹����һ��λ�õ�x����
		result[1]=y;				//�洢̹����һ��λ�õ�y����
		//������ʻ�������̹�˵���һ��λ��
		switch(dir){
		case LEFT:	
			if(result[0]-SIZE>=10)
				result[0] -= velocity;
			break;
		case RIGHT:
			if(result[0]+30<=TankGameWindow.GAME_WIDTH)
				result[0] += velocity;
			break;
		case UP:
			if(result[1]-SIZE>=30)
				result[1] -= velocity;
			break;
		case DOWN:
			if(result[1]+SIZE+10<=TankGameWindow.GAME_HEIGHT)
				result[1] += velocity;
			break;			
		}
		return result;
	}
	//�ƶ�̹��
 
	public Rectangle getNextRect(){
		int[] tmpP=nextPosition();
		return new Rectangle(tmpP[0]-SIZE/2,tmpP[1]-SIZE/2,SIZE,SIZE);
	}
	//��ÿһ֡ �����ø���״̬
	protected void calculateState(){
		//��¼������ȴ֡��	
		if(fireTime<=FIRECOOLTIME)		fireTime++;   
		//ѭ����¼̹���Ĵ�����
		trackState++;
		if (trackState>ANIMATIONTRACK) 	trackState=0;
		//��¼��ը����״̬
		if(!isAlive){
			if(dieState<ANIMATIONDIE)		
				dieState++;
			else
				isVisible = false;
		}
		
	}
	//ÿ֡����ִ�еĺ���
	public void draw(Graphics g){
		calculateState();			//ÿ֡����״̬
		Color c = g.getColor();     //���滭����ԭ������ɫ		
		g.setColor(color);			//���û����ϵ���ɫΪ̹����ɫ
		drawTank(g);
		
		g.drawString("Your score:"+TankGameMgr.score, 50, 50);
		if((!isAlive)&&(isVisible))
			drawExplode(g);			//���Ʊ�ը����		
		g.setColor(c);        		//�ָ������ϵĻ���ԭ������ɫ	
	}	
	//���Ʊ�ը��������
	private void drawExplode(Graphics g){				
		for(int i=0;i<dieState;i++)
			g.drawOval(x-i*2, y-i*2, i*4, i*4);	
	}
	//����̹�˺���
	private void drawTank(Graphics g){		
		//��������̹�˼�
		g.drawRect(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
		//��Բ����������̹�˼�С2������
		g.drawOval(x-SIZE/2+2, y-SIZE/2+2, SIZE-4, SIZE-4);
		switch(dir){
		case LEFT:			
			g.drawRect(x-20, y-1, 20, 2);  	//���������ڹܣ���2����20
			drawTrackLR(g,true);          	//���û����Ĵ��ĺ���
			break;
		case RIGHT:
			g.drawRect(x, y-1, 20, 2);     	//���������ڹܣ���2����20
			drawTrackLR(g,false);         	//���û����Ĵ��ĺ���
			break;
		case UP:
			g.drawRect(x-1, y-20, 2, 20);  	//���������ڹܣ���2����20
			drawTrackUD(g,true);       		//���û����Ĵ��ĺ���
			break;
		case DOWN:
			g.drawRect(x-1, y, 2, 20);   	//���������ڹܣ���2����20
			drawTrackUD(g,false);          	//���û����Ĵ��ĺ���			
			break;			
		}
	}
	//̹��������ʻʱ�������Ĵ�����
	private void drawTrackLR(Graphics g,boolean isLeft){	
		int tempNum;
		if(isLeft) tempNum = ANIMATIONTRACK-trackState;
		else tempNum = trackState;
		g.drawRect(x-12, y-14, 24, 4);	//���ϱ߳������Ĵ�����4����24			
		g.drawRect(x-12, y+10, 24, 4);	//���±߳������Ĵ�����4����24		
		for(int i=tempNum;i<24;i=i+3){
			g.drawLine(x-12+i, y-14, x-12+i, y-10);
			g.drawLine(x-12+i, y+10, x-12+i, y+14);
		}
	}
	//̹��������ʻʱ�������Ĵ�����
	private void drawTrackUD(Graphics g,boolean isUp){	
		int tempNum;
		if(isUp) tempNum = ANIMATIONTRACK-trackState;
		else tempNum = trackState;
		g.drawRect(x-14, y-12, 4, 24);	//����߳������Ĵ�����4����24	
		g.drawRect(x+10, y-12, 4, 24);	//���ұ߳������Ĵ�����4����24
		for(int i=tempNum;i<24;i=i+3){
			g.drawLine(x-14, y-12+i, x-10, y-12+i);
			g.drawLine(x+10, y-12+i, x+14, y-12+i);
		}
	}
	//�ƶ�̹��
	public void move(){
		int[] tmpP=nextPosition();
		x = tmpP[0];
		y = tmpP[1];
	}
	//�����ڵ�����
	public Shell fire(){
    	Shell shell=null;
    	if(fireTime>=FIRECOOLTIME){
    		fireTime = 0;           //�����ڵ���ȴʱ��     		
	    	int shellx=0,shelly=0;	//�ڵ��ĳ�ʼλ��
	    	switch(dir){
			case LEFT:	shellx = x-24;	shelly = y;	break;
			case RIGHT:	shellx = x+24; 	shelly = y;	break;
			case UP:	shellx = x;  	shelly = y-24;	break;
			case DOWN:	shellx = x;  	shelly = y+24;	break;
			}
	    	shell = new Shell(shellx,shelly);
	    	shell.setColor(color);
	    	shell.setDirection(dir);
		}    	
    	return shell;
    }
}
