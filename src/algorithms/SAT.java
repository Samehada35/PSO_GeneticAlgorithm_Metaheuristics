package algorithms;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SAT {
	
	private int clausesNumber, variablesNumber, clausesLength;
	private int[][] SATMatrix;
	private String instanceSource;
	
	public SAT(int clausesNumber, int variablesNumber, int clausesLength) {
		this.clausesNumber = clausesNumber;
		this.variablesNumber = variablesNumber;
		this.clausesLength = clausesLength;
		this.instanceSource = "";
		SATMatrix = new int[clausesNumber][variablesNumber];
		
		for(int i=0;i<clausesNumber;i++) {
			for(int j=0;j<variablesNumber;j++) {
				SATMatrix[i][j] = 0;
			}
		}
	}
	
	public static SAT loadFromFile(String path) throws IOException{
		final List<String> lines = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
		List<String[]> clauses = new ArrayList<>(); 
		
		int clausesNumber = 0;
		int clausesLength = 0;
		int variablesNumber = 0;
		
		for(String line : lines) {
			line = line.trim();
			if(line.length()>0) {
				char firstChar = line.charAt(0);
				if(firstChar == '-'|| (firstChar>='0' && firstChar<='9')) {
					String[] tempClause = line.split(" ");
					clauses.add(Arrays.copyOfRange(tempClause,0,tempClause.length-1));
				}else if(firstChar == 'p') {
					String[] fileInfos = line.split(" +");
					clausesNumber = Integer.parseInt(fileInfos[3]);
					variablesNumber = Integer.parseInt(fileInfos[2]);
				}else if(firstChar == '%') {
					break;
				}
			}
		}
		
		if(!clauses.isEmpty()) {
			clausesLength = clauses.get(0).length;
			
			int[][] SATMat = new int[clausesNumber][variablesNumber];
			
			for(int i=0;i<clausesNumber;i++) {
				for(int j=0;j<variablesNumber;j++) {
					SATMat[i][j] = -1;
				}
			}
			
			
			for(int i=0;i<clausesNumber;i++) {
				String[] clause = clauses.get(i);
				for(int j=0;j<clausesLength;j++) {
					int literal = Integer.parseInt(clause[j]);
					SATMat[i][Math.abs(literal)-1] = literal < 0 ? 0 : 1;
				}
			}
			
			SAT sat = new SAT(clausesNumber,variablesNumber,clausesLength);
			
			sat.setSATMatrix(SATMat);
			sat.setInstanceSource(new File(path).getName());
			
			return sat;
			
		}else {
			return null;
		}

	}
	
	public int[] generateRandomSolution() {
		int solution[] = new int[variablesNumber];
		
		for(int i=0;i<variablesNumber;i++) {
			solution[i] = (int) Math.floor(Math.random() * 2);
		}
		
		return solution;
	}
	
	public boolean is_satisfiable(int[] solution){
		boolean[] b = new boolean[SATMatrix.length];
		int i = 0;
		for(int [] clause : SATMatrix){
			int cpt = 0;
			for (int x : clause){
				if(x == solution[cpt]){
						b[i] = true;
						break;
				}
				cpt++;
			}
			i++;
		}
		for (boolean bool : b){
			if(!bool)
				return false;
		}
		
		return true;
	}

	
	public String toString() {
		String s = "";
		for(int i=0;i<clausesNumber;i++) {
			s = s + "Clause "+(i+1)+" = ";
			boolean first = true;
			for(int j=0;j<variablesNumber-1;j++) {
				if(SATMatrix[i][j] != -1) {
					if(!first) {
						s += " + ";
					}
					s = s+ (SATMatrix[i][j]==1 ? "":"¬")+"x"+(j+1);
					first = false;
				}
			}
			if(SATMatrix[i][variablesNumber-1] != -1) {
				s+= " + ";
				s = s+ (SATMatrix[i][variablesNumber-1]==1 ? "":"¬")+"x"+variablesNumber+"\n";
			}else {
				s+="\n";
			}
		}
		
		return s;
	}

	public int getClausesNumber() {
		return clausesNumber;
	}

	public void setClausesNumber(int clausesNumber) {
		this.clausesNumber = clausesNumber;
	}

	public int getVariablesNumber() {
		return variablesNumber;
	}

	public void setVariablesNumber(int variablesNumber) {
		this.variablesNumber = variablesNumber;
	}

	public int getClausesLength() {
		return clausesLength;
	}

	public void setClausesLength(int clausesLength) {
		this.clausesLength = clausesLength;
	}

	public int[][] getSATMatrix() {
		return SATMatrix;
	}

	public void setSATMatrix(int[][] sATMatrix) {
		SATMatrix = sATMatrix;
	}

	public String getInstanceSource() {
		return instanceSource;
	}

	public void setInstanceSource(String instanceSource) {
		this.instanceSource = instanceSource;
	}
	
	

}
