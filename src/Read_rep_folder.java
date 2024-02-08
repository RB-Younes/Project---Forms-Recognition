import java.io.File;
import java.util.Vector;

// read all classes of a representation
public class Read_rep_folder { //read all files of one specific representation
	
	private String representation_name;
	private Vector<Read_rep_file> Vec_class;
	
	public Read_rep_folder(String representation_name) {
		super();
		this.representation_name = representation_name;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String packageName = this.representation_name; // Replace with your actual package name
		String packagePath = packageName.replace('.', '/');

		java.net.URL packageUrl = classLoader.getResource(packagePath);

		if (packageUrl != null) {
		    File folder = new File(packageUrl.getFile());
		    File[] listOfFiles = folder.listFiles();

		    if (listOfFiles != null) {
		    	//init the vectors
		    	this.Vec_class = new Vector<Read_rep_file>();
		    	Read_rep_file R ;
		        for (int i = 0; i < listOfFiles.length; i++) {
		            if (listOfFiles[i].isFile()) {
		             R = new Read_rep_file("\\"+representation_name+"\\"+ listOfFiles[i].getName());
		             this.Vec_class.add(R);
		            }
		        }
		    } else {
		        System.err.println("Error listing files in the package");
		    }
		} else {
		    System.err.println("Package not found (false representation name)");
		}		
    }

	
	public String getRepresentation_name() {
		return representation_name;
	}


	public void setRepresentation_name(String representation_name) {
		this.representation_name = representation_name;
	}


	public Vector<Read_rep_file> get_vec_rep_folder() {
		return Vec_class;
	}


	public void set_vec_rep_folder(Vector<Read_rep_file> vec_class) {
		Vec_class = vec_class;
	}


	@Override
	public String toString() {
		return "Read_rep_folder [representation_name=" + representation_name + ", Vec_class=" + Vec_class + "]";
	}
		
}
	
