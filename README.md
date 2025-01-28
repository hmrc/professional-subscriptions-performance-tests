
# professional-subscriptions-performance-tests

Professional Subscriptions Performance Tests

## Start dependencies via Service Manager

To start all dependencies and services for professional subscriptions, use one of the following commands:
```
sm2 --start EE_ALL
sm2 --start EE_DEP (starts only dependencies).
```

### Running locally

To run the smoke test
```
sbt -Dperftest.runSmokeTest=true -DrunLocal=true gatling:test
```
It might be best running the smoke tests before your full suite.
These tests only use one user to check the journey is complete and working.

To run the full test
```
sbt -DrunLocal=true gatling:test
```

### Running in Staging
To run the smoke test
```
sbt -Dperftest.runSmokeTest=true gatling:test
```
It might be best running the smoke tests before your full suite.
These tests only use one user to check the journey is complete and working.

To run the full test
```
sbt gatling:test
```
### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
