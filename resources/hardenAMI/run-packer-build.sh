#!/bin/bash

dir_here="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
dir_repo_root="${dir_here}"

python ${dir_repo_root}/packer/build_packer_var_file.py
packer build -var-file ${dir_repo_root}/packer/vars.json ${dir_repo_root}/packer/packer.json
