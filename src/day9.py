with open("../resources/input9.txt", "r") as f:
  data = f.readlines()

data = [int(x) for x in data]

from itertools import combinations

def find_sum(coll, val):
  sum_pairs = [sum(comb) for comb in combinations(coll, 2)]
  return val in sum_pairs

def part1(coll, preamble_size):
  for i, x in enumerate(coll[preamble_size:]):
    if not find_sum(coll[i:i+preamble_size], x):
      return x

def part2(coll, invalid):
  for i, x in enumerate(coll):
    window = [x]
    for j, y in enumerate(coll[i + 1:]):
      window.append(y)
      window_sum = sum(window)
      if window_sum == invalid:
        return min(window) + max(window)
      if window_sum < invalid:
        continue
      else:
        break

ans = part1(data, 25)
print(ans)
print(part2(data, ans))