# Midsem Reviews
> Sunil, Mentor  
> Sateesh, External  
> 7th April 2014

## Sunil

**Q:** Why use such a nested tree structure? Assign an ID to every timeseries that exists in the cache and then bind the timeseries to that ID. The App server should take care of maintaining associated stuff to the ID. Think of it like using a `malloc()` function, you allocate some memory and get a pointer to that memory location. You can call it anything you want in the program, but to the system, you're still referring to it by the pointer.

**A:** My question here was centric around making things easier for the app server. In a case where an ID is returned, then the App server has to maintain a mapping between the ID and what resource that means. But today's discussion kind of made it clear that i'm taking up things on my head that isn't really the _function_ of the cache. The function of this cache is that it handles timeseries data, and handles it really well. Allowing for definition of different kinds of metadata deviates from what it was meant for. With that being said, metadata can exist, but as a different layer that is _decoupled_ from the timeseries module.

---

**Q:** Why use `TreeMap` to maintain the Timeseries structure? Timeseries is better maintained with plain vanilla `List`. `Lists` allow for `O(1)` retrieval by offsetting from an initial location. `TreeMap` on the other hand has a complexity of `O(logn)` for most of its operations. When you can give something in `O(1)` why go for `O(logn)`

**A:** The problem with using `Lists` to maintain something like a timeseries data is that there is a major requirement for the ticks between timestamps to be constant. Eg. If you consider stock data, even though on weekends, the stock market doesn't trade, the user is forced to enter data for those 2 days _because_ he defined a timeseries with a tick of 1 day. Even though this would take more memory than what a `TreeMap` would, the performance here would be really good since we have a constant retrieval time compared to the `O(logn)` with `TreeMaps`. This would mean that a lot of things would be user input like what is used to represent a `NULL` value in the timeseries,  Initial size of the timeseries. In addition, performing `insert_in_the_middle` operations on something like `Lists` would _HUGELY_ expensive so those operations would not be supported in this kind of a timeseries data. 

## Sateesh

**Q:** When using a JVM, memory for it is allocated up front. What happens when you need more memory? Wouldn't it be better to use a language like `C` or `C++` that allows dynamic allocation of memory so that scaling would become easier?

**A:** When starting the JVM, you can allocate some huge chunk of memory. Eg, if the system is a 8GB system, we can allocate 7GB to the JVM and get on with it. The JVM would manage the memory in the right fashion. Just because it is given 7GB doesn't mean it takes the whole thing right away. 