package data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class DataLogger {
	private static final int MAX_GRAPHS = 5;
	private final List<GraphData> savedGraphs;
	private final String fileDirectory;

	public DataLogger(String customDirectory) {
		// Default directory if none provided
		if (customDirectory == null || customDirectory.isBlank()) {
			this.fileDirectory = System.getProperty("user.home") + "/ProjectileSimulator";
		} else {
			this.fileDirectory = customDirectory;
		}

		ensureDirectoryExists();
		this.savedGraphs = loadGraphsFromFile();
		System.out.println("File path used by DataLogger: " + getFilePath());
	}

	public List<GraphData> getSavedGraphs() {
		return savedGraphs;
	}

	public void saveGraph(GraphData graphData) {
		if (savedGraphs.size() == MAX_GRAPHS) {
			savedGraphs.remove(0); // Remove the oldest graph to make space
		}
		savedGraphs.add(graphData); // Add the new graph

		saveGraphsToFile();
		System.out.println("Graph saved to " + getFilePath());
	}

	private void ensureDirectoryExists() {
		File directory = new File(fileDirectory);
		if (!directory.exists()) {
			boolean created = directory.mkdirs();
			if (created) {
				System.out.println("Directory created at: " + fileDirectory);
			} else {
				System.err.println("Failed to create directory at: " + fileDirectory);
			}
		}
	}

	private String getFilePath() {
		return fileDirectory + "/graphs.json";
	}

	private List<GraphData> loadGraphsFromFile() {
		File file = new File(getFilePath());
		if (!file.exists()) {
			System.out.println("Graphs file not found. Starting with an empty list.");
			return new ArrayList<>();
		}

		try (FileReader reader = new FileReader(file)) {
			Type listType = new TypeToken<ArrayList<GraphData>>() {}.getType();
			List<GraphData> graphs = new Gson().fromJson(reader, listType);

			return graphs;
		} catch (JsonSyntaxException e) {
			System.err.println("Error parsing JSON file: " + e.getMessage());
			backupCorruptedFile();
			return new ArrayList<>();
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	private void saveGraphsToFile() {
		File file = new File(getFilePath());
		try (FileWriter writer = new FileWriter(file)) {
			new Gson().toJson(savedGraphs, writer);
			System.out.println("Graphs saved successfully to: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error saving graphs to file: " + e.getMessage());
		}
	}

	private void backupCorruptedFile() {
		File corruptedFile = new File(getFilePath());
		File backupFile = new File(fileDirectory + "/graphs_backup.json");

		if (corruptedFile.exists()) {
			try {
				Files.copy(corruptedFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.err.println("Backed up corrupted graphs.json to graphs_backup.json.");
			} catch (IOException e) {
				System.err.println("Failed to create backup of corrupted file: " + e.getMessage());
			}
		}
	}
}
