import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class MainTank extends Tank {
	private final byte LIFTVALUE=40; 	//̹�˵�������ֵ��
	 byte lifeValue = 40;		//̹�˵�ǰ����ֵ
	 byte angryValue=0;//ŭ��ֵ
	 byte tankGrade=0;//����̹�˵ļ���
	
	public MainTank(int ax,int ay){
		super(ax,ay);
		setColor(Color.BLACK);//���캯������ʼ������ ������ɫ
	}
	public byte getLifeValue(){
		return lifeValue;
	}//��ȡ����ֵ
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
	}//����̹���������ֵ˥���ȼ�����
	public void draw(Graphics g){
		super.draw(g);//����̹��Ѫ����Ѫ����ɫΪ��ɫ

		Color c = g.getColor();
		g.setColor(Color.RED);//����Ϊ��ɫ
		g.drawRect(x-LIFTVALUE/2, y-SIZE-14, LIFTVALUE,4); //����Ѫ���յĲ���
		g.fillRect(x+LIFTVALUE/2-lifeValue, y-SIZE-14, lifeValue,4);//����Ѫ�����Ĳ��֣����
						
		g.setColor(Color.blue);//����Ϊ��ɫ
		g.drawRect(x-angryValue/2, y-SIZE-10, angryValue,4); //����ŭ�����յĲ���
		g.fillRect(x+angryValue/2-angryValue, y-SIZE-10, angryValue,4);//����ŭ�������Ĳ��֣����
		g.setColor(c);		
		
		g.drawString(tankGrade+"", 100, 100);
	}
	//���¼�ʱ�������ü�λ��־�����ڷ���λ�ж�  
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
        	setDirection(Direction.DOWN);	break;//������Ӧ�ļ����ı䷽��
        }
    } 
}
