#!/bin/bash

dstat -tcmdns --output dstat_`date "+%Y%m%d_%H%M%S"`.log
