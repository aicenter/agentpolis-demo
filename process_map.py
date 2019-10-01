from __future__ import print_function
import argparse
import os
import subprocess
import time
import codecs
import sys
import platform
import roadmaptools.clean_geojson
import roadmaptools.compute_edge_parameters
from roadmaptools import simplify_graph, estimate_speed_from_osm, calculate_curvature, \
    export_nodes_and_id_maker, prepare_geojson_to_agentpolisdemo, download_map, inout
from roadmaptools import osmtogeojson


def _save_map_for_ap():
    print_info('Preparing files for agentpolis-demo... ', end='')
    start_time = time.time()

    geojson_file = roadmaptools.inout.load_geojson(os.path.join(args.OUTDIR, 'output-parameters.geojson'))
    edges, nodes = roadmaptools.prepare_geojson_to_agentpolisdemo.get_nodes_and_edges_for_agentpolisdemo(geojson_file)
    roadmaptools.inout.save_geojson(nodes, os.path.join(args.OUTDIR, 'nodes.geojson'))
    roadmaptools.inout.save_geojson(edges, os.path.join(args.OUTDIR, 'edges.geojson'))

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def print_info(*args, **kwargs):
    pass
    print(*args, **kwargs)


def get_args():
    parser = argparse.ArgumentParser()
#    parser.add_argument('MAP', type=str, action='store', help='input map in OSM format')
    parser.add_argument('OUTDIR', type=str, action='store', help='the directory with resulting geojson files')
    parser.add_argument('--version', action='version', version='%(prog)s 0.1')
    return parser.parse_args()


if __name__ == '__main__':
    overall_start_time = time.time()
    print_info('Map preprocessing started.\n')

    # parse arguments
    args = get_args()

    # check if the data folder exists, otherwise create the folder
    if not os.path.isdir(args.OUTDIR):
        os.makedirs(args.OUTDIR)
        print_info('Created directory %s.' % args.OUTDIR)

    # 1 download the map
    roadmaptools.download_map.download_cities([(49.94, 14.22, 50.17, 14.71), (49.11, 16.42, 49.30,16.72)],
                                              os.path.join(args.OUTDIR, 'output.geojson'))

    # 2 cleanup
    roadmaptools.clean_geojson.clean_geojson_files(os.path.join(args.OUTDIR, 'output.geojson'),
                                                   os.path.join(args.OUTDIR, 'output-cleaned.geojson'))

    # 3 simplification
    roadmaptools.simplify_graph.simplify_geojson(os.path.join(args.OUTDIR, 'output-cleaned.geojson'),
                                                 os.path.join(args.OUTDIR, 'output-simplified.geojson'))

    # 4 reduction to single component
    roadmaptools.sanitize.sanitize(os.path.join(args.OUTDIR, 'output-simplified.geojson'),
                                   os.path.join(args.OUTDIR, 'output-single_component.geojson'))

    # 5 compute edge parameters
    roadmaptools.compute_edge_parameters.compute_edge_parameters(
        os.path.join(args.OUTDIR, 'output-single_component.geojson'),
        os.path.join(args.OUTDIR, 'output-parameters.geojson'))

    # 6 finalization: split to node and edges, node id and index generation
    _save_map_for_ap()

    # remove temporary files
    #output.osm
    for f in ['output-cleaned.geojson', 'output-simplified.geojson',
              'output-single_component.geojson', 'output-parameters.geojson']:
        os.remove(os.path.join(args.OUTDIR, f))

    print_info('\nProcessing finished in %.2f secs.' % (time.time() - overall_start_time))
