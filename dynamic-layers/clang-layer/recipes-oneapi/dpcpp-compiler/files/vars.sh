#!/bin/bash
# Intel oneAPI DPC++/C++ Compiler environment setup
# This is a simplified version for Yocto environments

COMPILER_VERSION=2025.3
ONEAPI_ROOT=/opt/intel/oneapi

# Compiler paths
export CMPLR_ROOT=${ONEAPI_ROOT}/compiler/${COMPILER_VERSION}

# Add compiler binaries to PATH
export PATH=${CMPLR_ROOT}/bin:${PATH}

# Library paths
export LD_LIBRARY_PATH=${CMPLR_ROOT}/lib:${LD_LIBRARY_PATH}
export LIBRARY_PATH=${CMPLR_ROOT}/lib:${LIBRARY_PATH}

# Compiler configuration
export CPATH=${CMPLR_ROOT}/include:${CPATH}
