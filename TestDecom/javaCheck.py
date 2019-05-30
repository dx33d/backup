""""""""""""""""""""""""""""""""""
//////////////////////////////////
Author : Amine Chentouf
Python version : 3.7
Description du script : 
Utilise les outils PMD & Spotbugs
pour parcourir et analyser le code
source de l'APK et retourner des
rapports concernant les différentes
vulnérabilités repéré.
/////////////////////////////////
"""""""""""""""""""""""""""""""""

from __future__ import print_function
from pathlib import Path

import os
import shutil
import atexit
import tempfile
import subprocess
import glob

_ROOT_PATH = os.path.dirname(os.path.abspath(__file__))
_LIBS = os.path.join(_ROOT_PATH, 'libs')
PMD = os.path.join(_LIBS, 'pmd')
SPOTBUGS = os.path.join(_LIBS, 'spotbugs')
CACHE_DIR = os.path.join(_ROOT_PATH, 'cache')
java_dir = os.path.join(CACHE_DIR, 'JADX')
jar_dir = os.path.join(CACHE_DIR, 'dex2jar')

def run(command, print_msg=True):
	if print_msg:
		print(command)
	subprocess.call(command,shell=True)

def make_executable(file):
	if not os.name == 'nt':
		run("chmod a+x %s" % (file), print_msg=False)	

def readable_dir(prospective_dir):
	if not os.path.isdir(prospective_dir):
		raise Exception("readable_dir:{0} is not a valid path".format(prospective_dir))
	if os.access(prospective_dir, os.R_OK):
		return prospective_dir
	else:
		raise Exception("readable_dir:{0} is not a readable directory".format(prospective_dir))

def findjar(jar_dir, rejar):
	for jars in Path(jar_dir).glob(rejar):
		return jars
	
def javaCheck():
	cache=os.path.join(CACHE_DIR, 'report-analysis')
	rejar='**/*.jar'
	if readable_dir(java_dir):
		print("\n C'est un bon repertoire")
		analyse_pmd(cache, java_dir)
	else : 
		print("\n C'est pas un bon repertoire")

	jars_path = findjar(jar_dir, rejar)
	jars = str(jars_path)
	analyse_spotbugs(cache,jars)
	print("\nDone")

def pmd_path():
	return os.path.join(PMD, 'bin', 'run.sh')

def spotbugs_path():
	return os.path.join(SPOTBUGS, 'bin', 'spotbugs')

def analyse_pmd(cache, java_dir):
	if readable_dir(java_dir):
		inputs = [java_dir]
	make_executable(pmd_path())	
	report_file = os.path.join(cache,'PMD-Report')
	run("%s pmd -dir %s -f xml -R rulesets/java/android.xml,category/java/errorprone.xml -language java -r %s -debug" % (pmd_path(), java_dir, report_file))


def analyse_spotbugs(cache,jars):
	if jars.endswith('.jar'):
		inputs = [jars]
	make_executable(spotbugs_path())
	report_file = os.path.join(cache, 'Spotbugs-Report')
	for file in inputs:
		run("%s -output %s -xml:withMessages -textui %s" % (spotbugs_path(), report_file, file))
