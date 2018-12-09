This program solves one of the quintessential problems of CPU Scheduling in Operating Systems.

The program starts by accepting 3 parameters, Sleep Time, # of Producer Threads, and # of Consumer Threads.

- In the Main class creates the number of producer and consumer threads listed above and calls the start method on them
- Depending on it the thread is a producer or consumer it will pass on that type value to the PC class.

- The PC class only has one method the run() method. 
- Back in the main class we call the start() method that starts the thread

The Producers work like This
- Get a random number between 0 - 10000
- Call the insert() method in the buffer class
- Acquire the full semephore (There is an empty spot in the list) if possible else skip over
- Acquire the mutex (Another thread is not accessing a producer or consumer method) if possible else skip over
- If both of those conditions are true, then insert into an available slot
- Update in variable
- Release both the empty semephore and mutex
- Wait for a random amount of time between 0 - 500 ms

The Consumers work like This
- Call the insert() method in the buffer class
- Acquire the empty semephore (There is an full spot in the list) if possible else skip over
- Acquire the mutex (Another thread is not accessing a producer or consumer method) if possible else skip over
- If both of those conditions are true, then remove from an available spot
- Set the value back to 0
- Release both the full semephore and mutex
- Return the value that was removed from the Array
- Wait for a random amount of time between 0 - 500 ms

For Both Producers and Consumers
- Producer: System.out.println(val inserted)
- Consumer: System.out.println(val removed)
