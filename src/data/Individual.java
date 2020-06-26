package data;

import java.util.ArrayList;
import java.util.Arrays;

public class Individual {
	private int[] literals;
	private int fitness;
	
	
	public Individual(int[] literals, int fitness) {
		super();
		this.literals = literals;
		this.fitness = fitness;
	}

	public Individual(int[] literals) {
		super();
		this.literals = literals;
	}


	public int[] getLiterals() {
		return literals;
	}


	public void setLiterals(int[] literals) {
		this.literals = literals;
	}


	public int getFitness() {
		return fitness;
	}


	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	@Override
	public String toString() {
		return "[literals=" + Arrays.toString(literals) + ", fitness=" + fitness + "]";
	}

	

}
