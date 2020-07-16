#!/bin/bash

ASAKUSA_CMD=${ASAKUSA_HOME}/bin/asakusa
# batch list
#BATCHS=`${ASAKUSA_CMD} list batch`
OUT=flowfigure
mkdir -p ${OUT}

for BATCH in `${ASAKUSA_CMD} list batch`
do
  echo ${BATCH}
  ${ASAKUSA_CMD} generate dot jobflow -o ${OUT}/${BATCH}.dot ${BATCH}
  dot -Tpdf ${OUT}/${BATCH}.dot > ${OUT}/${BATCH}.pdf

  for JOB in `~/asakusa/bin/asakusa list jobflow ${BATCH}`
  do
    echo jobflow:${JOB}
    ${ASAKUSA_CMD} generate dot operator ${BATCH} -o ${OUT}/${JOB}.dot --jobflow ${JOB}
    dot -Tpdf ${OUT}/${JOB}.dot > ${OUT}/${JOB}.pdf
  done
done
