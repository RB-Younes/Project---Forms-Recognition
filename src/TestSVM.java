import java.util.Arrays;

public class TestSVM {

    public TestSVM(int[] trueLabels, int[] predictedLabels, int numClasses) {
        // Initialize confusion matrix
        int[][] confusionMatrix = new int[numClasses][numClasses];

        for (int i = 0; i < trueLabels.length; i++) {
            int trueLabel = trueLabels[i];
            int predictedLabel = predictedLabels[i];
            confusionMatrix[trueLabel][predictedLabel]++;
        }

        // Compute precision, recall, and F1-score for each class
        double[] precision = new double[numClasses];
        double[] recall = new double[numClasses];
        double[] f1Score = new double[numClasses];

        for (int i = 0; i < numClasses; i++) {
            int truePositive = confusionMatrix[i][i];
            int falsePositive = Arrays.stream(confusionMatrix[i]).sum() - truePositive;
            int falseNegative = Arrays.stream(getColumn(confusionMatrix, i)).sum() - truePositive;

            precision[i] = calculatePrecision(truePositive, falsePositive);
            recall[i] = calculateRecall(truePositive, falseNegative);
            f1Score[i] = calculateF1Score(precision[i], recall[i]);
        }

        // Compute macro-averaged precision, recall, and F1-score
        double macroPrecision = Arrays.stream(precision).sum() / numClasses;
        double macroRecall = Arrays.stream(recall).sum() / numClasses;
        double macroF1Score = Arrays.stream(f1Score).sum() / numClasses;

        // Print results
        System.out.println("Precision:");
        System.out.println(Arrays.toString(precision));

        System.out.println("Recall:");
        System.out.println(Arrays.toString(recall));

        System.out.println("F1-Score:");
        System.out.println(Arrays.toString(f1Score));

        System.out.println("Macro Precision: " + macroPrecision);
        System.out.println("Macro Recall: " + macroRecall);
        System.out.println("Macro F1-Score: " + macroF1Score);

        // Print confusion matrix
        System.out.println("Confusion Matrix:");
        printConfusionMatrix(confusionMatrix);
    }

    private static double calculatePrecision(int truePositive, int falsePositive) {
        double precision = (double) truePositive / (truePositive + falsePositive);
        return Double.isNaN(precision) ? 0 : precision;
    }

    private static double calculateRecall(int truePositive, int falseNegative) {
        double recall = (double) truePositive / (truePositive + falseNegative);
        return Double.isNaN(recall) ? 0 : recall;
    }

    private static double calculateF1Score(double precision, double recall) {
        double f1Score = 2 * (precision * recall) / (precision + recall);
        return Double.isNaN(f1Score) ? 0 : f1Score;
    }

    private static int[] getColumn(int[][] matrix, int columnIndex) {
        return Arrays.stream(matrix).mapToInt(row -> row[columnIndex]).toArray();
    }

    private static void printConfusionMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
