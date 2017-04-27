import java.util.Scanner;

public class Do {
    public static void main(String[] args) {

        int size = 2048;

        SquareMatrix A = new SquareMatrix(size);
        A.generate(0,20);


        SquareMatrix B = new SquareMatrix(size);
        B.generate(0,20);

        try {

            long naitive_mult_time_start = System.nanoTime();

            SquareMatrix C1 = BigMatrixOperator.native_mult(A, B);

            long naitive_mult_time_end = System.nanoTime();
            System.out.println("\nNative algorithm takes " +
                    (naitive_mult_time_end - naitive_mult_time_start) / Math.pow(10,9)  + " seconds!");




            long rec_mult_time_start = System.nanoTime();

            SquareMatrix C2 = BigMatrixOperator.shtrassen_mult(A, B);

            long rec_mult_time_end = System.nanoTime();


            System.out.println("\nRecursive algorithm takes " +
                    (rec_mult_time_end - rec_mult_time_start)  / Math.pow(10,9) + " seconds!");



            if (BigMatrixOperator.equal(C1, C2)){
                System.out.println("Results are equal");
            } else {
                System.out.println("Results are NOT equal");
            }

            //C1.print();
            //C2.print();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
