import java.util.ArrayList;
import java.util.Vector;

public class Sets {
		private Read_rep_folder R;
		private double per_train;
		private Vector<Read_rep_file> train;
		private Vector<Read_rep_file> test;
		
		// we have to code it that we have "per_train" per class.
		public Sets(Read_rep_folder r, double per_train) {
			super();
			this.R = r;
			this.per_train = per_train;
			
			Vector <Read_rep_file> files =  R.get_vec_rep_folder();
			
			// list of class (unique)
			ArrayList<Integer> classes = new ArrayList<Integer>();
			
			for (int i = 0; i < files.size(); i++) {
				if (!classes.contains(files.get(i).getNum_class())) {
					classes.add(files.get(i).getNum_class());
				}
			}
			
			
			Vector<Read_rep_file> v1;
			this.train = new Vector<Read_rep_file>();
			this.test = new Vector<Read_rep_file>();
			
			// take the percentage of all classes 
			for (int i = 0; i < classes.size(); i++) {
				
				v1 = new Vector<Read_rep_file>();
				for (int j = 0; j < files.size(); j++) {
					if (classes.get(i) == files.get(j).getNum_class()) {
						v1.add(files.get(j));
					} 
				}
				
				for (int j = 0; j < v1.size(); j++) {
					if (j< v1.size() * this.per_train) {
						this.train.add(v1.get(j));
						
					} 
					else {
						this.test.add(v1.get(j));
					}
					
				}
			}	
		}
		
		public Read_rep_folder getR() {
			return R;
		}
		public void setR(Read_rep_folder r) {
			R = r;
		}
		public double getPer_train() {
			return per_train;
		}
		public void setPer_train(double per_train) {
			this.per_train = per_train;
		}
		public Vector<Read_rep_file> getTrain() {
			return train;
		}
		public void setTrain(Vector<Read_rep_file> train) {
			this.train = train;
		}
		public Vector<Read_rep_file> getTest() {
			return test;
		}
		public void setTest(Vector<Read_rep_file> test) {
			this.test = test;
		}
		
		
		
		
		
		
		
}
