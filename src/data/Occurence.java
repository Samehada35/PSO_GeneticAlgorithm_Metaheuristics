package data;

public class Occurence implements Comparable<Occurence>{
	private int literal;
	private int occurence;
	
	public Occurence(int literal, int occurence) {
		super();
		this.literal = literal;
		this.occurence = occurence;
	}
	
	public int getLiteral() {
		return literal;
	}
	public void setLiteral(int literal) {
		this.literal = literal;
	}
	public int getOccurence() {
		return occurence;
	}
	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	
	@Override
	public int compareTo(Occurence o) {
		return occurence < o.occurence ? 1 : occurence > o.occurence ? -1 : 0;
	}

	
	
	

}
