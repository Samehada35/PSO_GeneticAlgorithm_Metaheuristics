package algorithms;

import java.util.ArrayList;

import data.Measure;
import data.Method;
import data.Node;
import data.Occurence;
import data.SortedList;
import data.Statistics;
import utils.SearchingUtils;

public class AStar {
	private SortedList open;
	private ArrayList<Occurence> sortedLiterals;
	private SAT sat;
	private int developpedNodesNumber;
	private double executionTime;
	
	public AStar(SAT sat) {
		this.sat = sat;
		this.open = new SortedList();
		this.sortedLiterals = SearchingUtils.constructOccurencesList(sat);
	}
	
	
	public int[] run() {
		Node literalNodes = new Node(sat.getVariablesNumber());
		Node positiveLiteralNode;
		Node negativeLiteralNode;
		int randLiteral = 0;
		developpedNodesNumber = 0;
		executionTime = System.currentTimeMillis();
		
		
		open.insert(literalNodes);
		
		while(!open.isEmpty()) {
			literalNodes = open.remove();
			this.developpedNodesNumber++;
			
			
			
			if(literalNodes.getLiterals().size() == sat.getVariablesNumber()) {
				if(sat.is_satisfiable(SearchingUtils.getSolutionArray(literalNodes.getLiterals()))) {
					executionTime = (System.currentTimeMillis() - executionTime)/1000;
					
					Measure m = new Measure(sat.getInstanceSource(),executionTime,developpedNodesNumber,sat.getClausesNumber(),Method.A_STAR);
					
					Statistics s = Statistics.getStats();
					s.addMeasure(m);
					
					return SearchingUtils.getSolutionArray(literalNodes.getLiterals());
				}
			}else {
				positiveLiteralNode = new Node(new ArrayList<>(literalNodes.getLiterals()),literalNodes.getDepth()+1,0);
				negativeLiteralNode = new Node(new ArrayList<>(literalNodes.getLiterals()),literalNodes.getDepth()+1,0);
				
				
				for(Occurence o : sortedLiterals) {
					if(!literalNodes.getLiterals().contains(o.getLiteral()) && !literalNodes.getLiterals().contains(o.getLiteral()*-1)) {
						randLiteral = o.getLiteral();
						break;
					}
				}

				
				positiveLiteralNode.getLiterals().add(randLiteral);
				negativeLiteralNode.getLiterals().add(randLiteral*-1);
				
				positiveLiteralNode.setHeuristic(sat.getClausesNumber() - SearchingUtils.getNumberClausesSatisfied(sat,positiveLiteralNode.getLiterals()));
				negativeLiteralNode.setHeuristic(sat.getClausesNumber() - SearchingUtils.getNumberClausesSatisfied(sat,negativeLiteralNode.getLiterals()));
				
				
				
				int pushOrder = (int) Math.floor(Math.random()*2);
				
				if(pushOrder == 0) {
					open.insert(negativeLiteralNode);
					open.insert(positiveLiteralNode);
				}else {
					open.insert(positiveLiteralNode);
					open.insert(negativeLiteralNode);
				}
			}
			
		}
		
		executionTime = (System.currentTimeMillis() - executionTime)/1000;
		
		Measure m = new Measure(sat.getInstanceSource(),executionTime,developpedNodesNumber,0,Method.A_STAR);
		
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
