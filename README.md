[![Build Status](https://travis-ci.org/riquack/shopping-basket.svg?branch=master)](https://travis-ci.org/riquack/shopping-basket)

#Shopping Basket API

A small REST API for a online shopping basket build using [Play Framework](https://www.playframework.com/) and [Akka](http://akka.io/) in [Scala](https://www.scala-lang.org/)
 
##Usage
###Requests
`GET /items`
Retrieve items that are in the store
```
[
  {
    "id": "8bbeabea-c532-4e45-a8f1-56e0343d5c20",
    "name": "Igx87t2V9y9M by vSqgYZK0",
    "description": "2pbUnTZ7Ry...",
    "price": 874,
    "rating": 1.5,
    "inStock": true
  },
  {
    "id": "5ab62945-1f7e-4df6-98a8-7099262329e4",
    "name": "tEfVCb3xZHHy by RY6XTrw9",
    "description": "8JGokqj2WQ...",
    "price": 763,
    "rating": 0,
    "inStock": true
  },
  ...
```

`GET /items/:id`
Retrieve more information for a given product
```
{
  "id": "8bbeabea-c532-4e45-a8f1-56e0343d5c20",
  "name": "Igx87t2V9y9M",
  "vendor": "vSqgYZK0",
  "description": "2pbUnTZ7RyudTspEuikve82T26aA4fNQQGUUyYSCja05J0z7amntzUNYIxcwcVswFGcGWw",
  "category": "NL56zWtAC8",
  "price": 874,
  "rating": 1.5,
  "reviews": [
    {
      "rating": 0,
      "description": "qdB1vHRiVxuhAyHPPMdi"
    },
    {
      "rating": 3,
      "description": "3lKj3D9GfzhaRdKLoKBn"
    }
  ],
  "stock": 3
}
```

`GET /basket/items` Retrieve items that are placed in the shopping basket
```
{
  "items": [
    {
      "id": "726007fd-8a4e-4964-8b1d-b7ce6c53eefe",
      "name": "1w8T0buG5LpN by AqG7ILHY",
      "price": 699,
      "amount": 3
    },
    {
      "id": "298eaef1-92c9-4259-8eae-1ade25e25cd3",
      "name": "aUNb1GBwgRj8 by fAvRVWvC",
      "price": 59,
      "amount": 1
    }
  ],
  "value": 2156
}
```

`POST /basket/items` Adds a new item in the shopping basket
```
{
  "id":"298eaef1-92c9-4259-8eae-1ade25e25cd3",
  "amount": 1
}
```

`DELETE /basket/items/:id` Removes a specific item from the basket

###Errors
Every request that a API clients make may result in an error. In order to help out the API responds 
with an corresponding error with a predefined structure. Example:
 ```
 {
   "id": "d19af561-c6ab-4c25-b03c-85b814aab438",
   "date": "2017-02-02T22:48:58.648",
   "description": "Not enough stock for the request item"
 }
 ```

##Contribute
If you would like to contribute you can fork this repository and raise a Pull Request with your change. 

Execute `sbt` command in the project folder to set the project.

While in sbt interactive mode, execute `compile` to compile the project's sources.

While in sbt interactive mode, execute `run` command to start a HTTP server which is by default is available at [http://localhost:9000]().

While in sbt interactive mode, execute `test` command to run the test suite.

