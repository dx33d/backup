""""""""""""""""""""""""""""""""""
//////////////////////////////////
Author : Amine Chentouf
Python version : 3.7
Description du script : 
Utilise les outils JADX et dex2jar
pour décompiler l'APK sous des formats
différents, dans des dossiers différents
pour que les différents outils qui analyse
le code source puisse avoir l'APK sous
le format qu'ils traitent le mieux.
/////////////////////////////////
"""""""""""""""""""""""""""""""""
from __future__ import print_function

import os
import shutil
import subprocess
import zipfile
import glob
import sys
import stat
import fnmatch
import re


_ROOT_PATH = os.path.dirname(os.path.abspath(__file__))
_LIBS = os.path.join(_ROOT_PATH, 'libs')
DEX2JAR = os.path.join(_LIBS, 'dex2jar')
JADX = os.path.join(_LIBS, 'jadx')
CACHE_DIR = os.path.join(_ROOT_PATH, 'cache')
_NEED_UNZIP_FILES = ['patch.jar']

def run(command, print_msg=True):
	if print_msg:
		print(command)
	subprocess.call(command,shell=True)

def make_executable(file):
	if not os.name == 'nt':
		run("chmod a+x %s" % (file), print_msg=False)

def clean_temp_error_files():
	#when dex2jar decompile error, some error file generated 
	files = glob.glob("%s/classes?-error.zip" % (_ROOT_PATH))
	for file in files:
			os.remove(file)

def decompile(path_file):
	
	f = path_file
	cache = CACHE_DIR

	if os.path.abspath(cache) == os.path.abspath(CACHE_DIR):
		print("clearing cache...")
		rmtree(CACHE_DIR)
		print("clear cache done")
	if not os.path.exists(cache):
		os.mkdir(cache)

	print("output dir: %s" % (cache))

	decompile_by_jadx(cache, f)

	decompile_by_dex2jar(cache,f)
	clean_temp_error_files()

	print("\nDone")

def readonly_handler(func, path, execinfo):
	os.chmod(path, stat.S_IWRITE)
	func(path)

def rmtree(path):
	if os.path.exists(path):
		shutil.rmtree(path,onerror=readonly_handler)

def jadxpath():
	return os.path.join(JADX, 'bin', 'jadx')

def decompile_by_jadx(cache, fichier):
	if fichier.endswith('.apk'):
		inputs = [fichier]
	else:
		inputs = dex2jar(cache,fichier)
	make_executable(jadxpath())
	for file in inputs:
			run("%s -d %s/JADX -j 8 %s" % (jadxpath(), cache, file))
			print("Files are ready for PMD analysis")

def dex2jar(cache, fichier):
	cmd = os.path.join(DEX2JAR, 'd2j-dex2jar.sh')
	run("chmod a+x %s" % (cmd))
	dexes =[]
	jars= []
	cache_dex=os.path.join(cache,'dex2jar')
	temp_dir= os.path.abspath(os.path.join(cache_dex, os.path.splitext(os.path.basename(fichier))[0]))
	if fichier.endswith(".apk") or fichier in _NEED_UNZIP_FILES:
		print("unzip %s..." % (fichier))
		with zipfile.ZipFile(fichier, 'r') as z:
			z.extractall(temp_dir)
		dexes = [
			os.path.join(temp_dir, dex) for dex in os.listdir(temp_dir)
			if dex.endswith('.dex')
		]
		print("founded dexes: " + ', '.join(dexes))
		for dex in dexes : 
			dest = os.path.splitext(dex)[0] + "-dex2jar.jar"
			run("%s -f %s -o %s" % (cmd, dex, dest))
			if not os.path.exists(dest):
				print("\n%s decompile failed!\n" % (dest))
			else:
				jars.append(dest)
	elif fichier.endswith(".dex"):
		dest = os.path.join(temp_dir, "classes-dex2jar.jar")
		print(dest)
		run("%s -f %s -o %s" % (cmd, fichier, dest))
		if not os.path.exists(dest):
			print("%s decompile failed!" % (dest))
		else:
			jars.append(dest)
	elif fichier.endswith(".jar"):
		jars.append(fichier)
	else:
		print("error file extension!")
	print(dest)
	resdest = os.path.join(cache_dex, 'res')
	print("when decompile resources done, store path: %s" % (resdest))
	return jars

def decompile_by_dex2jar(cache, fichier):
	jars = dex2jar(cache,fichier)
	if jars :
		print("Files are ready for Spotbugs analysis")
