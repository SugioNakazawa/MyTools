#!/bin/bash -eu

ASAKUSA_MOD=${ASAKUSA_HOME}/bin/asakusa

echo "----------------------------------------------------------------------------------------------------"
for BATCH in `${ASAKUSA_MOD} list batch`
do
  echo "BatchName ${BATCH}"
  echo "--- importer ---------------------------------------------------------------------------------------"
  ${ASAKUSA_MOD} list directio input -v ${BATCH}
  echo "----------------------------------------------------------------------------------------------------"
done
