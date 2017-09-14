from __future__ import print_function
import time
import codecs
from utils import err_print
from roadmaptools import clean_geojson, simplify_graph, estimate_speed_from_osm, calculate_curvature, export_nodes_and_id_maker, prepare_geojson_to_agentpolisdemo


def get_cleaned_geojson(input_filename,output_filename):
    err_print("cleaning geoJSON...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = clean_geojson.load_geojson(input_stream)
    geojson_out = clean_geojson.get_cleaned_geojson(geojson_file)
    clean_geojson.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    err_print("time: %s secs\n" % (time.time() - start_time))


def get_simplified_geojson(input_filename,output_filename):
    err_print("simplifying geoJSON...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    # l_check set True whether you don't want to simplify edges with different number of lanes
    # c_check set True whether you don't want to simplify edges with different curvature
    geojson_file = simplify_graph.load_geojson(input_stream)
    geojson_out = simplify_graph.get_simplified_geojson(geojson_file,l_check=False, c_check=False)
    simplify_graph.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    err_print("time: %s secs\n" % (time.time() - start_time))


def get_speed_from_osm(input_filename,output_filename):
    err_print("getting speed from OSM...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = estimate_speed_from_osm.load_geojson(input_stream)
    geojson_out = estimate_speed_from_osm.get_geojson_with_speeds(geojson_file)
    estimate_speed_from_osm.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    err_print("time: %s secs\n" % (time.time() - start_time))


def get_curvature_of_edges(input_filename,output_filename):
    err_print("getting curvature from coordinates...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = calculate_curvature.load_geojson(input_stream)
    geojson_out = calculate_curvature.get_geojson_with_curvature(geojson_file)
    calculate_curvature.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    err_print("time: %s secs\n" % (time.time() - start_time))


def get_ids(input_filename,output_filename):
    err_print("generating ids...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = calculate_curvature.load_geojson(input_stream)
    points = export_nodes_and_id_maker.export_points_to_geojson(geojson_file)
    # if you want, you can save points here: export_nodes_and_id_maker.save_geojson(points,file_stream)
    geojson_out = export_nodes_and_id_maker.get_geojson_with_unique_ids(geojson_file)
    export_nodes_and_id_maker.save_geojson(geojson_out, output_stream, is_formated=False)
    input_stream.close()
    output_stream.close()

    err_print("time: %s secs\n" % (time.time() - start_time))


def get_agentpolisdemo_files(input_filename,output_filename_edges,output_filename_nodes):
    err_print("preparing files for agentpolis-demo...")
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream_edges = open(output_filename_edges, 'w')
    output_stream_nodes = open(output_filename_nodes, 'w')

    geojson_file = prepare_geojson_to_agentpolisdemo.load_geojson(input_stream)
    geojson_list_out = prepare_geojson_to_agentpolisdemo.get_nodes_and_edges_for_agentpolisdemo(geojson_file)
    prepare_geojson_to_agentpolisdemo.save_geojson(geojson_list_out[0], output_stream_edges)
    prepare_geojson_to_agentpolisdemo.save_geojson(geojson_list_out[1], output_stream_nodes)
    input_stream.close()
    output_stream_edges.close()
    output_stream_nodes.close()

    err_print("time: %s secs" % (time.time() - start_time))
