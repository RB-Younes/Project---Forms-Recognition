import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Knn {
	private int k; // number of neighbors
	private Sets data;
	private Vector <Read_rep_file> Train;
	private Vector <Read_rep_file> Test;
	private  Map<Read_rep_file, Integer> classification;
	private int DistType;
	
	public Knn(int k, Sets data,int DistType) {
		super();
		this.k = k;
		this.data = data;
		this.Train = data.getTrain();
		this.Test = data.getTest();
		this.DistType =DistType;
		this.classification = new HashMap<Read_rep_file, Integer>();
		
        for (int i = 0; i < Test.size(); i++) {
        	
            Map<Read_rep_file, Double> dict= new HashMap<Read_rep_file, Double>();
        	
			for (int j = 0; j < Train.size(); j++) 
			{
				double dist = calculateDistance(Test.get(i),Train.get(j),DistType);
				dict.put(Train.get(j),dist);				
			}

			Map<Read_rep_file, Double> sortedDictionary = sortDictionaryByValue(dict);

			
			/*for (Map.Entry<Read_rep_file, Double> entry : dict.entrySet()) {
	            Read_rep_file key = entry.getKey();
	            Double value = entry.getValue();
	            System.out.println("Value: " + value + ", Key: " + key);
	         }
			
			System.out.println("#################################################################");
			for (Map.Entry<Read_rep_file, Double> entry : sortedDictionary.entrySet()) {
	            Read_rep_file key = entry.getKey();
	            Double value = entry.getValue();
	            System.out.println("Value: " + value + ", Key: " + key);
	        }*/
			classification.put(Test.get(i), getMajorityClass(this.k,sortedDictionary));
				
			
			
        }
        
		 for (Map.Entry<Read_rep_file, Integer> entry : classification.entrySet()) {
			 	Read_rep_file key = entry.getKey();
	            int value = entry.getValue();
	            System.out.println("predicted class: " + value + ", real class: " + key.getNum_class());
	            if (value == key.getNum_class()) {

				}
		 }	
	}

	
	public int getK() {
		return k;
	}


	public void setK(int k) {
		this.k = k;
	}


	public Sets getData() {
		return data;
	}


	public void setData(Sets data) {
		this.data = data;
	}


	public Vector<Read_rep_file> getTrain() {
		return Train;
	}


	public void setTrain(Vector<Read_rep_file> train) {
		Train = train;
	}


	public Vector<Read_rep_file> getTest() {
		return Test;
	}


	public void setTest(Vector<Read_rep_file> test) {
		Test = test;
	}


	public Map<Read_rep_file, Integer> getClassification() {
		return classification;
	}


	public void setClassification(Map<Read_rep_file, Integer> classification) {
		this.classification = classification;
	}


	private double calculateDistance(Read_rep_file point1,Read_rep_file point2,int type) 
	{
		double sum = 0.0;
		if (type == 0) {
			 for (int i = 0; i < point1.getVec_car().size(); i++) {
		            sum += Math.pow(point1.getVec_car().get(i) - point2.getVec_car().get(i), 2);
		        }
		        return Math.sqrt(sum);
		}
		else {
			for (int i = 0; i < point1.getVec_car().size(); i++) {
	            sum += Math.sqrt(Math.pow(point1.getVec_car().get(i) - point2.getVec_car().get(i), 2));
	        }
	        return sum; 
		}
        
       
    }
	
	  // Method to sort a dictionary by values
    private static <K, V extends Comparable<? super V>> Map<K, V> sortDictionaryByValue(Map<K, V> unsortedDict) {
        List<Map.Entry<K, V>> entryList = new LinkedList<>(unsortedDict.entrySet());
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));

        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	private int getMajorityClass(int k, Map<Read_rep_file, Double> Dict) {
		Map<Integer, Integer> M = new HashMap<Integer, Integer>();
		int cpt=0;
		for (Map.Entry<Read_rep_file, Double> entry : Dict.entrySet()) {
			
			if (cpt == k) {
				break;
			}
			
            Read_rep_file key = entry.getKey();
            
            if (M.containsKey(key.getNum_class())) {
				 M.put(key.getNum_class(), M.get(key.getNum_class())+1);
			}
            else {
				 M.put(key.getNum_class(), 1);
            }
            
            cpt++;
            
        }
		
		// get the max value
		
		int max = 0 ;
		int key_max = -1 ;
		for (Map.Entry<Integer, Integer> entry : M.entrySet()) {
			
			 int key = entry.getKey();
			 int value = entry.getValue();
			 
			 if (value > max) {
				max = value;
				key_max = key;
			}
		}
		
        return key_max;
    }
	
}
