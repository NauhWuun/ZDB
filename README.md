# ZDB
JAVA K-V DataBase

# TODO
The efficient JAVA language K-V database integrates the btree algorithm and the most efficient cache algorithm. 

# Cache-Oblivious B-Trees
Currently under construction

Eventually, here will emerge an implementation of a Cache-Oblivious B-Tree, that performs efficiently without prior knowledge of the memory hierarchy. Essentially, the main idea is to build a van Emde Boas layout on top of a Packed Memory Array. The result is a binary search algorithm that takes advantage of cache locality and minimizes the amount of external memory reads.

Sit back, enjoy a cup of coffee and maybe have a look at some links on the topic:

The papers that sparked my interest in the topic: here and here
A paper on Adaptive Packed Memory Arrays (or packed memory arrays on steroids)
Or an MIT lecture on the topic given by the one and only Erik Demaine. Highly recommended! This guy is a legend.

CO-BTree Author: https://github.com/lodborg/cache-oblivious-btree
