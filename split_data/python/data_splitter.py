'''
Created on Mar 25, 2015

@author: Ian Wilkes
'''
import sys
import random


with open(sys.argv[1], 'r') as input_file:
    with open(sys.argv[2], 'w+') as training_file:
        with open(sys.argv[3], 'w+') as testing_file:
            for line in input_file:
                if random.random() >= 0.3:
                    training_file.write(line)
                else:
                    testing_file.write(line)

print "finished!"