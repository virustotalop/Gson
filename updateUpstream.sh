#!/bin/bash
git remote add upstream https://github.com/google/gson.git
git fetch upstream
git checkout main
git merge upstream/main
git remote rm upstream