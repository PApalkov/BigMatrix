
public class Do {
    public static void main(String[] args) {

        SquareMatrix matrix = new SquareMatrix(5);

        matrix.print();

        matrix.generate(5,0,10);

        matrix.print();

        System.out.print(matrix.toString());

        MatrixOperator.transpose(matrix).print();

    }
}
