from __future__ import print_function
import os
import subprocess
import platform
from os.path import join
import sys
import argparse
from roadmaptools import osmtogeojson


# print to STDERR
def err_print(*args, **kwargs):
    pass
    print(*args, file=sys.stderr, **kwargs)


# parser of configuration file
def get_all_params_osmfilter(file):
    loc_commands = ""
    for line in file:
        if line[0] != "#":
            loc_commands += line.replace("\n", " ")
    return loc_commands


# download osmfilter based on current system
def osmfilter_downloader(url_adress):
    try:
        subprocess.call(["wget", url_adress])
    except OSError as e:
        if e.errno == os.errno.ENOENT:
            err_print("wget not found!\nplease, install it, it's available both Linux and Windows")  # handle file not found error
        else:
            raise  # something else went wrong while trying to run `wget`


# check whether osmfilter exists, else download it
def check_osmfilter(osmfilter_version, is_linux, mapfile):
    if os.path.exists(osmfilter_version):
        with open("config", mode='r') as f:
            args = get_all_params_osmfilter(f)
        f.close()
        if is_linux:
            command = "./osmfilter {} {} > ../data/output.osm".format(mapfile, args)
        else:
            command = "osmfilter.exe {} {} > ../data/output.osm".format(mapfile, args)
        # print(command) #check what is executed
        os.system(command)
    else:
        err_print("downloading osmfilter...")
        if is_linux:
            os.system("wget -O - http://m.m.i24.cc/osmfilter.c |cc -x c - -O3 -o osmfilter")
            check_osmfilter("osmfilter", is_linux, mapfile)
        else:
            osmfilter_downloader("m.m.i24.cc/osmfilter.exe")
            check_osmfilter("osmfilter.exe", is_linux, mapfile)


def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('MAP', type=str, action='store', help='input map in OSM format')
    parser.add_argument('--version', action='version', version='%(prog)s 0.1.2')
    parser.add_argument('-r', action='store_true', default=False, dest='delete', help='remove all temporary files')
    return parser.parse_args()


def configure_and_download_dependecies():
    my_platform = platform.system()  # get system info

    results = get_args()

    if not os.path.exists(results.MAP):
        err_print("{} doesn't exist!".format(results.MAP))
        exit(1)

    if not os.path.isdir("../data"):  # make directory data if and only if doesn't exist
        os.makedirs("../data")
        err_print("creating folder...")

    if os.path.exists("../data/log.log"):  # remove log file with Google Maps errors
        os.remove("../data/log.log")

    err_print("cleaning OSM data...")
    if my_platform == "Linux":  # check if osmfilter is downloaded
        check_osmfilter("osmfilter", True, results.MAP)
    elif my_platform == "Windows":
        check_osmfilter("osmfilter.exe", False, results.MAP)

    err_print("converting OSM to geoJSON...")

    geojson_file = osmtogeojson.convert_osmtogeojson("../data/output.osm")
    f = open("../data/output.geojson", "w")
    osmtogeojson.save_geojson(geojson_file, f)
    f.close()


def remove_temporary_files():
    results = get_args()
    if results.delete == True:
        err_print("removing temporary files...")
        os.remove("../data/output.osm")
        os.remove("../data/output.geojson")
        os.remove("../data/output-result.geojson")
        os.remove("../data/output-cleaned.geojson")
        os.remove("../data/output-simplified.geojson")
        os.remove("../data/output-speeds.geojson")
        os.remove("../data/output-curvatures.geojson")


def remove_pyc_files():
    working_dir = os.path.dirname(os.path.abspath(__file__))  # remove temporary python files
    files = os.listdir(working_dir)

    for item in files:
        if item.endswith(".pyc"):
            os.remove(join(working_dir, item))
