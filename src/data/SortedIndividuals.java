package data;

import java.util.ArrayList;
import java.util.Arrays;

public class SortedIndividuals extends ArrayList<Individual>{
	
	public void insert(Individual individual) {
		int i=0;
		while(i<size() && individual.getFitness() < get(i).getFitness()) {
			if(individual.getFitness() > get(i).getFitness()) {
				 break;
			}
			i++;
		}
		
		add(i,individual);
	}
	
	public boolean hasSolution(int[] solution) {
		for(Individual i : this) {
			if(Arrays.equals(i.getLiterals(), solution)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void displayPopulation() {
		for(int i=0;i<size();i++) {
			System.out.println("Individual N°"+i+": "+get(i).toString());
		}
	}
}
