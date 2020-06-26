package gui.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;

import algorithms.AStar;
import algorithms.BFS;
import algorithms.DFS;
import algorithms.GeneticAlgorithm;
import algorithms.SAT;
import data.Measure;
import data.MetaHeuristicMeasure;
import data.Method;
import data.Statistics;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.NumberFormatter;
import utils.SearchingUtils;

public class MainController extends Controller implements Initializable{
	private Stage stage;
	private SAT sat;
	
	@FXML
	private Menu selectedDataset, uf20Dataset, uf50Dataset, uf75Dataset;
	
	@FXML
	private ComboBox<String> methodChoice, performanceChoice;
	
	@FXML
	private TabPane performanceTabPane, resultsPane;
	
	@FXML
	private Text clausesListDisplay, executionTimeResult, nbNodesDeveloppedResult, solutionResult;
	
	@FXML
	private HBox metaheuristicsPane;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private LineChart<String,Integer> nbNodesChart, qualityChart;
	
	@FXML
	private LineChart<String,Double> executionTimeChart;
	
	@FXML
	private BarChart<String,Double> averageNbNodesChart;
	
	@FXML
	private TextField populationSizeInput, maxIterInput, crossoverRateInput, mutationRateInput;
	
	@FXML 
	private LineChart<String,Integer> metaHeuristicsChart;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populationSizeInput.setTextFormatter(new NumberFormatter());
		maxIterInput.setTextFormatter(new NumberFormatter());	
		crossoverRateInput.setTextFormatter(new NumberFormatter());
		mutationRateInput.setTextFormatter(new NumberFormatter());
		
		metaheuristicsPane.setVisible(false);
		methodChoice.setItems(FXCollections.observableArrayList("BFS","DFS","A*","Algorithme Génétique","PSO"));
		methodChoice.getSelectionModel().select(0);
		
		performanceChoice.setItems(FXCollections.observableArrayList("Temps d'exécution","Nombre de noeuds développés","Moyenne noeuds développés","Qualité"));
		performanceChoice.getSelectionModel().select(0);
		
