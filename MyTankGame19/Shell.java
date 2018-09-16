import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Shell {	
	private final byte ANIMATIONDIE=5;		//爆炸动画帧数
	public  final byte SIZE=2;				//炮弹直径		
	public int x;							//炮弹坐标
	public int y;
	private byte velocity = 6;            	//炮弹的速度	
	private Color color=Color.RED;			//炮弹颜色
	private Direction dir = Direction.UP;	//炮弹方向	
	private byte damageValue=1;				//炮弹的伤害值
	private boolean  isAlive=true;			//炮弹是否还活着
	private boolean  isVisible=true;		//炮弹是否可见
	
	private byte dieState = 0;      		//爆炸动画状态
		
	//自定义构造函数
	public Shell(int ax,int ay){
		x = ax;
		y = ay;		
	}
	public void setColor(Color clr){
		color = clr;
	}
	public void setDirection(Direction d){
		dir = d;
	}
	public void setAlive(boolean s){
		isAlive = s;		
	}
	public boolean getAlive(){
		return isAlive;
	}
	public boolean getVisible(){
		return isVisible;
	}
	public byte getDamageValue(){
		return damageValue;
 	}
	//支持碰撞探测，返回代表炮弹的矩形
	public Rectangle getRect(){
		return new Rectangle(x-SIZE/2, y-SIZE/2, SIZE,SIZE);
	}
	//在每一帧 里设置各种状态
	private void calculateState(){
		if(!isAlive){
			if(dieState<ANIMATIONDIE) 
				dieState++;
			else
				isVisible = false;
		}
	}
	//此函数为每一帧都会调用的函数
	public void draw(Graphics g){
		calculateState();
		if(isAlive){             		//炮弹有效，直接绘制			
			Color c = g.getColor();     //保存画布上原画笔颜色	
			g.setColor(color);			//设置画布上的颜色为坦克颜色					
			g.drawOval(x-SIZE/2, y-SIZE/2, SIZE,SIZE); //画圆形炮弹
			g.setColor(c);        		//恢复画布上的画笔原来的颜色
		}else if(isVisible){ 			//炮弹无生命时启动爆炸动画
			Color c = g.getColor();     //保存画布上原画笔颜色	
			g.setColor(color);			//设置画布上的颜色为坦克颜色
			dieState++;
			g.drawOval(x-(SIZE+dieState)/2, y-(SIZE+dieState)/2, SIZE+dieState,SIZE+dieState); //画圆形炮弹
			g.setColor(c);        		//恢复画布上的画笔原来的颜色
		}
	}
	//炮弹的移动函数，超出边界爆炸
	public void move(){
		//进行边界探测
		boolean flag = true;
		if((x<10) || (x>TankGameWindow.GAME_WIDTH-10)
		||(y<30) || (y>TankGameWindow.GAME_HEIGHT-10)){
			isAlive = false;
			flag = false;
		}
		//坦克活着，同时没有碰到边界则移动
		if(isAlive && flag){
			switch(dir){
			case LEFT:	x -= velocity;	break;
			case RIGHT:	x += velocity;	break;
			case UP:	y -= velocity;	break;
			case DOWN:	y += velocity;	break;
			}		
		}
	}
}
