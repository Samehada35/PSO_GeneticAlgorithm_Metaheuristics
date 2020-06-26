package data;

import java.util.ArrayList;

public class Node {
	private ArrayList<Integer> literals;
	private int depth;
	private int heuristic;
	
	
	public Node(ArrayList<Integer> literals, int depth, int heuristic) {
		this.literals = literals;
		this.depth = depth;
		this.heuristic = heuristic;
	}
	
	public Node(int heuristic) {
		this.literals = new ArrayList<>();
		this.depth = 0;
		this.heuristic = heuristic;
	}
	
	public Node() {
		this.depth = 0;
		this.heuristic = 0;
	}
	
	public int cost() {
		return depth + heuristic;
	}

	public ArrayList<Integer> getLiterals() {
		return literals;
	}

	public void setLiterals(ArrayList<Integer> literals) {
		this.literals = literals;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

}
