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
			savedGraphs.removeFirst(); // Remove the oldest graph
		}

		savedGraphs.add(graphData);
		saveGraphsToFile();
		System.out.println("Graph saved to " + getFilePath());
	}

	private void ensureDirectoryExists() {
		File directory = new File(fileDirectory);
		if (!directory.exists()) {
			boolean created = directory.mkdirs();
			if (created) {
				System.out.println("Created directory at " + fileDirectory);
			} else {
				System.err.println("Failed to create directory at " + fileDirectory);
			}
		}
	}

	private String getFilePath() {
		return fileDirectory + "/graphs.json";
	}

	private List<GraphData> loadGraphsFromFile() {
		File file = new File(getFilePath());
		if (!file.exists()) {
			System.out.println("No graphs file found. Creating a new empty list.");
			return new ArrayList<>();
		}

		try (FileReader reader = new FileReader(file)) {
			Type listType = new TypeToken<ArrayList<GraphData>>() {}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (JsonSyntaxException | IOException e) {
			System.err.println("Error loading graphs: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	private void backupFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println("No file to back up at " + filePath);
			return;
		}

		File backup = new File(filePath + ".bak");
		try {
			Files.copy(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Backup created: " + backup.getAbsolutePath());
		} catch (IOException ex) {
			System.err.println("Failed to create backup: " + ex.getMessage());
		}
	}

	private void saveGraphsToFile() {
		File file = new File(getFilePath());
		try (FileWriter writer = new FileWriter(file)) {
			new Gson().toJson(savedGraphs, writer);
			System.out.println("Graphs successfully saved to file: " + file.getAbsolutePath());
			backupFile(file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Failed to save graphs to file: " + e.getMessage());
		}
	}
}
