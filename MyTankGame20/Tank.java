import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Tank {
	public final byte  SIZE = 20;         	//坦克的大小
	private final byte  FIRECOOLTIME=10;  	//坦克的炮弹冷却时间
	private final byte ANIMATIONTRACK=3;   	//履带动画帧数
	private final byte ANIMATIONDIE=10;    	//爆炸动画帧数
	public int x;                         	//坦克中心的x坐标
	public int  y;							//坦克中心的y坐标
	private Color  color = Color.WHITE;		//坦克颜色
	private Direction  dir=Direction.UP;	//坦克方向
	public byte  velocity = 2;				//坦克速度	
	private boolean  isAlive=true;			//坦克是否还活着
	private boolean  isVisible=true;		//坦克是否可见
	private byte  dieState = 0;				//坦克爆炸动画状态
	private byte  trackState = 0;			//坦克履带动画状态
	private byte fireTime = 0; 				//记录自上次发射炮弹以来的帧数
	

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
		//得到代表子弹的矩形
		Rectangle rect1 = new Rectangle(shell.x-shell.SIZE/2, shell.y-shell.SIZE/2, shell.SIZE,shell.SIZE);
		//得到代表坦克的矩形
		Rectangle rect2 = new Rectangle(x-SIZE/2,y-SIZE/2,SIZE,SIZE);
		if(rect1.intersects(rect2))
			result = true;
		return result;
	}
	//支持碰撞探测，返回代表坦克的矩形
	public Rectangle getRect(){
		return new Rectangle(x-SIZE/2,y-SIZE/2,SIZE,SIZE);
	}
	//根据当前坦克方向计算下一步的坦克坐标
	private int[] nextPosition(){
		
		int[] result = new int[2];
		result[0]=x;				//存储坦克下一个位置的x坐标
		result[1]=y;				//存储坦克下一个位置的y坐标
		//根据行驶方向计算坦克的下一个位置
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
	//移动坦克
 
	public Rectangle getNextRect(){
		int[] tmpP=nextPosition();
		return new Rectangle(tmpP[0]-SIZE/2,tmpP[1]-SIZE/2,SIZE,SIZE);
	}
	//在每一帧 里设置各种状态
	protected void calculateState(){
		//记录发射冷却帧数	
		if(fireTime<=FIRECOOLTIME)		fireTime++;   
		//循环记录坦克履带动画
		trackState++;
		if (trackState>ANIMATIONTRACK) 	trackState=0;
		//记录爆炸动画状态
		if(!isAlive){
			if(dieState<ANIMATIONDIE)		
				dieState++;
			else
				isVisible = false;
		}
		
	}
	//每帧都会执行的函数
	public void draw(Graphics g){
		calculateState();			//每帧计算状态
		Color c = g.getColor();     //保存画布上原画笔颜色		
		g.setColor(color);			//设置画布上的颜色为坦克颜色
		drawTank(g);
		
		g.drawString("Your score:"+TankGameMgr.score, 50, 50);
		if((!isAlive)&&(isVisible))
			drawExplode(g);			//绘制爆炸动画		
		g.setColor(c);        		//恢复画布上的画笔原来的颜色	
	}	
	//绘制爆炸动画函数
	private void drawExplode(Graphics g){				
		for(int i=0;i<dieState;i++)
			g.drawOval(x-i*2, y-i*2, i*4, i*4);	
	}
	//绘制坦克函数
	private void drawTank(Graphics g){		
		//画正方形坦克架
		g.drawRect(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
		//画圆形炮塔，比坦克架小2个像素
		g.drawOval(x-SIZE/2+2, y-SIZE/2+2, SIZE-4, SIZE-4);
		switch(dir){
		case LEFT:			
			g.drawRect(x-20, y-1, 20, 2);  	//画长方形炮管，宽2，长20
			drawTrackLR(g,true);          	//调用绘制履带的函数
			break;
		case RIGHT:
			g.drawRect(x, y-1, 20, 2);     	//画长方形炮管，宽2，长20
			drawTrackLR(g,false);         	//调用绘制履带的函数
			break;
		case UP:
			g.drawRect(x-1, y-20, 2, 20);  	//画长方形炮管，宽2，长20
			drawTrackUD(g,true);       		//调用绘制履带的函数
			break;
		case DOWN:
			g.drawRect(x-1, y, 2, 20);   	//画长方形炮管，宽2，长20
			drawTrackUD(g,false);          	//调用绘制履带的函数			
			break;			
		}
	}
	//坦克左右行驶时，绘制履带动画
	private void drawTrackLR(Graphics g,boolean isLeft){	
		int tempNum;
		if(isLeft) tempNum = ANIMATIONTRACK-trackState;
		else tempNum = trackState;
		g.drawRect(x-12, y-14, 24, 4);	//画上边长方形履带，宽4，长24			
		g.drawRect(x-12, y+10, 24, 4);	//画下边长方形履带，宽4，长24		
		for(int i=tempNum;i<24;i=i+3){
			g.drawLine(x-12+i, y-14, x-12+i, y-10);
			g.drawLine(x-12+i, y+10, x-12+i, y+14);
		}
	}
	//坦克上下行驶时，绘制履带动画
	private void drawTrackUD(Graphics g,boolean isUp){	
		int tempNum;
		if(isUp) tempNum = ANIMATIONTRACK-trackState;
		else tempNum = trackState;
		g.drawRect(x-14, y-12, 4, 24);	//画左边长方形履带，宽4，长24	
		g.drawRect(x+10, y-12, 4, 24);	//画右边长方形履带，宽4，长24
		for(int i=tempNum;i<24;i=i+3){
			g.drawLine(x-14, y-12+i, x-10, y-12+i);
			g.drawLine(x+10, y-12+i, x+14, y-12+i);
		}
	}
	//移动坦克
	public void move(){
		int[] tmpP=nextPosition();
		x = tmpP[0];
		y = tmpP[1];
	}
	//发射炮弹函数
	public Shell fire(){
    	Shell shell=null;
    	if(fireTime>=FIRECOOLTIME){
    		fireTime = 0;           //重置炮弹冷却时间     		
	    	int shellx=0,shelly=0;	//炮弹的初始位置
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
