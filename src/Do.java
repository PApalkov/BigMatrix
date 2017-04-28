import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Do {


    public static void main(String[] args) {

        //int size = 1024;
        //todo continue testing
        for (int size = 100; size < 1300; size += 100) {

            SquareMatrix A = new SquareMatrix(size);
            A.generate(0, 20);


            SquareMatrix B = new SquareMatrix(size);
            B.generate(0, 20);

            try {

                long naitive_mult_time_start = System.nanoTime();
                SquareMatrix C1 = BigMatrixMultiplier.native_mult(A, B);
                long naitive_mult_time_end = System.nanoTime();

                double native_mult_time = (naitive_mult_time_end - naitive_mult_time_start) / Math.pow(10, 9);
                System.out.println("\nNative algorithm takes " + native_mult_time + " seconds!");


                long rec_mult_time_start = System.nanoTime();
                SquareMatrix C2 = BigMatrixMultiplier.shtrassen_mult(A, B);
                long rec_mult_time_end = System.nanoTime();

                double rec_mult_time = (rec_mult_time_end - rec_mult_time_start) / Math.pow(10, 9);
                System.out.println("\nRecursive algorithm takes " + rec_mult_time + " seconds!");


                long rec_fork_mult_time_start = System.nanoTime();
                SquareMatrix C3 = BigMatrixMultiplier.shtrassen_fork_mult(A, B);
                long rec_fork_mult_time_end = System.nanoTime();

                double rec_fork_mult_time = (rec_fork_mult_time_end - rec_fork_mult_time_start) / Math.pow(10, 9);
                System.out.println("\nRecursive algorithm takes " + rec_fork_mult_time + " seconds!");


                String toWrite = size + "\n" + native_mult_time + "\n" + rec_mult_time + "\n" + rec_fork_mult_time + "\n";

                if (MatrixOperations.equal(C1, C3) && MatrixOperations.equal(C1, C2)) {
                    System.out.println("Results are equal");
                    //toWrite += "Results are equal\n\n";
                } else {
                    System.out.println("Results are NOT equal");
                    //toWrite += "Results are NOT equal\n\n";
                }

                write_file(toWrite);

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

    }


    public static void write_file(String toWrite){

        File coded_file = new File( "multData.txt");
        try(FileWriter writer = new FileWriter(coded_file, true))
        {
            writer.write(toWrite);
            writer.flush();
            writer.close();

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

    }
}
