with open("../resources/input9.txt", "r") as f:
  data = f.readlines()

data = [int(x) for x in data]

from itertools import combinations

def find_sum(coll, val):
  sum_pairs = [sum(comb) for comb in combinations(coll, 2)]
  if val in sum_pairs:
    return True
  return False

def part1():
  for i, x in enumerate(data[25:]):
    if not find_sum(data[i:i + 25], x):
      return x

ans = part1()
print(ans)

def part2(invalid):
  for i, x in enumerate(data):
    window = [x]
    for j, y in enumerate(data[i + 1:]):
      window.append(y)
      window_sum = sum(window)
      if window_sum == invalid:
        return min(window) + max(window)
      if window_sum < invalid:
        continue
      else:
        break

print(part2(ans))