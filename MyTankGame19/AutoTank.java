import java.awt.Color;

import java.util.Random;

public class AutoTank extends Tank{
	private byte turnRatio = 100; 		//ת�����
	private byte fireRatio = 5;		//�����ڵ�����
	private Random random= new Random();//Ϊ������ഴ��һ����random�Ķ���
	//���캯����̹��Ϊ��ɫ��
	public AutoTank(int ax,int ay){//���캯��
		super(ax,ay);//����λ��
		setColor(Color.BLUE);//���õз�̹�˵���ɫ
	}
	//��д�˺�����ÿ���ƶ�����һ���Ļ���ת��
	public void turnDirection(){
		switch(random.nextInt(turnRatio)){
		case 1:	setDirection(Direction.LEFT);	break;
		case 2: setDirection(Direction.UP);		break;
		case 3: setDirection(Direction.RIGHT);	break;
		case 4:	setDirection(Direction.DOWN);	break;//�������Ϊ����������½������ת��
		};
	}
	//��д�˺���������ȴʱ�䵽����һ���Ļ��ʷ����ڵ�
	public Shell fire(){
		Shell shell = super.fire();//��������shell���з����ڵ�
		if(shell!=null){
			if(random.nextInt(fireRatio)!=1){
				shell = null;//���֮һ�ļ��ʷ����ڵ�
			}
		}
		return shell;		
	}
}
