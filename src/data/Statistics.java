package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import utils.SearchingUtils;

public class Statistics implements Serializable{

	private static final long serialVersionUID = -6408096768701305087L;
	private transient static final String savePath ="./data.sat";
	ArrayList<Measure> measures;

	private Statistics() {
		this.measures = new ArrayList<>();
	}
	
	public static Statistics getStats() {
		Statistics s = new Statistics();
		s.measures = new ArrayList<>();
		s.loadMeasures();
		
		return s;
	}

	
	private void loadMeasures() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(savePath)));
			
			Statistics s= (Statistics) ois.readObject();
			this.measures.addAll(s.measures);

			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			File f = new File(savePath);
			try {
				f.createNewFile();
				ois = new ObjectInputStream(new FileInputStream(f));
				Statistics s= (Statistics) ois.readObject();
				this.measures.addAll(s.measures);

				ois.close();
			} catch (IOException | ClassNotFoundException e1) {
			}
		}
	}
	
	public void addMeasure(Measure m) {
		int i = -1;
		while((i = indexOf(m)) != -1) {
			measures.remove(i);
		}
		
		measures.add(m);
		
		saveMeasures();
	}
	
	public void addMeasureWithoutSave(Measure m) {
		int i = -1;
		while((i = indexOf(m)) != -1) {
			measures.remove(i);
		}
		
		measures.add(m);
	}
	
	public void addAllMeasures(ArrayList<Measure> meas) {
		for(Measure m : meas) {
			if(!measures.contains(m)) {
				measures.add(m);
			}
		}
		
	}
	
	public int indexOf(Measure measure) {
		
		for(int i=0;i<measures.size();i++) {
			Measure m = measures.get(i);
			if(measure.equals(m)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void saveMeasures() {
		ObjectOutputStream oos;
		Statistics s = Statistics.getStats();
		s.addAllMeasures(this.measures);
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(savePath)));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<Measure> getMeasures() {
		return measures;
	}
	
	public String[] getInstances() {
		ArrayList<String> ins = new ArrayList<>();
		
		for(Measure m : measures) {
			ins.add(m.getInstanceName());
		}
		
		return (String[]) ins.toArray(new String[ins.size()]);
	}
	
	public double getMean(Method method) {
		double mean = 0;
		int nb=0;
		
		for(Measure m : measures) {
			if(m.getMethod().equals(method)) {
				mean+=m.getDeveloppedNodesNumber();
				nb++;
			}
		}
		
		if(mean == 0) {
			return 0;
		}else {
			return mean/nb;
		}
	}
	
	public Statistics filterStats(int instanceType) {
		Statistics s = new Statistics();
		
		for(Measure m : measures) {
			if(SearchingUtils.getInstanceType(m.getInstanceName()) == instanceType && s.indexOf(m) == -1) {
				s.addMeasure(m);
			}
		}
		
		return s;
	}
	
	
	public Statistics filterStats(int instanceType,int instanceNumber) {
		Statistics s = new Statistics();
		
		for(Measure m : measures) {
			if(SearchingUtils.getInstanceType(m.getInstanceName()) == instanceType && SearchingUtils.getInstanceNumber(m.getInstanceName()) == instanceNumber && s.indexOf(m) == -1) {
				s.addMeasureWithoutSave(m);
			}
		}
		
		return s;
	}
	
	public Statistics eliminateRedundancies() {
		Statistics s = new Statistics();
		int index;
		
		for(Measure m : measures) {
			if(s.contains(m) == -1) {
				s.addMeasureWithoutSave(m);
			}/*else if((index = s.contains(m)) != -1 && s.measures.get(index).getBestFitness() < m.getBestFitness()) {
				s.measures.remove(index);
				s.measures.add(index, m instanceof MetaHeuristicMeasure ? new MetaHeuristicMeasure((MetaHeuristicMeasure)m) : new Measure(m));
			}*/
		}
		
		return s;
	}
	
	public int contains(Measure m) {
		int i=0;
		for(Measure meas : measures) {
			if(meas.getInstanceName().equals(m.getInstanceName()) && meas.getMethod().equals(m.getMethod()))
				return i;
			i++;
		}
		
		return -1;
	}
	
}
