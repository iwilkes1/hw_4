import sys
labels = ['monks-1', 'monks-2', 'monks-3', 'mushroom', 'house']

for file in sys.argv[1:]:
	with open(file, 'r') as in_file:
		with open('.\\output_' + file[2:], 'w') as out_file:
			count = 0
			lines = (line for line in in_file if len(line.split(',')) > 1)
			for line in lines:
				if (count % 5 == 0):
					out_file.write(labels[count/5] + '\n')
				out_file.write(line)
				count += 1
