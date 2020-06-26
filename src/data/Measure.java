package data;

import java.io.Serializable;

public class Measure implements Serializable,Comparable<Measure>{
	protected static final long serialVersionUID = 1L;
	protected String instanceName;
	protected double executionTime;
	protected int developpedNodesNumber;
	protected int bestFitness;
	protected Method method;
	
	
	
	public Measure(String instanceName, double executionTime, int developpedNodesNumber, int bestFitness, Method method) {
		super();
		this.instanceName = instanceName;
		this.executionTime = executionTime;
		this.developpedNodesNumber = developpedNodesNumber;
		this.bestFitness = bestFitness;
		this.method = method;
	}
	
	public Measure(Measure m) {
		super();
		this.instanceName = m.instanceName;
		this.executionTime = m.executionTime;
		this.developpedNodesNumber = m.developpedNodesNumber;
		this.bestFitness = m.bestFitness;
		this.method = m.method;
	}

	public String getInstanceName() {
		return instanceName;
	}


	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}


	public double getExecutionTime() {
		return executionTime;
	}


	public void setExecutionTime(double executionTime) {
		this.executionTime = executionTime;
	}


	public int getDeveloppedNodesNumber() {
		return developpedNodesNumber;
	}


	public void setDeveloppedNodesNumber(int developpedNodesNumber) {
		this.developpedNodesNumber = developpedNodesNumber;
	}


	public Method getMethod() {
		return method;
	}


	public void setMethod(Method method) {
		this.method = method;
	}

	public int getBestFitness() {
		return bestFitness;
	}

	public void setBestFitness(int bestFitness) {
		this.bestFitness = bestFitness;
	}


	
	@Override
	public String toString() {
		return "Measure [instanceName=" + instanceName + ", executionTime=" + executionTime + ", developpedNodesNumber="
				+ developpedNodesNumber + ", bestFitness=" + bestFitness + ", method=" + method + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanceName == null) ? 0 : instanceName.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Measure other = (Measure) obj;
		if (instanceName == null) {
			if (other.instanceName != null)
				return false;
		} else if (!instanceName.equals(other.instanceName))
			return false;
		if (method != other.method)
			return false;
		return true;
	}

	@Override
	public int compareTo(Measure o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	
}
