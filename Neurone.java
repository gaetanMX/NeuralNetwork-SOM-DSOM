import java.util.ArrayList;

public class Neurone {
    ArrayList<Float> poids;	//ici x,y
    ArrayList<Float> poids_angles; //
    int row,col;
    
    public Neurone(int cardinal,int row,int col){
	super();
	this.poids = new ArrayList<Float>();
	this.poids_angles = new ArrayList<Float>();
	this.row = row;
	this.col = col;
	
	for(int i=0;i<cardinal;i++){ //dimensions espace ici 2d
	    poids.add(new Float(Math.random()));
	}
	//
	poids_angles.add(new Float(Math.random()));
	//
	poids_angles.add(new Float(Math.random()));
    }
    
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public ArrayList<Float> getPoids() {
		return poids;
	}

	public void setPoids(ArrayList<Float> poids) {
		this.poids = poids;
	}

	
    public void setPoidsXY(double e, double h, Data data,boolean mode) {
	double wi;
	for(int i=0;i<poids.size();i++){
	    wi = poids.get(i);
	    if(mode){
		wi = wi + e*h*(data.getPoids().get(i) - wi); //SOM
	    }else{
		wi = wi +this.getDistanceXY(data)*h*(data.getPoids().get(i) - wi); //DSOM
	    }
	    poids.set(i, new Float(wi));
	}
    }
	
    public void setPoidsTeta(double e, double h, Data data,boolean mode) {
	double wi;
	for(int i=0;i<poids_angles.size();i++){
	    wi = poids_angles.get(i);
	    if(mode){
		wi = wi + 
		    e*h*(data.getPoids_angles().get(i) - wi); //SOM
	    }else{
		wi = wi + 
		    this.getDistanceTeta(data)*h*(data.getPoids_angles().get(i) - wi); //DSOM
	    }
	    poids_angles.set(i, new Float(wi));
	}
    }
  



    public double getDistanceXY(Data data) {
	//distance euclidienne
	double res = 0.0;
	for(int i=0;i<poids.size();i++){
	    res += Math.pow(poids.get(i)-data.getPoids().get(i),2);
	}
	return Math.sqrt(res);
    }
	
    public double getDistanceTetaEuc(Data data) {
	double distance=0.0;

	for(int i=0;i<poids_angles.size();i++){

	    distance += Math.pow(data.getPoids_angles().get(i) - this.poids_angles.get(i)
				 ,2);
	}
	return  Math.sqrt(distance);
    }

    public double getDistanceTeta(Data data) {
	double distance=0.0;//distance de manathan
	for(int i=0;i<poids_angles.size();i++){
	    distance += Math.abs(data.getPoids_angles().get(i)-this.poids_angles.get(i));
	}
	return distance;
    }

    public ArrayList<Float> getPoids_angles() {
	return poids_angles;
    }
    
    public void setPoids_angles(ArrayList<Float> poidsAngles) {
	poids_angles = poidsAngles;
    }
	
    // new

    public double distance(Data data, double factRetine, double factAngles) {
	double resultRet = 0.0;
	double resultAng = 0.0;
	
	// distance dimensions retiniennes
	for(int i=0;i<this.poids.size();i++){
	    resultRet += Math.pow(poids.get(i)-data.getPoids().get(i),2);
	}

	// distance dimensions angulaires
	for(int i=0;i<poids_angles.size();i++){
	    resultAng += Math.pow((this.poids_angles.get(i)- data.getPoids_angles().get(i)),2);
	}

	return (factRetine * Math.sqrt(resultRet) + factAngles * Math.sqrt(resultAng));
    }
   

    public double distance(Data data) {
	return this.distance(data, .5, .5);
    }


    public void dsomSetPoidsXY(double h, Data data) {
	double wi;
	for(int i=0;i<poids.size();i++){
	    wi = poids.get(i);	    
	    wi = wi +this.getDistanceXY(data)*h*(data.getPoids().get(i) - wi); //DSOM	    
	    poids.set(i, new Float(wi));
	}
    }
    
    public void dsomSetPoidsTeta(double h, Data data) {
	double wi;

	for(int i=0;i<poids_angles.size();i++) {
	    wi = poids_angles.get(i);	   
	    wi = wi + 
		this.getDistanceTeta(data)*h*(data.getPoids_angles().get(i) - wi); //DSOM 
	    
	    if((Double.isNaN(wi)) || (Double.isInfinite(wi)) || (wi < -1) ||(wi > 1) ) {
		System.out.println("Erreur mise a jour angle \t h = "+h);
		System.out.printf("neurone x = %2.3f, y = %2.3f, a1 = %2.3f, a2 = %2.3f\n",
				  this.poids.get(0), this.poids.get(1), 
				  this.poids_angles.get(0), this.poids_angles.get(1)); 
		
		System.out.printf(" %2.3f = %2.3f + %2.3f * h * (%2.3f - %2.3f)\n", wi, 
				  poids_angles.get(i), this.getDistanceTeta(data), 
				  data.getPoids_angles().get(i), poids_angles.get(i));
		System.exit(12);
	    } else {
		poids_angles.set(i, new Float(wi));
	    }
	}
    }

    public double distanceRetine(double x, double y) {
	// distance a un point(x,y) dans l'espace retinien
	
	double result = 
	    Math.pow(this.poids.get(0) - x,2) +
	    Math.pow(this.poids.get(1) - y,2);
	
	return Math.sqrt(result);
    }

 }

