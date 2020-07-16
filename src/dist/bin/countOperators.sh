#!/bin/bash

# count @operator by asakusa command
# necessary createFlow.sh in same directory
# example
# $ ./countOperators.sh

# prepare
ASAKUSA_CMD=${ASAKUSA_HOME}/bin/asakusa
# count batch
echo "batchNum:"`${ASAKUSA_CMD} list batch|wc -l`
# create title
ope_title="batchName,jobFlowNum"
for ope in @Branch @CoGroup @Convert @Extend @Extract @Fold @GroupSort \
@MasterBranch @MasterCheck @MasterJoin\) @MasterJoinUpdate \
@Project @Restructure @Split @Summarize @Update \
@Logging
do
  ope_title+=,"${ope} "
done
echo ${ope_title}
# create data
for BATCH in `${ASAKUSA_CMD} list batch`
do
  jobflowNum=`${ASAKUSA_CMD} list jobflow ${BATCH}|wc -l`
  ${ASAKUSA_CMD} list operator ${BATCH} > tmpOpeList
  ope_count=""
  for ope in @Branch @CoGroup @Convert @Extend @Extract @Fold @GroupSort \
  @MasterBranch @MasterCheck @MasterJoin\) @MasterJoinUpdate \
  @Project @Restructure @Split @Summarize @Update \
  @Logging
  do
    ope_count+=,`grep "${ope}" tmpOpeList|wc -l`
  done
  echo ${BATCH},${jobflowNum}${ope_count}
done
