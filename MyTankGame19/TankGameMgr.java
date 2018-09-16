import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TankGameMgr {
	private MainTank mainTank;									//��̹��
	private List<Shell> mainShells 	= new ArrayList<Shell>();	//��̹�˷�����ڵ��б�
	private List<AutoTank> autoTanks= new ArrayList<AutoTank>();//�Զ�̹���б�
	private List<Shell> autoShells 	= new ArrayList<Shell>();	//�Զ�̹�˷�����ڵ��б�
	private List<Block> blocks = new ArrayList<Block>();		//�ϰ����б�
	private short createTankRatio=200;						 	//����̹�˵Ļ���

	
	private Random random = new Random();
	public void initGame(){
		Random rand = new Random();
		mainTank = new MainTank(300,300);
		//��������������ϰ����
		int num = rand.nextInt(10)+3;
		for(int i=0;i<num;i++)
			blocks.add(new Block());
	}
	//��4/200�Ļ��ʴ���̹��
	private void createAutoTank(){
		AutoTank tank=null;
		
		int r1 = random.nextInt(createTankRatio); //�Ƿ�����̹�ˣ����ĸ�������̹��
		int r2 = random.nextInt(2); //̹�˵����з���
		switch(r1){
		case 0:           //���Ͻ�
			tank = new AutoTank(30,50);
			if(r2==0)tank.setDirection(Direction.RIGHT);
			else	 tank.setDirection(Direction.DOWN);
			break;
		case 1:            //���Ͻ�
			tank = new AutoTank(770,50);
			if(r2==0)tank.setDirection(Direction.LEFT);
			else  	 tank.setDirection(Direction.DOWN);
			break;
		case 2:            //���½�
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
	//���̹����blocks����ײ
	private boolean isTankHitBlock(Tank tank){
		boolean result=false;
		for(int i=0;i<blocks.size();i++){
			Block block = blocks.get(i);
			//̽��̹�˵���һ���Ƿ�������ϰ���
			if(block.getRect().intersects(tank.getNextRect()))
				result = true;			
		}
		return result;
	}
	//����ڵ���blocks����ײ
	private boolean isShellHitBlock(Shell shell){
		boolean result=false;
		for(int i=0;i<blocks.size();i++){
			Block block = blocks.get(i);
			if(block.getRect().intersects(shell.getRect()))
				result = true;			
		}
		return result;
	}
	//�����ƶ���������̹��
	private void handleDrawMainTank(Graphics g){
		//�ƶ���������̹��
		if(mainTank.getAlive()){
			if(!isTankHitBlock(mainTank))
				mainTank.move();
		}else
			mainTank.setColor(Color.RED);
		mainTank.draw(g);
	}
	//isMain==true �����ƶ���������̹���ڵ�
	//isMain==false�����ƶ��������Զ�̹���ڵ�
	private void handleDrawShells(Graphics g,boolean isMain){
		List<Shell> tmpList;
		Shell shell;
		AutoTank tank;
		
		if(isMain)  tmpList = mainShells;	//���isMain==true��������̹���ڵ�		
		else 		tmpList = autoShells;	//���isMain==true�������Զ�̹���ڵ�
		
		//�����ڵ�����顢���㡢�ƶ�������		
		for(int i=tmpList.size()-1;i>=0;i--){
			shell = (Shell)tmpList.get(i);
			if(shell.getAlive()){
				if(isShellHitBlock(shell))//����ڵ��Ƿ������ϰ���
					shell.setAlive(false);
				else{
					shell.move();	//�ƶ��ڵ�
					if(isMain){		//�������̹���ڵ�
						//�������е��Զ�̹�ˣ������ڵ��Ƿ����֮
						for(int j=autoTanks.size()-1;j>=0;j--){
							tank = autoTanks.get(j);
							if(tank.isHitByShell(shell)){
								if(mainTank .lifeValue>0&&mainTank .lifeValue<35)
								{
									mainTank .lifeValue+=5;//���������л�Ѫ
								}
								shell.setAlive(false);
								tank.setAlive(false);						
							}						
						}
					}else{		//������Զ�̹���ڵ�						
						//�����ڵ��Ƿ������̹��				
						if(mainTank.isHitByShell(shell)){
							shell.setAlive(false);	
							mainTank.damage(shell.getDamageValue());					
						}
					}					
				}
			}
			//����ӵ�����Ч������ƣ������Ƴ�
			if(shell.getVisible())			
				shell.draw(g);	//�����ڵ�
			else
				tmpList.remove(i);
		}
	}
	//�����ƶ��������Զ�̹��
	private void handleDrawAutoTanks(Graphics g){
		AutoTank tank;
		Shell shell;
		//�����Զ�̹�ˣ��ƶ�������
		for(int i=autoTanks.size()-1;i>0;i--){
			tank = (AutoTank)autoTanks.get(i);
			//���̹�˻����ţ����ƶ����������ڵ�
			if(tank.getAlive()){
				if(!isTankHitBlock(tank)) 
					tank.move();
				tank.turnDirection();
				shell = tank.fire();
				if(shell != null)
					autoShells.add(shell);
			}
			//���̹�˻��ɼ�������ƣ������Ƴ�
			if(tank.getVisible()){
				tank.draw(g);
			}else
				autoTanks.remove(i);
		}
	}
	//�����ƶ��������ϰ���
	private void handleDrawBlocks(Graphics g){
		//����blocks�飬���Ƶ�ͼ
		for(int i=0;i<blocks.size();i++){
			Block block;
			block = blocks.get(i);
			block.draw(g);
		}
	}
	public void draw(Graphics g){
		//��һ�����ʲ����µ��Զ�̹��
		createAutoTank();		
		//�����ƶ���������̹��
		handleDrawMainTank(g);	
		//�����ƶ���������̹���ڵ�
		handleDrawShells(g,true);
		//�����ƶ��������Զ�̹��
		handleDrawAutoTanks(g);
		//�����ƶ��������Զ�̹���ڵ�
		handleDrawShells(g,false);
		//�����ƶ��������ϰ���
		handleDrawBlocks(g);
	}
	//���¼�ʱ�������ü�λ��־�����ڷ���λ�ж�  
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
