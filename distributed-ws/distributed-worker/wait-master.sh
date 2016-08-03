##!/bin/bash
#
#set -e
#
#host="$1"
#shift
#cmd="$@"
#
#until psql -h "$host" -U "master" -c '\l'; do
#  >&2 echo "Master is unavailable - sleeping"
#  sleep 1
#done
#
#>&2 echo "Master is up - executing command"
#exec $cmd