package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataLogger {
	private static final String FILE_PATH = "savedGraphs.json";
	private List<GraphData> savedGraphs;

	public DataLogger() {
		this.savedGraphs = loadGraphs();
		if (this.savedGraphs == null) {
			this.savedGraphs = new ArrayList<>();
		}
	}

	//Saves a new graph to the logger Maintains a maximum of 5 graphs.
	public void saveGraph(GraphData graph) {
		if (savedGraphs.size() >= 5) {
			savedGraphs.remove(0); // Remove the oldest graph
		}
		savedGraphs.add(graph);
		saveToFile();
	}

	//Loads saved graphs from JSON file.
	private List<GraphData> loadGraphs() {
		try (Reader reader = new FileReader(FILE_PATH)) {
			Gson gson = new Gson();
			return gson.fromJson(reader, new TypeToken<List<GraphData>>() {}.getType());
		} catch (FileNotFoundException e) {
			System.out.println("No saved graphs found, starting fresh.");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	//Saves the current list of graphs to JSON.
	private void saveToFile() {
		try (Writer writer = new FileWriter(FILE_PATH)) {
			Gson gson = new Gson();
			gson.toJson(savedGraphs, writer);
			System.out.println("Graphs saved to " + FILE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Returns all saved graphs.
	public List<GraphData> getSavedGraphs() {
		return new ArrayList<>(savedGraphs);
	}
}
