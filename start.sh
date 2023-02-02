#! /bin/bash

java mcgui.Main multicaster.MultiCaster 0 ./multicaster/localhostsetup &
java mcgui.Main multicaster.MultiCaster 1 ./multicaster/localhostsetup &
java mcgui.Main multicaster.MultiCaster 2 ./multicaster/localhostsetup &

# java mcgui.Main lab1example.ExampleCaster 0 ./lab1example/localhostsetup &
# java mcgui.Main lab1example.ExampleCaster 1 ./lab1example/localhostsetup &
# java mcgui.Main lab1example.ExampleCaster 2 ./lab1example/localhostsetup &