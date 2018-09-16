import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class MainTank extends Tank {
	private final byte LIFTVALUE=40; 	//坦克的满生命值数
	 byte lifeValue = 40;		//坦克当前生命值
	 byte angryValue=0;//怒气值
	 byte tankGrade=0;//设置坦克的级别
	
	public MainTank(int ax,int ay){
		super(ax,ay);
		setColor(Color.BLACK);//构造函数：初始化坐标 设置颜色
	}
	public byte getLifeValue(){
		return lifeValue;
	}//获取生命值
	public void damage(byte aValue){
		if(lifeValue > 0)
		{
			lifeValue -= aValue;
			if(angryValue<=40)
			{
				angryValue+=5;
			}
			else
			{
				if(tankGrade<=10)
				{
					tankGrade++;
					angryValue=0;
				}
				
			}
		}
			
		else 
			super.setAlive(false);
	}//设置坦克损毁生命值衰减度及死亡
	public void draw(Graphics g){
		super.draw(g);//绘制坦克血条，血条颜色为红色

		Color c = g.getColor();
		g.setColor(Color.RED);//设置为红色
		g.drawRect(x-LIFTVALUE/2, y-SIZE-14, LIFTVALUE,4); //绘制血条空的部分
		g.fillRect(x+LIFTVALUE/2-lifeValue, y-SIZE-14, lifeValue,4);//绘制血条满的部分：填充
						
		g.setColor(Color.blue);//设置为蓝色
		g.drawRect(x-angryValue/2, y-SIZE-10, angryValue,4); //绘制怒气条空的部分
		g.fillRect(x+angryValue/2-angryValue, y-SIZE-10, angryValue,4);//绘制怒气条满的部分：填充
		g.setColor(c);		
		
		g.drawString(tankGrade+"", 100, 100);
	}
	//按下键时进行设置键位标志，用于方向定位判断  
    public void keyPressed(KeyEvent e) {  
        int key = e.getKeyCode();
        switch(key) {
        case KeyEvent.VK_LEFT :              
            setDirection(Direction.LEFT);	break;  
        case KeyEvent.VK_UP :             
            setDirection(Direction.UP);  	break;  
        case KeyEvent.VK_RIGHT :              
        	setDirection(Direction.RIGHT); 	break;  
        case KeyEvent.VK_DOWN :               
        	setDirection(Direction.DOWN);	break;//按下相应的键，改变方向
        }
    } 
}
