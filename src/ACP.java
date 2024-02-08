import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class ACP {
    private Read_rep_folder R;
    private int numComponents;

    public ACP(Read_rep_folder R,int numComponents) {
        this.R = R;
        this.numComponents = numComponents;
        Vector <Read_rep_file> files =  R.get_vec_rep_folder();

        double[][] data = new double[files.size()][files.get(0).getVec_car().size()];

        for (int i = 0; i < files.size(); i++) {
         for (int j = 0; j < files.get(0).getVec_car().size(); j++) {
            	
                data[i][j] = files.get(i).getVec_car().get(j);
            }
        }

        // Étape 1: Centrer les données
        double[] mean = calculateMean(data);
        centerData(data, mean);

        // Étape 2: Calculer la matrice de covariance
        RealMatrix covarianceMatrix = calculateCovarianceMatrix(data);
        // Étape 3: Calculer les vecteurs propres et les valeurs propres
        EigenDecomposition eigenDecomposition = new EigenDecomposition(covarianceMatrix);

        // Étape 4: Trier les vecteurs propres
        RealMatrix sortedEigenvectors = sortEigenvectors(eigenDecomposition.getV(), eigenDecomposition.getRealEigenvalues());
        

        // Étape 5: Sélectionner les composantes principales
        RealMatrix principalComponents = selectPrincipalComponents(sortedEigenvectors,numComponents);

        // Étape 6: Transformer les données
        RealMatrix transformedData = transformData(data, principalComponents);

        // Les données transformées sont stockées dans transformedData
        System.out.println(transformedData);
        System.out.println("Taux de données dans les "+numComponents+" premieres\n"
        		+ " composantes principales : " + calculateVarianceExplained(eigenDecomposition.getRealEigenvalues(), numComponents) + "%");
        Vector <Float> reduced_vector;
        for (int i = 0; i < files.size(); i++) {
        	 reduced_vector = new Vector<Float>();
                 double[] rowValues = transformedData.getRow(i);
                 for (double value : rowValues) {
                	 reduced_vector.addElement((float) value);
                 }
            files.get(i).setVec_car(reduced_vector);
           }

    }

    private static double[] calculateMean(double[][] data) {
        double[] mean = new double[data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                mean[j] += data[i][j];
            }
        }
        for (int j = 0; j < mean.length; j++) {
            mean[j] /= data.length;
        }
        return mean;
    }

    private static void centerData(double[][] data, double[] mean) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] -= mean[j];
            }
        }
    }
    
    private static double calculateVarianceExplained(double[] eigenvalues, int numComponents) {
        double totalVariance = 0.0;
        for (double eigenvalue : eigenvalues) {
            totalVariance += eigenvalue;
        }

        double varianceExplained = 0.0;
        for (int i = 0; i < numComponents; i++) {
            varianceExplained += eigenvalues[i];
        }

        return (varianceExplained / totalVariance) * 100.0;
    }

    private static RealMatrix calculateCovarianceMatrix(double[][] data) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealMatrix transpose = matrix.transpose();
        
        return transpose.multiply(matrix).scalarMultiply(1.0 / (data.length - 1));
    }

    private static RealMatrix sortEigenvectors(RealMatrix eigenvectors, double[] eigenvalues) {
        List<EigenPair> eigenPairs = new ArrayList<>();
        for (int i = 0; i < eigenvalues.length; i++) {
            eigenPairs.add(new EigenPair(eigenvalues[i], eigenvectors.getColumn(i)));
        }

        Collections.sort(eigenPairs, Collections.reverseOrder());

        double[][] sortedEigenvectorsData = new double[eigenvectors.getRowDimension()][eigenvectors.getColumnDimension()];
        for (int i = 0; i < eigenPairs.size(); i++) {
            sortedEigenvectorsData[i] = eigenPairs.get(i).getVector();
        }

        return MatrixUtils.createRealMatrix(sortedEigenvectorsData);
    }

    private static RealMatrix selectPrincipalComponents(RealMatrix eigenvectors, int numComponents) {
        // Select the specified number of eigenvectors
        if (numComponents < 1 || numComponents > eigenvectors.getColumnDimension()) {
            throw new IllegalArgumentException("Invalid number of components");
        }

        return eigenvectors.getSubMatrix(0, eigenvectors.getRowDimension() - 1, 0, numComponents - 1);
    }

    private static RealMatrix transformData(double[][] data, RealMatrix principalComponents) {
        System.out.println("Data dimensions: " + data.length + " x " + data[0].length);
        System.out.println("Principal components dimensions: " + principalComponents.getRowDimension() + " x " + principalComponents.getColumnDimension());

        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealMatrix transformedData = matrix.multiply(principalComponents);

        System.out.println("Transformed data dimensions: " + transformedData.getRowDimension() + " x " + transformedData.getColumnDimension());

        return transformedData;
    }


    private static class EigenPair implements Comparable<EigenPair> {
        private double eigenvalue;
        private double[] vector;

        public EigenPair(double eigenvalue, double[] vector) {
            this.eigenvalue = eigenvalue;
            this.vector = vector;
        }

        public double getEigenvalue() {
            return eigenvalue;
        }

        public double[] getVector() {
            return vector;
        }

        @Override
        public int compareTo(EigenPair other) {
            return Double.compare(this.eigenvalue, other.eigenvalue);
        }
    }
}
