package gui.controllers;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import algorithms.AStar;
import algorithms.BFS;
import algorithms.DFS;
import algorithms.GeneticAlgorithm;
import algorithms.SAT;
import data.Measure;
import data.Method;
import data.Statistics;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.SearchingUtils;

public class Controller implements Initializable{
	
	private Stage stage;
	private SAT sat;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void close(ActionEvent event) {
		Alert a =  new Alert(AlertType.CONFIRMATION,"Voulez-vous vraiment quitter ?",ButtonType.YES,ButtonType.NO);
		
		Optional<ButtonType> result = a.showAndWait();
		
		if(result.get() == ButtonType.YES) {
			Platform.exit();
			System.exit(0);
		}
		
	}
	
	@FXML
	private void showClauses(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données au format .cnf à partir de Fichier->Importer un fichier cnf",ButtonType.OK);
			a.showAndWait();
			return;
		}
		
		setTab(1);
	}
	
	@FXML 
	private void showInfos(ActionEvent event) {
		Alert a =  new Alert(AlertType.INFORMATION,"Projet encadré par Mme Drias H.\nProgramme réalisé par Arabi Sami et Bokhari Lotfi",ButtonType.CLOSE);
		
		a.setHeaderText("Projet intelligence en essaim");
		a.setTitle("A propos");
		
		a.showAndWait();
	}
	
	@FXML
	private void openFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CNF Files", "*.cnf"));
		fileChooser.setTitle("Ouvrir fichier cnf");
		File f = fileChooser.showOpenDialog(stage);
		
		
		try {
			if(f!=null) {
				sat = SAT.loadFromFile(f.getAbsolutePath());
				
				Text clausesNumberDisplay = (Text) stage.getScene().lookup("#clauses-number-display");
				Text clausesLengthDisplay = (Text) stage.getScene().lookup("#clauses-length-display");
				Text VariablesNumberDisplay = (Text) stage.getScene().lookup("#variables-number-display");
				Text loadingInfosDisplay = (Text) stage.getScene().lookup("#loading-infos-display");
				Text clausesListDisplay = (Text) stage.getScene().lookup("#clauses-list-display");
				
				clausesNumberDisplay.setText(String.valueOf(sat.getClausesNumber()));
				clausesLengthDisplay.setText(String.valueOf(sat.getClausesLength()));
				VariablesNumberDisplay.setText(String.valueOf(sat.getVariablesNumber()));
				loadingInfosDisplay.setText("Fichier "+f.getName()+" correctement lu");
				clausesListDisplay.setText(sat.toString());
				
				setTab(1);
			}
			
		}catch(Exception e) {
			Alert a = new Alert(AlertType.ERROR,"Le fichier "+f.getName()+" n'a pas pu être lu",ButtonType.OK);
			a.showAndWait();
		}
		
	}
	
	@FXML
	private void searchDFS(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données au format .cnf à partir de Fichier->Importer un fichier cnf",ButtonType.OK);
			a.showAndWait();
			return;
		}
		setTab(2);
		
		new Thread(() -> {

			DFS dfs = new DFS(sat);
			
			int[] solution = dfs.run();
			
			Text algorithmChoiceDisplay = (Text) stage.getScene().lookup("#algorithm-choice-result");
			Text solutionDisplay = (Text) stage.getScene().lookup("#solution-result");
			Text nbNodesDeveloppedDisplay = (Text) stage.getScene().lookup("#nb-nodes-developped-result");
			Text executionTimeDisplay = (Text) stage.getScene().lookup("#execution-time-result");
			Text executionResultsTitle = (Text) stage.getScene().lookup("#execution-results-title");
			
			
			executionResultsTitle.setText("Résultats d'exécution de l'algorithme DFS :");
			algorithmChoiceDisplay.setText("Profondeur\nD'abord");
			
			if(solution != null) {
				solutionDisplay.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionDisplay.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedDisplay.setText(String.valueOf(dfs.getDeveloppedNodesNumber()));
			executionTimeDisplay.setText(String.valueOf(dfs.getExecutionTime())+" s");
			setTab(3);
		}).start();
	}
	
	@FXML
	private void searchBFS(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données au format .cnf à partir de Fichier->Importer un fichier cnf",ButtonType.OK);
			a.showAndWait();
			return;
		}
		setTab(2);
		
		new Thread(() -> {

			BFS bfs = new BFS(sat);
			
			int[] solution = bfs.run();
			
			Text algorithmChoiceDisplay = (Text) stage.getScene().lookup("#algorithm-choice-result");
			Text solutionDisplay = (Text) stage.getScene().lookup("#solution-result");
			Text nbNodesDeveloppedDisplay = (Text) stage.getScene().lookup("#nb-nodes-developped-result");
			Text executionTimeDisplay = (Text) stage.getScene().lookup("#execution-time-result");
			Text executionResultsTitle = (Text) stage.getScene().lookup("#execution-results-title");
			
			executionResultsTitle.setText("Résultats d'exécution de l'algorithme BFS :");
			algorithmChoiceDisplay.setText("Largeur\nD'abord");
			
			if(solution != null) {
				solutionDisplay.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionDisplay.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedDisplay.setText(String.valueOf(bfs.getDeveloppedNodesNumber()));
			executionTimeDisplay.setText(String.valueOf(bfs.getExecutionTime())+" s");
			setTab(3);
		}).start();
	}
	

	@FXML
	private void searchAStar(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données au format .cnf à partir de Fichier->Importer un fichier cnf",ButtonType.OK);
			a.showAndWait();
			return;
		}
		setTab(2);
		
		new Thread(() -> {

			AStar astar = new AStar(sat);
			
			int[] solution = astar.run();
			
			Text algorithmChoiceDisplay = (Text) stage.getScene().lookup("#algorithm-choice-result");
			Text solutionDisplay = (Text) stage.getScene().lookup("#solution-result");
			Text nbNodesDeveloppedDisplay = (Text) stage.getScene().lookup("#nb-nodes-developped-result");
			Text executionTimeDisplay = (Text) stage.getScene().lookup("#execution-time-result");
			Text executionResultsTitle = (Text) stage.getScene().lookup("#execution-results-title");
			
			executionResultsTitle.setText("Résultats d'exécution de l'algorithme A* :");
			algorithmChoiceDisplay.setText("A*");
			
			if(solution != null) {
				solutionDisplay.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionDisplay.setText("Insatisfaisable");
			}
			
			nbNodesDeveloppedDisplay.setText(String.valueOf(astar.getDeveloppedNodesNumber()));
			executionTimeDisplay.setText(String.valueOf(astar.getExecutionTime())+" s");
			setTab(3);
		}).start();
	}
	
	@FXML
	private void searchGA(ActionEvent event) {
		if(sat == null) {
			Alert a = new Alert(AlertType.ERROR,"Vous devez d'abord sélectionner des données au format .cnf à partir de Fichier->Importer un fichier cnf",ButtonType.OK);
			a.showAndWait();
			return;
		}
		setTab(2);
		
		new Thread(() -> {

			//GeneticAlgorithm ga = new GeneticAlgorithm(sat);
			
			GeneticAlgorithm ga = null;
			
			int[] solution = ga.run();
			
			Text solutionDisplay = (Text) stage.getScene().lookup("#solution-result-ga");
			Text executionTimeDisplay = (Text) stage.getScene().lookup("#execution-time-result-ga");
			Text executionResultsTitle = (Text) stage.getScene().lookup("#execution-results-title");
			Text parametersGA = (Text) stage.getScene().lookup("#parameters-ga");
			
			executionResultsTitle.setText("Résultats d'exécution de l'algorithme génétique :");
			
			if(solution != null) {
				solutionDisplay.setText(SearchingUtils.formatSolution(solution));
			}else {
				solutionDisplay.setText("Solution non trouvée");
			}
			
			String s;
			
			s = "Taille population : "+ga.getPopulationSize()+"\n";
			s += "Taux de croisement : "+ga.getCrossoverRate()+"\n";
			s += "Taux de mutation : "+ga.getMutationRate()+"\n";
			s += "Nb itérations : "+ga.getDeveloppedNodesNumber();
			
			parametersGA.setText(s);
			
			executionTimeDisplay.setText(String.valueOf(ga.getExecutionTime())+" s");
			setTab(7);
		}).start();
	}
	
	@FXML
	private void showNumberStatesDevelopped(ActionEvent event) {
		LineChart<String,Integer> chart = (LineChart) stage.getScene().lookup("#states-number-chart");
		
		chart.setAnimated(false);
		chart.setLegendVisible(true);
		chart.setLegendSide(Side.RIGHT);
		chart.setTitle("Graphe comparatif des cinq méthodes de résolution (nombre d'états développés)");
		chart.setTitleSide(Side.BOTTOM);
		chart.getData().clear();
		
		Statistics s = Statistics.getStats();
		
		
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

		chart.getData().addAll(seriesBFS,seriesDFS,seriesAStar,seriesGA,seriesPSO);


		setTab(4);
	}
	
	@FXML
	private void showAvegargeStatesDevelopped(ActionEvent event) {
		BarChart<String,Double> chart = (BarChart) stage.getScene().lookup("#states-average-chart");
		
		chart.setAnimated(false);
		chart.setLegendVisible(false);
		chart.setTitle("Graphe comparatif des cinq méthodes de résolution (Moyenne des états développés)");
		chart.setTitleSide(Side.BOTTOM);
		chart.getData().clear();
		chart.setCategoryGap(0);
		chart.setBarGap(0);
		
		
		Statistics s = Statistics.getStats();
		
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
	
		chart.getData().add(serie);
		
		int index = -1;
		
		for(Node n:chart.lookupAll(".default-color0.chart-bar")) {
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


		setTab(5);
		
	}
	
	@FXML
	private void showExecutionTime(ActionEvent event) {
		LineChart<String,Double> chart = (LineChart) stage.getScene().lookup("#exec-time-chart");
		
		chart.setAnimated(false);
		chart.setLegendVisible(true);
		chart.setLegendSide(Side.RIGHT);
		chart.setTitle("Graphe comparatif des cinq méthodes de résolution (temps d'exécution)");
		chart.setTitleSide(Side.BOTTOM);
		chart.getData().clear();
		
		Statistics s = Statistics.getStats();
		
		
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

		chart.getData().addAll(seriesBFS,seriesDFS,seriesAStar,seriesGA,seriesPSO);

		setTab(6);
		
	}
	
	

	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}
	
	private void setTab(int index) {
		TabPane tabPane = (TabPane) stage.getScene().lookup("#main-tab-pane");
		
		tabPane.getSelectionModel().select(index);
		
	}

}
