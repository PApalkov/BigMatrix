public class BigMatrixOperator {

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
}
