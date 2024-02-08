import java.util.Vector;

public class MAIN {

    public static void main(String[] args) {
        //read data
        Read_rep_folder R1 = new Read_rep_folder("GFD");
        
        
        
	        	 System.out.println("****************************************(KNN)****************************************");
	
	            //-------------------------------------------------KNN--------------------------------------------------------- 
	        	 Sets S1 = new Sets(R1, 0.6);
	            // split data train and test
	            long startTime = System.currentTimeMillis();
	           
	            Knn knn = new Knn(2, S1,0);// dist type 0 ---Euclidean  type 1-----Manhattan
	
	            Tests T_Knn = new Tests(knn, 9);
	            // Record end time
	            long endTime = System.currentTimeMillis();
	
	            // Calculate and print the execution time
	            long executionTime = endTime - startTime;
	            System.out.println("Execution time: " + executionTime + " milliseconds");
	            
	            System.out.println("****************************************(end)****************************************");
	            
        
        //-------------------------------------------------SVM---------------------------------------------------------
	    /*for (float n = (float) 0.6; n < 0.9; n+=0.1) {
	    	float m = (float) 0.1;
	    	for (int b = 0; b < 3; b++) {
	    		int d = 100;
	    		for (int e = 0; e <3; e++) {
	    			System.out.println();
			  	    System.out.println("****************************************("+R1.getRepresentation_name()+"-train "+n*100+"% -bias = "+m+" -Nb epoch "+d+")****************************************");
			  	           
			          System.out.println("****************************************(SVM)****************************************");
			          Sets S1 = new Sets(R1, n);
			          long  startTime = System.currentTimeMillis();
			          SVM svm = new SVM(9, R1.get_vec_rep_folder().get(0).getVec_car().size(),m, S1);
			          svm.train(S1, d);
			          // get test data
			          double[][] test = new double[S1.getTest().size()][S1.getTest().get(0).getVec_car().size()];
			  		int[] true_labels = new int[S1.getTest().size()];
			  		int[] pridcted_labels =  new int[S1.getTest().size()];
			  		for (int i = 0; i < S1.getTest().size(); i++) {
			              for (int j = 0; j < S1.getTest().get(i).getVec_car().size(); j++) {
			              	Vector<Float> vector = S1.getTest().get(i).getVec_car();
			              	test[i][j] = vector.get(j);
			              	// 
			              	pridcted_labels[i] = svm.predict(test[i]);
			              	true_labels[i] = S1.getTest().get(i).getNum_class()-1;// our cassses are labeled from 1 to 9 -> 0 to 8 to match
			              }
			              
			              //System.out.println("Predicted class: "+ svm.predict(test[i])+",real class: "+true_labels[i]);
			          }
			  		
			  		TestSVM tests = new TestSVM(true_labels, pridcted_labels, 9);
			  		 // Record end time
			  		long  endTime = System.currentTimeMillis();

			          // Calculate and print the execution time
			  		long  executionTime = endTime - startTime;
			          System.out.println("Execution time: " + executionTime + " milliseconds");
			          d *=10;
				}
		        m *=0.1 ;
			}
		}*/
	  
        //-------------------------------------------------Kmeans---------------------------------------------------------
        /*ACP acp = new ACP(R1, 2);
        int dist_type = 0;
        for (int j = 0; j < 2; j++) {
        	int d = 100;
            for (int i = 0; i <3; i++) {
            	System.out.println();
    	  	    System.out.println("****************************************("+R1.getRepresentation_name()+"-Nb itterations "+d+" -DistType"+dist_type+")****************************************");
    	          System.out.println("****************************************(Kmeans)****************************************");
    		      long startTime = System.currentTimeMillis();
    		      Kmeans kmeans = new Kmeans(9, R1.get_vec_rep_folder(), d,dist_type);
    		      
    		      Tests T_kmeans = new Tests(kmeans);
    		      // Record end time
    		      long endTime = System.currentTimeMillis();
    		
    		      // Calculate and print the execution time
    		      long executionTime = endTime - startTime;
    		      System.out.println("Execution time: " + executionTime + " milliseconds");
    		      System.out.println("****************************************(end)****************************************");
    		      d *=10;
    		}
            dist_type = 1;
		}*/
        
         

    }
}