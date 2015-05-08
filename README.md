# SDF Akka Workshop

This is the starting point of the SDF Akka workshop.

To run the application:

1. Run activator on the root of the project:

  ```.../sdf-akka-workshop $ activator```

2. From within the activator console, run the app:

  ```
  > run
  Hit <enter> to stop the simulation
  ```

3. On a separate terminal, tail the file sample.log
 
  You should see plenty of log entries like the following:
 
  ```Received the following message: Request(3054005172663496902,1431094557745,/store,google,chrome)```

You'll probably want to start by replacing DummyRequestConsumer by a slightly more useful actor.

Have fun!
