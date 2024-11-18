#!/usr/bin/env bash
#
# Simple react bash script for building front end

export BASE_DIR=${PWD}
echo "BASE_DIR : ${BASE_DIR}"
export SRC_REACT="${BASE_DIR}/src/main/react"
echo "SRC_REACT : ${SRC_REACT}"
cd ${SRC_REACT}
npm install --legacy-peer-deps
npm run build

