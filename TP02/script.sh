#!/bin/bash
javac TP02.java
for i in {0..23}
do
	java TP02 < tc_bona/in/$i.txt > tc_bona/myout/$i.txt
done