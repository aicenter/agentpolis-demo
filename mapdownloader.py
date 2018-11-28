# from __future__ import print_function
# import requests
# import re
# import os
# import bz2file
# import time
# import subprocess
# import argparse
# import sys
#
#
# def print_info(*args, **kwargs):
#     pass
#     print(*args, **kwargs)
#
#
# def print_err(*args, **kwargs):
#     pass
#     print(*args, file=sys.stderr, **kwargs)
#
#
# def create_filename(name):
#     name.replace(" ", "_")
#     return name.lower()
#
#
# def map_downloader(url, filename):
#     try:
#         subprocess.call(["wget", "-O", filename, url, "--no-check-certificate"])
#     except OSError as e:
#         if e.errno == os.errno.ENOENT:
#             print_err("wget not found!\nplease, install it, it's available both Linux and Windows")  # handle file not found error
#         else:
#             raise  # Something else went wrong while trying to run `wget`
#
#
# def substring_after(s, delim):
#     return s.partition(delim)[2]
#
#
# if __name__ == '__main__':
#
#     parser = argparse.ArgumentParser()
#     parser.add_argument("-city",dest="city", nargs='?', default="Prague", type=str, help="name of city to download")
#     parser.add_argument('OUTDIR', type=str, action='store', help='the directory with resulting map files')
#     parser.add_argument('--version', action='version', version='%(prog)s 0.2')
#     args = parser.parse_args()
#
#     URL_BASE = 'https://mapzen.com'
#     url = URL_BASE + '/data/metro-extracts/'
#
#     response = requests.get(url)
#
#     list_of_content = [line for line in response.iter_lines()]
#
#     my_city = args.city
#
#     all_cities = []
#     for line in list_of_content:
#         if "class=\"city\"" in line:
#             if not os.path.isdir(args.OUTDIR):  # make directory maps if and only if doesn't exist
#                 os.makedirs(args.OUTDIR)
#
#             line = re.split("<|>", line)
#             all_cities.append(line[-3])
#
#             if line[-3] == my_city:
#                 city_url = substring_after(line[1], "href=")
#                 url = URL_BASE + city_url.replace("\"", "")
#                 response = requests.get(url)
#                 list_of_content = [line for line in response.iter_lines()]
#
#                 for l in list_of_content:
#                     if "OSM XML" in l:
#                         l = re.split(" |<|>", l)  # cut string into list
#                         print_info("size:", l[-5])  # size in MB
#                         downloading_page = substring_after(l[11], "=")  # http download
#                         filename = create_filename(my_city)
#                         compress_filename = filename + ".osm.bz2"
#                         map_downloader(downloading_page.replace("\"", ""), os.path.join(args.OUTDIR, compress_filename))
#
#                         print_info("Unpacking... ", end='')
#                         start_time = time.time()
#                         bz_file = bz2file.open(os.path.join(args.OUTDIR, compress_filename))
#                         with open(os.path.join(args.OUTDIR, filename + ".osm"), "w") as out:  # decompress bz2 file
#                             out.write(bz_file.read())
#                         out.close()
#                         print_info('done. (%.2f secs)' % (time.time() - start_time))
#                         exit(0)
#
#     print_err("Make sure you spelled the name of metropolitan area correctly.")
#     print_err("These are the available cities: %s" % sorted(all_cities))
import roadmaptools.inout
import overpass

from typing import Tuple, List


HIGHWAY_FILTER = 'highway~"(motorway|motorway_link|trunk|trunk_link|primary|primary_link|secondary|secondary_link|tertiary|tertiary_link|unclassified|unclassified_link|residential|residential_link|living_street)"'


def download_cities(bounding_boxes: List[Tuple[float, float, float, float]], filepath: str):
	api = overpass.API(debug=True)
	query = '(('

	for bounding_box in bounding_boxes:
		query += 'way({})[{}][access!="no"];'.format(",".join(map(str, list(bounding_box))), HIGHWAY_FILTER)

	query += ')->.edges;.edges >->.nodes;);'
	out = api.get(query, verbosity='geom')
	roadmaptools.inout.save_geojson(out, filepath)


if __name__ == '__main__':
	download_cities([(49.94, 14.22, 50.17, 14.71), (49.11, 16.42, 49.30,16.72)], "test.geojson")