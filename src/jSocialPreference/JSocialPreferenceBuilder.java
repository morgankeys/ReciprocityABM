package jSocialPreference;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class JSocialPreferenceBuilder implements ContextBuilder<Object> {
	
	@Override
	public Context build(Context<Object> context) {
		context.setId("jSocialPreference");
		// TODO Auto-generated method stub
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("game network", context, true);
		netBuilder.buildNetwork ();
		
		/*
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null );
		Grid<Object> grid = gridFactory.createGrid ("grid", context, 
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), true, 16, 16));
		*/
		
		//Create Agents
		
		//Distribution params
		double rhoMean=.4;
		double rhoSD=.5;
		double sigmaMean=.023;
		double sigmaSD=.5;
		double thetaMean=.111;
		double thetaSD=.5;
		
		//Number of Agents
		int agentCount = 256;
		
		//Create distributions
		Normal rdist = RandomHelper.createNormal(rhoMean,rhoSD);
		Normal sdist = RandomHelper.createNormal(sigmaMean,sigmaSD);
		Normal tdist = RandomHelper.createNormal(thetaMean,thetaSD);
		Uniform udist = RandomHelper.createUniform();
		double rho;
		double sigma;
		double theta;
		int type;
		
		
		for(int i=1;i<=agentCount;i++){
			//Get random values based on distributional parameters
			rho=rdist.nextDouble();
			sigma=sdist.nextDouble();
			theta=tdist.nextDouble();
			//agentList.add(new Agent(i,7));
			context.add(new Agent(i,rho,sigma,theta));
			
			/*
			type=udist.nextIntFromTo(1,7);
			System.out.println("Type: "+type);
			agentList.add(new Agent(i,type));
			*/
		}
		
		return context;
	}

	
}
