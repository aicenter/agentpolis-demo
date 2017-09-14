from __future__ import print_function
from python_scripts import get_cleaned_geojson,get_simplified_geojson,get_speed_from_osm,get_curvature_of_edges,get_ids,get_agentpolisdemo_files
from utils import configure_and_download_dependecies, remove_temporary_files, remove_pyc_files,err_print
import time

start_time = time.time()
err_print("starting script..")

configure_and_download_dependecies()

# run pipeline...
err_print("starting pipeline...\n")
get_cleaned_geojson("data/output.geojson","data/output-cleaned.geojson")  # remove all unused features from map
get_simplified_geojson("data/output-cleaned.geojson","data/output-simplified.geojson")  # simplify edges of graph, (optional) 2.param=simplification lanes and 3.param=simplification curvature
get_speed_from_osm("data/output-simplified.geojson","data/output-speeds.geojson")  # get speed from OSM data, if missing use heuristic
get_curvature_of_edges("data/output-speeds.geojson","data/output-curvatures.geojson")  # add avarage value of curvature in single edge
get_ids("data/output-curvatures.geojson","data/output-result.geojson")  # extract all intersections of edges in file,validation of geojson file, (optional) 2.param=formated output file
get_agentpolisdemo_files("data/output-result.geojson","data/edges.geojson","data/nodes.geojson") # get the biggest strongly connected component and create DiGraph from MultiDiGraph (without losing any edge)

# if sys.argv[-1] == '-r':  # removing temporary files
remove_temporary_files()  # if it is required ("-r" argument)

remove_pyc_files()

err_print("finished in time: {}".format(time.time() - start_time))
