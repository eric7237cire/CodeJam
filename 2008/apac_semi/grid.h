#include <iomanip>

template <typename T> class Grid;

template <typename T> ostream& operator<<(ostream& os, const Grid<T>& grid);

template<typename T> class Grid
{
public:
  const unsigned int rows, cols;
  
  typedef vector<T> VectorCells;
  typedef vector<VectorCells> VectorRows;
  VectorRows cells;
  
  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows)
  {
    for(typename VectorRows::iterator it = cells.begin(); it != cells.end(); ++it) {
      *it = VectorCells(cols, T());      
    }
  }
  
  int getIndex(int row, int col)
  {
    return row * cols + col;
  }
  
  void set(int row, int col, const T& val) {
    cells[row][col] = val;
  }
  
};


template<typename T> ostream& operator<<(ostream& os, const Grid<T>& grid)
{
  os << endl;
  for(unsigned int c = 0; c < grid.cols; ++c) {
    os << (c % 10);
  }
  os << endl << endl;
  for(int r = grid.rows - 1; r >= 0; --r) {
    
    for(unsigned int c = 0; c < grid.cols; ++c) {
      
      os << setw(5) << grid.cells[r][c];
      
    }
    os << " :" << r;
    
    os << endl;
  }
  
  return os;    
}
