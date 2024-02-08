import java.util.Arrays;
import java.util.Vector;
import java.util.stream.IntStream;

public class SVM {

	 	private double[][] weights;  // Weight vectors for each class
	    private double[] bias;       // Bias terms for each class
	    private double learningRate; // Learning rate
		private Vector <Read_rep_file> Train;

	    public SVM(int numClasses, int numFeatures, double learningRate,Sets data) {
	        this.weights = new double[numClasses][numFeatures];
	        this.bias = new double[numClasses];
	        this.learningRate = learningRate;
	        this.Train = data.getTrain();
	    }
	    
	    public void train(Sets data, int numEpochs) {
	    	
	    	// Reshape the vector into a 2D array TRAIN DATA
	    				double[][] X = new double[Train.size()][Train.get(0).getVec_car().size()];
	    				int[] y = new int[Train.size()];
	    				for (int i = 0; i < Train.size(); i++) {
	    		            for (int j = 0; j < Train.get(i).getVec_car().size(); j++) {
	    		            	Vector<Float> vector = Train.get(i).getVec_car();
	    		            	X[i][j] = vector.get(j);
	    		            	y[i] = Train.get(i).getNum_class()-1;// our cassses are labeled from 1 to 9 -> 0 to 8 to match
	    		            }
	    		        }	
	    				
	        for (int epoch = 0; epoch < numEpochs; epoch++) {
	            for (int i = 0; i < X.length; i++) {
	            	
	                int trueLabel = y[i];
	                // Calculate scores for each class
	                double[] scores = new double[weights.length];
	                
	                for (int c = 0; c < weights.length; c++) {
	                    scores[c] = dotProduct(weights[c], X[i]) + bias[c];
	                }

	                // Update weights using the one-vs-all strategy
	                for (int c = 0; c < weights.length; c++) {
	                    double margin = scores[c] - scores[trueLabel] + 1; // hinge loss
	                    if (c == trueLabel) {
	                        continue; // skip the true class
	                    }
	                    if (margin > 0) {
	                        // Update weights for incorrect classes
	                        double[] gradient = scalarMultiply(X[i], learningRate);
	                        weights[c] = subtractVectors(weights[c], gradient);
	                        bias[c] -= learningRate;
	                    }
	                }
	            }
	        }
	    }

	    public int predict(double[] x) {
	        int bestClass = -1;
	        double bestScore = Double.NEGATIVE_INFINITY;

	        for (int c = 0; c < weights.length; c++) {
	            double score = dotProduct(weights[c], x) + bias[c];
	            if (score > bestScore) {
	                bestScore = score;
	                bestClass = c;
	            }
	        }

	        return bestClass;
	    }

	    private double dotProduct(double[] a, double[] b) {
	        double result = 0.0;
	        for (int i = 0; i < a.length; i++) {
	            result += a[i] * b[i];
	        }
	        return result;
	    }

	    private double[] scalarMultiply(double[] vector, double scalar) {
	        return Arrays.stream(vector).map(v -> v * scalar).toArray();
	    }

	    private double[] subtractVectors(double[] a, double[] b) {
	        return IntStream.range(0, a.length).mapToDouble(i -> a[i] - b[i]).toArray();
	    }


}
