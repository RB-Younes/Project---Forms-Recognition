import java.util.Arrays;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

public class Tests {
	
	private Kmeans kmeans;
	private Knn knn;
	
	
//---------------------------------------------------[KNN]-----------------------------------------------------------------
	public Tests(Knn knn, int number_of_classes) {
		super();
		this.knn = knn;
		
        System.out.println("####################################[Precsion,recall,f1-score]####################################");

		for (int i = 1; i <= 9; i++) {
	        System.out.println("------------------------------------Class N°["+i+"]-------------------------------------");
			double precision = Knn_calculatePrecision(this.knn, i);
			double recall = Knn_calculateRecall(this.knn, i);
			double f1_score = Knn_calculateF1Score(precision, recall);
			System.out.println("Precision for class " + i + ": " + precision);
            System.out.println("Recall for class " + i + ": " + recall);
            System.out.println("f1-score for class " + i + ": " + f1_score);
		}
		
        System.out.println("#################################{MACRO-[Precsion,recall,f1-score]}#################################");
        Macro(this.knn, number_of_classes);
        
        System.out.println("########################################{Confusion Matrix}##########################################");

        printConfusionMatrix(calculateConfusionMatrix(this.knn,number_of_classes));
        
	}
	
	//precision for each class individually
	private static double Knn_calculatePrecision(Knn knn, int classLabel) {
    	Map<Read_rep_file, Integer> classification = knn.getClassification();
        int truePositives = 0;
        int falsePositives = 0;
        
		 for (Map.Entry<Read_rep_file, Integer> entry : classification.entrySet()) 
		 {
			 Read_rep_file key = entry.getKey(); //file
			 int trueLabel = key.getNum_class();
			 int predictedLabel = entry.getValue();
			 if (predictedLabel == classLabel) {
				
				 if (trueLabel == classLabel) {
					 truePositives++;
				 } else {
				       falsePositives++;
				 }
			}
		 }
		 System.out.println(truePositives);
        return (double) truePositives / (truePositives + falsePositives);
    }
    
    
    //recall for each class individually
    private static double Knn_calculateRecall(Knn knn, int classLabel) {
    	Map<Read_rep_file, Integer> classification = knn.getClassification();
        int truePositives = 0;
        int falseNegatives = 0;
        
        for (Map.Entry<Read_rep_file, Integer> entry : classification.entrySet()) 
		 {
			 Read_rep_file key = entry.getKey(); //file
			 int trueLabel = key.getNum_class();
			 int predictedLabel = entry.getValue();
			 
            if (trueLabel == classLabel) {
                if (predictedLabel == classLabel) {
                    truePositives++;
                }
            } else {
                if (predictedLabel == classLabel) {
                    falseNegatives++;
                }
            }
        }

        return (double) truePositives / (truePositives + falseNegatives);
    }
    