		performanceChoice.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				showStats();
				
			}
			
		});
		
		methodChoice.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				switch(arg2) {
				case "BFS":
				case "DFS":
				case "A*":
					metaheuristicsPane.setVisible(false);
					break;
				case "Algorithme Génétique":
				case "PSO":
					metaheuristicsPane.setVisible(true);
					break;
				}
				
				showStats();
				showMetaHeuristicsChart(SearchingUtils.getInstanceType(sat.getInstanceSource()),SearchingUtils.getInstanceNumber(sat.getInstanceSource()));
				
			}
			
		});
		
		selectedDataset.setText("Choisir");
		for(int i=0;i<1000;i++) {
			MenuItem m1 = new MenuItem(String.valueOf(i+1)), m2 = new MenuItem(String.valueOf(i+1)), m3 = new MenuItem(String.valueOf(i+1));
			uf20Dataset.getItems().add(m1);
			uf50Dataset.getItems().add(m2);
			uf75Dataset.getItems().add(m3);
			
			final int j = i;
			
			m1.setOnAction(new EventHandler<ActionEvent>() {
				
				
				@Override
				public void handle(ActionEvent arg0) {
					openFile(20,j+1);
					
					clausesListDisplay.setText(sat.toString());
					selectedDataset.setText(sat.getInstanceSource());
					showStats();
					showMetaHeuristicsChart(SearchingUtils.getInstanceType(sat.getInstanceSource()),SearchingUtils.getInstanceNumber(sat.getInstanceSource()));
					
				}
				
			});			
			m2.setOnAction(new EventHandler<ActionEvent>() {
				
				
				@Override
				public void handle(ActionEvent arg0) {
					openFile(50,j+1);
					
					clausesListDisplay.setText(sat.toString());		
					selectedDataset.setText(sat.getInstanceSource());
					showStats();
					showMetaHeuristicsChart(SearchingUtils.getInstanceType(sat.getInstanceSource()),SearchingUtils.getInstanceNumber(sat.getInstanceSource()));
					
				}
				
			});			
			m3.setOnAction(new EventHandler<ActionEvent>() {
				
				
				@Override
				public void handle(ActionEvent arg0) {
					openFile(75,j+1);
					
					clausesListDisplay.setText(sat.toString());	
					selectedDataset.setText(sat.getInstanceSource());
					showStats();
					showMetaHeuristicsChart(SearchingUtils.getInstanceType(sat.getInstanceSource()),SearchingUtils.getInstanceNumber(sat.getInstanceSource()));
					
				}
				
			});
		}
		
		
		nbNodesChart.setAnimated(false);
		nbNodesChart.setLegendVisible(true);
		nbNodesChart.setLegendSide(Side.RIGHT);
		nbNodesChart.setTitle("Graphe comparatif des cinq méthodes de résolution (nombre d'états développés)");
		nbNodesChart.setTitleSide(Side.BOTTOM);
		
		executionTimeChart.setAnimated(false);
		executionTimeChart.setLegendVisible(true);
		executionTimeChart.setLegendSide(Side.RIGHT);
		executionTimeChart.setTitle("Graphe comparatif des cinq méthodes de résolution (temps d'exécution)");
		executionTimeChart.setTitleSide(Side.BOTTOM);
		
		averageNbNodesChart.setAnimated(false);
		averageNbNodesChart.setLegendVisible(true);
		averageNbNodesChart.setLegendSide(Side.RIGHT);
		averageNbNodesChart.setTitle("Graphe comparatif des cinq méthodes de résolution (Moyenne des états développés)");
		averageNbNodesChart.setTitleSide(Side.BOTTOM);		
		
		qualityChart.setAnimated(false);
		qualityChart.setLegendVisible(true);
		qualityChart.setLegendSide(Side.RIGHT);
		qualityChart.setTitle("Graphe comparatif des cinq méthodes de résolution (Meilleure qualité)");
		qualityChart.setTitleSide(Side.BOTTOM);
	
		metaHeuristicsChart.setAnimated(false);
		metaHeuristicsChart.setLegendVisible(false);
		metaHeuristicsChart.setLegendSide(Side.RIGHT);
		metaHeuristicsChart.setTitle("Amélioration de la fonction fitness en fonction de MaxIter");
		metaHeuristicsChart.setTitleSide(Side.BOTTOM);
	}
	

	private void openFile(int instanceType, int instanceNumber) {

		String path = "./instances/uf"+instanceType+"-";
		
		switch(instanceType) {
			case 20:
				path+="91/";
				break;
			case 50:
				path+="218/";
				break;
			case 75:
				path+="325/";
			break;
		}
		
		path+= "uf"+instanceType+"-0"+instanceNumber+".cnf";
		
		System.out.println(path);
		
		File f = new File(path);
		
		
		try {
			if(f!=null) {
				sat = SAT.loadFromFile(f.getAbsolutePath());

				Text clausesListDisplay = (Text) stage.getScene().lookup("#clauses-list-display");

				clausesListDisplay.setText(sat.toString());
				selectedDataset.setText(sat.getInstanceSource());
				
			}
			
		}catch(Exception e) {
		}
		
	}
	
	@FXML
	private void search(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données",ButtonType.OK);
			a.showAndWait();
			return;
		}
		
		resultsPane.getSelectionModel().select(1);
		switch(methodChoice.getSelectionModel().getSelectedItem()) {
			case "BFS":
				searchBFS();
				break;		
			case "DFS":
				searchDFS();
				break;
			case "A*":
				searchAStar();
				break;
			case "Algorithme Génétique":
				searchGA();
				break;
			case "PSO":
				searchPSO();
				break;
		}
		
	}
	
	private void searchBFS() {
		new Thread(()->{

			BFS bfs = new BFS(sat);
			
			int[] solution = bfs.run();
			

			if(solution != null) {
				solutionResult.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionResult.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedResult.setText(String.valueOf(bfs.getDeveloppedNodesNumber()));
			executionTimeResult.setText(String.valueOf(bfs.getExecutionTime())+" s");

			resultsPane.getSelectionModel().select(0);
			Platform.runLater(()->{
				showStats();
			});
		}).start();
		
	}
	
	private void searchDFS() {
		new Thread(() -> {
			DFS dfs = new DFS(sat);
			
			int[] solution = dfs.run();

			
			if(solution != null) {
				solutionResult.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionResult.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedResult.setText(String.valueOf(dfs.getDeveloppedNodesNumber()));
			executionTimeResult.setText(String.valueOf(dfs.getExecutionTime())+" s");

			resultsPane.getSelectionModel().select(0);
			Platform.runLater(()->{
				showStats();
			});
		}).start();
	}
	
	private void searchAStar() {
		new Thread(()->{
			AStar astar = new AStar(sat);
			
			int[] solution = astar.run();

			
			if(solution != null) {
				solutionResult.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionResult.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedResult.setText(String.valueOf(astar.getDeveloppedNodesNumber()));
			executionTimeResult.setText(String.valueOf(astar.getExecutionTime())+" s");

			resultsPane.getSelectionModel().select(0);
			Platform.runLater(()->{
				showStats();
			});
		}).start();
	}
	
	private void searchGA() {
		new Thread(()->{
			if(populationSizeInput.getText().isEmpty() || maxIterInput.getText().isEmpty() || crossoverRateInput.getText().isEmpty() || mutationRateInput.getText().isEmpty()) {
				System.out.println("hey !");
				Platform.runLater(()->{
					Alert a = new Alert(AlertType.ERROR,"Vous devez introduire les paramètres",ButtonType.OK);
					a.showAndWait();
				});
				return;
			}
			
			int populationSize = Integer.parseInt(populationSizeInput.getText());
			int maxIter = Integer.parseInt(maxIterInput.getText());
			int crossoverRate = Integer.parseInt(crossoverRateInput.getText());
			int mutationRate = Integer.parseInt(mutationRateInput.getText());
			
			
			GeneticAlgorithm ga = new GeneticAlgorithm(sat,maxIter, populationSize, crossoverRate, mutationRate);
			
			int[] solution = ga.run();
			
			if(solution != null) {
				solutionResult.setText(SearchingUtils.formatSolution(solution));
			}else {
				System.out.println("Solution non trouvée");
				solutionResult.setText("Solution non trouvée");
			}
			
			String s;
			
			s = "Taille population : "+ga.getPopulationSize()+"\n";
			s += "Taux de croisement : "+ga.getCrossoverRate()+"\n";
			s += "Taux de mutation : "+ga.getMutationRate()+"\n";
			s += "Nb itérations : "+ga.getDeveloppedNodesNumber();
			
			nbNodesDeveloppedResult.setText(s);
			
			executionTimeResult.setText(String.valueOf(ga.getExecutionTime())+" s");

			resultsPane.getSelectionModel().select(0);
			Platform.runLater(()->{
				showStats();
				showMetaHeuristicsChart(SearchingUtils.getInstanceType(sat.getInstanceSource()),SearchingUtils.getInstanceNumber(sat.getInstanceSource()));
			});
		}).start();;
	}
	
	private void searchPSO() {
		
	}
	
	private void showStats() {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données",ButtonType.OK);
			a.showAndWait();
			return;
		}
		
		switch(performanceChoice.getSelectionModel().getSelectedItem()) {
		case "Temps d'exécution":
			showExecutionTime(sat.getVariablesNumber());
			performanceTabPane.getSelectionModel().select(0);
			break;
		case "Nombre de noeuds développés":
			showNumberStatesDevelopped(sat.getVariablesNumber());
			performanceTabPane.getSelectionModel().select(1);
			break;
		case "Moyenne noeuds développés":
			showAverageStatesDevelopped(sat.getVariablesNumber());
			performanceTabPane.getSelectionModel().select(2);
			break;
		case "Qualité":
			showQuality(sat.getVariablesNumber());
			performanceTabPane.getSelectionModel().select(3);
			break;

		}
	}
	

	private void showNumberStatesDevelopped(int instanceType) {

		nbNodesChart.getData().clear();
		
		Statistics s = Statistics.getStats().filterStats(instanceType).eliminateRedundancies();
		
		
		
		XYChart.Series<String, Integer> seriesBFS = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesDFS = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesAStar = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesGA = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesPSO = new XYChart.Series<>();
		
		seriesBFS.setName("BFS");
		seriesDFS.setName("DFS");
		seriesAStar.setName("A*");
		seriesGA.setName("Genetique");
		seriesPSO.setName("PSO");
		
		for(Measure m : s.getMeasures()) {
			switch(m.getMethod()) {
			case BFS:
				seriesBFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getDeveloppedNodesNumber()));
				break;
			case DFS:
				seriesDFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getDeveloppedNodesNumber()));
				break;
			case A_STAR:
				seriesAStar.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getDeveloppedNodesNumber()));
				break;
			case GA:
				seriesGA.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getDeveloppedNodesNumber()));
				break;
			case PSO:
				seriesPSO.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getDeveloppedNodesNumber()));
				break;
			}
		}

		nbNodesChart.getData().addAll(seriesBFS,seriesDFS,seriesAStar,seriesGA,seriesPSO);

	}
	
	private void showAverageStatesDevelopped(int instanceType) {
		
		averageNbNodesChart.setAnimated(false);
		averageNbNodesChart.setLegendVisible(false);
		averageNbNodesChart.setTitle("Graphe comparatif des cinq méthodes de résolution (Moyenne des états développés)");
		averageNbNodesChart.setTitleSide(Side.BOTTOM);
		averageNbNodesChart.getData().clear();
		averageNbNodesChart.setCategoryGap(0);
		averageNbNodesChart.setBarGap(0);
		
		
		Statistics s = Statistics.getStats().filterStats(instanceType).eliminateRedundancies();
		
		XYChart.Series<String, Double> serie = new XYChart.Series<>();
		
		double BFSMean = s.getMean(Method.BFS);
		double DFSMean = s.getMean(Method.DFS);
		double AStarMean = s.getMean(Method.A_STAR);
		double GAMean = s.getMean(Method.GA);
		double PSOMean = s.getMean(Method.PSO);

		
		
		serie.getData().add(new XYChart.Data<>("BFS",BFSMean));
		serie.getData().add(new XYChart.Data<>("DFS",DFSMean));
		serie.getData().add(new XYChart.Data<>("A*",AStarMean));
		serie.getData().add(new XYChart.Data<>("Genetique",GAMean));
		serie.getData().add(new XYChart.Data<>("PSO",PSOMean));
	
		averageNbNodesChart.getData().add(serie);
		
		int index = -1;
		
		for(Node n:averageNbNodesChart.lookupAll(".default-color0.chart-bar")) {
			switch(index) {
			case 0:
	            n.setStyle("-fx-bar-fill: #f9d900;");
				break;
			case 1:
	            n.setStyle("-fx-bar-fill: #a9e200;");
				break;
			case 2:
	            n.setStyle("-fx-bar-fill: #22bad9;");
				break;
			case 3:
	            n.setStyle("-fx-bar-fill: #0181e2;");
				break;
			case 4:
	            n.setStyle("-fx-bar-fill: #2f357f;");
				break;
			}
			index++;
        }


		
	}
	
	private void showExecutionTime(int instanceType) {

		executionTimeChart.getData().clear();
		
		Statistics s = Statistics.getStats().filterStats(instanceType).eliminateRedundancies();
		
		
		XYChart.Series<String, Double> seriesBFS = new XYChart.Series<>();
		XYChart.Series<String, Double> seriesDFS = new XYChart.Series<>();
		XYChart.Series<String, Double> seriesAStar = new XYChart.Series<>();
		XYChart.Series<String, Double> seriesGA = new XYChart.Series<>();
		XYChart.Series<String, Double> seriesPSO = new XYChart.Series<>();
		
		seriesBFS.setName("BFS");
		seriesDFS.setName("DFS");
		seriesAStar.setName("A*");
		seriesGA.setName("Genetique");
		seriesPSO.setName("PSO");
		
		for(Measure m : s.getMeasures()) {
		
			switch(m.getMethod()) {
			case BFS:
				seriesBFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getExecutionTime()));
				break;
			case DFS:
				seriesDFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getExecutionTime()));
				break;
			case A_STAR:
				seriesAStar.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getExecutionTime()));
				break;
			case GA:
				seriesGA.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getExecutionTime()));
				break;
			case PSO:
				seriesPSO.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getExecutionTime()));
				break;
			}
		}

		executionTimeChart.getData().addAll(seriesBFS,seriesDFS,seriesAStar,seriesGA,seriesPSO);

		
	}
	
	private void showQuality(int instanceType) {

		qualityChart.getData().clear();
		
		Statistics s = Statistics.getStats().filterStats(instanceType).eliminateRedundancies();
		
		
		
		XYChart.Series<String, Integer> seriesBFS = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesDFS = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesAStar = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesGA = new XYChart.Series<>();
		XYChart.Series<String, Integer> seriesPSO = new XYChart.Series<>();
		
		seriesBFS.setName("BFS");
		seriesDFS.setName("DFS");
		seriesAStar.setName("A*");
		seriesGA.setName("Genetique");
		seriesPSO.setName("PSO");
		
		for(Measure m : s.getMeasures()) {
			switch(m.getMethod()) {
			case BFS:
				seriesBFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getBestFitness()));
				break;
			case DFS:
				seriesDFS.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getBestFitness()));
				break;
			case A_STAR:
				seriesAStar.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getBestFitness()));
				break;
			case GA:
				seriesGA.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getBestFitness()));
				break;
			case PSO:
				seriesPSO.getData().add(new XYChart.Data<>(m.getInstanceName(),m.getBestFitness()));
				break;
			}
		}

		qualityChart.getData().addAll(seriesBFS,seriesDFS,seriesAStar,seriesGA,seriesPSO);
		
	}
	
	private void showMetaHeuristicsChart(int instanceType, int instanceNumber) {
		metaHeuristicsChart.getData().clear();
		
		Statistics s = Statistics.getStats().filterStats(instanceType, instanceNumber);
		
		ArrayList<Measure> measures = s.getMeasures();
		
		measures.sort(null);
		
		
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		
		
		for(Measure m : measures) {
			switch(m.getMethod()) {
			case GA:
				MetaHeuristicMeasure mh = (MetaHeuristicMeasure) m;
				series.getData().add(new XYChart.Data<>((String.valueOf(mh.getMaxIter())),m.getBestFitness()));
				break;
			case PSO:
				MetaHeuristicMeasure mh2 = (MetaHeuristicMeasure) m;
				series.getData().add(new XYChart.Data<>(String.valueOf(mh2.getMaxIter()),m.getBestFitness()));
				break;
			default:
				break;
			}
		}
		
        for (Data<String, Integer> entry : series.getData()) {  
            Tooltip t = new Tooltip(entry.getYValue().toString());
            Tooltip.install(entry.getNode(), t);
        }

		metaHeuristicsChart.getData().add(series);
	}
	

}
