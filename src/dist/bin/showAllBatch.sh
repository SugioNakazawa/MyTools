#!/bin/bash -eu

ASAKUSA_MOD=${ASAKUSA_HOME}/bin/asakusa

echo "----------------------------------------------------------------------------------------------------"
for BATCH in `${ASAKUSA_MOD} list batch`
do
  echo "BatchName:${BATCH}"
  echo "--- parameters -------------------------------------------------------------------------------------"
  for PARAM in `${ASAKUSA_MOD} list parameter ${BATCH}`
  do
    echo "param:${PARAM}"
  done
  echo "--- jobs -------------------------------------------------------------------------------------------"
  for JOB in `${ASAKUSA_MOD} list jobflow ${BATCH}`
  do
    echo "job:${JOB}"
  done
  echo "--- importers --------------------------------------------------------------------------------------"
  ${ASAKUSA_MOD} list directio input ${BATCH}
  echo "--- exporters --------------------------------------------------------------------------------------"
  ${ASAKUSA_MOD} list directio output ${BATCH}
  echo "----------------------------------------------------------------------------------------------------"
done
