package algorithms;

import data.Individual;
import data.Measure;
import data.MetaHeuristicMeasure;
import data.Method;
import data.SortedIndividuals;
import data.Statistics;
import utils.SearchingUtils;

public class GeneticAlgorithm {
	private SAT sat;
	private int maxIteration = 10000,
				crossoverRate = 60,
				mutationRate = 40,
				populationSize = 50;
	private final int stagnationLimit = 500;
	private int rc, rm, lastBestFitness;
	private int developpedNodesNumber;
	private int[] childSolution1, childSolution2;
	private double executionTime;
	private SortedIndividuals population;

	public GeneticAlgorithm(SAT sat, int maxIteration, int populationSize, int crossoverRate, int mutationRate) {
		this.sat = sat;
		this.maxIteration = maxIteration;
		this.populationSize = populationSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
	}
	
	public int[] run() {
		int i=0;
		Individual parent1, parent2, child1, child2;

		generatePopulation();
		
		lastBestFitness = population.get(0).getFitness();
		developpedNodesNumber = 0;
		executionTime = System.currentTimeMillis();
		
		while(i<maxIteration) {
			developpedNodesNumber++;
			//System.out.println("\n\nGeneration "+i+", best = "+population.get(0).getFitness());
			
			/*for(Individual ind : population) {
				System.out.println(ind.toString());
			}*/
			
			//System.out.println(population.get(0));
			
			if(solutionFound()) {
				executionTime = (System.currentTimeMillis() - executionTime)/1000;
				
				MetaHeuristicMeasure m = new MetaHeuristicMeasure(sat.getInstanceSource(),executionTime,developpedNodesNumber,sat.getClausesNumber(),maxIteration,Method.GA);
				
				Statistics s = Statistics.getStats();
				s.addMeasure(m);
				
				return population.get(0).getLiterals();
			}
			
			if(i!=0 && i%stagnationLimit == 0 && population.get(0).getFitness() == lastBestFitness) {
				int[] newSolution = population.get(1).getLiterals();
				
				for(int j=0;j<newSolution.length;j++) {
					newSolution[j] = 1 - newSolution[j];
				}

				lastBestFitness = population.get(0).getFitness();
			}else if(i%stagnationLimit==0) {
				lastBestFitness = population.get(0).getFitness();
			}

			
			
			int crossoverNumber = (int) Math.floor(((double) crossoverRate/100.) * population.size());
			
			
			for(int k=0;k<crossoverNumber;k++) {
				int parent1Index = (int) Math.floor(Math.random()*populationSize);
				int parent2Index;
				
				while((parent2Index =(int) Math.floor(Math.random()*populationSize)) == parent1Index);
				
				parent1 = population.get(parent1Index);
				parent2 = population.get(parent2Index);
				
				crossover(parent1.getLiterals(), parent2.getLiterals());
				
				child1 = new Individual(childSolution1,SearchingUtils.getNumberClausesSatisfied(sat, childSolution1));
				child2 = new Individual(childSolution2,SearchingUtils.getNumberClausesSatisfied(sat, childSolution2));
				
				Individual bestChild, otherChild;
				
				if(child1.getFitness()<=child2.getFitness()) {
					bestChild = child2;
					otherChild = child1;
				}else {					
					bestChild = child1;
					otherChild = child2;
					
				}	
				
				if(!population.hasSolution(bestChild.getLiterals()) && bestChild.getFitness() > population.get(population.size()-2).getFitness()) {
					population.remove(population.size()-2);
					population.insert(bestChild);
				}
					
				if(!population.hasSolution(otherChild.getLiterals()) && otherChild.getFitness() > population.get(population.size()-1).getFitness()) {
					population.remove(population.size()-1);
					population.insert(otherChild);
				}
			}
			

			int mutationNumber = (int) Math.floor(((double) mutationRate/100.) * population.size());
			
			for(int k=0;k<mutationNumber;k++) {
				int mutatedIndividualIndex = (int) Math.floor(Math.random()*populationSize);
				Individual mutatedIndividual = population.get(mutatedIndividualIndex);
				
				mutate(mutatedIndividual.getLiterals());
			}
		
			i++;
			/*
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		executionTime = (System.currentTimeMillis() - executionTime)/1000;
		
		MetaHeuristicMeasure m = new MetaHeuristicMeasure(sat.getInstanceSource(),executionTime,developpedNodesNumber,population.get(0).getFitness(),maxIteration,Method.GA);
		
		Statistics s = Statistics.getStats();
		s.addMeasure(m);
		
		
		return null;
	}
	
	public void generatePopulation() {
		this.population = new SortedIndividuals();
		
		for(int k=0;k<populationSize;k++) {
			int[] sol = sat.generateRandomSolution();
			
			while(population.hasSolution(sol)) {
				sol = sat.generateRandomSolution();
			}
			
			population.insert(new Individual(sol,SearchingUtils.getNumberClausesSatisfied(sat, sol)));
		}
	}
	
	public boolean solutionFound() {
		for(Individual ind : population) {
			if(sat.is_satisfiable(ind.getLiterals())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void crossover(int[] solution1, int[] solution2) {
		childSolution1 = new int[sat.getVariablesNumber()];
		childSolution2 = new int[sat.getVariablesNumber()];

		int i=0;
		int cutIndex = (int) Math.ceil(Math.random()*sat.getVariablesNumber());

		
		while(i<cutIndex) {
			childSolution1[i] = solution1[i];
			childSolution2[i] = solution2[i];
			i++;
		}
		
		while(i<sat.getVariablesNumber()) {
			childSolution1[i] = solution2[i];
			childSolution2[i] = solution1[i];
			i++;
		}
	}
	
	public void mutate(int[] solution) {
		int mutatedBit =  (int) Math.floor(Math.random()*sat.getVariablesNumber());
		
		solution[mutatedBit] = 1 - solution[mutatedBit];
	}
	
	public SortedIndividuals getPopulation() {
		return population;
	}

	public int getDeveloppedNodesNumber() {
		return developpedNodesNumber;
	}

	public double getExecutionTime() {
		return executionTime;
	}

	public int getMaxIteration() {
		return maxIteration;
	}

	public int getCrossoverRate() {
		return crossoverRate;
	}

	public int getMutationRate() {
		return mutationRate;
	}

	public int getPopulationSize() {
		return populationSize;
	}
	
	
}
