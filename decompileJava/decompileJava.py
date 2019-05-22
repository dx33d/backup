#!/usr/bin/env python
#-*- coding : utf-8 -*-

#python decompileAPK.py *.apk/*.dex/*.jar

from __future__ import print_function

import os
import shutil
import subprocess
import zipfile
import argparse
import glob
import sys
import stat
import fnmatch
import re

_ROOT_PATH = os.path.dirname(os.path.abspath(__file__))
_LIBS = os.path.join(_ROOT_PATH, 'libs')

JADX = os.path.join(_LIBS, 'jadx')

CACHE_DIR = os.path.join(_ROOT_PATH, 'cache')

def run(command, print_msg=True):
	if print_msg:
		print(command)
	subprocess.call(command,shell=True)

def make_executable(file):
	if not os.name == 'nt':
		run("chmod a+x %s" % (file), print_msg=False)

def main():
	parser=argparse.ArgumentParser(description='android decompile tool', formatter_class=argparse.ArgumentDefaultsHelpFormatter)
	parser.add_argument('-o', '--output', nargs='?', help='output directory, optional')
	parser.add_argument('file', help='input file path, *.apk/*.dex/*.jar')
	args=parser.parse_args()

	if args.output:
		args.output = os.path.join(os.getcwd(), args.output)
	cache = args.output if args.output else CACHE_DIR

	if not os.path.exists(cache):
		os.mkdir(cache)

	f=os.path.join(os.getcwd(), args.file)
	args.file = f

	print("output dir: %s" % (cache))
	decompile_by_jadx(cache, args)
	print("\nDone")

def jadxpath():
	if os.name == 'nt':
		return os.path.join(JADX, 'bin', 'jadx.bat')
	else :
		return os.path.join(JADX, 'bin', 'jadx')

def decompile_by_jadx(cache, args):
	#jadx has bug when pass .aar file.
	name = os.path.splitext(os.path.basename(args.file))[0]
	cache_path= os.path.join(cache, name)
	if args.file.endswith('.apk'):
		inputs = [args.file]
	make_executable(jadxpath())
	for file in inputs:
			run("%s -d %s -j 8 %s" % (jadxpath(), cache_path, file))

if __name__ == "__main__":
	main()
