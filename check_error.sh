#!/usr/bin/env bash
if [ $(find ./  -maxdepth 1 -name "*error.log" | xargs grep "ERROR" |wc -l) -gt 0 ]; then exit 1; fi

