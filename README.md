
# professional-subscriptions-performance-tests

Professional Subscriptions Performance Tests

## Start dependencies via Service Manager

To start all dependencies and services for professional subscriptions, use one of the following commands:
```
sm --start EE_ALL -r
sm --start EE_DEP -r (starts only dependencies).
```

Local testing -

 Smoke - sbt -Dperftest.runSmokeTest=true test
 
 It might be best running the smoke tests before your full suite.
 These tests only use one user to check the journey is complete and working.
 
 sbt test - runs the full journey.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
