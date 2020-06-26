package data;

import java.util.ArrayList;

public class SortedList extends ArrayList<Node>{

	
	public void insert(Node node) {
		int i=0;
		while(i<size() && node.cost()>get(i).cost()) {
			if(node.cost()<get(i).cost()) {
				 break;
			}
			i++;
		}
		
		add(i,node);
	}
	
	public Node remove() {
		return remove(0);
	}
}
