from __future__ import print_function
from utils import err_print
import requests
import re
import os
import bz2file
import time
import subprocess
import argparse


def create_filename(name):
    name.replace(" ","_")
    return name.lower()

def map_downloader(url, filename):
    try:
        subprocess.call(["wget", "-O", "../maps/" + filename + ".osm.bz2", url, "--no-check-certificate"])
    except OSError as e:
        if e.errno == os.errno.ENOENT:
            err_print("wget not found!\nplease, install it, it's available both Linux and Windows")  # handle file not found error
        else:
            raise  # Something else went wrong while trying to run `wget`


def substring_after(s, delim):
    return s.partition(delim)[2]


URL_BASE = 'https://mapzen.com'
url = URL_BASE + '/data/metro-extracts/'

responce = requests.get(url)

list_of_content = [line for line in responce.iter_lines()]

parser = argparse.ArgumentParser()
parser.add_argument('city', nargs='?', default="Prague",type=str,help="name of city to download")
parser.add_argument('--version', action='version', version='%(prog)s 0.1.2')
arg = parser.parse_args()

my_city = arg.city

all_cities = []
for line in list_of_content:
    if "class=\"city\"" in line:
        if not os.path.isdir("../maps"):  # make directory maps if and only if doesn't exist
            os.makedirs("../maps")
        line = re.split("<|>", line)
        all_cities.append(line[-3])
        if line[-3] == my_city:
            city_url = substring_after(line[1], "href=")
            url = URL_BASE + city_url.replace("\"", "")

            responce = requests.get(url)
            list_of_content = [line for line in responce.iter_lines()]
            for line in list_of_content:
                if "OSM XML" in line:
                    line = re.split(" |<|>", line)  # cut string into list
                    err_print("size:", line[-5])  # size in MB
                    downloading_page = substring_after(line[11], "=")  # http download
                    filename = create_filename(my_city)
                    map_downloader(downloading_page.replace("\"", ""), filename)

                    err_print("unpacking...")
                    start_time = time.time()
                    bz_file = bz2file.open("../maps/" + filename + ".osm.bz2")
                    with open("../maps/" + filename + ".osm", "w") as out:  # decompress bz2 file
                        out.write(bz_file.read())
                    out.close()
                    err_print("time:", (time.time() - start_time))
                    exit()

err_print("spell your city correctly, or choose another one from this list: {}".format(sorted(all_cities)))
