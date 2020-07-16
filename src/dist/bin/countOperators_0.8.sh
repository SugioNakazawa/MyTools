#!/bin/bash
# count @operator for flowgraph on asakusafw0.8
# {1} top directory(batchc) for search
# example
# $ ./showOperators_0.8.sh  ~/ns/nstt/src/nacs-sales-batch/build/batchc/

echo "total"`find ${1} -name "flowgraph.dot"|xargs grep "label=\"@"|\
awk '{ print $1,$4 }'|sort|uniq -c|wc -l`
# each operators
for ope in @Branch @CoGroup @Convert @Extend @Extract @Fold @GroupSort \
@MasterBranch @MasterCheck @MasterJoin\\\\n @MasterJoinUpdate \
@Project @Restructure @Split @Summarize @Update \
@Logging
do
  echo ${ope},`find ${1} -name "flowgraph.dot"|xargs grep "${ope}"|\
  awk '{ print $1,$4 }'|sort|uniq -c|wc -l`
done

# count Batch
echo "batchNum "`find ${1} -name "original-structure.txt"|xargs grep batch:|wc -l`
