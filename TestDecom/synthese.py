from __future__ import print_function
from pathlib import Path

import re, collections
import lxml.etree as etree


xml = etree.parse('/root/Desktop/Dev/TestDecom/cache/report-analysis/PMD-Report') 
print etree.tostring(x, pretty_print = True)

#xml_root = etree.fromstring(xml)
#raw_tree = etree.ElementTree(xml_root)
#nice_tree = collections.OrderedDict()

"""
for tag in xml_root.iter():
	path = re.sub('\[[0-9]+\]', '', raw_tree.getpath(tag)
	if path not in nice_tree : 
		nice_tree[path] = []
	if len(tag.keys()) > 0 :
		nice_tree[path].extend(attrib for attrib in tag.keys() if attrib not in nice_tree[path])

for path, attribs in nice_tree.items():
	intent = int(path.count('/') -1)
	print('{0}{1}:{2}[{3}]'.format('     ' * indent, indent, path.split('/')[-1], ', '.join(attribs) if len(attribs) > 0 else '-' ))
"""
