package data;

public class MetaHeuristicMeasure extends Measure{
	private int maxIter;

	public MetaHeuristicMeasure(String instanceName, double executionTime, int developpedNodesNumber, int bestFitness, int maxIter, Method method) {
		super(instanceName, executionTime, developpedNodesNumber, bestFitness, method);
		this.maxIter = maxIter;
	}
	
	public MetaHeuristicMeasure(MetaHeuristicMeasure m) {
		super(m.instanceName, m.executionTime, m.developpedNodesNumber, m.bestFitness, m.method);
		this.maxIter = m.maxIter;
	}

	@Override
	public String toString() {
		return "MetaHeuristicMeasure [maxIter=" + maxIter + ", Measure=" + super.toString() + "]";
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((instanceName == null) ? 0 : instanceName.hashCode());
		result = prime * result + maxIter;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaHeuristicMeasure other = (MetaHeuristicMeasure) obj;
		if (instanceName == null) {
			if (other.instanceName != null)
				return false;
		} else if (!instanceName.equals(other.instanceName))
			return false;
		if (maxIter != other.maxIter)
			return false;
		if (method != other.method)
			return false;
		return true;
	}

	@Override
	public int compareTo(Measure m) {
		if(m instanceof MetaHeuristicMeasure) {
			return maxIter<((MetaHeuristicMeasure)m).maxIter ? -1 : maxIter == ((MetaHeuristicMeasure)m).maxIter ? 0 : 1;
		}else {
			return super.compareTo((Measure)m);
		}
	}





	
}
