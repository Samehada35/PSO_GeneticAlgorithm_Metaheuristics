package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import data.Measure;
import data.Method;
import data.Statistics;
import utils.SearchingUtils;

public class BFS {
	private Queue<ArrayList<Integer>> open;
	private SAT sat;
	private int developpedNodesNumber;
	private double executionTime;

	public BFS(SAT sat) {
		this.sat = sat;
		this.open = new LinkedList<>();
	}


	public int[] run() {
		ArrayList<Integer> literalNodes = new ArrayList<>();
		ArrayList<Integer> positiveLiteralNode;
		ArrayList<Integer> negativeLiteralNode;
		ArrayList<Integer> unpickedLiterals;
		int randLiteral;
		developpedNodesNumber = 0;
		executionTime = System.currentTimeMillis();
		


		open.add(literalNodes);

		while(!open.isEmpty()) {
			literalNodes = open.remove();
			this.developpedNodesNumber++;


			if(literalNodes.size() == sat.getVariablesNumber()) {
				if(sat.is_satisfiable(SearchingUtils.getSolutionArray(literalNodes))) {
					executionTime = (System.currentTimeMillis() - executionTime)/1000;
					
					Measure m = new Measure(sat.getInstanceSource(),executionTime,developpedNodesNumber,sat.getClausesNumber(),Method.BFS);
					Statistics s = Statistics.getStats();
					s.addMeasure(m);
					return SearchingUtils.getSolutionArray(literalNodes);
				}
			}else {
				positiveLiteralNode = new ArrayList<>(literalNodes);
				negativeLiteralNode = new ArrayList<>(literalNodes);

				unpickedLiterals = new ArrayList<>();
				
				for(int i=1;i<=sat.getVariablesNumber();i++) {
					if(!literalNodes.contains(i) && !literalNodes.contains(i*-1)) {
						unpickedLiterals.add(i);
					}
				}
				
				randLiteral = unpickedLiterals.get((int) Math.floor(Math.random()*unpickedLiterals.size()));

				positiveLiteralNode.add(randLiteral);
				negativeLiteralNode.add(randLiteral*-1);

				int pushOrder = (int) Math.floor(Math.random()*2);

				if(pushOrder == 0) {
					open.add(negativeLiteralNode);
					open.add(positiveLiteralNode);
				}else {
					open.add(positiveLiteralNode);
					open.add(negativeLiteralNode);
				}

			}

		}

		executionTime = (System.currentTimeMillis() - executionTime)/1000;

		Measure m = new Measure(sat.getInstanceSource(),executionTime,developpedNodesNumber,0,Method.BFS);
		
		Statistics s = Statistics.getStats();
		s.addMeasure(m);
		
		return null;

	}


	public int getDeveloppedNodesNumber() {
		return developpedNodesNumber;
	}

	public double getExecutionTime() {
		return executionTime;
	}






}
