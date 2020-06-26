package application;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import algorithms.AStar;
import algorithms.BFS;
import algorithms.DFS;
import algorithms.GeneticAlgorithm;
import algorithms.PSO;
import algorithms.SAT;
import data.Measure;
import data.MetaHeuristicMeasure;
import data.Method;
import data.Statistics;
import gui.controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import utils.SearchingUtils;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private Scene scene;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/resources/view/main_view.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root,1900,1000);
			Controller c = loader.getController();
			
			c.setStage(primaryStage);
			scene.getStylesheets().add(getClass().getResource("/gui/resources/css/application.css").toExternalForm());
			primaryStage.setTitle("Projet Métaheuristique");
			primaryStage.setScene(scene);
			
			
			this.scene = scene;
			
			primaryStage.show();

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		/*Statistics s = Statistics.getStats();
		
		for(int i=0;i<s.getMeasures().size();i++) {
			if(s.getMeasures().get(i).getInstanceName().equals("uf20-08.cnf")) {
				s.getMeasures().remove(i);
				System.out.println("done");
				break;
			}
		}
		
		s.saveMeasures();
		*/
		//launch(args);

		SAT sat;
		
		try {
			sat = SAT.loadFromFile("C:\\Users\\pc\\Desktop\\Projet métaheuristique\\a\\uf20-065.cnf");
			
			PSO pso = new PSO(sat);
			
			int[] sol = pso.run();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Statistics s = Statistics.getStats();
		/*
		Measure m1 = new MetaHeuristicMeasure("a",20,30,15,30,Method.GA);
		Measure m2 = new MetaHeuristicMeasure("a",20,30,15,40,Method.GA);
		
		s.addMeasure(m1);
		s.addMeasure(m2);
		*/
		/*
		for(Measure m : s.getMeasures()) {
			System.out.println(m);
		}
		 */
		/*
		SAT sat;
		try {
			sat = SAT.loadFromFile("C:\\Users\\pc\\Desktop\\Projet métaheuristique\\a\\uf20-065.cnf");
			GeneticAlgorithm ga = new GeneticAlgorithm(sat);

			int[] sol = ga.run();
			
			
			if(sol != null) {
				System.out.println("Solution : "+SearchingUtils.formatSolution(sol));
			}else {
				System.out.println("Solution not found : "+ga.getPopulation().get(0).getFitness());
			}
			
			System.out.println(ga.getExecutionTime());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	
	}
	
	public void getElementsById() {
		
	}
}
