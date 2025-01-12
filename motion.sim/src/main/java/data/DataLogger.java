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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class DataLogger {
	private static final int MAX_GRAPHS = 5;
	private final List<GraphData> savedGraphs;
	private final String fileDirectory;

	public DataLogger(String customDirectory) {
		// Define directory path
		if (customDirectory == null || customDirectory.isBlank()) {
			this.fileDirectory = System.getProperty("user.home") + "/ProjectileSimulator";
		} else {
			this.fileDirectory = customDirectory;
		}

		// Ensure the directory exists
		ensureDirectoryExists();

		// Load saved graphs from the file
		this.savedGraphs = loadGraphsFromFile();
		System.out.println("File path used by DataLogger: " + getFilePath());
	}

	public List<GraphData> getSavedGraphs() {
		return savedGraphs;
	}

	public void saveGraph(GraphData graphData) {
		// Remove oldest graph if the maximum capacity is reached
		if (savedGraphs.size() == MAX_GRAPHS) {
			savedGraphs.remove(0);
		}

		// Add new graph and save
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
				System.out.println("Failed to create directory at " + fileDirectory);
			}
		}
	}

	private String getFilePath() {
		return fileDirectory + "/graphs.json"; // Correctly point to the JSON file
	}

	private List<GraphData> loadGraphsFromFile() {
		File file = new File(getFilePath());
		if (!file.exists()) {
			System.out.println("No existing file found. Creating a new graph store.");
			return new ArrayList<>();
		}

		try (FileReader reader = new FileReader(file)) {
			Type listType = new TypeToken<ArrayList<GraphData>>() {}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException | JsonSyntaxException e) {
			System.err.println("Error reading file. Backing up corrupted file.");
			backupFile(getFilePath());
			return new ArrayList<>();
		}
	}

	private void backupFile(String filePath) {
		File backup = new File(filePath + ".bak");
		try {
			Files.copy(Path.of(filePath), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.err.println("Backup created: " + backup.getAbsolutePath());
		} catch (IOException ex) {
			System.err.println("Failed to create backup: " + ex.getMessage());
		}
	}

	private void saveGraphsToFile() {
		File file = new File(getFilePath());
		try (FileWriter writer = new FileWriter(file)) {
			new Gson().toJson(savedGraphs, writer);
			System.out.println("Graphs successfully saved to file: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Failed to save graphs to file: " + e.getMessage());
		}
	}
}
