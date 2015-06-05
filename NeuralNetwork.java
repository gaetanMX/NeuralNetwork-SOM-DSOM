import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.io.*;

public class NeuralNetwork extends Observable implements Runnable{
	private boolean data_paint = false;
	private static ArrayList<ArrayList<Neurone>> neurones;
    private ArrayList<Data> data;
    private ArrayList<Data> picked;
    private int numRow,numCol;
    //private static int echelle = 500;
    private int max_iter = 500000;//40000;//2000;
    private static int dimension = 2; //ici 2d  
    private Neurone winner;
    private Data currentData; //data courrante étudié pour robot_move
    //robot
    private Data interet = new Data(.5,.5);
    //private ArrayList<Data> list_interet;
	
    private static Point2D.Double reference = new Point2D.Double(0, 0.5); 
    //point d'où part L1 debut du robot

    private static double l1 = .25; //distance L1
    private static double l2 = .25; //distance L2

    private static double masterl1 = .25; //distance L1
    private static double masterl2 = .25; //distance L2

	
    private int nb_data = 100000; //nombre de données
    private boolean mode=true; //mode == true => SOM sinon DSOM, 
    private boolean compar=true; //compar == true => distribution dynamique des données 
    private boolean robot=false; //mode robot
    private boolean robot_move=false;
    private char   parcours = 'M'; // definir le type de parcous pour le bras 
    private ArrayList<Data> list_interet;//data pour zone d'interet
    private boolean on=true;//on/off
    //taux pour le déplacement
    private double e_init = 0.5; // U pour le vainqueur
    private double e_final = 0.05; // U pour le vainqueur
    private double sigma_init = 10; // B pour les voisins du vainqueur
    private double sigma_final = 0.01;
    private double elasticity = 2.3;// 15.5; avec limite à 20
    //private double epsilon = 0.00001;//
    
    private Random generatorx = new Random();
    private Random generatory = new Random();
    private double erreur  = .25;

    public NeuralNetwork(int col, int ligne) {
	numRow=ligne;
	numCol=col;
	//creation des neurones
	neurones = new ArrayList<ArrayList<Neurone>>();
		
	for(int k=0;k<ligne;k++){
	    neurones.add(new ArrayList<Neurone>());
	    for(int k1=0;k1<col;k1++){
		neurones.get(k).add(new Neurone(dimension,k,neurones.get(k).size()));
	    }
	}
		
	//création data
	data = new ArrayList<Data>();
		
	//test si tt les exemples ont été tirés
	picked = new ArrayList<Data>();
		
	for(int i=0;i<nb_data;i++){
	    data.add(new Data(4));
	}
	//pour robot_ interet
	list_interet = new ArrayList<Data>();
	list_interet.add(new Data((100.0/500.0),((500.0/2.0)/500.0)));
    }

    //DSOM voisinage

    private double hn(Neurone current, Neurone winner, double distdataWinner, 
		      double density, double elasticite) {	
	if (distdataWinner == 0)
	    distdataWinner = .000001;
	double result = Math.exp((-1.0/			 
				  Math.pow(density * elasticite, 2))*
				 (distance(current, winner)/
				  Math.pow(distdataWinner, 2)));	
	if(Double.isNaN(result)) {
	    System.out.println("NaN  distdataWinner = "+distdataWinner);
	    System.exit(12);
	}
	return result;
    }
 

    private double hnAngles(Neurone current, Neurone winner, Data data, double density) {	
	double distdataWinner = winner.getDistanceTetaEuc(data);
	return hn(current, winner, distdataWinner, density, elasticity);
    }
 

    private double hnRetine(Neurone current, Neurone winner, Data data, double density) {	
	double distdataWinner = winner.getDistanceXY(data);
	return hn(current, winner, distdataWinner, density, elasticity);
    }
   
    //SOM voisinage
    private double h(int iter, Neurone neurone, Neurone currentBest) {
	return Math.exp(-1.0*distance(neurone,currentBest)/sigma_f(iter)); 
    }


    private double sigma_f(int iter) {
	return 2*Math.pow((sigma_init*Math.pow((sigma_final /sigma_init), ((double)iter/(double)max_iter))),2);
    }
	
    private double epsilon_f(int iter){
	return e_init*Math.pow((e_final /e_init), ((double)iter/(double)max_iter));
    }
	
