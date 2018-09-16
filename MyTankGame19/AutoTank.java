import java.awt.Color;

import java.util.Random;

public class AutoTank extends Tank{
	private byte turnRatio = 100; 		//转向机率
	private byte fireRatio = 5;		//发射炮弹机率
	private Random random= new Random();//为随机数类创建一个叫random的对象
	//构造函数，坦克为白色。
	public AutoTank(int ax,int ay){//构造函数
		super(ax,ay);//设置位置
		setColor(Color.BLUE);//设置敌方坦克的颜色
	}
	//重写此函数在每次移动后以一定的机率转向
	public void turnDirection(){
		switch(random.nextInt(turnRatio)){
		case 1:	setDirection(Direction.LEFT);	break;
		case 2: setDirection(Direction.UP);		break;
		case 3: setDirection(Direction.RIGHT);	break;
		case 4:	setDirection(Direction.DOWN);	break;//在随机数为这四种情况下进行随机转向
		};
	}
	//重写此函数，在冷却时间到后，以一定的机率发射炮弹
	public Shell fire(){
		Shell shell = super.fire();//创建对象shell进行发射炮弹
		if(shell!=null){
			if(random.nextInt(fireRatio)!=1){
				shell = null;//五分之一的几率发射炮弹
			}
		}
		return shell;		
	}
}
