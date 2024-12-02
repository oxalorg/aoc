with open('resources/2023/1.txt', 'r') as f:
    lines = f.readlines()

lines = list(map(lambda x: x.strip(), lines))

code_1 = ord('1')
code_9 = ord('9')
cal_sum = 0

for line in lines:
    first = None
    last = None
    for c in line:
        if code_1 <= ord(c) <= code_9:
            if first:
                last = c
            else:
                first = c

    if last is None:
        last = first
    calibration = int(first + last)
    print(calibration)
    cal_sum += calibration

print(cal_sum)
