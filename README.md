# agentpolis-demo

Simulation of traffic in real cities with visualisation.


### Prerequisites

We will work with [pip](https://pypi.python.org/pypi/pip), [wget](https://www.gnu.org/software/wget/), [maven](https://maven.apache.org/) and [virtualenv](https://virtualenv.pypa.io/en/stable/)(optional). You can follow steps below to install and run it properly.

### Installing

Use

```
python get-pip.py
```

for installing pip, 

```
sudo apt-get install maven
```

for installing maven and

```
sudo pip install virtualenv
```

for virtualenv or eventually on Windows use


```
pip install virtualenv
```

If you are running in Linux, type


```
mkdir ~/virtualEnvironment && cd ~/virtualEnvironment
```

then clone this GitHub project

```
git clone https://github.com/aicenter/agentpolis-demo.git
```
or clone with ssh
```
git clone git@github.com:aicenter/agentpolis-demo.git
```

and finally use

```
virtualenv --python path/to/python2.7/interpreter --no-site-packages --distribute .env && source .env/bin/activate
```

where you subtitute path/to/python2.7/interpreter with your own path to python 2.7 interpreter to create isolated Python environment.

## Examples of usage

Go in folder python_scripts and type

map_downloader.py (optional)

```bash
    path/to/python2.7/interpreter map_downloader.py "Hanoi" # or another city (there are almost 200 cities all around the world)
```

run_demo.py

```bash
    path/to/python2.7/interpreter run_demo.py path/to/OSMfile.osm
```

clean, build and run visualisation

```bash
    mvn -X clean install exec:java -Dexec.mainClass="cz.cvut.fel.aic.agentpolis.demo.OnDemandVehiclesSimulation"
```

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


