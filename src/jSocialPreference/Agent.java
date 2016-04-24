package jSocialPreference;

import cern.jet.random.Uniform;
import repast.simphony.random.RandomHelper;

public class Agent {
	public double rho,sigma,theta;
	public int agentID;//Which agent is this?
	public Uniform udist = RandomHelper.createUniform();
	public Report theReport;
	public static Model model;
		
	public Agent(int ID, double r, double s, double t){
		agentID=ID;
		rho=r;
		sigma=s;
		theta=t;
		theReport = model.theReport;
	}
	
	public Agent(int ID,int type){
		agentID=ID;
		switch (type){
			case 1:	//Self-interest.------------------------------
				rho=0;		sigma=0;		theta=0;		break;
			case 2:	//Single-parameter; altruism.-----------------
				rho=.212;	sigma=.212;		theta=0;		break;
			case 3:	//Single-parameter; behindness aversion;------
				rho=0;		sigma=.118;		theta=0;		break;
			case 4:	//Single-parameter; charity.------------------
				rho=.422;	sigma=0;		theta=0;		break;
			case 5:	//Rho-Sig w/o reciprocity.--------------------
				rho=.423;	sigma=-.014;	theta=0;		break;
			case 6:	//Reciprocal charity.-------------------------
				rho=.425;	sigma=0;		theta=-.089;	break;
			case 7:	//Rho-sigma model.----------------------------
				rho=.424;	sigma=.023;		theta=.111;		break;
		}
	}
	
	public static void setModel( Model m ) { model = m; }
	
	public double computePref(int mypay, int yourpay,int q){
		int r,s;
		if(mypay>yourpay){r=1;}else{r=0;}
		if(mypay<yourpay){s=1;}else{s=0;}
		//Based on Charness and Rabin
		double value=(rho*r+sigma*s+theta*q)*yourpay+
			(1-rho*r-sigma*s-theta*q)*mypay;
		return value;
	}
	
	public void update(boolean increase, boolean decrease){
		//Increase or decrease values by logarithmic increments
		//Reciprocity is backward. Higher values mean harsher reactions
		if(decrease){
			theta+=RandomHelper.nextDoubleFromTo(0.0,.1);
			if(theta<0){theta=0;}else if(theta>1){theta=1;}
		}
		if(increase){
			theta-=RandomHelper.nextDoubleFromTo(0.0,.1);
			if(theta<0){theta=0;}else if(theta>1){theta=1;}
		}
	}
	public int tossUp(){
		int ret = udist.nextIntFromTo(0,1);
		return ret;
		
	}
}
