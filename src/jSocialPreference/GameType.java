package jSocialPreference;

public class GameType {
	/*
	 * Payoff = (A,B)
	 * A chooses first
	 * -------------------------------------------------------
	 * Response game diagram:
	 * 					(A)
	 * 					/ \
	 * 				   /   \
	 * 				  /		\
	 * 				 /		 \
	 * 		  (OutA,OutB)	 (B)=In
	 * 					  	 / \
	 * 						/   \
	 * 					   /	 \
	 * 					  /		  \
	 *  		(LeftA,LeftB)   (RightA,RightB)
	 * --------------------------------------------------------
	 * (1)Two person response games--B's payoff identical 
	 *  	(4 games)
	 * 	1-Barc7:	(750,0)		or	(400,400)	vs	(750,400)
	 * 	2-Barc5:	(550,550)	or	(400,400)	vs	(750,400)
	 * 	3-Berk28:	(100,1000)	or	(75,125)	vs	(125,125)
	 * 	4-Berk32:	(450,900)	or	(200,400)	vs	(400,400)
	 * 
	 * (2)Two-person response games--B's sacrifice helps A
	 *  	(11 games)
	 * 	5-Barc3:	(725,0)		or	(400,400)	vs	(750,375)
	 * 	6-Barc4:	(800,0)		or	(400,400)	vs	(750,375)
	 * 	7-Berk21:	(750,0)		or	(400,400)	vs	(750,375)
	 * 	8-Barc6:	(750,100)	or	(300,600)	vs	(700,500)
	 * 	9-Barc9:	(450,0)		or	(350,450)	vs	(450,350)
	 * 	10-Berk25:	(450,0)		or	(350,450)	vs	(450,350)
	 * 	11-Berk19:	(700,200)	or	(200,700)	vs	(600,600)
	 * 	12-Berk14:	(800,0)		or	(0,800)		vs	(400,400)
	 * 	13-Barc1:	(550,550)	or	(400,400)	vs	(750,375)
	 * 	14-Berk13:	(550,550)	or	(400,400)	vs	(750,375)
	 * 	15-Berk18:	(0,800)		or	(0,800)		vs	(400,400)
	 * 
	 * (3)Two-person response games--B's sacrifice hurts A
	 *  	(5 games)
	 * 	16-Barc11:	(375,1000)	or	(400,400)	vs	(350,350)
	 * 	17-Berk22:	(375,1000)	or	(400,400)	vs	(250,350)
	 * 	18-Berk27:	(500,500)	or	(800,200)	vs	(0,0)
	 * 	19-Berk31:	(750,750)	or	(800,200)	vs	(0,0)
	 * 	20-Berk30:	(400,1200)	or	(400,200)	vs	(0,0)
	 */
	private static int[][] gameTypeVals={
		//(1)Two person response games--B's payoff identical 
		{750,0,400,400,750,400},	//1
		{550,550,400,400,750,400},	//2
		{100,1000,75,125,125,125},	//3
		{450,900,200,400,400,400},	//4
		//(2)Two-person response games--B's sacrifice helps A
		{725,0,400,400,750,375},	//5
		{800,0,400,400,750,375},	//6
		{750,0,400,400,750,375},	//7
		{750,100,300,600,700,500},	//8
		{450,0,350,450,450,350},	//9
		{450,0,350,450,450,350},	//10
		{700,200,200,700,600,600},	//11
		{800,0,0,800,400,400},		//12
		{550,550,400,400,750,375},	//13
		{550,550,400,400,750,375},	//14
		{0,800,0,800,400,400},		//15
		//(3)Two-person response games--B's sacrifice hurts A
		{375,1000,400,400,350,350},	//16
		{375,1000,400,400,250,350},	//17
		{500,500,800,200,0,0},		//18
		{750,750,800,200,0,0},		//19
		{400,1200,400,200,0,0}		//20
	};
	public static int[] getGame(int whichGame){
		//Let users pick games from 1 to 20
		return gameTypeVals[whichGame-1];
	}
}