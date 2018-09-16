import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankGameMgr {
	private MainTank mainTank;									//主坦克
	private List<Shell> mainShells 	= new ArrayList<Shell>();	//主坦克发射的炮弹列表
	private List<AutoTank> autoTanks= new ArrayList<AutoTank>();//自动坦克列表
	private List<Shell> autoShells 	= new ArrayList<Shell>();	//自动坦克发射的炮弹列表
	private List<Block> blocks = new ArrayList<Block>();		//障碍物列表
	private short createTankRatio=200;						 	//创建坦克的机率

	
	private Random random = new Random();
	public void initGame(){
		Random rand = new Random();
		mainTank = new MainTank(300,300);
		//生机随机数量的障碍物块
		int num = rand.nextInt(10)+3;
		for(int i=0;i<num;i++)
			blocks.add(new Block());
	}
	//以4/200的机率创建坦克
	private void createAutoTank(){
		AutoTank tank=null;
		
		int r1 = random.nextInt(createTankRatio); //是否生成坦克，在哪个角生成坦克
		int r2 = random.nextInt(2); //坦克的运行方向
		switch(r1){
		case 0:           //左上角
			tank = new AutoTank(30,50);
			if(r2==0)tank.setDirection(Direction.RIGHT);
			else	 tank.setDirection(Direction.DOWN);
			break;
		case 1:            //右上角
			tank = new AutoTank(770,50);
			if(r2==0)tank.setDirection(Direction.LEFT);
			else  	 tank.setDirection(Direction.DOWN);
			break;
		case 2:            //右下角
			tank = new AutoTank(770,570);
			if(r2==0)tank.setDirection(Direction.LEFT);
			else 	 tank.setDirection(Direction.UP);
			break;
		case 3:
			tank = new AutoTank(30,570);
			if(r2==0)tank.setDirection(Direction.RIGHT);
			else	 tank.setDirection(Direction.UP);			
		}
		if(tank != null)
			autoTanks.add(tank);
	}
	//检测坦克与blocks的碰撞
	private boolean isTankHitBlock(Tank tank){
		boolean result=false;
		for(int i=0;i<blocks.size();i++){
			Block block = blocks.get(i);
			//探测坦克的下一步是否会碰到障碍物
			if(block.getRect().intersects(tank.getNextRect()))
				result = true;			
		}
		return result;
	}
	//检测炮弹与blocks的碰撞
	private boolean isShellHitBlock(Shell shell){
		boolean result=false;
		for(int i=0;i<blocks.size();i++){
			Block block = blocks.get(i);
			if(block.getRect().intersects(shell.getRect()))
				result = true;			
		}
		return result;
	}
	//处理、移动、绘制主坦克
	private void handleDrawMainTank(Graphics g){
		//移动、绘制主坦克
		if(mainTank.getAlive()){
			if(!isTankHitBlock(mainTank))
				mainTank.move();
		}else
			mainTank.setColor(Color.RED);
		mainTank.draw(g);
	}
	//isMain==true 处理、移动、绘制主坦克炮弹
	//isMain==false处理、移动、绘制自动坦克炮弹
	private void handleDrawShells(Graphics g,boolean isMain){
		List<Shell> tmpList;
		Shell shell;
		AutoTank tank;
		
		if(isMain)  tmpList = mainShells;	//如果isMain==true，处理主坦克炮弹		
		else 		tmpList = autoShells;	//如果isMain==true，处理自动坦克炮弹
		
		//遍历炮弹，检查、计算、移动并绘制		
		for(int i=tmpList.size()-1;i>=0;i--){
			shell = (Shell)tmpList.get(i);
			if(shell.getAlive()){
				if(isShellHitBlock(shell))//检查炮弹是否碰到障碍物
					shell.setAlive(false);
				else{
					shell.move();	//移动炮弹
					if(isMain){		//如果是主坦克炮弹
						//遍历所有的自动坦克，看该炮弹是否击中之
						for(int j=autoTanks.size()-1;j>=0;j--){
							tank = autoTanks.get(j);
							if(tank.isHitByShell(shell)){
								if(mainTank .lifeValue>0&&mainTank .lifeValue<35)
								{
									mainTank .lifeValue+=5;//奖励：击中回血
								}
								shell.setAlive(false);
								tank.setAlive(false);						
							}						
						}
					}else{		//如果是自动坦克炮弹						
						//看该炮弹是否击中主坦克				
						if(mainTank.isHitByShell(shell)){
							shell.setAlive(false);	
							mainTank.damage(shell.getDamageValue());					
						}
					}					
				}
			}
			//如果子弹还有效，则绘制，否则移除
			if(shell.getVisible())			
				shell.draw(g);	//绘制炮弹
			else
				tmpList.remove(i);
		}
	}
	//处理、移动、绘制自动坦克
	private void handleDrawAutoTanks(Graphics g){
		AutoTank tank;
		Shell shell;
		//遍历自动坦克，移动并绘制
		for(int i=autoTanks.size()-1;i>0;i--){
			tank = (AutoTank)autoTanks.get(i);
			//如果坦克还活着，则移动，并发射炮弹
			if(tank.getAlive()){
				if(!isTankHitBlock(tank)) 
					tank.move();
				tank.turnDirection();
				shell = tank.fire();
				if(shell != null)
					autoShells.add(shell);
			}
			//如果坦克还可见，则绘制，否则移除
			if(tank.getVisible()){
				tank.draw(g);
			}else
				autoTanks.remove(i);
		}
	}
	//处理、移动、绘制障碍物
	private void handleDrawBlocks(Graphics g){
		//遍历blocks块，绘制地图
		for(int i=0;i<blocks.size();i++){
			Block block;
			block = blocks.get(i);
			block.draw(g);
		}
	}
	public void draw(Graphics g){
		//以一定机率产生新的自动坦克
		createAutoTank();		
		//处理、移动、绘制主坦克
		handleDrawMainTank(g);	
		//处理、移动、绘制主坦克炮弹
		handleDrawShells(g,true);
		//处理、移动、绘制自动坦克
		handleDrawAutoTanks(g);
		//处理、移动、绘制自动坦克炮弹
		handleDrawShells(g,false);
		//处理、移动、绘制障碍物
		handleDrawBlocks(g);
	}
	//按下键时进行设置键位标志，用于方向定位判断  
    public void keyPressed(KeyEvent e) {  
    	 int key = e.getKeyCode();
         switch(key) {
         case KeyEvent.VK_SPACE :               
         	Shell shell = mainTank.fire();
         	if(shell!=null)mainShells.add(shell);
         	break;
         }
        mainTank.keyPressed(e);
    } 	
}
