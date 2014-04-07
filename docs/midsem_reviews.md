# Midsem Reviews
> Sunil, Mentor  
> Sateesh, External  
> 7th April 2014

## Sunil

* Why use such a nested tree structure? Assign an ID to every timeseries that exists in the cache and then bind the timeseries to that ID. The App server should take care of maintaining associated stuff to the ID. Think of it like using a `malloc()` function, you allocate some memory and get a pointer to that memory location. You can call it anything you want in the program, but to the system, you're still referring to it by the pointer.
* Why use `TreeMap` to maintain the Timeseries structure? Timeseries is better maintained with plain vanilla `List`. `Lists` allow for `O(1)` retrieval by offsetting from an initial location. `TreeMap` on the other hand has a complexity of `O(logn)` for most of its operations. When you can give something in `O(1)` why go for `O(logn)`

## Sateesh

* When using a JVM, memory for it is allocated up front. What happens when you need more memory? Wouldn't it be better to use a language like `C` or `C++` that allows dynamic allocation of memory so that scaling would become easier?