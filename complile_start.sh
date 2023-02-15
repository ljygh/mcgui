#! /bin/bash

export CLASSPATH="/home/ljy/Documents/distributed_system_adv/lab/mcgui"

cd multicaster
rm *.class
javac MultiCaster.java
cd ..

java mcgui.Main multicaster.MultiCaster 0 ./multicaster/localhostsetup &
java mcgui.Main multicaster.MultiCaster 1 ./multicaster/localhostsetup &
java mcgui.Main multicaster.MultiCaster 2 ./multicaster/localhostsetup &

# java mcgui.Main lab1example.ExampleCaster 0 ./lab1example/localhostsetup &
# java mcgui.Main lab1example.ExampleCaster 1 ./lab1example/localhostsetup &
# java mcgui.Main lab1example.ExampleCaster 2 ./lab1example/localhostsetup &