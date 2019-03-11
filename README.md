# Java Assignment 1

# Write a program to simulate a bus traveling between 5 different stations and repeats the cycle, the bus can take up to a maximum of 50 persons, at each station random number of persons get off the bus and random number of persons get on the bus, consider these cases

- If bus does not have enough space for all persons, persons will have to
stay in station for next cycle
- Persons cannot mount on bus until persons on bus dismount first.
- You can simulate bus trip with a fixed delay between each stop to
simulate travel time.
- Persons can not mount/dismount the bus until bus arrives to the
designated station.

# Hints:
Use semaphores to control access to the bus and other utilities to control access
to bus. Use thread pools to manage thread management. You can use latches,
cyclic barriers, re-entrant locks and condition variables to write your code.