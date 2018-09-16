import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/*
 * 用于制造障碍物
 * */
public class Block {
	private final byte SIZE=50;		//大小
	public int x;					//石块左上角坐标
	public int y;
	private byte number=1;			//石块包含的方块个数
	private boolean isVertical=true;//石块是否竖向排列	
	private Color color=Color.BLUE;	//石块颜色	
	
	private static Toolkit tkk = Toolkit.getDefaultToolkit();
	private static Image[] blockImages = null;  
	private static Map<String, Image> imgbs = new HashMap<String, Image>();  
	
	static 
	{
		blockImages=new  Image[]
				{
						tkk.getImage(Block.class.getClassLoader().getResource("Image/img01.png")),
						tkk.getImage(Block.class.getClassLoader().getResource("Image/img02.jpg")),
						tkk.getImage(Block.class.getClassLoader().getResource("Image/img03.jpg")),
				};
	}
	//自定义构造函数,随机产生石块
	public Block(){
		Random rand= new Random();				
		isVertical = rand.nextBoolean();
		if(isVertical){
			number = (byte)(rand.nextInt(3)+3);
			x = rand.nextInt(TankGameWindow.GAME_WIDTH-50)+10;
			y = rand.nextInt(TankGameWindow.GAME_HEIGHT-40-number*SIZE)+30;			
		}else{	
			number = (byte)(rand.nextInt(3)+3);
			x = rand.nextInt(TankGameWindow.GAME_WIDTH-20-number*SIZE)+10;
			y = rand.nextInt(TankGameWindow.GAME_HEIGHT-70)+30;
		}
	}
	//构造函数重载，指定大小创建block
	public Block(int ax,int ay,byte aNumber,boolean aIsV){
		x = ax;
		y = ay;
		number = aNumber;
		isVertical = aIsV;
	}
	//支持碰撞探测，返回代表方块的矩形
	public Rectangle getRect(){
		Rectangle rect=null;
		if(isVertical)
			rect = new Rectangle(x,y,SIZE,SIZE*number);
		else
			rect = new Rectangle(x,y,SIZE*number,SIZE);
		return rect;//新建矩形障碍
	}

	//此函数为每一帧都会调用的函数
	public void draw(Graphics g){
		int tmpx,tmpy;
		tmpx = x;
		tmpy = y;
		Color c = g.getColor();     //保存画布上原画笔颜色	
		g.setColor(color);			//设置画布上的颜色为坦克颜色
		
		g.drawImage(blockImages[2], x-10, y-10, null);
//		for(int i=0;i<number;i++){
//			g.drawRect(tmpx, tmpy, SIZE, SIZE);
//		g.drawRect(tmpx+5, tmpy+5,SIZE-10,SIZE-10);
//		g.drawLine(tmpx, tmpy, tmpx+SIZE, tmpy+SIZE);
//		g.drawLine(tmpx, tmpy+SIZE, tmpx+SIZE, tmpy);
//			if(isVertical)
//				tmpy += SIZE;//每个障碍物的坐标 发生变化
//			else
//				tmpx += SIZE;			
//		}		
		g.setColor(c);        		//恢复画布上的画笔原来的颜色		
	}	
}