    //f1 score for each class individually
    private static double Knn_calculateF1Score(double precision, double recall) {
        return 2 * (precision * recall) / (precision + recall);
    }
    
    
    //**************************macro-averaged**********************************
    private static void Macro(Knn knn,int numClasses) {
    	double macroPrecision = 0.0;
        double macroRecall = 0.0;
        double macroF1Score = 0.0;
        
        for (int classLabel = 1; classLabel <= numClasses; classLabel++) {
            double precision = Knn_calculateRecall(knn, classLabel);
            double recall = Knn_calculateRecall(knn, classLabel);
            double f1Score = Knn_calculateF1Score(precision, recall);
            if (!Double.isNaN(precision) && !Double.isNaN(recall) && !Double.isNaN(f1Score)) {
            	 macroPrecision += precision;
                 macroRecall += recall;
                 macroF1Score += f1Score;
			}
            else {
            	numClasses -=1;
            }
        }

        macroPrecision /= numClasses;
        macroRecall /= numClasses;
        macroF1Score /= numClasses;

        System.out.println("Macro-Averaged Precision: " + macroPrecision);
        System.out.println("Macro-Averaged Recall: " + macroRecall);
        System.out.println("Macro-Averaged F1 Score: " + macroF1Score);
    }
	
    
    
    
    // Confusion Matrix
    private static int[][] calculateConfusionMatrix(Knn knn, int numClasses) {
    	Map<Read_rep_file, Integer> classification = knn.getClassification();
        int[][] confusionMatrix = new int[numClasses][numClasses];

        for (Map.Entry<Read_rep_file, Integer> entry : classification.entrySet()) 
		 {
			 Read_rep_file key = entry.getKey(); //file
			 int trueLabel = key.getNum_class();
			 int predictedLabel = entry.getValue();
             confusionMatrix[trueLabel - 1][predictedLabel - 1]++;
        }

        return confusionMatrix;
    }
    
    
    // Print confusion Matrix
    private static void printConfusionMatrix(int[][] confusionMatrix) {
        System.out.println("Confusion Matrix:");
        for (int[] row : confusionMatrix) {
            System.out.println(Arrays.toString(row));
        }
    }
    
    
//---------------------------------------------------[Kmeans]-----------------------------------------------------------------
	public Tests(Kmeans kmeans) {
		super();
		this.kmeans = kmeans;
        System.out.println("#######################################[Silhouette Score]#######################################");
        double Silhouette_Score = silhouetteScore(this.kmeans);
        System.out.println("The Silhouette Score is: "+Silhouette_Score+" .");
        /*System.out.println("####################################[calinskiHarabaszIndex]####################################");

        System.out.println(calinskiHarabaszIndex(kmeans));
        System.out.println("####################################[daviesBouldinIndex]####################################");

        System.out.println(daviesBouldinIndex(kmeans));*/
	}
	
	
	// Silhouette Score
    public static double silhouetteScore(Kmeans kmeans) {
        double totalSilhouette = 0.0;
        int totalPoints = 0;

        for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : kmeans.getClusters().entrySet()) 
		{
        	Vector<Read_rep_file> Points= entry.getValue();
        	
        	
            for (Read_rep_file point : Points) {
            	 if (Points.size() > 1) {
            		 double a = averageDistance(point, Points);
                     double b = minAverageDistance(point, kmeans.getClusters());
                    
                     double silhouette = (b - a) / Math.max(a, b); //calculer silhouette pour un point
                     
                     totalSilhouette += silhouette;
                     totalPoints++;
            	 }
            	 else { // le cas ou un seul point se retrouve dans un cluster donc on aura averageDistance return nan car on se retouve avec un suel point
            		 totalPoints++;
            	 }
                
            }
        }
        return totalSilhouette / totalPoints;
    }
    
    private static double averageDistance(Read_rep_file point, Vector<Read_rep_file> points) {
        double totalDistance = 0.0;
        int count = 0;

        for (Read_rep_file otherPoint : points) {
            if (!point.equals(otherPoint)) {
                totalDistance += calculateEuclideanDistance(point, otherPoint);
               
                count++;
            }
        }
 
        return totalDistance / count;
    }
    
 // Helper method: Minimum average distance of a point to points in other clusters
    private static double minAverageDistance(Read_rep_file point,Map <Vector<Float>, Vector<Read_rep_file>>  clusters) {
        double minDistance = Double.MAX_VALUE;

        for (Entry<Vector<Float>, Vector<Read_rep_file>> cluster : clusters.entrySet()) 
		{
            if (!cluster.getValue().contains(point)) {
                double distance = averageDistance(point, cluster.getValue());
                minDistance = Math.min(minDistance, distance);
            }
        }
        
        return minDistance;
    }
    
    
    private static double calculateEuclideanDistance(Read_rep_file point1,Read_rep_file point2) 
	{
        double sum = 0.0;
        for (int i = 0; i < point1.getVec_car().size(); i++) {
            sum += Math.pow(point1.getVec_car().get(i) - point2.getVec_car().get(i), 2);
        }
        return Math.sqrt(sum);
    }
    
    
    // Calinski-Harabasz Index
    public static double calinskiHarabaszIndex(Kmeans kmeans) {
        double numerator = 0.0;
        double denominator = 0.0;

        for (Entry<Vector<Float>, Vector<Read_rep_file>> cluster : kmeans.getClusters().entrySet()) {
            Vector<Float> centroid = calculateCentroid(cluster.getValue());

            double clusterNumerator = cluster.getValue().size() * euclideanDistanceSquared(centroid, cluster.getKey());
            numerator += clusterNumerator;

            for (Read_rep_file point : cluster.getValue()) {
                denominator += euclideanDistanceSquared(point.getVec_car(), centroid);
            }
        }

        int totalClusters = kmeans.getClusters().size();
        int totalPoints = kmeans.getPoints().size();

        return (numerator / (totalClusters - 1)) / (denominator / (totalPoints - totalClusters));
    }

    
    private static Vector<Float> calculateCentroid(Vector<Read_rep_file> points) {
        int dimensions = points.get(0).getVec_car().size();
        Vector<Float> centroid = new Vector<>(dimensions);

        for (int i = 0; i < dimensions; i++) {
            double sum = 0.0;
            for (Read_rep_file point : points) {
                sum += point.getVec_car().get(i);
            }
            centroid.add((float) (sum / points.size()));
        }

        return centroid;
    }
    
 
    private static double euclideanDistanceSquared(Vector<Float> vector1, Vector<Float> vector2) {
        double sum = 0.0;
        int dimensions = vector1.size();

        for (int i = 0; i < dimensions; i++) {
            sum += Math.pow(vector1.get(i) - vector2.get(i), 2);
        }

        return sum;
    }


    // Davies-Bouldin Index
 // Davies-Bouldin Index without explicitly stored centroids
    public static double daviesBouldinIndex(Kmeans kmeans) {
        double maxDaviesBouldin = Double.MIN_VALUE;

        for (Entry<Vector<Float>, Vector<Read_rep_file>> cluster1 : kmeans.getClusters().entrySet()) {
            double maxDistance = Double.MIN_VALUE;

            for (Entry<Vector<Float>, Vector<Read_rep_file>> cluster2 : kmeans.getClusters().entrySet()) {
                if (!cluster1.getKey().equals(cluster2.getKey())) {
                    double distance = daviesBouldinDistance(cluster1.getValue(), cluster2.getValue(), kmeans);
                    maxDistance = Math.max(maxDistance, distance);
                }
            }

            maxDaviesBouldin += maxDistance;
        }

        int totalClusters = kmeans.getClusters().size();
        return maxDaviesBouldin / totalClusters;
    }

    // Helper method: Davies-Bouldin distance between two clusters
    private static double daviesBouldinDistance(Vector<Read_rep_file> cluster1, Vector<Read_rep_file> cluster2, Kmeans kmeans) {
        Vector<Float> centroid1 = calculateCentroid(cluster1);
        Vector<Float> centroid2 = calculateCentroid(cluster2);

        double distance = euclideanDistance(centroid1, centroid2);

        double spread1 = averageDistanceToCentroid(centroid1, cluster1);
        double spread2 = averageDistanceToCentroid(centroid2, cluster2);

        return distance / (spread1 + spread2);
    }

    // Helper method: Average distance of points to the centroid
    private static double averageDistanceToCentroid(Vector<Float> centroid, Vector<Read_rep_file> points) {
        double totalDistance = 0.0;
        for (Read_rep_file point : points) {
            totalDistance += euclideanDistance(point.getVec_car(), centroid);
        }
        return totalDistance / points.size();
    }
    
 // Helper method: Euclidean distance between two vectors
    private static double euclideanDistance(Vector<Float> vector1, Vector<Float> vector2) {
        double sum = 0.0;
        for (int i = 0; i < vector1.size(); i++) {
            sum += Math.pow(vector1.get(i) - vector2.get(i), 2);
        }
        return Math.sqrt(sum);
    }



    
    
}
