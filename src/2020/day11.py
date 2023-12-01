from pprint import pprint

def find_adjacents_visible(matrix, i, j, m, n):
  adjacents = []
  for edge_dx in [(0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (1, -1), (-1, -1), (-1, 1)]:
    new_i = i + edge_dx[0]
    new_j = j + edge_dx[1]
    a = 0
    while 0 <= new_i < m and 0 <= new_j < n:
      a = matrix[new_i][new_j]
      if a == 2 or a == 1:
        break
      new_i += edge_dx[0]
      new_j += edge_dx[1]
    adjacents.append(a)
  return adjacents

def find_adjacents(matrix, i, j, m, n):
  adjacents = []
  for edge in [(0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (1, -1), (-1, -1), (-1, 1)]:
    if 0 <= i + edge[0] < m and 0 <= j + edge[1] < n:
      try:
        a = matrix[i + edge[0]][j + edge[1]]
      except:
        a = 0
    else:
      a = 0
    adjacents.append(a)
  return adjacents

def single_pass(matrix, m, n):
  new_mat = [[0 for _ in range(n)] for _ in range(m)]
  changed = False
  occupied = 0
  for i, row in enumerate(matrix):
    for j, seat in enumerate(row):
      adjacents = find_adjacents_visible(matrix, i, j, m, n)
      if seat == 1 and adjacents.count(2) == 0:
        new_mat[i][j] = 2
        occupied += 1
        changed = True
      elif seat == 2 and adjacents.count(2) >= 5:
        new_mat[i][j] = 1
        changed = True
      else:
        new_mat[i][j] = seat
        if seat == 2:
          occupied += 1
  return changed, occupied, new_mat
  

def part1(matrix, m, n):
  mat = matrix
  while True:
    changed, occupied, mat = single_pass(mat, m, n)
    print(changed, occupied)
    # pprint(mat)
    if not changed:
      return occupied


def part2(data):
  return 0


if __name__ == "__main__":  
  with open("./resources/input11.txt", "r") as f:
    data = f.readlines()

  matrix = []
  row_length = 0
  col_length = 0
  for row in data:
    mrow = []
    mmap = {
      ".": 0,
      "L": 1,
      "#": 2,
    }
    row_length += 1
    col_length = 0
    for col in (row).strip():
      col_length += 1
      mrow.append(mmap[col])
    matrix.append(mrow)
  
  pprint(matrix)
  print(row_length, col_length)
  ans = part1(matrix, row_length, col_length)
  print(ans)
  ans = part2(data)
  print(ans)