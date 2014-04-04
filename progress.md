# PCache - Learnings

## The Idea

We handle Timeseries data on a daily basis in our team and it turns out to be
very volume oriented and something that could greatly benifit from a cache.  The 
PCache (_Project Cache_) started off as an idea that was floated within the
current team I work in as something that "should" exist. At that time, none of us
had the time to build something like that out. With the MS project around the
corner, I decided i'd build this for my MS project

I've always been interested in doing something that is systems oriented and this 
seemed like the right opportunity to try my hand at it. 

## Motivation

The concept of a cache has been there for a very long period of time. Through our
experience with Timeseries data, we realized the necessity of building a cache
that was built from the ground up for this kind of data. Doing something like
this gives us the opportunity to add features into the cache that come from our
learning with the Timeseries data. 

## Choice of Technology

The idea was to use a technology like GO to write the cache. GO or GOLANG as it
is sometimes called, is a language that is developed by Google and is majorly
used for systems level programs (like a cache). The advantage of using something
like GO would've been in the fact that GO was designed from the ground up to be
a concurrent programming language. Given that a cache needs to handle a lot of 
simultaneous connections, GO would've helped there.

I ended up going for JAVA because of 1 simple reason. It is productionizable in a
company like SAP far more easily than a more recent language like GO. 

## Progress

### Basic assumptions

* Cache should sit inside the memory
* Cache fetches should be as fast as possible
* Handle a good set of points

### Maintaining different sets of data

There was a considerable debate on how to maintain different sets of timeseries
data. This cache would be usable by any number of people, storing all kinds of
timeseries' so it was key to form some kind of a barrier between all of them

The key difference to understand with this cache, is that it is modelled as a 
"Backend" cache, which means that it exists between the App server and the DB.
This means that it will store all kinds of data that is timeseries based.

Usually, what happens is, timeseries data doesn't make too much sense when it 
stands alone. It has to be augmented with some information for it to be used 
well. I had to come up with a way to wrap timeseries with some "information".

To solve the issue of keeping boundaries and adding information to the timeseries,
I came up with the concept of "Structures"

### Structures, Structure instances and Namespaces

Namespaces forms the top most classification for everything within the cache. A
Namespace is something like an application name. Everything that relates to 
that particular app would be store under that namespace.

Structures are a way to define what kind of "information" will be wrapping the 
timeseries. To give a simple example, A sensor timeseries would be augmented with
information about what kind of sensor it is, what is the name of the sensor, etc.
Structures are a barebones way of describing what kind of information is going to 
augment the timeseries. A given namespace can contain many structures.

Structure Instances are instances of a given structure. Structure instances are a 
directly linked to the timeseries data. As they represet the layer "around" the
timeseries that contains some information (defined by the structure). The 
structure instance should give values to whatever the structure defined. What was
defined needs to match what was instantiated

### The Timeseries 

Modelling the timeseries itself was the next task, this was a hard task because
of the simple fact that CRUD on the timeseries was to be supported. This means
that Points should be added into the timeseries, retrieved, updated and removed
all in a good period of time. 

For this reason, i went ahead with a Red-Black tree based data structure for 
containing the timseries. Timestamps were to be in the ISO 8601 format and the
datapoints associated can be in any datatype. The timestamps are converted to the 
number of miliseconds since EPOC. This allows me to handle information at a 
milisecond granularity and in a continuous fashion because of the Red Black tree
structure.


