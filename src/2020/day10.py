def part1(jolts):
  jolts.append(0)
  jolts = sorted(jolts)
  diff_one = 0
  diff_three = 1
  for i in range(len(jolts)-1):
    diff = jolts[i + 1] - jolts[i]
    if diff == 1:
      diff_one += 1
    elif diff == 3:
      diff_three += 1
  print(diff_one, diff_three)
  return diff_one * diff_three

sol = {}    
      
def find_sol(jolts, i):
  jlen = len(jolts)
  if i == jlen - 1:
    return 1
  if i >= jlen:
    return 0
  if sol.get(i, None):
    return sol[i]
  ans = 0
  # print(i, jlen)
  if jolts[i + 1] - jolts[i] <= 3:
    ans += find_sol(jolts,i + 1)
  if i+2 < jlen and jolts[i + 2] - jolts[i] <= 3:
    ans += find_sol(jolts,i + 2)
  if i+3 < jlen and jolts[i + 3] - jolts[i] <= 3:
    ans += find_sol(jolts, i + 3)
  sol[i] = ans
  return ans


def part2(jolts):
  jolts.append(max(jolts) + 3)
  jolts.append(0)
  # jolts = reversed(sorted(jolts))
  print(jolts)
  return find_sol(sorted(jolts), 0)

if __name__ == "__main__":  
  with open("./resources/input10.txt", "r") as f:
    data = f.readlines()
  data = [int(x) for x in data]
  # ans = part1(data)
  # print(ans)
  ans = part2(data)
  print(ans)