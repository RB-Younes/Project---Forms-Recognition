import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class Kmeans {
	private int k; 
	private Vector<Vector<Float>> centroids; // each centroid is a vector
    private Vector<Read_rep_file> points;
	private Map<Vector<Float>, Vector<Read_rep_file>> Clusters;
	private int Dtype;
	
	
	public Kmeans(int k, Vector<Read_rep_file> points, int it_number,int Dtype) {
		super();
		this.k = k;
		this.Dtype = Dtype;
		this.points = points;
		
		initializeClusters_Rand(k);
		Boolean On = true;
		Vector<Boolean> B =  new Vector<Boolean>(); 
		int i =0;
		while (On) 
		{
	        assignPointsToClusters(Dtype);
	        
	        Map<Vector<Float>, Vector<Read_rep_file>> new_Clusters = updateClusterCentroids(); //new clusters (new centroids)
	        // we get the old centroids
	        for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : this.Clusters.entrySet()) 
			{
	        	Boolean equal = false;
	        	for (Entry<Vector<Float>, Vector<Read_rep_file>> entry1 : new_Clusters.entrySet()) 
				{
	        		if ( entry.getKey().equals(entry1.getKey())) {
	        			equal = true;
					}
				}
	        	
	        	B.add(equal);
			}
	        // compare
	        Boolean alltrue = true;
	        for (Boolean b : B) {
	        	 if (b == false) {
	        		 alltrue =false;
	 			}
			}
	        
	        i++;
	        On = !alltrue;
	        // aff
	        
	        if (i==it_number) {
				break;
			}
	        this.Clusters = new_Clusters;
	    }
		
		/*System.out.println("Number of iterations: " + i);
        for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : this.Clusters.entrySet()) 
        {
        	System.out.println("-------------"+entry.getKey()+"-------------------");
        	Vector<Read_rep_file> cluster =  entry.getValue();
        	for (Read_rep_file point : cluster) {
				System.out.println(point.getNum_class());
			}
        }*/
	
		
	}
	
	
	//shuffle to select random points as cetroids than creat clusters
	 private void initializeClusters_Rand(int K) {
		 		 
		 Collections.shuffle(this.points);
		 
		 
		 this.centroids =  new Vector<Vector<Float>>();
	        for (int i = 0; i < K; i++) {
	        	this.centroids.add(this.points.get(i).getVec_car());
	        }
	        
	        //put each centroid as cluster in the map
			this.Clusters = new HashMap<Vector<Float>, Vector<Read_rep_file>>();
			
			for (Vector<Float> centroid : this.centroids) {
				this.Clusters.put(centroid, new Vector<Read_rep_file>());
				// notice that the vector here is "empty"
		    }
	  }
	 
	/*
	    private void initializeClusters_KMeansPlusPlus(int K) {
	        this.centroids = new Vector<Vector<Float>>();
	        Random random = new Random();

	        // Choose the first centroid randomly from the data points
	        int firstCentroidIndex = random.nextInt(points.size());
	        centroids.add(points.get(firstCentroidIndex).getVec_car());

	        // Choose the remaining centroids using the k-means++ method
	        for (int i = 1; i < K; i++) {
	            List<Double> distancesSquared = new ArrayList<>();
	            Vector<Float> newCentroid = null;

	            // Calculate squared distances for each data point to the nearest existing centroid
	            for (Read_rep_file point : points) {
	                double minDistanceSquared = Double.MAX_VALUE;
	                for (Vector<Float> centroid : centroids) {
	                    double distanceSquared = calculateDistanceSquared(centroid, point.getVec_car());
	                    minDistanceSquared = Math.min(minDistanceSquared, distanceSquared);
	                }
	                distancesSquared.add(minDistanceSquared);
	            }

	            // Calculate probabilities based on squared distances
	            double sumDistancesSquared = distancesSquared.stream().mapToDouble(Double::doubleValue).sum();
	            double cumulativeProbability = 0.0;
	            double randomValue = random.nextDouble() * sumDistancesSquared;

	            // Choose the next centroid
	            for (int j = 0; j < points.size(); j++) {
	                cumulativeProbability += distancesSquared.get(j);
	                if (cumulativeProbability >= randomValue) {
	                    newCentroid = points.get(j).getVec_car();
	                    break;
	                }
	            }

	            centroids.add(newCentroid);
	        }

	        // Put each centroid as a cluster in the map
	        this.Clusters = new HashMap<Vector<Float>, Vector<Read_rep_file>>();
	        for (Vector<Float> centroid : centroids) {
	            this.Clusters.put(centroid, new Vector<Read_rep_file>());
	        }
	    }
	    
	    private double calculateDistanceSquared(Vector<Float> vector1, Vector<Float> vector2) {
	        double sum = 0.0;
	        for (int i = 0; i < vector1.size(); i++) {
	            sum += Math.pow(vector1.get(i) - vector2.get(i), 2);
	        }
	        return sum;
	    }*/

	 
	 
	 
	 // assign all points to the nearest cluster
	 private void assignPointsToClusters(int Dtype) {
		 	// clear the existing points in each cluster
		    for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : this.Clusters.entrySet()) {
		           entry.setValue(new Vector<Read_rep_file>()); // set empty_vectros

		    }
		    
		    // Assign Each Data Point to a cluster
		    for (Read_rep_file point : this.points) {
		    	Vector<Float> nearestCluster = findNearestCluster(point,Dtype);
		        //add the point to the cluster
		    	 Vector<Read_rep_file> pointsofCluster = Clusters.get(nearestCluster);
		    	 pointsofCluster.add(point);
		    	 Clusters.put(nearestCluster, pointsofCluster);
		    }
	 }
	 
	 // find the nearest cluster to a point
	 private Vector<Float> findNearestCluster(Read_rep_file point,int Dtype) {
		 
		Vector<Float> nearestCluster = null;
		double minDistance = Double.MAX_VALUE;
		 
		for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : this.Clusters.entrySet()) 
		{
		 	Vector<Float> key = entry.getKey(); 
		 	 double distance = calculateDistance(key, point,Dtype);
		 	 
		 	 if (distance < minDistance) {
		            minDistance = distance;
		            nearestCluster = key;
		        }
		}

		return nearestCluster; 
	 }
	 
	 public int getK() {
		return k;
	}


	public void setK(int k) {
		this.k = k;
	}


	public Vector<Vector<Float>> getCentroids() {
		return centroids;
	}


	public void setCentroids(Vector<Vector<Float>> centroids) {
		this.centroids = centroids;
	}


	public Vector<Read_rep_file> getPoints() {
		return points;
	}


	public void setPoints(Vector<Read_rep_file> points) {
		this.points = points;
	}


	public Map<Vector<Float>, Vector<Read_rep_file>> getClusters() {
		return Clusters;
	}


	public void setClusters(Map<Vector<Float>, Vector<Read_rep_file>> clusters) {
		Clusters = clusters;
	}


	//calculate eucledian distance between points
	 private double calculateDistance(Vector<Float> key,Read_rep_file point2,int type) 
	 {
	        double sum = 0.0;
	        if (type == 0) {
	        	for (int i = 0; i < key.size(); i++) {
		            sum += Math.pow(key.get(i) - point2.getVec_car().get(i), 2);
		        }
		        return Math.sqrt(sum);
			}else {
				for (int i = 0; i < key.size(); i++) {
		            sum += Math.sqrt(Math.pow(key.get(i) - point2.getVec_car().get(i), 2));
		        }
		        return sum;
			}
	        
	  }
	
	 
	 //update the cluster's centroids
	 private Map<Vector<Float>, Vector<Read_rep_file>> updateClusterCentroids() {

		 Map<Vector<Float>, Vector<Read_rep_file>> new_Cluster = new HashMap<Vector<Float>, Vector<Read_rep_file>>();
		
		 for (Entry<Vector<Float>, Vector<Read_rep_file>> entry : this.Clusters.entrySet()) 
			{
			
			 Vector<Float> new_centroid = updateCentroid(entry.getKey(),entry.getValue());
			 

	    	 new_Cluster.put(new_centroid, entry.getValue()); // !!!!!!

		    }
		 
		 return new_Cluster;
		}
	 
	 // calculate the mean and give back the new centroid
	 public Vector<Float> updateCentroid(Vector<Float> vector,Vector<Read_rep_file> p) {
		    
		    
	        Vector<Float> meanVector = new Vector<Float>();
	        
		    for (int i = 0; i < points.get(0).getVec_car().size(); i++) {
		    	float sum = 0;
		    	 for (Read_rep_file point : p) {
				    	
		                sum += point.getVec_car().get(i);    
			    }

		    	 int numPoints = p.size();
				    if (numPoints > 0) {
			            float mean = sum / numPoints;
			            meanVector.add(mean);
				    }
		    }
		    return meanVector ;
		   
		}
	
	
	
}
