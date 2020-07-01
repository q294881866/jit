#!/usr/bin/env bash
if [ $(find ~/logs -name "*.log" | xargs grep "ERROR" |wc -l) -gt 0 ]; then exit 1; fi

