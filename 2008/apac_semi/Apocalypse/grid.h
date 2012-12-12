#include <iomanip>

template <typename T> class Grid;

template <typename T> ostream& operator<<(ostream& os, const Grid<T>& grid);

template<typename T> class Grid
{
public:
  const unsigned int rows, cols;
  
  vector<T> cells;
  
  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows * cols, T())
  {
    
  }
  
  Grid(const Grid& rhs) : rows(rhs.rows), cols(rhs.cols), cells(rhs.cells) {
    //cout << "Copy"; 
  }
  
  int getIndex(int row, int col) const
  {
    return row * cols + col;
  }
  
  void set(int row, int col, const T& val) {
    cells[getIndex(row, col)] = val;
  }
  
  void set(int index, const T& val) {
    cells[index] = val;
  }
  
  const T& operator[](unsigned int index) const {
    assert(index >= 0 && index < rows * cols);
    return cells[index];
  }
  T& operator[](unsigned int index) {
    assert(index >= 0 && index < rows * cols);
    return cells[index];
  }
  
  void reset(const T& val) {
    fill(cells.begin(), cells.end(), val);
    
  }
  
  class iterator;
  
  bool isValid(const iterator& it) {
     int index = it.ptr - begin().ptr;
     if (index >= 0 && index < rows*cols) {
       return true;
     }
     return false;
  }
  
  
  
  int getIndex(iterator it) {
    __int64 d = distance(begin().ptr, it.ptr); 
	assert(d < 10000000);
	return (int) d;
  }
  
  vector<unsigned int> getAdjacentSquaresIndexes(unsigned int sqIndex) const {
    assert(sqIndex >= 0 && sqIndex < rows * cols);
    const unsigned int row = sqIndex / cols;
    const unsigned int col = sqIndex % cols;
    vector<unsigned int> adjSquares;
    
    if (row > 0) {
      adjSquares.push_back(sqIndex - cols);
      assert(adjSquares.back() >= 0 && adjSquares.back() < rows * cols);
    }
    if (col > 0) {
      adjSquares.push_back(sqIndex - 1);
      assert(adjSquares.back() >= 0 && adjSquares.back() < rows * cols);
    }
    if (col < cols - 1) {
      adjSquares.push_back(sqIndex + 1);
      assert(adjSquares.back() >= 0 && adjSquares.back() < rows * cols);
    }
    if (row < rows - 1) {
      adjSquares.push_back(sqIndex + cols);
      assert(adjSquares.back() >= 0 && adjSquares.back() < rows * cols);
    }
    return adjSquares;
  }
  
  
  vector<iterator> getAdjacentSquares(iterator sq) {
    const int index = getIndex(sq);
    assert(index >= 0 && index < rows * cols);
    const int row = index / cols;
    const int col = index % cols;
    assert(isValid(sq));
    vector<iterator> adjSquares;
    
    if (row > 0) {
      adjSquares.push_back(sq.up());
      assert(isValid(adjSquares.back()));
    }
    if (col > 0) {
      adjSquares.push_back(sq.left());
      assert(isValid(adjSquares.back()));
    }
    if (col < cols - 1) {
      adjSquares.push_back(sq.right());
      assert(isValid(adjSquares.back()));
    }
    if (row < rows - 1) {
      LOG(row);
      adjSquares.push_back(sq.down());
      assert(isValid(adjSquares.back()));
    }
    return adjSquares;
  }
  
  iterator begin() {
    return iterator(cells.begin(), cols);
  }
  
  iterator end() {
    return iterator(cells.end(), cols);
  }
  
  class iterator {
  public:
    iterator(const iterator &other) :
    ptr(other.ptr), cols(other.cols)
    {
    }
    
    iterator left() const {
      return iterator(ptr - 1, cols);
    }
    
    iterator right() const {
      return iterator(ptr + 1, cols);
    }
    
    iterator up() const {
      return iterator(ptr - cols, cols);
    }
    
    iterator down() const {
      LOG(&*ptr);
      LOG(cols);
      return iterator(ptr + cols, cols);
    }
    
    iterator &operator++() {
      ++ptr;
      return *this;
    }
    
    iterator &operator--() {
      --ptr;
      return *this;
    }
    
    iterator operator++(int) {
      ++*this;
      return iterator(ptr + 1);
    }
    
    iterator operator--(int) {
      --*this;
      return iterator(ptr - 1);
    }
    
    bool operator!=(const iterator& rhs) {
      return ptr != rhs.ptr;
    }
    bool operator==(const iterator& rhs) {
      return ptr == rhs.ptr;
    }
    
    iterator& operator=(const iterator& rhs) {
      if (&rhs != this) {
        ptr = rhs.ptr;
        assert(cols == rhs.cols);
      }
      return *this;
    }
    
    T operator*() const {
      return *ptr;
    }
    
    T& operator*() {
      return *ptr;
    }
    
  private:
    //iterator();
    iterator(typename vector<T>::iterator ptr_, int cols) :
    ptr(ptr_), cols(cols)
    {
    }
    
    typename vector<T>::iterator ptr;
    const int cols;
    
    friend class Grid;
  };
  
};


template<typename T> ostream& operator<<(ostream& os, const Grid<T>& grid)
{
  os << endl;
  for(unsigned int c = 0; c < grid.cols; ++c) {
    os << setw(5) << (c+1);
  }
  os << endl;
  string s(5, '_');
  for(unsigned int c = 0; c < grid.cols; ++c) {
    os << s;
  }
  os << endl;
  for(unsigned int r = 0; r < grid.rows; ++r) {
    
    for(unsigned int c = 0; c < grid.cols; ++c) {
      
      os << setw(5) << grid.cells[grid.getIndex(r, c)];
      
    }
    os << "| " << (r+1);
    
    os << endl;
  }
  
  return os;    
}
