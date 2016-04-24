package jSocialPreference;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Report{
	//private int numPlayers,numRounds;
	
	//public ArrayList<double> roundSummary;
	public ArrayList<int[]> allResults;
	public ArrayList<Agent> agents;
	public ArrayList<Game> games;
	
	public FileWriter agentOut;
	public PrintWriter agentFile;
	
	public FileWriter tossOut;
	public PrintWriter tossFile;
	
	public static Model model;
	
	//Get Date, for time-stamping files
	//---------------------------------------------------------------------
	Calendar now = Calendar.getInstance();
	public String m 	= Integer.toString(now.get(Calendar.MONTH)+1);
	public String d 	= Integer.toString(now.get(Calendar.DAY_OF_MONTH));
	public String y 	= Integer.toString(now.get(Calendar.YEAR));
	public String h 	= Integer.toString(now.get(Calendar.HOUR_OF_DAY));
	public String min 	= Integer.toString(now.get(Calendar.MINUTE));
	
	
	public Report(){
		allResults 	= new ArrayList<int[]>();
		agents		= model.agentList;
		games		= model.games;
		try{
			agentOut = new FileWriter("Agent History_"+m+"-"+d+"-"+y+"_"+h+"h"+min+"m_game-"+model.gameNumber+".txt");
			agentFile = new PrintWriter(agentOut);
			String header = "Round\t";
			//for(int id=1;id<=agents.size();id++){header+=id+"_rho\t"+id+"_sig\t"+id+"_thet\t";}
			for(int id=1;id<=agents.size();id++){header+=id+"_thet\t";}
			agentFile.println(header);
			
			tossOut		= new FileWriter("Toss-Ups_"+m+"-"+d+"-"+y+"_"+h+"h"+min+"m_game-"+model.gameNumber+".txt");
			tossFile	= new PrintWriter(tossOut);
		}catch(Exception e){System.out.println("ERROR: "+e.getMessage());}
	}

	public static void setModel( Model m ) { model = m; }
	
	public void addResult(int round, int idA, int idB, int[] outcome, int gameType){
		//A result is fully defined by the round, and the two players playing
		int[] add = {round,idA,idB,outcome[0],outcome[1],outcome[2],outcome[3], gameType};
		allResults.add(add);
	}
		
	public void updateAgents(int round){
		String line = round+"\t";
		for(int a=1;a<=agents.size();a++){
			Agent ag = null;
			//Cycle through and find the right agent, to print in order
			for(int find=1;find<=agents.size();find++){
				if(agents.get(find-1).agentID==a)
					ag=agents.get(find-1);
			}
			line+=ag.theta+"\t";
		}
		agentFile.println(line);
	}
	
	public void printFinalSummary(){
		int rds=model.numRounds;	//int games = model.numAgents/2;
		
			
		//Record per-round history of game outcomes
		//---------------------------------------------------------------------
		int[] outs=new int[rds];	int[] lefts=new int[rds];	int[] rights=new int[rds];
		int[] errors=new int[rds];	int[] game=new int[rds];
		DecimalFormat df = new DecimalFormat("#.##");
		for(int init=0;init<rds;init++){outs[init]=0;	lefts[init]=0;	rights[init]=0;	errors[init]=0;}
		for(int[] i : allResults){
			for(int j=0;j<rds;j++){
				if(i[0]==j+1){
					if(i[3]==1){outs[j]+=1;}
					if(i[5]==1){lefts[j]+=1;}
					if(i[6]==1){rights[j]+=1;}
					if(i[3]!=1&&i[5]!=1&&i[6]!=1){System.out.println("Error - Score incorrectly recorded");}
				}
				game[j]=i[7];
			}
		}
		try{
			FileWriter fstream = new FileWriter("All Rounds_"+m+"-"+d+"-"+y+"_"+h+"h"+min+"m_game-"+model.gameNumber+".txt");
			PrintWriter outfile = new PrintWriter(fstream);

			outfile.println("Round\tOut\tLeft\tRight\tGame Type");
			for(int r=0;r<rds;r++){
				double out		= (double) outs[r];	
				//int in			= games-outs[r];
				double left		= (double) lefts[r];
				double right	= (double) rights[r];
				outfile.println((r+1)+"\t"+df.format(out)+"\t"+df.format(left)+"\t"+df.format(right)+"\t"+game[r]);
		}
			
			//Close the output stream
			outfile.close();
		}catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
		}
		
		//Console Summary
		//----------------------------------------------------------------------
		System.out.println("Final Summary: ");
		System.out.println("Game\tOut\tIn\tLeft\tRight\n");
		int totgames;	int cOuts=0;	int cLefts=0;	int cRights=0;
		for(int j=1;j<=20;j++){
			totgames=0;
			for(int[] i : allResults){
				if(i[7]==j){
					totgames+=1;
					if(i[3]==1){cOuts+=1;}
					if(i[5]==1){cLefts+=1;}
					if(i[6]==1){cRights+=1;}
				}
			}
			if(totgames>0){
				System.out.println(
						j+"\t"
						+df.format((double) cOuts/totgames)+"\t"
						+df.format((double) (totgames-cOuts)/totgames)+"\t"
						+df.format((double) cLefts/(totgames-cOuts))+"\t"
						+df.format((double) cRights/(totgames-cOuts))
				);
			}
		}
		
	}
	
	public void close(){
		agentFile.close();
	}
	
	
	
	//Unused methods
	//---------------------------------------------------------------------
	
	/*
	public String printResult(int idA, int idB, boolean[] result){
		String outcome="";
		for(int i=0;i<4;i++){
			if(result[i]==true){outcome+="True-";}
			else{outcome+="False-";}
		}
		String out =
			"<result>"+
			"	<playerA>"+idA+"</playerA>"+
			"	<playerB>"+idA+"</playerB>"+
			"	<outcome>"+outcome+"</outcome>"+
			"</result>";
		return out;	
	}	
	public String printAgentCurrentStatus(){
		String agentStr="";
		for(int i=0;i<agents.size();i++){
			agentStr+=
				"<agent>" +
					"<aName>"+agents.get(i).agentID+"</aName>" +
					"<theta>"+agents.get(i).theta+"</theta>" +
				"<agent>\n";
		}
		String out = 
			"<round>\n"+
				"<roundNum>"+roundNum+"</roundNum>\n"+
				agentStr+
			"</round>";
		
		return out;
	}
	
	*/
}