    //distance entre deux neurones (voisinage)
    private double distance(Neurone n1, Neurone n2) {
	double dCol;
	double dRow;
	
	dCol = Math.pow(((double) n1.getCol() - (double) n2.getCol())/numCol,2);
	dRow = Math.pow(((double) n1.getRow() - (double) n2.getRow())/numRow,2);

	return Math.sqrt(dCol + dRow);
	//return Math.pow(distance_voisins(currentBest,n1),2);
	
    }

    public double distance_voisins(Neurone currentBest,Neurone n) {
	return Math.abs(currentBest.getCol()-n.getCol())+Math.abs(currentBest.getRow()-n.getRow());
    }
	
    public ArrayList<Neurone> getConnectedNeurons(Neurone neurone) {    	
    	int index_ligne = getIndex(neurone);
    	int index_col = neurones.get(index_ligne).indexOf(neurone);
        ArrayList<Neurone> connectedNeurons = new ArrayList<Neurone>();
        
        
    	if(index_col < neurones.get(index_ligne).size() -1){
	    connectedNeurons.add(neurones.get(index_ligne).get(index_col + 1));
    	}
       	if(index_col > 0){
	    connectedNeurons.add(neurones.get(index_ligne).get(index_col - 1));
    	}
       	
        if(index_ligne == 0 && neurones.size() > 1){
	    connectedNeurons.add(neurones.get(index_ligne + 1).get(index_col));
        }else if(index_ligne == neurones.size() -1 && neurones.size()!=1){
	    connectedNeurons.add(neurones.get(index_ligne - 1).get(index_col));
        }else if(neurones.size()!=1){
	    connectedNeurons.add(neurones.get(index_ligne + 1).get(index_col));
	    connectedNeurons.add(neurones.get(index_ligne - 1).get(index_col));
        }
        
	return connectedNeurons;   
    }
    

    private int getIndex(Neurone neurone) {
	int index_ligne = -1;
	for(int i=0;i<neurones.size();i++){
	    if(neurones.get(i).contains(neurone)){
		index_ligne = i;
	    }
	}
	return index_ligne;
    }

    public void reinitializeNeurones(){
	neurones.removeAll(neurones);
	for(int k=0;k<numRow;k++){
	    neurones.add(new ArrayList<Neurone>());
	    for(int k1=0;k1<numCol;k1++){
		neurones.get(k).add(new Neurone(dimension,k,neurones.get(k).size()));
	    }
	}
    }


    static Neurone procheRetine(double x, double y) {	
	// determination du neurone le plus proche dans l'espace retinien
	
	Neurone current_best;
	double distance_best;
	double distance;

	current_best = neurones.get(0).get(0);
	distance_best = current_best.distanceRetine(x, y);
	for(int i=0;i<neurones.size();i++){
	    for(int k=0;k<neurones.get(i).size();k++){
		//recherche meilleur_candidat
		distance = neurones.get(i).get(k).distanceRetine(x, y);
		
		if(distance < distance_best){
		    distance_best = distance;
		    current_best = neurones.get(i).get(k);
		}
	    }
	}
	return (current_best);				
    }


    public double erreurTest() {
	// Evalue erreur entre position souhaitée et position constatée
	int   nbTests = 1000;
	double erreur = 0.0;
	
	for (int i = 0; i < nbTests ; i++) {
	    // determination d'un point existant
	    double a1 = Math.random();
	    double a2 = Math.random();
	    Data reel = datafromAngle(a1, a2);
	    // coordonnes de ce point 
	    double abscisse = reel.getPoids().get(0);
	    double ordonnee = reel.getPoids().get(1);
	    
	    // Neurone le plus proche sur la retine
	    winner = procheRetine(abscisse, ordonnee);

	    // determination du deplacement reel porte 
	    // par les angles du neurone vainqueur
	    a1 = winner.getPoids_angles().get(0);
	    a2 = winner.getPoids_angles().get(1);

	    reel = datafromAngle(a1, a2);	

	// erreur : distance entre le point vise et le point d'arrivee.. 
	erreur += Math.sqrt(Math.pow((abscisse - reel.getPoids().get(0)),2) + 
			    Math.pow((ordonnee - reel.getPoids().get(1)),2));
	
	}
	erreur = erreur/nbTests;
	return (erreur);
    }


