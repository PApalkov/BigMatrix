import java.util.Scanner;

public class Do {
    public static void main(String[] args) {
/*
        SquareMatrix matrix = new SquareMatrix(5);

        matrix.print();

        matrix.generate(5,0,10);

        matrix.print();

        System.out.print(matrix.toString());

        BigMatrixOperator.transpose(matrix).print();
*/

        SquareMatrix A = new SquareMatrix(256);
        A.generate(0,50);


        SquareMatrix B = new SquareMatrix(256);
        B.generate(0,50);



        try {
            SquareMatrix C1 = BigMatrixOperator.native_mult(A, B);
            SquareMatrix C2 = BigMatrixOperator.rec_mult(A, B);
            System.out.println(BigMatrixOperator.equal(C1, C2));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}
