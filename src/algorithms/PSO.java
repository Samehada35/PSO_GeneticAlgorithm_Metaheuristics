package algorithms;


import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;

import data.Particle;
import utils.SearchingUtils;

public class PSO {
	private SAT sat;
    private Particle[] particles;
    private int[] GBest;
    private int maxIteration = 10000;
    private int[] r1,r2;
    private int nbParticles = 100;

	public PSO() {
		super();
	}

	public PSO(SAT sat) {
		super();
		this.sat = sat;
		this.particles = new Particle[nbParticles];
		this.r1 = new int[nbParticles];
		this.r2 = new int[nbParticles];
		this.GBest = new int[nbParticles];
		
	    for (int i=0; i< particles.length;i++){
	      particles[i] = new Particle(sat.getVariablesNumber());
	      r1[i] = (int) Math.random()%2;
	      r2[i] = (int) Math.random()%2;
	    }

    }

    public int[] run(){
      init();
      int i=0;
      while(i<maxIteration){
        for(Particle particle : particles){
        	updateVelocity(particle);
    		updatePosition(particle);
        	updatePBest(particle);
        	evaluateFitness(particle);
 
            if (SearchingUtils.getNumberClausesSatisfied(sat,particle.getPBest()) > SearchingUtils.getNumberClausesSatisfied(sat, GBest)){
          	  GBest = particle.getPBest();
           }
        	
        }

        System.out.println("le GBest est: " + Arrays.toString(GBest) + "\nle nb de clause satisfaites est: "
        		+ SearchingUtils.getNumberClausesSatisfied(sat,GBest));
        i++;
    }
      
      System.out.println("iterations = "+i);
    
      return GBest;
    }

  public void updatePBest(Particle particle){
    if (SearchingUtils.getNumberClausesSatisfied(sat,particle.getPBest()) < SearchingUtils.getNumberClausesSatisfied(sat,particle.getPosition())){
      particle.setPBest(particle.getPosition());
    }

  }

  public void updateVelocity(Particle particle){
    int[] param1,param2,param3,param4,param5,param6;
    param1 = sub_vector(particle.getPBest(),particle.getPosition());
    param2 = sub_vector(GBest, particle.getPosition());
    param3 = mult_vector(param2,r2);
    param4 = add_vector(param3,param1);
    param5 = mult_vector(param4,r1);
    param6 = add_vector(particle.getVelocity(),param5);

    particle.setVelocity(param6);

  }
  public void updatePosition(Particle particle){
    particle.setPosition(add_vector(particle.getPosition(),particle.getVelocity()));

  }
  public void evaluateFitness(Particle particle){
    particle.setFitness(SearchingUtils.getNumberClausesSatisfied(sat,particle.getPosition()));
  }

  public void updateGBest(){
    for(Particle particle : particles){
      if (SearchingUtils.getNumberClausesSatisfied(sat,particle.getPBest()) > SearchingUtils.getNumberClausesSatisfied(sat, GBest)){
        GBest = particle.getPBest();
        System.out.println("HEY");
      }

    }

  }

	public SAT getSat() {
		return sat;
	}

	public void setSat(SAT sat) {
		this.sat = sat;
	}

	@Override
	public String toString() {
		return "PSO [sat=" + sat + "]";
	}

  public void init(){
	particles[0].setPosition(sat.generateRandomSolution());
	particles[0].setVelocity(sat.generateRandomSolution());
    particles[0].setPBest(particles[0].getPosition());
    
    GBest = particles[0].getPBest();
	
    for(int i=1;i<particles.length;i++){
      particles[i].setPosition(sat.generateRandomSolution());
      particles[i].setVelocity(sat.generateRandomSolution());
      particles[i].setPBest(particles[i].getPosition());
      
      if (SearchingUtils.getNumberClausesSatisfied(sat,particles[i].getPBest()) > SearchingUtils.getNumberClausesSatisfied(sat, GBest)){
    	  GBest = particles[i].getPBest();
      }
    }


  }
  
  public int[] getGBest(){
    return GBest;
  }
  
  public void setGBest(int[] GBest){
    this.GBest = GBest;
  }

  public int[] add_vector(int[] vector1,int[] vector2){
    int[] transition = new int[sat.getVariablesNumber()];
    for(int i=0;i < vector1.length;i++){
     transition[i] = (vector1[i] + vector2[i]) % 2;
    }
    return transition;
  }
  
  public int[] mult_vector(int[] vector1,int[] vector2){
	int[] transition = new int[sat.getVariablesNumber()];
    for(int i=0;i < vector1.length;i++){
     transition[i] = (vector1[i] * vector2[i]) % 2;
    }
    return transition;
  }

  public int[] sub_vector(int[] vector1,int[] vector2){
	  int[] transition = new int[sat.getVariablesNumber()];
    for(int i=0;i < vector1.length;i++){
     transition[i] = (vector1[i] - vector2[i]) % 2;
    }
    return transition;
  }

}
