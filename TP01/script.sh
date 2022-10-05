javac TP01.java

for tc in *.txt
do
	echo $tc
	output=`echo $tc | sed 's/\.txt$/_mine.txt/'`
	java TP01 < $tc > mine/$output
done