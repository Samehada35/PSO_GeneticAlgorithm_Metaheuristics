package data;

import java.util.Arrays;

public class Particle {
  private int[] position;
  private int[] velocity;
  private int[] PBest;
  private int fitness;
  
  public Particle(int nbVariables) {
	  this.position = new int[nbVariables];
	  this.velocity = new int[nbVariables];
	  this.PBest = new int[nbVariables];
  }

  public Particle(int[] position, int[] velocity) {
	  this.position = position;
      this.velocity = velocity;
	}
  
  public int getFitness(){
    return fitness;
  }

  public void setFitness(int fitness){
    this.fitness = fitness;
  }

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int[] getVelocity() {
		return velocity;
	}

	public void setVelocity(int[] velocity) {
		this.velocity = velocity;
	}

	public int[] getPBest() {
		return PBest;
	}

	public void setPBest(int[] PBest) {
		this.PBest = PBest;
	}

	@Override
	public String toString() {
		return "Particle [position=" + Arrays.toString(position) + ", velocity=" + Arrays.toString(velocity)
				+ ", PBest=" + Arrays.toString(PBest) + ", fitness=" + fitness + "]";
	}
	
	
}
