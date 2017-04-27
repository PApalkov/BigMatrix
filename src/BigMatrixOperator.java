

public class BigMatrixOperator {

    private static final int MIN_SIZE = 16;

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

    //a class with the borders of the matrix parts
    private static class Borders{
        public int i_beg;
        public int j_beg;
        public int i_end;
        public int j_end;

        public Borders(int i_beg, int j_beg, int i_end, int j_end) {
            this.i_beg = i_beg;
            this.j_beg = j_beg;
            this.i_end = i_end;
            this.j_end = j_end;
        }

        public Borders(){}
    }


    public static SquareMatrix shtrassen_mult(SquareMatrix A, SquareMatrix B) throws Exception{
        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {

            int[][] a = A.getMatrix();
            int[][] b = B.getMatrix();

            Borders a_borders = new Borders(0,0, A.getSize(), A.getSize());
            Borders b_borders = new Borders(0,0, B.getSize(), B.getSize());

            int[][] result = rec_shtrassen_mult(a, b, a_borders ,b_borders);

            return new SquareMatrix(result);
        }

    }


    private static int[][] rec_shtrassen_mult(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        if (size < MIN_SIZE){
            return native_border_mult(a, b, a_borders, b_borders);
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
            divide(a_borders, a_1_1, a_1_2, a_2_1, a_2_2);
            divide(b_borders, b_1_1, b_1_2, b_2_1, b_2_2);


            //making 10 summations and

            int[][] s1 = substract(b, b, b_1_2, b_2_2);
            int[][] s2 = add(a, a, a_1_1, a_1_2);
            int[][] s3 = add(a, a, a_2_1, a_2_2);
            int[][] s4 = substract(b, b, b_2_1, b_1_1);
            int[][] s5 = add(a, a, a_1_1, a_2_2);
            int[][] s6 = add(b, b, b_1_1, b_2_2);
            int[][] s7 = substract(a, a, a_1_2, a_2_2);
            int[][] s8 = add(b, b, b_2_1, b_2_2);
            int[][] s9 = substract(a, a, a_1_1, a_2_1);
            int[][] s10 = add(b, b, b_1_1, b_1_2);

            //all the S matrices have the same size
            Borders s_borders = new Borders(0, 0, s1.length, s1.length);

            int[][] p1 = rec_shtrassen_mult(a, s1, a_1_1, s_borders);
            int[][] p2 = rec_shtrassen_mult(s2, b, s_borders, b_2_2);
            int[][] p3 = rec_shtrassen_mult(s3, b, s_borders, b_1_1);
            int[][] p4 = rec_shtrassen_mult(a, s4, a_2_2, s_borders);
            int[][] p5 = rec_shtrassen_mult(s5, s6, s_borders, s_borders);
            int[][] p6 = rec_shtrassen_mult(s7, s8, s_borders, s_borders);
            int[][] p7 = rec_shtrassen_mult(s9, s10, s_borders, s_borders);


            return combine(size, p1, p2, p3, p4, p5, p6, p7);
        }


    }


