# Agentpolis Demo Application

This repository contains an example simulation that demonstrates how can be Agentpolis used to simulate urban transportation scenarios. First, we demonstrate how to convert raw openstreetmap data, preprocess them, and convert them to geojson in Python. Then, create a simple Java application that simulates and visualizes movement of several vehicles over the road network spcified in the input geojson files. 

### Prerequisites

This repository contains code in Python and Java. We will need pip, virtualenv (optional), maven, and wget. If you do not have those installed already, here is a guide [how to install prerequisities](https://github.com/aicenter/agentpolis-demo/wiki/Installing-prerequisities).


## Examples of usage

Go to folder python_scripts and type

map_downloader.py (optional)

```bash
    path/to/python2.7/interpreter map_downloader.py "Hanoi" # or another city (there are almost 200 cities all around the world)
```

preprocess_map.py

```bash
    path/to/python2.7/interpreter preprocess_map.py path/to/OSMfile.osm
```

go back to parent directory then clean, build and run visualisation

```bash
    mvn -X clean install exec:java -Dexec.mainClass="cz.cvut.fel.aic.agentpolis.demo.OnDemandVehiclesSimulation"
```
!FILES CREATED BY SCRIPTS ARE SAVED IN ROOT DIRECTORY OF PROJECT!

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [GitHub](https://github.com) for versioning. For the versions available, see the [tags on this repository](https://github.com/aicenter/agentpolis-demo/tags). 

## Authors

* **David Fiedler** - *Initial work*
* **Martin Korytak** - *Pythons' scripts and random demand*

See also the list of [contributors](https://github.com/aicenter/agentpolis-demo/graphs/contributors) who participated in this project.

## License

This project is licensed under the APACHE 2.0 - see the [LICENSE.txt](LICENSE.txt) file for detail.


