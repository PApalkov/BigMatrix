public class BigMatrixOperator {

    private static class Border {
        public int x_beg;
        public int y_beg;
        public int x_end;
        public int y_end;


        Border(int size){
            x_beg = 0;
            y_beg = 0;
            x_end = size;
            y_end = size;
        }

        public Border(int x_beg, int y_beg, int x_end, int y_end) {
            this.x_beg = x_beg;
            this.y_beg = y_beg;
            this.x_end = x_end;
            this.y_end = y_end;
        }

        public int getSizeX(){
            return x_end - x_beg;
        }

        public int getSizeY(){
            return y_end - y_beg;
        }


    }


    public static SquareMatrix transpose(SquareMatrix matrix){
        int m_transposed_size = matrix.getSize();

        int[][] transposed_matrix = new int[m_transposed_size][m_transposed_size];

        for (int i = 0; i < m_transposed_size; i++) {
            for (int j = 0; j < m_transposed_size; j++) {
                transposed_matrix[i][j] = matrix.getElement(j, i);
            }
        }

        return new SquareMatrix(transposed_matrix);
    }

    //todo продолжить реализацию методов


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


    public static SquareMatrix rec_mult(SquareMatrix A, SquareMatrix B) throws Exception{

        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {

            int result_matrix_size = A.getSize();
            int[][] result = new int[result_matrix_size][result_matrix_size];

            for (int i = 0; i < result_matrix_size; i++) {
                for (int j = 0; j < result_matrix_size; j++) {
                    result[i][j] = 0;
                }
            }

            do_rec_mult(A,B,result, new Border(A.getSize() - 1),
                    new Border(B.getSize() - 1), new Border(result_matrix_size - 1));


            return new SquareMatrix(result);
        }
    }


    private static void do_rec_mult(SquareMatrix A, SquareMatrix B, int[][] C,
                                    Border a, Border b, Border c) {

        if ((a.getSizeX() == 0) && (a.getSizeY() == 0)) {
            //if one element left
            C[c.y_beg][c.x_beg] += A.getElement(a.y_beg,a.x_beg) * B.getElement(b.y_beg,b.x_beg);

        } else {
            //dividing matrices into 4 parts by making new borders
            Border a_1_1 = new Border(a.x_beg, a.y_beg, (a.x_beg + a.x_end) / 2,
                    (a.y_beg + a.y_end) / 2);

            Border a_1_2 = new Border((a.x_beg + a.x_end) / 2 + 1, a.y_beg,
                    a.x_end, (a.y_beg + a.y_end) / 2);

            Border a_2_1 = new Border(a.x_beg, (a.y_beg + a.y_end) / 2 + 1,
                    (a.x_beg + a.x_end) / 2, a.y_end);

            Border a_2_2 = new Border((a.x_beg + a.x_end) / 2 + 1, (a.y_beg + a.y_end) / 2 + 1,
                    a.x_end, a.y_end);



            Border b_1_1 = new Border(b.x_beg, b.y_beg, (b.x_beg + b.x_end) / 2,
                    (b.y_beg + b.y_end) / 2);

            Border b_1_2 = new Border((b.x_beg + b.x_end) / 2 + 1, b.y_beg,
                    b.x_end, (b.y_beg + b.y_end) / 2);

            Border b_2_1 = new Border(b.x_beg, (b.y_beg + b.y_end) / 2 + 1,
                    (b.x_beg + b.x_end) / 2, b.y_end);

            Border b_2_2 = new Border((b.x_beg + b.x_end) / 2 + 1, (b.y_beg + b.y_end) / 2 + 1,
                    b.x_end, b.y_end);


            Border c_1_1 = new Border(c.x_beg, c.y_beg, (c.x_beg + c.x_end) / 2,
                    (c.y_beg + c.y_end) / 2);

            Border c_1_2 = new Border((c.x_beg + c.x_end) / 2 + 1, c.y_beg,
                    c.x_end, (c.y_beg + c.y_end) / 2);

            Border c_2_1 = new Border(c.x_beg, (c.y_beg + c.y_end) / 2 + 1,
                    (c.x_beg + c.x_end) / 2, c.y_end);

            Border c_2_2 = new Border((c.x_beg + c.x_end) / 2 + 1, (c.y_beg + c.y_end) / 2 + 1,
                    c.x_end, c.y_end);



            do_rec_mult(A, B, C, a_1_1, b_1_1, c_1_1);
            do_rec_mult(A, B, C, a_1_2, b_2_1, c_1_1);

            do_rec_mult(A, B, C, a_1_1, b_1_2, c_1_2);
            do_rec_mult(A, B, C, a_1_2, b_2_2, c_1_2);

            do_rec_mult(A, B, C, a_2_1, b_1_1, c_2_1);
            do_rec_mult(A, B, C, a_2_2, b_2_1, c_2_1);

            do_rec_mult(A, B, C, a_2_1, b_1_2, c_2_2);
            do_rec_mult(A, B, C, a_2_2, b_2_2, c_2_2);


        }
    }


    public static boolean equal(SquareMatrix A, SquareMatrix B){

        if (A.getSize() != B.getSize()) return false;

        for (int i = 0; i < A.getSize(); i++) {
            for (int j = 0; j < A.getSize(); j++) {
                if (A.getElement(i,j) != B.getElement(i,j)){
                    System.out.println(A.getElement(i,j) + " " + B.getElement(i,j));
                    return false;
                }
            }
        }

        return true;
    }

}