    private static int[][] native_border_mult(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        int a_i = a_borders.i_beg;
        int b_j = b_borders.j_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                int k_a = a_borders.j_beg;
                int k_b = b_borders.i_beg;

                while ((k_a < a_borders.j_end) && (k_b < b_borders.i_end)){
                    result[i][j] += (a[a_i + i][k_a] * b[k_b][b_j + j]);
                    k_a++;
                    k_b++;
                }
            }
        }

        return result;
    }

    private static void divide(Borders borders, Borders part_1_1, Borders part_1_2,
                               Borders part_2_1, Borders part_2_2) {

        //todo посмотреть как влияют операции деления на все время выполнения
        part_1_1.i_beg = borders.i_beg;
        part_1_1.j_beg = borders.j_beg;
        part_1_1.i_end = (borders.i_beg + borders.i_end) / 2;
        part_1_1.j_end = (borders.j_beg + borders.j_end) / 2;

        part_1_2.i_beg = borders.i_beg;
        part_1_2.j_beg = (borders.j_beg + borders.j_end) / 2;
        part_1_2.i_end = (borders.i_beg + borders.i_end) / 2;
        part_1_2.j_end = borders.j_end;

        part_2_1.i_beg = (borders.i_beg + borders.i_end) / 2;
        part_2_1.j_beg = borders.j_beg;
        part_2_1.i_end = borders.i_end;
        part_2_1.j_end = (borders.j_beg + borders.j_end) / 2;

        part_2_2.i_beg = (borders.i_beg + borders.i_end) / 2;
        part_2_2.j_beg = (borders.j_beg + borders.j_end) / 2;
        part_2_2.i_end = borders.i_end;
        part_2_2.j_end = borders.j_end;

    }

    private static int[][] add(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = a[a_borders.i_beg + i][a_borders.j_beg + j] + b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }

        return result;
    }

    private static int[][] substract(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = a[a_borders.i_beg + i][a_borders.j_beg + j] - b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }

        return result;

    }

    private static void borders_substract(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[a_borders.i_beg + i][a_borders.j_beg + j] -= b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }
    }

    private static void borders_add(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[a_borders.i_beg + i][a_borders.j_beg + j] += b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }
    }



    private static int[][] combine(int size, int[][] p1, int[][] p2, int[][] p3,
                                   int[][] p4, int[][] p5, int[][] p6, int[][] p7){

        int[][] c = new int[size][size];

        Borders c_1_1 = new Borders();
        Borders c_1_2 = new Borders();
        Borders c_2_1 = new Borders();
        Borders c_2_2 = new Borders();

        //giving values to borders' parts
        divide(new Borders(0, 0, size, size), c_1_1, c_1_2, c_2_1, c_2_2);

        Borders p_borders = new Borders(0, 0, p1.length, p1.length);

        //combining c1 part
        borders_add(c, p5, c_1_1, p_borders);
        borders_add(c, p4, c_1_1, p_borders);
        borders_add(c, p6, c_1_1, p_borders);
        borders_substract(c, p2, c_1_1, p_borders);

        //combining c2 part
        borders_add(c, p1, c_1_2, p_borders);
        borders_add(c, p2, c_1_2, p_borders);

        //combining c3 part
        borders_add(c, p3, c_2_1, p_borders);
        borders_add(c, p4, c_2_1, p_borders);

        //combining c4 part
        borders_add(c, p5, c_2_2, p_borders);
        borders_add(c, p1, c_2_2, p_borders);
        borders_substract(c, p3, c_2_2, p_borders);
        borders_substract(c, p7, c_2_2, p_borders);


        return c;

    }




    //======================FORK SHTRASSEN ALGORITHM=============


    public static SquareMatrix shtrassen_fork_mult(SquareMatrix A, SquareMatrix B) throws Exception{
        if (A.getSize() != B.getSize()){
            throw new Exception("Different matrix sizes");
        } else {

            int[][] a = A.getMatrix();
            int[][] b = B.getMatrix();

            Borders a_borders = new Borders(0,0, A.getSize(), A.getSize());
            Borders b_borders = new Borders(0,0, B.getSize(), B.getSize());

            //todo
            //int[][] result = rec_fork_shtrassen_mult(a, b, a_borders ,b_borders);

            //return new SquareMatrix(result);
            return null;
        }

    }

    /*
    private static int[][] rec_fork_shtrassen_mult(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        if (size < MIN_SIZE){
            return native_border_mult(a, b, a_borders, b_borders);
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
            divide(a_borders, a_1_1, a_1_2, a_2_1, a_2_2);
            divide(b_borders, b_1_1, b_1_2, b_2_1, b_2_2);


            //making 10 summations and

            int[][] s1 = substract(b, b, b_1_2, b_2_2);
            int[][] s2 = add(a, a, a_1_1, a_1_2);
            int[][] s3 = add(a, a, a_2_1, a_2_2);
            int[][] s4 = substract(b, b, b_2_1, b_1_1);
            int[][] s5 = add(a, a, a_1_1, a_2_2);
            int[][] s6 = add(b, b, b_1_1, b_2_2);
            int[][] s7 = substract(a, a, a_1_2, a_2_2);
            int[][] s8 = add(b, b, b_2_1, b_2_2);
            int[][] s9 = substract(a, a, a_1_1, a_2_1);
            int[][] s10 = add(b, b, b_1_1, b_1_2);

            //all the S matrices have the same size
            Borders s_borders = new Borders(0, 0, s1.length, s1.length);

            int[][] p1 = rec_shtrassen_mult(a, s1, a_1_1, s_borders);
            int[][] p2 = rec_shtrassen_mult(s2, b, s_borders, b_2_2);
            int[][] p3 = rec_shtrassen_mult(s3, b, s_borders, b_1_1);
            int[][] p4 = rec_shtrassen_mult(a, s4, a_2_2, s_borders);
            int[][] p5 = rec_shtrassen_mult(s5, s6, s_borders, s_borders);
            int[][] p6 = rec_shtrassen_mult(s7, s8, s_borders, s_borders);
            int[][] p7 = rec_shtrassen_mult(s9, s10, s_borders, s_borders);


            return combine(size, p1, p2, p3, p4, p5, p6, p7);
        }


    }
    */

    //===================SUPPORT======================

    public static boolean equal(SquareMatrix a, SquareMatrix b){

        if(a.getSize() != b.getSize()) {
            return false;
        } else {

            int size = a.getSize();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (a.getElement(i, j) != b.getElement(i,j)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
















