#!/bin/bash
for j in 1..1; do
	for i in {1..5};
	do
    	./run_evolutionary.sh ../data/monks-1.train ../data/monks-1.test
	done
	for a in {1..5};
	do
    	./run_evolutionary.sh ../data/monks-2.train ../data/monks-2.test
	done
	for b in {1..5};
	do
    	./run_evolutionary.sh ../data/monks-3.train ../data/monks-3.test
	done
	for c in {1..5};
	do
	    ./run_evolutionary.sh ../data/mushroom_training.txt ../data/mushroom_testing.txt
	done
	for d in {1..5};
	do
	    ./run_evolutionary.sh ../data/house_votes_training.data ../data/house_votes_testing.data
	done
done
