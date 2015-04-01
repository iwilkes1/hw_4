'''
Created on Mar 25, 2015
script to split data files into training and testing files.
The split will have 30% of the lines going to the test set,
and the remaining 70% going to the testing set.
The names of the sets are specified as command line arguments,
with the input file being the first argument, then the training
data, and finally the testing data.
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