    public double calculErreur(){
	Neurone      current;
	double           x,y;
	double theta1,theta2;
	double     axex,axey;
	double   reelx,reely;       
	double    erreur = 0;
	double  erreurLocale;
	double         theta;
	int nbNeurones = 0;

	for (int z = 0 ; z < neurones.size() ; z ++){
	    for(int l = 0 ; l < neurones.get(z).size() ; l ++){
		current =           neurones.get(z).get(l);
		x       =        current.getPoids().get(0);
		y       =        current.getPoids().get(1);
		theta1  = current.getPoids_angles().get(0);
		theta2  = current.getPoids_angles().get(1);

		//Calcul de l'axe
		theta = Math.toRadians(theta1*180 -90);
		axex = reference.x + l1 * Math.cos(theta);
		axey = reference.y - l1 * Math.sin(theta);
		
		
		// Calcul de l'arrivee du bras
		theta = Math.toRadians(theta2*180 -90);
		reelx = axex + l2 * Math.cos(theta);
		reely = axey - l2 * Math.sin(theta);

		// Distance de la retine au deplacement reel
		erreurLocale = Math.sqrt(Math.pow(reelx - x,2) + Math.pow(reely - y,2));
		erreur += erreurLocale;
		nbNeurones ++;
	    }	    
	}
	erreur = erreur / nbNeurones;
	if (Double.isNaN(erreur))
	    System.exit(12);
	return erreur;
    }


