# FindFiles

CS349 - Assignment 1 by Rafit Jamil (rf2jamil)

## Description

FindFiles is a CLI File Search Utility that will find files given options and parameters, outputting the absolute file paths of the found files.

## Usage
```bash
java FindFiles filetofind [-option argument1 argument2 ...]
```

## Options
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

```
  -help                     :: print out a help page and exit the program.
  -reg                      :: find files using [filietofind] argument as a regular expression.
  -r                        :: execute the command recursively in subfiles.
  -dir [directory]          :: find the files the specified directory [directory]. Default directory is the calling directory.
  -ext [ext1,ext2,...]      :: find the files matching [filetofind] and with the given extensions [ext1, ext2,...]
```

## Notes
1. Filename (`fileToFind`) cannot begin with `-`
2. All options must start with `-` 
3. `-help` will override all other commands
4. Will report first error if multiple errors in command found
5. Options parameters must be passed as a comma-separated list
6. Options will be overridden with the last declared version of the option if declared multiple times

## Submission Details
 Rafit Jamil\
 20785154 rf2jamil\
 openjdk 11.0.7 2020-04-14\
 macOS 10.15.4 (MacBook Pro 2019)
