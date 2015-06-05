import java.util.ArrayList;


public class Data {
	ArrayList<Float> poids;	//ici x,y 
	ArrayList<Float> poids_angles;//
	public Data(int cardinal){
		super();
		poids = new ArrayList<Float>();
		for(int i=0;i<cardinal;i++){
			poids.add(new Float(Math.random()));
		}
		poids_angles = new ArrayList<Float>();
	}

	public ArrayList<Float> getPoids_angles() {
		return poids_angles;
	}

	public void setPoids_angles(ArrayList<Float> poidsAngles) {
		poids_angles = poidsAngles;
	}

	public Data(ArrayList<Float> poids2) {
		super();
		this.poids=poids2;
	}

    public Data(Float x, Float y, Float a1, Float a2) {
	super();
	poids = new ArrayList<Float>();
	poids.add(x);
	poids.add(y);
	poids_angles = new ArrayList<Float>();
	poids_angles.add(a1);
	poids_angles.add(a2);
    }

	public Data(ArrayList<Float> poids2, ArrayList<Float> poidsAngles) {
		super();
		this.poids=poids2;
		this.poids_angles=poidsAngles;
	}

	public Data(double x,double y){
		super();
		poids = new ArrayList<Float>();
		poids.add(new Float(x));
		poids.add(new Float(y));	
	}
	
	public ArrayList<Float> getPoids() {
		return poids;
	}

	public void setPoids(ArrayList<Float> poids) {
		this.poids = poids;
	}

	public double getDistanceXY(Data interet) {
		double res = 0.0;
		for(int i=0;i<poids.size();i++){
			res += Math.pow(Math.abs((poids.get(i)-interet.getPoids().get(i))),2);
		}
		return Math.sqrt(res);
	}
}
