package jSocialPreference;

import java.util.ArrayList;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;
import cern.jet.random.*;

public class Model{
	//Variables
	//***********************************************
	public ArrayList<Agent> agentList;
	public ArrayList<Game> games;
	
	//Model parameters
	public String runType	= "normal";
	public int 	numAgents;//	= 100;
	public int 	numRounds;//	= 1;
	public int 	gameNumber;//	= 3;
	
	public double rhoMean	= .424;
	public double rhoSD		= .1; //rhoMean*.5;
	public double sigmaMean = .023;
	public double sigmaSD	= .1; //sigmaMean*.5;
	public double thetaMean	= .111;
	public double thetaSD	= .1; //thetaMean*.5;
	public static String[] params;
	
	
	public int[] typeDist= new int[7];
	//typeDist= {10,10,10,10,10,10,10};
	
	public int round=0;
	
	public RandomHelper rh = new RandomHelper();
	
	public Report theReport;
	
	//Parameter distributions
	Normal rdist;
	Normal sdist;
	Normal tdist;
	
	//***********************************************
	//End variables
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			setParamsList(args);
		}catch(Exception e){
			System.out.println(
					"Parameters: runType [Agents Rounds " +
						"[rhoMean rhoSD sigmaMean sigmaSD thetaMean thetaSD]]");
			System.out.println("Error: " +e.getMessage());
		}
		Model modelInstance = new Model();
		System.out.println("Running!");
		modelInstance.run();
	}
	
	public static void setParamsList(String[] args){
		params=args;
	}
	public void setParams(){
		String[] rgs=params;
		if(rgs.length>1){
			if(rgs[0].equals("settypes")){
				runType		=	rgs[0];	
				numRounds	=	Integer.parseInt(rgs[1]);
				gameNumber	=	Integer.parseInt(rgs[2]);
				for(int i=3;i<=9;i++){
					typeDist[i-3]=Integer.parseInt(rgs[i]);
					//numAgents+=typeDist[i-3];
					//System.out.println(rgs[i]+" -> "+typeDist[i-3]);
				}
			}
			if(rgs[0].equals("normal")){
				if(rgs.length == 4){
					runType 	=	rgs[0];
					numAgents	=	Integer.parseInt(rgs[1]);
					numRounds	=	Integer.parseInt(rgs[2]);
					gameNumber	=	Integer.parseInt(rgs[3]);
				}
				else if(rgs.length == 10){
					runType		=	rgs[0];
					numAgents	=	Integer.parseInt(rgs[1]);
					numRounds	=	Integer.parseInt(rgs[2]);
					gameNumber	=	Integer.parseInt(rgs[3]);
					rhoMean		=	Double.parseDouble(rgs[4]);
					rhoSD		=	Double.parseDouble(rgs[5]);
					sigmaMean	=	Double.parseDouble(rgs[6]);
					sigmaSD		=	Double.parseDouble(rgs[7]);
					thetaMean	=	Double.parseDouble(rgs[8]);
					thetaSD		=	Double.parseDouble(rgs[9]);
				}
			}
			if(!rgs[0].equals("normal")&& !rgs[0].equals("settypes")){/*System.out.println(
					"Parameters: [settype|] [Agents Rounds Game_Type " +
						"[rhoMean rhoSD sigmaMean sigmaSD thetaMean thetaSD]]");*/
				System.out.println("None of these!!!!!!");
			}
		}else{
			numAgents	= 1000;
			numRounds	= 100;
			gameNumber	= 3;
		}
	}
	
	public void setup(){
		//Initialize parameters, pass Model to classes
		setParams();
		Agent.setModel(this);
		Game.setModel(this);
		Report.setModel(this);

		//1-Initialize agentList
		agentList = new ArrayList<Agent>();
		Normal rdist = RandomHelper.createNormal(rhoMean,rhoSD);
		Normal sdist = RandomHelper.createNormal(sigmaMean,sigmaSD);
		Normal tdist = RandomHelper.createNormal(thetaMean,thetaSD);
		double rho;
		double sigma;
		double theta;		
		
		System.out.println(numAgents);
		//2-Create agents based on runType
		for(int i=1;i<=numAgents;i++){
			//Get random values based on distributional parameters
			if(runType.equals("settypes")){
				for(int j=1; j<=7;j++){
					for(int k=1;k<=typeDist[j-1];k++){
						agentList.add(new Agent(i,j));
					}
				}
			}
			else if(runType.equals("normal")){
				rho		=	rdist.nextDouble();
				sigma	=	sdist.nextDouble();
				theta	=	tdist.nextDouble();
				//Make sure these 
				if(rho>1)	{rho=1;}	else if(rho<0)	{rho=0;}
				if(sigma>1)	{sigma=1;}	else if(sigma<0){rho=0;}
				if(theta>1)	{theta=1;}	else if(theta<0){theta=0;}
				
				agentList.add(new Agent(i,rho,sigma,theta));
			}
		}
		//3-Initialize a Report object
		theReport = new Report();
		//4-Make an initial-state update
		theReport.updateAgents(0);
	}
	
	
	public void step(){
		//1-Increment round
		round+=1;
		
		//Tick up game type
		//gameNumber = gameNumber>19 ? 1:gameNumber+1;
		//gameNumber=3;
		
		//2-Clear games list
		games=new ArrayList<Game>();		
		
		//3-Create games
		SimUtilities.shuffle(agentList, RandomHelper.getUniform());
		for(int i=0; i<numAgents;i=i+2){
			//Get two agents
			Agent playerA=agentList.get(i); 
			Agent playerB=agentList.get(i+1); 
			Game makeGame=new Game(round,playerA,playerB,gameNumber);
			games.add(makeGame);
		}
		
		//4-Run games
		for(int j=0; j<games.size();j++){
			//For every game, run it.
			Game playgame = games.get(j);
			playgame.play();
		}
		
		//Update Agent report
		theReport.updateAgents(round);
	}
	/*
	public Agent getAgent(int method){
		Random r = new Random();
		Agent ret;
		switch (method){
			case 1:
				//return a random agent
				ret = agentList.get(r.nextInt());
			: ret = new Agent(-1,7);
		}
		return ret;	
	}*/
	
	public void report(){
		//Call Print method from report
		theReport.printFinalSummary();
	}
	public void run(){
		setup();
		for(int i=1;i<=numRounds;i++){
			step();
		}
		report();
		theReport.close();
	}
}
