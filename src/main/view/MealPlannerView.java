package main.view;

import entity.Recipe;
import usecase.MealPlannerUsecase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MealPlannerView {
    private final String title = "Meal Planner";
    JFrame frame = new JFrame(title);

    // Inputs
    JTextField maxCalories = new JTextField(8);
    JTextField minCalories = new JTextField(8);
    JTextField minProtein  = new JTextField(8);
    JTextField maxCarbs    = new JTextField(8);
    JTextField maxFat      = new JTextField(8);

    JButton saveGoalsButton = new JButton("Save Goals");
    JButton calculateButton = new JButton("Calculate");

    JLabel resultsCalories = new JLabel("");
    JLabel resultsProtein  = new JLabel("");
    JLabel resultsCarbs    = new JLabel("");
    JLabel resultsFat      = new JLabel("");

    JLabel savedGoalsSummary = new JLabel("Saved Goals: (none)");

    JPanel resultsPanel = new JPanel();
    MacroChartPanel chartPanel = new MacroChartPanel();

    private final MealPlannerUsecase mealPlannerUsecase;

    public MealPlannerView(MealPlannerUsecase mpUsecase) {
        mealPlannerUsecase = mpUsecase;
        createView();
    }

    private void createView() {
        frame = new JFrame("Meal Planenr");
        frame.setSize(1100, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(createTitleBar());
        topPanel.add(createMacrosPanel());
        frame.add(topPanel, BorderLayout.NORTH);

        frame.add(createMealsListBox(), BorderLayout.CENTER);
        frame.add(createBottomPanel(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMealsListBox() {
        JPanel panel = new JPanel();

        JPanel mealsPanel = new JPanel();
        mealsPanel.setLayout(new BoxLayout(mealsPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Selected Meals");
        mealsPanel.add(titleLabel);

        for (int i = 0; i < mealPlannerUsecase.getMeals().size(); i++) {
            JPanel mealPanel = new JPanel();
            JLabel mealLabel = new JLabel(mealPlannerUsecase.getMeals().get(i).getName());
            JButton removeMealButton = new JButton("Remove Meal");
            int finalI = i;
            removeMealButton.addActionListener(e -> {
                mealPlannerUsecase.removeFromPlanner(mealPlannerUsecase.getMeals().get(finalI));
                mealsPanel.remove(mealPanel);
                mealsPanel.revalidate();
                mealsPanel.repaint();
            });
            mealPanel.add(mealLabel);
            mealPanel.add(removeMealButton);
            mealsPanel.add(mealPanel);
        }

        panel.add(mealsPanel);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel totals = new JPanel();
        totals.setLayout(new BoxLayout(totals, BoxLayout.Y_AXIS));
        totals.add(new JLabel("Macros"));
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.add(resultsCalories);
        resultsPanel.add(resultsProtein);
        resultsPanel.add(resultsCarbs);
        resultsPanel.add(resultsFat);
        totals.add(resultsPanel);

        panel.add(totals, BorderLayout.WEST);

        chartPanel.setPreferredSize(new Dimension(700, 200));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputBox(String text, JComponent inputComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(inputComponent);
        return panel;
    }

    private JPanel createTitleBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            frame.dispose();
            new HomePageView(mealPlannerUsecase);
        });
        JLabel label = new JLabel(this.title);
        panel.add(homeButton);
        panel.add(label);
        return panel;
    }

    private JPanel createMacrosPanel() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel inputRow = new JPanel();
        inputRow.add(createInputBox("Min Calories", minCalories));
        inputRow.add(createInputBox("Max Calories", maxCalories));
        inputRow.add(createInputBox("Max Carbs",    maxCarbs));
        inputRow.add(createInputBox("Max Fat",      maxFat));
        inputRow.add(createInputBox("Min Protein",  minProtein));

        saveGoalsButton.addActionListener(e -> {
            Integer minCal = parseOrNull(minCalories.getText());
            Integer maxCal = parseOrNull(maxCalories.getText());
            Integer mCarbs = parseOrNull(maxCarbs.getText());
            Integer mFat   = parseOrNull(maxFat.getText());
            Integer minPro = parseOrNull(minProtein.getText());

            mealPlannerUsecase.saveGoals(minCal, maxCal, mCarbs, mFat, minPro);
            savedGoalsSummary.setText(buildGoalsSummary());
            JOptionPane.showMessageDialog(frame, "Goals saved.", "Meal Planner", JOptionPane.INFORMATION_MESSAGE);
        });

        // >>> Changed: use nullable parse; hide labels when field blank <<<
        calculateButton.addActionListener(e -> {
            Integer minCal = parseOrNull(minCalories.getText());
            Integer maxCal = parseOrNull(maxCalories.getText());
            Integer mCarbs = parseOrNull(maxCarbs.getText());
            Integer mFat   = parseOrNull(maxFat.getText());
            Integer minPro = parseOrNull(minProtein.getText());

            resultsCalories.setText(""); // clear all
            resultsCarbs.setText("");
            resultsFat.setText("");
            resultsProtein.setText("");

            String calMsg = mealPlannerUsecase.evaluateCalories(minCal, maxCal);
            String carbMsg = mealPlannerUsecase.evaluateCarbs(mCarbs);
            String fatMsg  = mealPlannerUsecase.evaluateFat(mFat);
            String proMsg  = mealPlannerUsecase.evaluateProtein(minPro);

            if (calMsg != null) resultsCalories.setText(calMsg);
            if (carbMsg != null) resultsCarbs.setText(carbMsg);
            if (fatMsg  != null) resultsFat.setText(fatMsg);
            if (proMsg  != null) resultsProtein.setText(proMsg);

            resultsPanel.revalidate();
            resultsPanel.repaint();

            // Chart still compares SAVED goals vs totals
            chartPanel.updateData(
                    mealPlannerUsecase.getTotalCalories(),
                    mealPlannerUsecase.getTotalProtein(),
                    mealPlannerUsecase.getTotalCarbs(),
                    mealPlannerUsecase.getTotalFat(),
                    mealPlannerUsecase.getMinCaloriesGoal(),
                    mealPlannerUsecase.getMaxCaloriesGoal(),
                    mealPlannerUsecase.getMinProteinGoal(),
                    mealPlannerUsecase.getMaxCarbsGoal(),
                    mealPlannerUsecase.getMaxFatGoal()
            );
        });

        inputRow.add(saveGoalsButton);
        inputRow.add(calculateButton);

        root.add(new JLabel("Daily Macro Goals", SwingConstants.CENTER), BorderLayout.NORTH);
        root.add(inputRow, BorderLayout.CENTER);

        JPanel savedPanel = new JPanel();
        savedPanel.setLayout(new BoxLayout(savedPanel, BoxLayout.Y_AXIS));
        savedPanel.add(new JLabel(" "));
        savedPanel.add(savedGoalsSummary);
        root.add(savedPanel, BorderLayout.EAST);

        return root;
    }

    private String buildGoalsSummary() {
        Integer minCal = mealPlannerUsecase.getMinCaloriesGoal();
        Integer maxCal = mealPlannerUsecase.getMaxCaloriesGoal();
        Integer mCarbs = mealPlannerUsecase.getMaxCarbsGoal();
        Integer mFat   = mealPlannerUsecase.getMaxFatGoal();
        Integer minPro = mealPlannerUsecase.getMinProteinGoal();

        return String.format(
                "<html><b>Saved Goals</b><br/>Calories: %s – %s<br/>Max Carbs: %s g<br/>Max Fat: %s g<br/>Min Protein: %s g</html>",
                (minCal == null ? "-" : minCal),
                (maxCal == null ? "-" : maxCal),
                (mCarbs == null ? "-" : mCarbs),
                (mFat   == null ? "-" : mFat),
                (minPro == null ? "-" : minPro)
        );
    }

    private Integer parseOrNull(String s) {
        try { return (s == null || s.isBlank()) ? null : Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    // (MacroChartPanel stays the same as in the previous version)
    static class MacroChartPanel extends JPanel {
        private float actualCalories, actualProtein, actualCarbs, actualFat;
        private Integer minCal, maxCal, minPro, maxCarbs, maxFat;

        void updateData(float aCal, float aPro, float aCarb, float aFat,
                        Integer minCal, Integer maxCal, Integer minPro, Integer maxCarbs, Integer maxFat) {
            this.actualCalories = aCal;
            this.actualProtein  = aPro;
            this.actualCarbs    = aCarb;
            this.actualFat      = aFat;
            this.minCal = minCal; this.maxCal = maxCal; this.minPro = minPro; this.maxCarbs = maxCarbs; this.maxFat = maxFat;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int left = 80, right = 30, top = 20, bottom = 40;
            int chartW = w - left - right, chartH = h - top - bottom;

            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(left, h - bottom, left + chartW, h - bottom);
            g2.drawLine(left, top, left, h - bottom);

            String[] labels = {"Calories", "Protein(g)", "Carbs(g)", "Fat(g)"};
            float[] actual  = {actualCalories, actualProtein, actualCarbs, actualFat};
            float[] goals   = {
                    (maxCal != null ? maxCal : (minCal != null ? minCal : 0)),
                    (minPro != null ? minPro : 0),
                    (maxCarbs != null ? maxCarbs : 0),
                    (maxFat != null ? maxFat : 0)
            };

            float maxVal = 0f;
            for (int i = 0; i < labels.length; i++) {
                maxVal = Math.max(maxVal, Math.max(actual[i], goals[i]));
            }
            if (minCal != null && maxCal != null) maxVal = Math.max(maxVal, maxCal);
            if (maxVal <= 0) maxVal = 1f;

            int groupW = chartW / labels.length;
            int barWidth = (int) (groupW * 0.28);

            for (int i = 0; i < labels.length; i++) {
                int xCenter = left + i * groupW + groupW / 2;

                if (i == 0 && minCal != null && maxCal != null) {
                    int yMax = (int) (h - bottom - (maxCal / maxVal) * chartH);
                    int yMin = (int) (h - bottom - (minCal / maxVal) * chartH);
                    g2.setColor(new Color(0, 128, 0, 60));
                    g2.fillRect(xCenter - (int)(barWidth*1.5), yMax, (int)(barWidth*3.0), Math.max(4, yMin - yMax));
                }

                int goalH = (int) ((goals[i] / maxVal) * chartH);
                int gx = xCenter - barWidth - 3;
                int gy = h - bottom - goalH;
                g2.setColor(new Color(180, 180, 180));
                if (goals[i] > 0) g2.fillRect(gx, gy, barWidth, Math.max(3, goalH));

                int actH = (int) ((actual[i] / maxVal) * chartH);
                int ax = xCenter + 3;
                int ay = h - bottom - actH;
                boolean within =
                        (i == 0) ? ((minCal == null || actual[i] >= minCal) && (maxCal == null || actual[i] <= maxCal))
                                : (i == 1) ? ((minPro == null) || actual[i] >= minPro)
                                : (i == 2) ? ((maxCarbs == null) || actual[i] <= maxCarbs)
                                : ((maxFat == null) || actual[i] <= maxFat);
                g2.setColor(within ? new Color(90, 130, 255) : new Color(220, 80, 80));
                g2.fillRect(ax, ay, barWidth, Math.max(3, actH));

                g2.setColor(Color.DARK_GRAY);
                String lbl = labels[i];
                int strW = g2.getFontMetrics().stringWidth(lbl);
                g2.drawString(lbl, xCenter - strW / 2, h - bottom + 18);
            }
        }
    }
}
