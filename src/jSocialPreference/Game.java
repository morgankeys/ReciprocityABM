package jSocialPreference;

public class Game {
	//Agents, payoffs
	Agent playerA, playerB;
	int outA,outB,leftA,leftB,rightA,rightB;
	//Int array to represent the outcome. 1 is true, 0 is false.
		//A opts out: 			{1,0,0,0}
		//A opts in, B left: 	{0,1,1,0}
		//A opts in, B right:	{0,1,0,1}
	public int[] outInLeftRight={0,0,0,0}; //All zeros is null.
	public Report theReport;
	public int round;
	int[] game;
	int gameType;
	
	public static Model model;

	public Game(int thisRound, Agent pA, Agent pB, int gameNum){
		gameType=gameNum;
		int[] game = GameType.getGame(gameNum);
		playerA=pA;		playerB=pB;		outA=game[0];
		outB=game[1];	leftA=game[2];	leftB=game[3];
		rightA=game[4];	rightB=game[5];	round=thisRound;
		theReport=model.theReport;
	}
	
	public static void setModel( Model m ) { model = m; }

	public void play(){
		//Stage 1
		//Compute options
		
		//Player A: Compute values of Opt-in and Opt-out
		double optOutA		= playerA.computePref(outA,outB,0);
		double optLeftA		= playerA.computePref(leftA,leftB,0);
		double optRightA	= playerA.computePref(rightA,rightB,0);
		double optInA		= Math.max(optLeftA, optRightA); //Should this use backward induction?

		//Assuming no defection, what does B think of it's options?
		double optOutB		= playerB.computePref(outB,outA,0);
		double optLeftB 	= playerB.computePref(leftB,leftA,0);
		double optRightB 	= playerB.computePref(rightB,rightA,0);
		
		boolean pAOptedOut;
		if(optOutA>optInA){pAOptedOut=true;}
		else if(optOutA<optInA){pAOptedOut=false;}
		else {
			int toss = playerB.tossUp();
			pAOptedOut=toss>0?true:false;
			theReport.tossFile.println("Toss-up("+toss+
					"): Opt-out \t#"+playerB.agentID+"\tRd: "+round+" Game: "+gameType);
		}
		
		if(pAOptedOut){
			//PlayerA opts out, game is over.
			int q=0;	//Default to zero, assume cooperation
			if			(optOutB>Math.max(optLeftB,optRightB))	{q=-1;}
			else if		(optOutB<Math.max(optLeftB,optRightB))	{q=0;}
			else		{theReport.tossFile.println("Toss-up: Should A behave? #"+
					playerA.agentID+"\tRd: "+round+" Game: "+gameType);	q=playerB.tossUp()>0?-1:0;}
			
			//Update playerB only. (increase,decrease)
			if(q==-1)	{playerB.update(false,true);}
			if(q==0)	{playerB.update(true,false);}
			outInLeftRight[0]=1;
		}
		else if (!pAOptedOut){
			//Stage 2
			//Player A has opted in. Opt-In is true.
			outInLeftRight[1]=1;
							
			//Behavior variable q (B's perspective)
			int q=0;	//Default to zero, assume cooperation
			
			//Has A misbehaved?
			if			(optOutB>Math.max(optLeftB,optRightB)/*CHECK THIS: A misbehaved*/ )		{q=-1;}
			else if		(optOutB<Math.max(optLeftB,optRightB)/*CHECK THIS: A behaved*/ )		{q=0;}
			else		{theReport.tossFile.println("Toss-up: Did A behave? #"+
					playerB.agentID+"\tRd: "+round+" Game: "+gameType);	q=0;}
			
			//Based on A's behavior, left or right? (recalculate preferences)
			double bGoLeft		= playerB.computePref(leftB,leftA,q);
			double bGoRight		= playerB.computePref(rightB,rightA,q);

			//Evaluate options with new prefs
			if			(bGoLeft>bGoRight)		{/*Choose left*/	outInLeftRight[2]=1;}
			else if		(bGoLeft<bGoRight)		{/*Choose right*/	outInLeftRight[3]=1;}
			else		{theReport.tossFile.println("Toss-up: Left or Right? #"+
					playerB.agentID+"\tRd: "+round+" Game: "+gameType);
				outInLeftRight[(playerB.tossUp()>0?2:3)]=1;}
			
			//Did B behave?
			double leftPrefA 	= playerA.computePref(leftA,leftB,0);
			double rightPrefA 	= playerA.computePref(rightA,rightB,0);
			
			
			//Update A depending on whether or not B behaved.
			if			(outInLeftRight[2]==1 	&&	leftPrefA > rightPrefA)	{playerA.update(/*Good*/true,false);}
			else if		(outInLeftRight[2]==1 	&&	leftPrefA < rightPrefA)	{playerA.update(/*Bad*/false,true);}
			else if		(outInLeftRight[3]==1 	&&	leftPrefA > rightPrefA)	{playerA.update(/*Good*/true,false);}
			else if		(outInLeftRight[3]==1 	&&	leftPrefA < rightPrefA)	{playerA.update(/*Bad*/false,true);}
			else		{theReport.tossFile.println("Error updating A. \tRd: "+round+" Game: "+gameType);}
			
		}
		else{/*This shouldn't happen.*/ System.out.println("SHOULD NOT HAPPEN!");}
		
		//Send results
		theReport.addResult(round,playerA.agentID,playerB.agentID,outInLeftRight,gameType);
		
	}

}
