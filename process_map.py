import agentpolis.init as ap_init
import agentpolis.prepare_map as ap_map
import os

def setup_config_demo():
	config = ap_init.config
	working_dir = os.getcwd()
	config.data_dir = working_dir + '/'
	config.map_dir = config.data_dir + 'data/'	
	config.raw_filepath = config.map_dir + 'map_raw.geojson'
	config.simplified_filepath = config.map_dir + 'map_simplified.geojson'
	config.cleaned_filepath = config.map_dir + 'map_cleaned.geojson'
	config.sanitized_filepath = config.map_dir + 'map_single_component.geojson'
	config.file_with_computed_parameters_filepath =  config.map_dir + 'map_final.geojson'
	config.nodes_filepath = config.map_dir + 'nodes.geojson'
	config.edges_filepath =  config.map_dir + 'edges.geojson'
	config.adj_matrix_filepath =  config.data_dir + '/adj.csv'
	config.dm_filepath =  config.data_dir + '/dm.csv'
	config.station_locations_filepath = config.data_dir + '/station_positions.csv'
	return config


if __name__ == '__main__':
	ap_map.config = setup_config_demo()
	ap_map._prepare_map()
