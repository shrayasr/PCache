# Project Cache [![Build Status](https://travis-ci.org/shrayasr/PCache.svg?branch=master)](https://travis-ci.org/shrayasr/PCache)
> Time series cache for the final sem dissertation 2014

**UPDATE: I finished my dissertation presentation yesterday, the 28th of June 2014. Feels good, kinda. The MS is now over!**

**THIS IS STILL MAJOR WIP** 

*TBH, i don't even know what i'm doing :D*


## Topic

Time Series Data is all around us: Temperature, stocks, weather are a few to name. This kind of data however usually occurs in bulk and it is very expensive to store and retrieve out of a database. Some of the computations that can be performed on this data can be offloaded to the cache because of its simplicity. The aim is to build a high performance backend cache that caters to Time Series Data. Such a cache would allow for a lot of functions that are Time Series Data specific like updates of individual points inside it without having to re-warm the entire cache and small computations.

## Objectives

The key objective is to build an in memory cache that is tailor made for Time Series Data. This cache would be open to connections from multiple endpoints (REST APIs and socket connections) through which changes can be made within the cache for single / multiple data points. The cache would also allow a restricted set of computations. The fact that this is an in memory cache means that it is very vital that the memory underneath be managed in the best of ways. The cache also aims to hold large amounts of data in memory by adopting a self adaptive storage which optimizes the memory to hold large chunks of data while using the least memory. This project is intended to be a part of an SAP product which works very closely with Stock Time Series Data. 

## Scope

* Efficiently handle Time Series Data, represented by a TIMESTAMP (ISO8601) and a value
* Allow updates to individual / multiple points stored in the cache
* Allow communication through different interfaces - REST, Sockets, etc
* Keep cache updates atomic, thereby always maintaining consistency in what is served
* Implement Self Adaptive Storage i.e. the cache would adapt its underlying store to hold the maximum data using the least memory as possible. 
* Support an initial set of computations at the cache level:
	* Resolution of points: Allow data to be fetched at a given resolution level i.e. all points, 1 point for every 10 points and so forth. Such a case would be useful for plotting on mobile devices like the iPad
	* Precision Levels: Allow data to be fetched at different precision levels. Such a case would be useful where decimal point precision is required (experiments) or not (simple UIs)
