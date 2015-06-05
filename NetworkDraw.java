
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;


public class NetworkDraw extends JPanel implements Observer{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private NeuralNetwork nw ;
    private Integer x_clicked;
    private Integer y_clicked;
    private int echelle;
    private int echAngles = 180;
	
    public NetworkDraw(NeuralNetwork nw2, int echelle) {
	super();
	this.nw=nw2;
	this.echelle = echelle;
		
	}
	

    public void paintComponent(Graphics g) {
	int echellex = this.echelle;
	int echelley = this.echelle;
	/*if(nw.isRobot_move()){	  
	  echellex = (int) echelle/2;
	 }
	*/
	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        
        g2.setPaint(Color.lightGray);
        for(int i=0;i<nw.getData().size();i=i+2){
	    Ellipse2D.Double d = 
		new Ellipse2D.Double(nw.getData().get(i).getPoids().get(0) * echellex,
				     nw.getData().get(i).getPoids().get(1) * echelley,
				     2,2);	    
	    g2.draw(d);
        }
        
        
        //centre d'interet - Robot_move
        /*if(nw.isRobot_move()){
	    g2.setPaint(Color.green);
	    for(int i=0;i<nw.getList_interet().size();i++) {
		Ellipse2D.Double inter = 
		    new Ellipse2D.Double(nw.getList_interet().get(i).getPoids().get(0) * echellex,
					 nw.getList_interet().get(i).getPoids().get(1) * echelley,
					 4,4);
		g2.fill(inter);
	    }
        }
	*/
	
        if(!nw.isOn()){ //rendre plus beau
	    // Enable antialiasing for shapes
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
        }
        
	if(nw.isRobot_move()){	    
	    Neurone gagnant = nw.getWinner(); 	 
	    g2.setPaint(Color.green);
	    Ellipse2D.Double n = 
		new Ellipse2D.Double(gagnant.getPoids().get(0) * echellex,
				     gagnant.getPoids().get(1) * echelley, 
				     10 , 10);
	    g2.fill(n);
	    g2.setPaint(Color.black);
	    
	    double theta1 = gagnant.getPoids_angles().get(0);
	    double theta2 = gagnant.getPoids_angles().get(1);
	    Point2D.Double ref = NeuralNetwork.getReference();
	    Point2D.Double l1 = NeuralNetwork.getPoint(theta1,NeuralNetwork.getL1(),ref);
	    Point2D.Double l2 = NeuralNetwork.getPoint(theta2,NeuralNetwork.getL2(),l1);
	    g2.drawLine((int) (ref.x * echellex), (int)(ref.y * echelley),(int) (l1.x * echellex),(int) (l1.y * echelley));
	    g2.drawLine((int)(l1.x * echellex), (int)(l1.y * echelley),(int) (l2.x * echellex),(int) (l2.y * echelley));  
	    
	    /*
	      g2.setPaint(Color.red);
	      for(int i=0;i<NeuralNetwork.getNeurones().size();i++){
	      for(int k=0;k<NeuralNetwork.getNeurones().get(i).size();k++){
	      Neurone neurone =  NeuralNetwork.getNeurones().get(i).get(k);
	      Ellipse2D.Double kz = 
	      new Ellipse2D.Double(neurone.getPoids().get(0) * echellex,
	      neurone.getPoids().get(1) * echelley,
	      5,5);
	      g2.fill(kz);
	   
	      }
	      } */
	}
    

	    
	g2.setPaint(Color.red);
	for(int i=0;i<NeuralNetwork.getNeurones().size();i++){
	    for(int k=0;k<NeuralNetwork.getNeurones().get(i).size();k++){
		Neurone neurone =  NeuralNetwork.getNeurones().get(i).get(k);
		Ellipse2D.Double n = new Ellipse2D.Double(neurone.getPoids().get(0) * echellex,
							  neurone.getPoids().get(1) * echelley,
							  5,5);
		g2.fill(n);
		// g2.draw(n);
	    }
	}
	
	
	//afficher le reseau avec theta1/2
	if(nw.isRobot() || nw.isRobot_move()){
	    g2.setPaint(Color.black);
	    for(int i=0;i<NeuralNetwork.getNeurones().size();i++){
		for(int k=0;k<NeuralNetwork.getNeurones().get(i).size();k++){
		    Neurone neurone =  NeuralNetwork.getNeurones().get(i).get(k);
		    double theta1 = neurone.getPoids_angles().get(0);
		    double theta2 = neurone.getPoids_angles().get(1);

		    Ellipse2D.Double n = new Ellipse2D.Double(300 + theta1 * echAngles,
							      200 + theta2 * echAngles,
							      5,5);

		    g2.fill(n);
		    // g2.draw(n);
		}
	    }
	}
	
	if(!nw.isRobot() && !nw.isRobot_move()){
	    g2.setPaint(Color.BLUE);
	    for(int i=0;i<NeuralNetwork.getNeurones().size();i++){
		for(int k=0;k<NeuralNetwork.getNeurones().get(i).size();k++){
		    Neurone n = NeuralNetwork.getNeurones().get(i).get(k);        	
		    ArrayList<Neurone> voisins = nw.getConnectedNeurons(n);
		    for(int j=0;j<voisins.size();j++){
			Point2D.Double origine = new Point2D.Double(n.getPoids().get(0) * echellex,
								    n.getPoids().get(1) * echelley);
			Point2D.Double finale  = new Point2D.Double(voisins.get(j).getPoids().get(0) * echellex,
								    voisins.get(j).getPoids().get(1) * echelley);
			Line2D.Double l = new Line2D.Double(origine, finale);
			g2.draw(l);
		    }
		}
	    }
	}
	
	if(x_clicked != null){
	    g2.setPaint(Color.BLACK);   	 
	    Neurone gagnant = NeuralNetwork.procheRetine(x_clicked/(echellex*1.0),y_clicked/(echelley*1.0));
	    double theta1 = gagnant.getPoids_angles().get(0);
	    double theta2 = gagnant.getPoids_angles().get(1);
	   	MapInterface.jTextPane_Theta1.setText(""+theta1* echAngles);
		MapInterface.jTextPane_Theta2.setText(""+theta2* echAngles);
	    Point2D.Double ref = NeuralNetwork.getReference();
	    Point2D.Double l1 = NeuralNetwork.getPoint(theta1,NeuralNetwork.getL1(),ref);
	    Point2D.Double l2 = NeuralNetwork.getPoint(theta2,NeuralNetwork.getL2(),l1);
	    g2.drawLine((int) (ref.x * echellex), (int)(ref.y * echelley),(int) (l1.x * echellex),(int) (l1.y * echelley));
	    g2.drawLine((int)(l1.x * echellex), (int)(l1.y * echelley),(int) (l2.x * echellex),(int) (l2.y * echelley)); 
	    g2.setPaint(Color.BLUE);
	    g2.drawLine((int) (l2.x * echellex),(int) (l2.y * echellex),
			Math.round(gagnant.getPoids_angles().get(0)* echAngles +300),
			Math.round(gagnant.getPoids_angles().get(1)* echAngles +200));	 
	    x_clicked=null;
	    y_clicked=null;
	}         
        
        revalidate();
    }



	@Override
	public void update(Observable arg0, Object arg1) {
		this.setNw((NeuralNetwork) arg1);
		repaint();
	}


	public NeuralNetwork getNw() {
		return nw;
	}


	public void setNw(NeuralNetwork nw) {
		this.nw = nw;
	}


	public void drawArm(int x, int y) {
		x_clicked=x;
		y_clicked=y;
		repaint();
	}
}
