import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// read the content of one specified file


public class Read_rep_file { // lire un seul fichier

	private String representation_path;
	private int num_class;
	private int num_echantillion;
	private Vector<Float> Vec_car;

	public Read_rep_file(String representation_path) {
		super();
		this.representation_path = representation_path;

		// Définir le modèle de regex pour extraire la partie de la chaîne entre le point et le "s" minuscule
        String regexPart = "(?i)s(\\d+)(?i)n(\\d+)\\.";
        Pattern patternPart = Pattern.compile(regexPart);

        // Créer un objet Matcher avec la chaîne d'entrée
        Matcher matcherPart = patternPart.matcher(representation_path);

        // Vérifier si la correspondance est trouvée
        if (matcherPart.find()) {
            // Extraire la partie de la chaîne entre le point et le "s" minuscule
        	this.num_class = Integer.parseInt(matcherPart.group(1));
        	this.num_echantillion = Integer.parseInt(matcherPart.group(2));
        }
        
        // construction
		this.Vec_car = new Vector<Float>();
		
		
		// Use the class loader to get the input stream for the file
        InputStream inputStream = Read_rep_file.class.getClassLoader().getResourceAsStream(representation_path);

        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Vec_car.addElement(Float.parseFloat((line)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found: " + representation_path);
        }
		
	}

	public String getRepresentation_path() {
		return representation_path;
	}

	public void setRepresentation_path(String representation_path) {
		this.representation_path = representation_path;
	}

	public int getNum_class() {
		return num_class;
	}

	public void setNum_class(int num_class) {
		this.num_class = num_class;
	}

	public int getNum_echantillion() {
		return num_echantillion;
	}

	public void setNum_echantillion(int num_echantillion) {
		this.num_echantillion = num_echantillion;
	}

	public Vector<Float> getVec_car() {
		return Vec_car;
	}

	public void setVec_car(Vector<Float> vec_car) {
		Vec_car = vec_car;
	}

	@Override
	public String toString() {
		/*return "Read_rep_file [representation_path=" + representation_path + ", num_class=" + num_class
				+ ", num_echantillion=" + num_echantillion + ", Vec_car=" + Vec_car + "]";*/
		// more compact
		return   "[num_class=" + num_class + ", num_echantillion=" + num_echantillion + ", Vec_car=" + Vec_car + "]";
		
	}

	


	
}
