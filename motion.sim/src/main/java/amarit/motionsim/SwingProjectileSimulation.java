package amarit.motionsim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SwingProjectileSimulation extends JFrame {
	private JPanel visualiserPanel;
	private JSlider velocitySlider, angleSlider;
	private JTextField velocityTextField, angleTextField, customGravityField;
	private JComboBox<String> gravityDropdown;
	private JButton launchButton, playButton, pauseButton, resetButton, toggleGridButton, toggleAxesButton, changeLineStyleButton;
	private JLabel heightLabel, velocityLabel, timeLabel;
	private Timer animationTimer;
	private boolean isRunning = false;
	private PhysicsEngine physicsEngine;
	private SwingVisualiser visualiser;

	public SwingProjectileSimulation() {
		setTitle("2D Projectile Motion Simulation");
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		createComponents();
		visualiser = new SwingVisualiser(850, 500);
		visualiserPanel = visualiser.createVisualiserPanel();
		add(visualiserPanel, BorderLayout.CENTER); // Add the panel FIRST
		visualiserPanel.repaint(); // Force initial repaint
		layoutComponents();
		addListeners();

		setVisible(true);
	}

	private void createComponents() {
		velocitySlider = createSlider(0, 100, 0);
		angleSlider = createSlider(0, 90, 45);
		velocityTextField = new JTextField("0", 5);
		angleTextField = new JTextField("45", 5);

		gravityDropdown = new JComboBox<>(new String[]{"Earth", "Mars", "Moon", "Custom"});
		customGravityField = new JTextField(5);
		customGravityField.setVisible(false);

		launchButton = new JButton("Launch");
		playButton = new JButton("Play");
		pauseButton = new JButton("Pause");
		resetButton = new JButton("Reset");
		toggleGridButton = new JButton("Toggle Grid");
		toggleAxesButton = new JButton("Toggle Axes");
		changeLineStyleButton = new JButton("Change Trajectory Line Style");

		heightLabel = new JLabel("Height: 0.0 m");
		velocityLabel = new JLabel("Velocity: 0.0 m/s");
		timeLabel = new JLabel("Time: 0.0 s");
	}

	private void layoutComponents() {
		JPanel inputPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		addLabelAndComponent(inputPanel, "Initial Velocity:", velocitySlider, velocityTextField, gbc, 0);
		addLabelAndComponent(inputPanel, "Launch Angle:", angleSlider, angleTextField, gbc, 1);
		addLabelAndComponent(inputPanel, "Gravity (Planet):", gravityDropdown, customGravityField, gbc, 2);

		JPanel controlPanel = new JPanel(new FlowLayout());
		controlPanel.add(launchButton);
		controlPanel.add(playButton);
		controlPanel.add(pauseButton);
		controlPanel.add(resetButton);

		JPanel togglePanel = new JPanel(new GridLayout(3, 1, 5, 5));
		togglePanel.add(toggleGridButton);
		togglePanel.add(toggleAxesButton);
		togglePanel.add(changeLineStyleButton);

		JPanel statsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		statsPanel.add(heightLabel);
		statsPanel.add(velocityLabel);
		statsPanel.add(timeLabel);
		statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(inputPanel, BorderLayout.NORTH);
		leftPanel.add(togglePanel, BorderLayout.CENTER);
		leftPanel.add(statsPanel, BorderLayout.SOUTH);

		add(leftPanel, BorderLayout.WEST);
		add(visualiserPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
	}

	private void addListeners() {
		velocitySlider.addChangeListener(e -> velocityTextField.setText(String.valueOf(velocitySlider.getValue())));
		angleSlider.addChangeListener(e -> angleTextField.setText(String.valueOf(angleSlider.getValue())));

		velocityTextField.addActionListener(e -> updateSliderFromTextField(velocityTextField, velocitySlider, 0, 100));
		angleTextField.addActionListener(e -> updateSliderFromTextField(angleTextField, angleSlider, 0, 90));

		gravityDropdown.addActionListener(e -> {
			boolean isCustom = gravityDropdown.getSelectedItem().equals("Custom");
			customGravityField.setVisible(isCustom);
			if (!isCustom) customGravityField.setText("");
		});

		launchButton.addActionListener(e -> launchSimulation());		playButton.addActionListener(e -> startSimulation());
		pauseButton.addActionListener(e -> pauseSimulation());
		resetButton.addActionListener(e -> resetSimulation());

		toggleGridButton.addActionListener(e -> visualiser.toggleGrid());
		toggleAxesButton.addActionListener(e -> visualiser.toggleAxes());
		changeLineStyleButton.addActionListener(e -> visualiser.toggleLineStyle());
	}

	private JSlider createSlider(int min, int max, int value) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
		slider.setMajorTickSpacing((max - min) / 5);
		slider.setMinorTickSpacing((max - min) / 25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		return slider;
	}

	private void addLabelAndComponent(JPanel panel, String labelText, JComponent comp1, JComponent comp2, GridBagConstraints gbc, int row) {
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		panel.add(comp1, gbc);

		gbc.gridx = 2;
		panel.add(comp2, gbc);
	}

	private void updateSliderFromTextField(JTextField textField, JSlider slider, int min, int max) {
		try {
			int value = Integer.parseInt(textField.getText());
			if (value >= min && value <= max) {
				slider.setValue(value);
			} else {
				textField.setText(String.valueOf(slider.getValue()));
			}
		} catch (NumberFormatException ex) {
			textField.setText(String.valueOf(slider.getValue()));
		}
	}

	private double getGravityValue() {
		String selected = (String) gravityDropdown.getSelectedItem();
		switch (selected) {
			case "Earth": return 9.8;
			case "Moon": return 1.6;
			case "Mars": return 3.7;
			case "Custom":
				try {
					return Double.parseDouble(customGravityField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Invalid gravity value. Using Earth's gravity (9.8 m/s²).");
					return 9.8;
				}
			default: return 9.8;
		}
	}

	private void updateStatistics(double height, double velocity, double time) {
		heightLabel.setText(String.format("Height: %.2f m", height));
		velocityLabel.setText(String.format("Velocity: %.2f m/s", velocity));
		timeLabel.setText(String.format("Time: %.2f s", time));
	}

	private void launchSimulation() {
		System.out.println("launchSimulation() called");

		double velocity = velocitySlider.getValue();
		double angle = angleSlider.getValue();
		double gravity = getGravityValue();

		physicsEngine = new PhysicsEngine(velocity, angle, gravity, 60);
		System.out.println("physicsEngine created: " + physicsEngine);

		double maxRange = physicsEngine.calculateRange();
		double maxHeight = physicsEngine.calculateMaxHeight();

		visualiser.setMaxBounds(maxRange, maxHeight); // Set bounds *before* starting
		visualiser.reset();

		if (!isRunning) { // Start timer only if not already running
			isRunning = true;
			startSimulation();
		}
	}

	private void startSimulation() {
			if (physicsEngine != null && !isRunning) {
				System.out.println("physicsEngine is not null, starting timer");// Check for null physicsEngine
			isRunning = true;
			animationTimer = new Timer(16, e -> updateSimulation());
			animationTimer.start();
			} else if (physicsEngine == null){
				System.out.println("physicsEngine is null");
			} else {
				System.out.println("Simulation already running");
			}
	}

	private void pauseSimulation() {
		if (isRunning) {
			isRunning = false;
			if (animationTimer != null) {
				animationTimer.stop();
			}
		}
	}
	private void resetSimulation() {
		isRunning = false;
		if (animationTimer != null) {
			animationTimer.stop();
		}
		velocitySlider.setValue(0);
		angleSlider.setValue(45);
		gravityDropdown.setSelectedItem("Earth");
		customGravityField.setText("");
		visualiser.reset();
		updateStatistics(0, 0, 0);
	}

	private void updateSimulation() {
		System.out.println("updateSimulation() called");
		if (physicsEngine != null) {
			System.out.println("physicsEngine in updateSimulation(): " + physicsEngine);
			physicsEngine.updateTime();
			double x = physicsEngine.calculateX();
			double y = physicsEngine.calculateY();
			System.out.println("x: " + x + ", y: " + y);

			if (physicsEngine.hasProjectileHitGround() || y < 0) {
				pauseSimulation();
			} else {
				SwingUtilities.invokeLater(() -> {
					visualiser.drawPoint(x, y);
					updateStatistics(y, Math.sqrt(Math.pow(physicsEngine.calculateVX(), 2) + Math.pow(physicsEngine.calculateVY(), 2)), physicsEngine.getTime());
				});
			}
		} else {
			System.out.println("Physics engine is NULL in update simulation");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(SwingProjectileSimulation::new);
	}
}
class PhysicsEngine {
	private final double velocity;
	private final double angle;
	private final double gravity;
	private final double deltaTime;
	private double currentTime;

	public PhysicsEngine(double velocity, double angle, double gravity, int fps) {
		this.velocity = velocity;
		this.angle = Math.toRadians(angle);
		this.gravity = gravity;
		this.deltaTime = 1.0 / fps;
		this.currentTime = 0;
	}

	public double calculateX() {
		return velocity * Math.cos(angle) * currentTime;
	}

	public double calculateY() {
		return (velocity * Math.sin(angle) * currentTime) - (0.5 * gravity * Math.pow(currentTime, 2));
	}

	public double calculateVX() {
		return velocity * Math.cos(angle);
	}

	public double calculateVY() {
		return (velocity * Math.sin(angle)) - (gravity * currentTime);
	}

	public double calculateMaxHeight() {
		return Math.pow(velocity * Math.sin(angle), 2) / (2 * gravity);
	}

	public double calculateRange() {
		return (Math.pow(velocity, 2) * Math.sin(2 * angle)) / gravity;
	}

	public boolean hasProjectileHitGround() {
		return calculateY() <= 0;
	}

	public void updateTime() {
		currentTime += deltaTime;
	}

	public double getTime() {
		return currentTime;
	}

	public void reset() {
		currentTime = 0;
	}
}
 class SwingVisualiser extends JPanel {
	private final int width;
	private final int height;
	private boolean showGrid;
	private boolean showAxes;
	private String lineStyle;
	private double maxX;
	private double maxY;
	private List<Point> points = new ArrayList<>();

	public SwingVisualiser(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		this.showGrid = true;
		this.showAxes = true;
		this.lineStyle = "Solid";
		this.maxX = 100;
		this.maxY = 100;
		setBackground(Color.WHITE); // Crucial: Set the background color
	}

	public JPanel createVisualiserPanel() {
		return this;
	}

	 public void drawPoint(double x, double y) {
		 int screenX = (int) (x / maxX * width);
		 int screenY = height - (int) (y / maxY * height); // Corrected: Invert y-axis
		 System.out.println("drawPoint() called - x: " + x + ", y: " + y + ", screenX: " + screenX + ", screenY: " + screenY);
		 SwingUtilities.invokeLater(() -> {
			 points.add(new Point(screenX, screenY));
			 repaint();
		 });
	 }

	 public void reset() {
		 SwingUtilities.invokeLater(() -> { // Use invokeLater
			 maxX = 100;
			 maxY = 100;
			 points.clear();
			 repaint();
		 });
	 }

	 public void setMaxBounds(double maxX, double maxY) {
		 System.out.println("setMaxBounds() called with maxX: " + maxX + ", maxY: " + maxY); // ***DEBUG***
		 double newMaxX = Math.max(this.maxX, maxX * 1.1); // Add a 10% buffer
		 double newMaxY = Math.max(this.maxY, maxY * 1.1); // Add a 10% buffer

		 if (newMaxX != this.maxX || newMaxY != this.maxY) {
			 this.maxX = newMaxX;
			 this.maxY = newMaxY;
			 System.out.println("New max bounds set - maxX: " + this.maxX + ", maxY: " + this.maxY); // ***DEBUG***
			 repaint();
		 }
	 }

	 @Override
	 protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 Graphics2D g2d = (Graphics2D) g;

		 if (showGrid) drawGrid(g2d);
		 if (showAxes) drawAxes(g2d);

		 if (points.size() > 1) { // Only draw if there are at least two points
			 g2d.setColor(Color.RED);
			 if (lineStyle.equals("Dashed")) {
				 g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0));
				 for (int i = 0; i < points.size() - 1; i++) {
					 Point p1 = points.get(i);
					 Point p2 = points.get(i + 1);
					 g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
				 }
			 } else {
				 g2d.setStroke(new BasicStroke(1));
				 for (Point p : points) {
					 g2d.fillOval(p.x - 2, p.y - 2, 4, 4);
				 }
			 }
		 } else if(points.size() == 1) {
			 g2d.setColor(Color.RED);
			 g2d.setStroke(new BasicStroke(1));
			 Point p = points.get(0);
			 g2d.fillOval(p.x - 2, p.y - 2, 4, 4);
		 }
	 }


	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i <= 10; i++) {
			int x = i * width / 10;
			int y = i * height / 10;
			g2d.drawLine(x, 0, x, height);
			g2d.drawLine(0, y, width, y);
		}
	}

	private void drawAxes(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, height - 1, width, height - 1);
		g2d.drawLine(1, 0, 1, height);

		g2d.setFont(new Font("Arial", Font.PLAIN, 10));
		for (int i = 0; i <= 10; i++) {
			int x = i * width / 10;
			int y = height - i * height / 10;
			g2d.drawString(String.format("%.1f", i * maxX / 10), x, height - 5);
			g2d.drawString(String.format("%.1f", i * maxY / 10), 5, y);
		}
	}

	public void toggleGrid() {
		showGrid = !showGrid;
		repaint();
	}

	public void toggleAxes() {
		showAxes = !showAxes;
		repaint();
	}

	public void toggleLineStyle() {
		lineStyle = lineStyle.equals("Solid") ? "Dashed" : "Solid";
		repaint(); // Important: Repaint after changing line style
	}
}
