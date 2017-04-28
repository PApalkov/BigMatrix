

public class BigMatrixMultiplier {

    private static final int MIN_SIZE = 64;

    //======================NATIVE ALGORITHM========================


    public static SquareMatrix native_mult(SquareMatrix A, SquareMatrix B) throws Exception{



        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {



            int result_matrix_size = A.getSize();

            int[][] result = new int[result_matrix_size][result_matrix_size];



            for (int i = 0; i < result_matrix_size; i++) {
                for (int j = 0; j < result_matrix_size; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < result_matrix_size; k++) {
                        result[i][j] += A.getElement(i, k) * B.getElement(k, j);

                    }
                }
            }
            return new SquareMatrix(result);
        }
    }


    //========================USUAL SHTRASSEN ALGORITHM=======================


    public static SquareMatrix shtrassen_mult(SquareMatrix A, SquareMatrix B) throws Exception{
        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {

            //resizing for well weoking algorithm
            int old_dim = A.getSize();
            int new_dim = MatrixOperations.new_dim(A.getSize());

            int[][] a = MatrixOperations.resize(A.getMatrix(), new_dim);
            int[][] b = MatrixOperations.resize(B.getMatrix(), new_dim);

            Borders a_borders = new Borders(0,0, new_dim, new_dim);
            Borders b_borders = new Borders(0,0, new_dim, new_dim);

            int[][] result = rec_shtrassen_mult(a, b, a_borders ,b_borders);

            int[][] norma_result = MatrixOperations.resize(result, old_dim);
            return new SquareMatrix(norma_result);
        }

    }


    private static int[][] rec_shtrassen_mult(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        if (size < MIN_SIZE){
            return MatrixOperations.native_border_mult(a, b, a_borders, b_borders);
        } else {
            //making borders for smaller parts
            Borders a_1_1 = new Borders();
            Borders a_1_2 = new Borders();
            Borders a_2_1 = new Borders();
            Borders a_2_2 = new Borders();

            Borders b_1_1 = new Borders();
            Borders b_1_2 = new Borders();
            Borders b_2_1 = new Borders();
            Borders b_2_2 = new Borders();

            //giving values to borders' parts
            MatrixOperations.divide(a_borders, a_1_1, a_1_2, a_2_1, a_2_2);
            MatrixOperations.divide(b_borders, b_1_1, b_1_2, b_2_1, b_2_2);


            //making 10 summations and
            int[][] s1 = MatrixOperations.substract(b, b, b_1_2, b_2_2);
            int[][] s2 = MatrixOperations.add(a, a, a_1_1, a_1_2);
            int[][] s3 = MatrixOperations.add(a, a, a_2_1, a_2_2);
            int[][] s4 = MatrixOperations.substract(b, b, b_2_1, b_1_1);
            int[][] s5 = MatrixOperations.add(a, a, a_1_1, a_2_2);
            int[][] s6 = MatrixOperations.add(b, b, b_1_1, b_2_2);
            int[][] s7 = MatrixOperations.substract(a, a, a_1_2, a_2_2);
            int[][] s8 = MatrixOperations.add(b, b, b_2_1, b_2_2);
            int[][] s9 = MatrixOperations.substract(a, a, a_1_1, a_2_1);
            int[][] s10 = MatrixOperations.add(b, b, b_1_1, b_1_2);

            //all the S matrices have the same size
            Borders s_borders = new Borders(0, 0, s1.length, s1.length);

            int[][] p1 = rec_shtrassen_mult(a, s1, a_1_1, s_borders);
            int[][] p2 = rec_shtrassen_mult(s2, b, s_borders, b_2_2);
            int[][] p3 = rec_shtrassen_mult(s3, b, s_borders, b_1_1);
            int[][] p4 = rec_shtrassen_mult(a, s4, a_2_2, s_borders);
            int[][] p5 = rec_shtrassen_mult(s5, s6, s_borders, s_borders);
            int[][] p6 = rec_shtrassen_mult(s7, s8, s_borders, s_borders);
            int[][] p7 = rec_shtrassen_mult(s9, s10, s_borders, s_borders);


            return MatrixOperations.combine(size, p1, p2, p3, p4, p5, p6, p7);
        }
    }


    //======================FORK SHTRASSEN ALGORITHM=============


    public static SquareMatrix shtrassen_fork_mult(SquareMatrix A, SquareMatrix B) throws Exception{
        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {

            //resizing for well weoking algorithm
            int old_dim = A.getSize();
            int new_dim = MatrixOperations.new_dim(A.getSize());

            int[][] a = MatrixOperations.resize(A.getMatrix(), new_dim);
            int[][] b = MatrixOperations.resize(B.getMatrix(), new_dim);

            Borders a_borders = new Borders(0,0, new_dim, new_dim);
            Borders b_borders = new Borders(0,0, new_dim, new_dim);


            int[][] result = new MyRecursiveShtrassenTask(a, b, a_borders ,b_borders).compute();

            int[][] norma_result = MatrixOperations.resize(result, old_dim);
            return new SquareMatrix(norma_result);

        }
    }

}
