    //Initialisation des données
    public void setData(char c) {
	data=new ArrayList<Data>();
	robot = false;
	robot_move = false;
	switch(c){
	case 'C': //carre
	    for(int i=0;i<nb_data/4;i++){
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(100.0));
		poids.add(new Float(100.0+i));
		data.add(new Data(poids));
	    }
	    for(int i=0;i<nb_data/4;i++){					
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(100.0+i));
		poids.add(new Float(100.0));
		data.add(new Data(poids));
	    }
	    for(int i=0;i<nb_data/4;i++){
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(100.0+data.size()));
		poids.add(new Float(100.0+i));
		data.add(new Data(poids));
	    }
	    for(int i=0;i<nb_data/4;i++){
		ArrayList<Float> poids = new ArrayList<Float>();
		poids.add(new Float(100.0+i));
		poids.add(new Float(100.0+data.size()));
		data.add(new Data(poids));
	    }		
	    break;
	case 'L':			
	    //lune		
	    int nb = 0;
	    Ellipse2D.Double n = new Ellipse2D.Double(0,0, 500, 500);
	    Ellipse2D.Double n2 = new Ellipse2D.Double(0,0, 300, 300);
	    while(nb<nb_data){
		double x = Math.random()*500;
		double y = Math.random()*500;
		Point2D.Double p = new Point2D.Double(x,y);
		if(n.contains(p) && !n2.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add((new Float(x)/new Float(500.0)));
		    poids.add((new Float(y)/new Float(500.0)));
		    data.add(new Data(poids));
		    nb++;
		}
	    }
	    break;
	case 'R':			
		//robot		
		robot = true;
		int nb_ = 0;
		while(nb_<nb_data){
			double teta1 = Math.random();
			double teta2 = Math.random();
			data.add(datafromAngle(teta1,teta2));
			nb_++;
		}
	    break;
	case 'S':			
	    //robot		
	    robot_move = true;
	    data = new ArrayList<Data>();
	    break;
	case 'A':			
	    //anneau	
	    int nb2 = 0;
	    Ellipse2D.Double n1 = new Ellipse2D.Double(0,0, 500, 500);
	    Ellipse2D.Double n22 = new Ellipse2D.Double(100,100, 300, 300);
	    while(nb2<nb_data){
		double x = Math.random()*500;
		double y = Math.random()*500;
		Point2D.Double p = new Point2D.Double(x,y);
		if(n1.contains(p) && !n22.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add((new Float(x)/new Float(500.0)));
		    poids.add((new Float(y)/new Float(500.0)));
		    data.add(new Data(poids));
		    nb2++;
		}
	    }
	    break;
	case 'r':			
		//robot		
		robot = true;
		int nbr = 0;
		while(nbr<nb_data){

			double teta1 = Math.random();
			double teta2 = Math.random();
			Data d = datafromAngle(teta1,teta2);
			if(interet(d)){
				data.add(d);
				nbr++;
			}
		}
	break;
	case 'c':			
	    //cercle	
	    int nb3 = 0;
	    Ellipse2D.Double n3 = new Ellipse2D.Double(0,0, 500, 500);
	    Ellipse2D.Double n4 = new Ellipse2D.Double(100,100, 300, 300);
	    while(nb3<(nb_data*(3.0/4.0))){
		double x = Math.random()*500;
		double y = Math.random()*500;
		Point2D.Double p = new Point2D.Double(x,y);
		if(n3.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add((new Float(x)/new Float(500.0)));
		    poids.add((new Float(y)/new Float(500.0)));
		    data.add(new Data(poids));
		    nb3++;
		}
	    }
	    //setData('A');
	    nb3=0;
	    while(nb3<(nb_data*(3.0/4.0))){
		double x = Math.random()*500;
		double y = Math.random()*500;
		Point2D.Double p = new Point2D.Double(x,y);
		if(n4.contains(p)){
		    ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add((new Float(x)/new Float(500.0)));
		    poids.add((new Float(y)/new Float(500.0)));
		    data.add(new Data(poids));
		    nb3++;
		}
	    }
	    break;		
	case 'x': //carre
	    for(int i=0;i<nb_data;i++){
			double x = Math.random()*250;
			double y = Math.random()*250;
			ArrayList<Float> poids = new ArrayList<Float>();
		    poids.add((new Float(x)/new Float(500.0)));
		    poids.add((new Float(y)/new Float(500.0)));
			data.add(new Data(poids));
		    }
	    break;
	case 'y': //carre
	    for(int i=0;i<nb_data;i++){
		double x = Math.random()*250+250;
		double y = Math.random()*250+250;
		ArrayList<Float> poids = new ArrayList<Float>();
	    poids.add((new Float(x)/new Float(500.0)));
	    poids.add((new Float(y)/new Float(500.0)));
		data.add(new Data(poids));
	    }
	    break;
	case 'z': //carre
	    for(int i=0;i<nb_data;i++){
		double x = Math.random()*250+250;
		double y = Math.random()*250;
		ArrayList<Float> poids = new ArrayList<Float>();
	    poids.add((new Float(x)/new Float(500.0)));
	    poids.add((new Float(y)/new Float(500.0)));
		data.add(new Data(poids));
	    }
	    break;
	case 'w': //carre				
	    for(int i=0;i<nb_data;i++){
		double x = Math.random()*250;
		double y = Math.random()*250+250;
		ArrayList<Float> poids = new ArrayList<Float>();
	    poids.add((new Float(x)/new Float(500.0)));
	    poids.add((new Float(y)/new Float(500.0)));
		data.add(new Data(poids));
	    }
	    break;			
	}	
    }

	private boolean interet(Data d) {
		if(d.getDistanceXY(list_interet.get(0))*500.0 < 60.0){
			return true;
		}
		return false;
	}
    
    public void setParcours(char parcours) {
	this.parcours = parcours;		    
    }


    public Data datafromAngle (double a1, double a2) {
	// coordonnee de l'axe
	double theta = Math.toRadians(a1*180 -90);
	double axex = reference.x + l1 * Math.cos(theta);
	double axey = reference.y - l1 * Math.sin(theta);
	
	
	// Calcul de l'arrivee du bras
	theta = Math.toRadians(a2*180 -90);
	double reelx = axex + l2 * Math.cos(theta);
	double reely = axey - l2 * Math.sin(theta);
	
	return (new Data(new Float (reelx), new Float (reely),
			 new Float(a1), new Float(a2)));
    }


    public Data dataRandomAngles() {
	double a1 = Math.random();			
	double a2 = Math.random();			
	
	return datafromAngle(a1, a2);
    }

    public void viseDsom(Neurone winner, double densite, double erreur, Data data) {
	Neurone current;
	double distance;
	double distance_angles;

	for(int z=0;z<neurones.size();z++){
	    for(int l=0;l<neurones.get(z).size();l++){
		current  = neurones.get(z).get(l);
		distance  = hn(current, winner, erreur, densite, elasticity);
		distance_angles = hnAngles(current, winner, data, densite);//DSOM
		// mise a jour des poids
		current.dsomSetPoidsXY(distance, data);
		current.dsomSetPoidsTeta(distance_angles, data);
		
	    }
	}	
    }


    public void dsom(Neurone winner, Data data, double densite) {
	Neurone current;
	double distance_retine;
	double distance_angles;
	for(int z=0;z<neurones.size();z++){
	    for(int l=0;l<neurones.get(z).size();l++){
		current = neurones.get(z).get(l);
		
		distance_retine = hnRetine(current, winner, data, densite);//DSOM		
		distance_angles = hnAngles(current, winner, data, densite);//DSOM
		
		// mise a jour des poids
		current.dsomSetPoidsXY(distance_retine, data);
		current.dsomSetPoidsTeta(distance_angles, data);
		
	    }
	}	
    }

    public Neurone close(Data data, double factRetine, double factAngle) {
		//recherche du neurone le plus proche de la donnee observee
	Neurone current_best;
	double distance_best;
	double distance;

	current_best = neurones.get(0).get(0);
	distance_best = current_best.distance(data, factRetine, factAngle);
	for(int i=0;i<neurones.size();i++){
	    for(int k=0;k<neurones.get(i).size();k++){
		//recherche meilleur_candidat
		distance = neurones.get(i).get(k).distance(data, factRetine, factAngle);
		
		if(distance < distance_best){
		    distance_best = distance;
		    current_best = neurones.get(i).get(k);
		}
	    }
	}
	return current_best;
    }



    
    public void load(double st, String name) {
	
	try {
	    FileWriter fw = new FileWriter(name, true);
	    BufferedWriter bw = new BufferedWriter ( fw ) ;
	    bw.newLine();
	    PrintWriter pw = new PrintWriter ( bw ) ;
	    pw. print (st+"\n");
	    pw. close();
	}
	catch ( IOException e ) {
	    System.out.println ( " Problème à l'écriture du fichier " ) ;
	    System.exit(0);
	} 
    }
    
    public void retine() {
	// apprend a partir de point cherches dans la retine
	double densite = 1;
	double alpha1   = .9;
	double alpha2   = 1 - alpha1;

	elasticity = 2.5;//3.7;//3.5;//2.5;

	// tirage de points au hasard dans la retine
	double x = Math.random();
	double y = Math.random();

	// determination du neurone le plus proche dans l'espace retinien
	
	/*Neurone current_best;
	double distance_best;
	double distance;*/
	
	winner = procheRetine(x, y);

	// Ce neurone (x,y,a1,a2) fournit une donnee 'reelle' observee 
	//  à partir de ses angles (a, a') bruites
	double a1 = winner.getPoids_angles().get(0);
	double a2 = winner.getPoids_angles().get(1);
	double r1;
	double r2;
	
	do {	    
	    r1 = generatorx.nextGaussian()*4*erreur;
	    // tracer l'evolution du bruit
	    load(r1, "/tmp/random.txt");
	} while ((a1+r1 <= 0) || (a1+r1 >= 1));
	do {
	    r2 = generatory.nextGaussian()*4*erreur;
	} while ((a2 +r2<= 0) || (a2+r2 >=1));
	a1 = a1 + r1;
	a2 = a2 + r2;


	Data interest = datafromAngle(a1, a2);
	
	// Pour tracer le point courant
	data.add(interest);


	// delimitation d'une zone de motivation
	double abcisse = interest.getPoids().get(0);
	double ordonne = interest.getPoids().get(1);
	
	if ((ordonne < .55) &&  (ordonne > .45) && 
	    (abcisse < .35) &&  (abcisse > .3)) {
	    densite = densite * 1.5;
	}
	
	//application de DSOM
	
	// definition de la distance double carte
	double factRetine = .5;
	double factAngle  = 1 - factRetine;
	
	winner = close(interest, factRetine, factAngle);

	double errCurrent;
	errCurrent = Math.sqrt(
			       Math.pow(winner.getPoids().get(0)-interest.getPoids().get(0),2) +
			       Math.pow(winner.getPoids().get(1)-interest.getPoids().get(1),2));
	erreur = alpha1 * erreur +  alpha2 * errCurrent;

	System.out.printf("erreur : %1.3f  \r", erreur);
	load(erreur, "/tmp/erreur.txt");
	load(errCurrent, "/tmp/errCurrent.txt");
	// Mise à jour des poids suivant DSom
	dsom(winner, interest, densite);
    }



    public void vise() { 
	// apprentissage double carte retine vs angles
	// a partir données visees
	double densite = 1;
	

	// points recherches
	ArrayList<Data> list_interet = new ArrayList<Data>();
	list_interet.add(new Data(.5, .2));
	list_interet.add(new Data(.3, .3));
	list_interet.add(new Data(.8, .8));
	list_interet.add(new Data(.4, .6));
	list_interet.add(new Data(.1, .1));

	int index = (int) (Math.random()*list_interet.size());
	
	// Choix d'un point vise
	double x = list_interet.get(index).getPoids().get(0);
	double y = list_interet.get(index).getPoids().get(1);
	System.out.printf("data[%d] \t\t(%.3f  %.3f)\n", index, x, y);
	// determination du neurone le plus proche dans l'espace retinien
	
	Neurone current_best;
	double distance_best;
	double distance;

	current_best = neurones.get(0).get(0);
	distance_best = current_best.distanceRetine(x, y);
	for(int i=0;i<neurones.size();i++){
	    for(int k=0;k<neurones.get(i).size();k++){
		//recherche meilleur_candidat
		distance = neurones.get(i).get(k).distanceRetine(x, y);
		
		if(distance < distance_best){
		    distance_best = distance;
		    current_best = neurones.get(i).get(k);
		}
	    }
	}
	winner = current_best;			

	// Ce neurone (x,y,a1,a2) fournit une donnee 'reelle' observee 
	//  à partir de ses angles (a, a')
	double a1 = winner.getPoids_angles().get(0);			
	double a2 = winner.getPoids_angles().get(1);			

	Data interest = datafromAngle(a1, a2);
	
	// Pour tracer le point courant
	data.add(interest);

	// erreur : distance entre le point vise et la point d'arrivee.. 
	double erreur = Math.sqrt(Math.pow((x-winner.getPoids().get(0)),2) + 
				  Math.pow((y-winner.getPoids().get(1)),2));

	// delimitation d'une zone de motivation
	double abscisse = interest.getPoids().get(0);
	double ordonnee = interest.getPoids().get(1);
	if ((ordonnee < .55) &&  (ordonnee > .45) && 
	    (abscisse < .35) &&  (abscisse > .3)) {
	    densite = 1;
	}
	
	
	//application de DSOM
	
	// definition de la distance double carte
	double factRetine = .9;
	double factAngle  = 1 - factRetine;
	
	winner = close(interest, factRetine, factAngle);		

	// Mise à jour des poids suivant DSom
	//dsom(winner, interest, densite);
	viseDsom(winner, densite, erreur, interest);
    }
   

    public void bras() { 
	// apprentissage double carte retine vs angles
	// a partir de balbutiements angulaires
	double densite = 1;

	// tirage d'un point au hasard à partir de ses coordonnes angulaires... 
	Data interest = dataRandomAngles();

	// Pour tracer le point courant
	data.add(interest);

	// delimitation d'une zone de motivation
	double abcisse = interest.getPoids().get(0);
	double ordonne = interest.getPoids().get(1);

	if ((ordonne < .55) &&  (ordonne > .45) && 
	    (abcisse < .35) &&  (abcisse > .3)) {
	    densite = 1;
	}
	
	
	//application de DSOM

	// definition de la distance double carte
	double factRetine = .5;
	double factAngle  = 1 - factRetine;

	winner = close(interest, factRetine, factAngle);	
	

	// Mise à jour des poids suivant DSom
	dsom(winner, interest, densite);
    }


    @Override
    public void run() {//=learn
	reinitializeNeurones();
	int index = 0;
	for(int iter=0;iter<max_iter;iter++){
	    if(on){

		picked = new ArrayList<Data>();
	            
	
		if(compar){ //changement dynamique - Bouton "Random"
		    int iter_changement = max_iter/4; 
		    if(iter==iter_changement){
			this.setData('z');
		    }else if(iter==iter_changement*2){
			this.setData('y');
		    }else if(iter==iter_changement*3){
			this.setData('w');
		    }else if(iter==iter_changement*4){
			this.setData('x');
		    }
		}
	            
	            
		//Affichage du déroulement de l'algorithme
		//System.out.println("Nb_itération :"+iter); 
		if(mode) {
		    System.out.println("Epsilon :"+epsilon_f(iter));
		    System.out.println("Sigma :"+sigma_f(iter));
		}
	            
	            
		/* case 's' - Bouton "RobotMove" 
		 * Sans données initiales
		 */
		if(robot_move){
		    double   erreurTest = erreurTest();
		    // Suivre l'évolution de l'erreur entre coordonnes retine et angles 
		    if ((iter % 1000) == 0) {
			System.out.printf(" [%7d] Erreur positions: %.3f", iter, calculErreur());
			System.out.print("\t\t");
			System.out.printf("Moyenne erreurs cumulees : %.3f", erreurTest);
			System.out.println();
		    }
		    switch(parcours) {
		    case 'M' : // balbutiement moteurs
			bras();
			break;
		    case 'R' : // essais de mouvement aléatoires
			retine();
			break;
		    }
		    
		    if ((iter != 0) && ((iter % 10000) == 0)) {
			System.out.printf("\nBING \t anc l1 = %1.3f \t anc l2 = %1.3f\n", l1, l2);
			l1 = masterl1 * Math.random();
			l2 = masterl2 * Math.random();
			System.out.printf("BING \t     l1 = %1.3f \t     l2 = %1.3f\n", l1, l2);
			data.clear();
		    }
				
		}else{ //algo normal
		    //choix d'une donnee	            	
		    index = (int) (Math.random()*data.size());
		    
		    currentData = data.get(index);
		    //recherche du meilleur neurone
		    Neurone current_best = neurones.get(0).get(0);
		    double distance_best;
		    if(robot){
			distance_best= 0.5*current_best.getDistanceXY(data.get(index))+0.5*current_best.getDistanceTeta(data.get(index));
		    }else{
			distance_best= current_best.getDistanceXY(data.get(index));
		    }
		    double distance;
		    for(int i=0;i<neurones.size();i++){
			for(int k=0;k<neurones.get(i).size();k++){
			    //recherche meilleur_candidat
			    if(robot){
				distance = 0.5*neurones.get(i).get(k).getDistanceXY(data.get(index))+
				    0.5*neurones.get(i).get(k).getDistanceTetaEuc(data.get(index));
			    }else{
				distance = neurones.get(i).get(k).getDistanceXY(data.get(index));
			    }
			    if(distance < distance_best){
				distance_best = distance;
				current_best = neurones.get(i).get(k);
			    }
			}
		    }
					
		    winner = current_best;
					
		    //fonction de voisinage
		    double distance_voisinage;
		    for(int z=0;z<neurones.size();z++){
			for(int l=0;l<neurones.get(z).size();l++){
			    if(mode){//SOM
				distance_voisinage = h(iter,neurones.get(z).get(l),current_best);//SOM
			    }else{//DSOM
				distance_voisinage = hnRetine(neurones.get(z).get(l),current_best,data.get(index), 1);//DSOM
			    }
			    neurones.get(z).get(l).setPoidsXY(epsilon_f(iter),distance_voisinage,data.get(index),mode);
			    if(robot)
				neurones.get(z).get(l).setPoidsTeta(epsilon_f(iter),distance_voisinage,data.get(index),mode);
			}
		    }
		}
	
		//changement des poids	
		setChanged();		
		notifyObservers(this);
		/*if(robot_move){
		  try {
		  Thread.sleep(1000);
		  } catch (InterruptedException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		  }
		  }*/
	    }
	}
		    System.out.println("\t \t .end.");

	this.on=false;
	setChanged();
	notifyObservers(this);
	/*if(robot)
	  printResults();*/
    }
	


    //retourne x,y en fonction de l'angle, la distance et le point de référence
    public static Point2D.Double getPoint(double angle, double distance,Point2D.Double ref) {
        double theta = Math.toRadians(180*angle -90);
        Point2D.Double p = new Point2D.Double();
        p.x = ref.x + distance*Math.cos(theta);
        p.y = ref.y - distance*Math.sin(theta);
        return p;
    }
    
    //template
    public static Point2D.Double getPointFromTheta(double theta1, double theta2) {
        return getPoint(theta2,l2,getPoint(theta1,l1,reference));
    }

    public void printResults(){
    	for(int i=0;i<neurones.size();i++){
	    for(int j=0;j<neurones.get(i).size();j++){
		double theta1=neurones.get(i).get(j).getPoids_angles().get(0);
		double theta2=neurones.get(i).get(j).getPoids_angles().get(1);
		double x = neurones.get(i).get(j).getPoids().get(0);
		double y = neurones.get(i).get(j).getPoids().get(1);
		System.out.print("X:"+x);
		System.out.print(" Y:"+y);
		System.out.print(" | Theta1:"+theta1);
		System.out.println(" Theta2:"+theta2);
		System.out.println("Res Theorique x,y pour theta1,theta2"+getPoint(theta2,l2,getPoint(theta1,l1,reference))+"\n");
	    }    		
    	}
    }

    public static Neurone getGagnantRobot(Integer xClicked, Integer yClicked) {
	//recherche du meilleur neurone
	Neurone current_best = NeuralNetwork.getNeurones().get(0).get(0);
	Data tmp_data = new Data(new Float(xClicked),new Float(yClicked));
	double distance_best = current_best.getDistanceXY(tmp_data);
	double distance;
	for(int i=0;i<neurones.size();i++){
	    for(int k=0;k<neurones.get(i).size();k++){
		distance = neurones.get(i).get(k).getDistanceXY(tmp_data);
		if(distance < distance_best){
		    distance_best = distance;
		    current_best = neurones.get(i).get(k);
		}
	    }
	}
	return current_best;
    }
	
    /************************* 
     *  GETTERS AND SETTERS   *
     *  ***********************/
	
    public Data getCurrentData() {
	return currentData;
    }

    public Neurone getWinner() {
	return winner;
    }
	
	
    public boolean isOn() {
	return on;
    }

    public void setOn(boolean on) {
	this.on = on;
    }
	
    public static Point2D.Double getReference() {
	return reference;
    }
	
    public ArrayList<Data> getPicked() {
	return picked;
    }

    public void setPicked(ArrayList<Data> picked) {
	this.picked = picked;
    }
	
    public boolean isRobot_move() {
	return robot_move;
    }

    public void setRobot_move(boolean robotMove) {
	robot_move = robotMove;
    }
	
    public int getNb_data() {
	return nb_data;
    }

    public void setNb_data(int nbData) {
	nb_data = nbData;
    }
	
    public boolean getOn() {
	return on;
    }

    public boolean isMode() {
	return mode;
    }

    public static double getL2() {
	return l2;
    }

    public static void setL2(double l2) {
	NeuralNetwork.l2 = l2;
    }

    public static double getL1() {
	return l1;
    }
	
    public void setMode(boolean mode) {
	this.mode = mode;
    }
	
    public boolean isRobot() {
	return robot;
    }

    public void setRobot(boolean robot) {
	this.robot = robot;
    }

    public Data getInteret() {
	return interet;
    }

    public void setInteret(Data interet) {
	this.interet = interet;
    }

    public boolean isCompar() {
	return compar;
    }

    public void setCompar(boolean compar) {
	this.compar = compar;
    }

    public double getE_init() {
	return e_init;
    }

    public void setE_init(double eInit) {
	e_init = eInit;
    }

    public static ArrayList<ArrayList<Neurone>> getNeurones() {
	return neurones;
    }

    public ArrayList<Data> getData() {
	return data;
    }


    public void setData(ArrayList<Data> data) {
	this.data = data;
    }	

    public int getMax_iter() {
	return max_iter;
    }

    public void setMax_iter(int maxIter) {
	max_iter = maxIter;
    }

    public double getE_final() {
	return e_final;
    }

    public void setE_final(double eFinal) {
	e_final = eFinal;
    }

    public double getSigma_init() {
	return sigma_init;
    }

    public void setSigma_init(double sigmaInit) {
	sigma_init = sigmaInit;
    }

    public double getSigma_final() {
	return sigma_final;
    }

    public void setSigma_final(double sigmaFinal) {
	sigma_final = sigmaFinal;
    }

    public double getElasticity() {
	return elasticity;
    }

    public void setElasticity(double elasticity) {
	this.elasticity = elasticity;
    }
	
    public boolean isData_paint() {
		return data_paint;
	}

	public void setData_paint(boolean dataPaint) {
		data_paint = dataPaint;
	}
    /*public ArrayList<Data> getList_interet() {
      return list_interet;
      }
    */    
}
