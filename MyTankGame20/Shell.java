import java.awt.*;  
import java.awt.event.*;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.Random; 


public class Shell {	
	private final byte ANIMATIONDIE=5;		//��ը����֡��
	public  final byte SIZE=2;				//�ڵ�ֱ��		
	public int x;							//�ڵ�����
	public int y;
	private byte velocity = 6;            	//�ڵ����ٶ�	
	private Color color=Color.RED;			//�ڵ���ɫ
	private Direction dir = Direction.UP;	//�ڵ�����	
	private byte damageValue=1;				//�ڵ����˺�ֵ
	private boolean  isAlive=true;			//�ڵ��Ƿ񻹻���
	private boolean  isVisible=true;		//�ڵ��Ƿ�ɼ�
	private byte dieState = 0;      		//��ը����״̬
	private static Random r = new Random(); 
	private int num = r.nextInt(4);
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] shellImages = null;  //�ҵ�̹��
	private static Map<String, Image> imgs = new HashMap<String, Image>();  //����ҵ�̹�˵ļ���

	static {
	 
	//�ҵ�̹��
	shellImages = new Image[] {
	 
	tk.getImage(Shell.class.getClassLoader().getResource("image/1.jpg")),
	tk.getImage(Shell.class.getClassLoader().getResource("image/2.jpg")),
	tk.getImage(Shell.class.getClassLoader().getResource("image/3.jpg")),
	tk.getImage(Shell.class.getClassLoader().getResource("image/4.jpg"))
	 
	};

	}
	//�Զ��幹�캯��
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
	//֧����ײ̽�⣬���ش����ڵ��ľ���
	public Rectangle getRect(){
		return new Rectangle(x-SIZE/2, y-SIZE/2, SIZE,SIZE);
	}
	//��ÿһ֡ �����ø���״̬
	private void calculateState(){
		if(!isAlive){
			if(dieState<ANIMATIONDIE) 
				dieState++;
			else
				isVisible = false;
		}
	}
	//�˺���Ϊÿһ֡������õĺ���
	public void draw(Graphics g){
		calculateState();
		if(isAlive){             		//�ڵ���Ч��ֱ�ӻ���			
			Color c = g.getColor();     //���滭����ԭ������ɫ	
			g.setColor(color);			//���û����ϵ���ɫΪ̹����ɫ					
			switch(num){
			case 0:
				g.drawImage(shellImages[0], x-18, y-18, null);
				break;
			case 1:
				g.drawImage(shellImages[1], x-18, y-18, null);
				break;
			case 2:
				g.drawImage(shellImages[2], x-18, y-18, null);
				break;
			case 3:
				g.drawImage(shellImages[3], x-18, y-18, null);
				break;
			
			}
			g.setColor(c);        		//�ָ������ϵĻ���ԭ������ɫ
		}else if(isVisible){ 			//�ڵ�������ʱ������ը����
			Color c = g.getColor();     //���滭����ԭ������ɫ	
			g.setColor(color);			//���û����ϵ���ɫΪ̹����ɫ
			dieState++;
			g.drawOval(x-(SIZE+dieState)/2, y-(SIZE+dieState)/2, SIZE+dieState,SIZE+dieState); //��Բ���ڵ�
			g.setColor(c);        		//�ָ������ϵĻ���ԭ������ɫ
		}
	}
	//�ڵ����ƶ������������߽籬ը
	public void move(){
		//���б߽�̽��
		boolean flag = true;
		if((x<10) || (x>TankGameWindow.GAME_WIDTH-10)
		||(y<30) || (y>TankGameWindow.GAME_HEIGHT-10)){
			isAlive = false;
			flag = false;
		}
		//̹�˻��ţ�ͬʱû�������߽����ƶ�
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
