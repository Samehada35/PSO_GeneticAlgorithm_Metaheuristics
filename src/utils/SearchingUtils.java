package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algorithms.SAT;
import data.Occurence;

public class SearchingUtils {
	
	
	public static int[] getSolutionArray(ArrayList<Integer> node){
		int[] solution = new int[node.size()];
		
		for(int x : node) {
			solution[Math.abs(x)-1] = x>0 ? 1 : 0;
		}
		
		return solution;
	}
	
	
	
	public static String formatSolution(int[] solution) {
		String s = "";
		
		for(int i=0;i<solution.length-1;i++) {
			s+= "x"+(i+1)+" = "+solution[i]+"\n";
		}
		
		s+= "x"+solution.length+" = "+solution[solution.length-1];
		
		return s;
	}


	public static int getNumberClausesSatisfied(SAT sat, int literal) {
		int nb = 0;
		
		if(literal > 0) {
			
			for(int[] clause : sat.getSATMatrix()) {
				if(clause[Math.abs(literal)-1] == 1) {
					nb++;
				}
			}
			
		}else {
			
			for(int[] clause : sat.getSATMatrix()) {
				if(clause[Math.abs(literal)-1] == 0) {
					nb++;
				}
			}
			
		}

		return nb;
	}
	
	public static int getNumberClausesSatisfied(SAT sat, ArrayList<Integer> literals) {
		int nb = 0;
		
		for(int[] clause : sat.getSATMatrix()) {
			for(int l : literals) {
				if(l>0 && clause[Math.abs(l)-1] == 1) {
					nb++;
					break;
				}else if(l<0 && clause[Math.abs(l)-1] == 0) {
					nb++;
					break;
				}
				
			}
		}
		
		return nb;
	}
	
	public static int getNumberClausesSatisfied(SAT sat, int[] solution) {
		int nb = 0;
		int[][] satMatrix = sat.getSATMatrix();
		
		for(int i=0;i<sat.getClausesNumber();i++) {
			for(int j=0;j<sat.getVariablesNumber();j++) {
				if(satMatrix[i][j] == solution[j]) {
					nb++;
					break;
				}
			}
		}
		
		return nb;

	}
	
	public static ArrayList<Occurence> constructOccurencesList(SAT sat){
		ArrayList<Occurence> occurencesList = new ArrayList<>();
		for(int i=1;i<=sat.getVariablesNumber();i++) {
			occurencesList.add(new Occurence(i,SearchingUtils.getNumberClausesSatisfied(sat,i)));
			occurencesList.add(new Occurence(i*-1,SearchingUtils.getNumberClausesSatisfied(sat,i*-1)));
		}
		
		Collections.sort(occurencesList);
		
		return occurencesList;
	}
	
	public static int getInstanceType(String instanceName) {
		Matcher m = Pattern.compile("uf(\\d+)\\-\\d+.cnf").matcher(instanceName);
		
		return m.matches() ? Integer.valueOf(m.group(1)) : -1;
	}	
	
	public static int getInstanceNumber(String instanceName) {
		Matcher m = Pattern.compile("uf\\d+\\-(\\d+).cnf").matcher(instanceName);
		
		return m.matches() ? Integer.valueOf(m.group(1)) : -1;
	}
	
}
